package main.java.com.djrapitops.plan.command.commands;

import com.djrapitops.plugin.command.CommandType;
import com.djrapitops.plugin.command.CommandUtils;
import com.djrapitops.plugin.command.ISender;
import com.djrapitops.plugin.command.SubCommand;
import com.djrapitops.plugin.task.AbsRunnable;
import main.java.com.djrapitops.plan.Log;
import main.java.com.djrapitops.plan.Permissions;
import main.java.com.djrapitops.plan.Plan;
import main.java.com.djrapitops.plan.data.WebUser;
import main.java.com.djrapitops.plan.database.tables.SecurityTable;
import main.java.com.djrapitops.plan.locale.Locale;
import main.java.com.djrapitops.plan.locale.Msg;
import main.java.com.djrapitops.plan.utilities.Check;
import main.java.com.djrapitops.plan.utilities.PassEncryptUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

/**
 * Command for registering web users.
 * <p>
 * Registers a new webuser to the database.
 * <p>
 * No permission required for self registration. (Constructor string is empty)
 * <p>
 * plan.webmanage required for registering other users.
 *
 * @author Rsl1122
 * @since 3.5.2
 */
public class RegisterCommand extends SubCommand {

    private final Plan plugin;

    public RegisterCommand(Plan plugin) {
        super("register",
                CommandType.CONSOLE_WITH_ARGUMENTS,
                "", // No Permission Requirement
                Locale.get(Msg.CMD_USG_WEB_REGISTER).toString(),
                "<password> [name] [access lvl]");
        this.plugin = plugin;
        setupFilter();
    }

    @Override
    public String[] addHelp() {
        return Locale.get(Msg.CMD_HELP_WEB_REGISTER).toArray();
    }

    @Override
    public boolean onCommand(ISender sender, String commandLabel, String[] args) {
        String notEnoughArgsMsg = Locale.get(Msg.CMD_FAIL_REQ_ARGS).parse("(3) " + super.getArguments());
        String hashErrorMsg = "§cPassword hash error.";
        String permLvlErrorMsg = "§cIncorrect perm level, not a number: ";
        try {
            if (CommandUtils.isPlayer(sender)) {
                Log.info(sender.getName() + " issued WebUser register command.");
                playerRegister(args, sender);
            } else {
                consoleRegister(args, sender, notEnoughArgsMsg);
            }
        } catch (PassEncryptUtil.CannotPerformOperationException e) {
            Log.toLog(this.getClass().getSimpleName(), e);
            sender.sendMessage(hashErrorMsg);
        } catch (NumberFormatException e) {
            sender.sendMessage(permLvlErrorMsg + args[2]);
        } catch (Exception e) {
            Log.toLog(this.getClass().getSimpleName(), e);
        }
        return true;
    }

    private void consoleRegister(String[] args, ISender sender, String notEnoughArgsMsg) throws PassEncryptUtil.CannotPerformOperationException {
        if (Check.isTrue(args.length >= 3, notEnoughArgsMsg, sender)) {
            int permLevel;
            permLevel = Integer.parseInt(args[2]);
            String passHash = PassEncryptUtil.createHash(args[0]);
            registerUser(new WebUser(args[1], passHash, permLevel), sender);
        }
    }

    private void playerRegister(String[] args, ISender sender) throws PassEncryptUtil.CannotPerformOperationException {
        final String notEnoughArgsMsg = Locale.get(Msg.CMD_FAIL_REQ_ARGS).parse("(1 or 3) " + super.getArguments());
        boolean registerSenderAsUser = args.length == 1;
        if (registerSenderAsUser) {
            String user = sender.getName();
            String pass = PassEncryptUtil.createHash(args[0]);
            int permLvl = getPermissionLevel(sender);
            registerUser(new WebUser(user, pass, permLvl), sender);
        } else if (sender.hasPermission(Permissions.MANAGE_WEB.getPermission())) {
            consoleRegister(args, sender, notEnoughArgsMsg);
        } else {
            sender.sendMessage(Locale.get(Msg.CMD_FAIL_NO_PERMISSION).parse());
        }
    }

    private int getPermissionLevel(ISender sender) {
        final String permAnalyze = Permissions.ANALYZE.getPerm();
        final String permInspectOther = Permissions.INSPECT_OTHER.getPerm();
        final String permInspect = Permissions.INSPECT.getPerm();
        if (sender.hasPermission(permAnalyze)) {
            return 0;
        }
        if (sender.hasPermission(permInspectOther)) {
            return 1;
        }
        if (sender.hasPermission(permInspect)) {
            return 2;
        }
        return 100;
    }

    private void registerUser(WebUser webUser, ISender sender) {
        plugin.getRunnableFactory().createNew(new AbsRunnable("Register WebUser Task") {
            @Override
            public void run() {
                final String existsMsg = "§cUser Already Exists!";
                final String userName = webUser.getName();
                final String successMsg = "§aAdded a new user (" + userName + ") successfully!";
                try {
                    SecurityTable securityTable = plugin.getDB().getSecurityTable();
                    boolean userExists = securityTable.userExists(userName);
                    if (!Check.isTrue(!userExists, existsMsg, sender)) {
                        return;
                    }
                    securityTable.addNewUser(webUser);
                    sender.sendMessage(successMsg);
                    Log.info("Registered new user: " + userName + " Perm level: " + webUser.getPermLevel());
                } catch (Exception e) {
                    Log.toLog(this.getClass().getName(), e);
                } finally {
                    this.cancel();
                }
            }
        }).runTaskAsynchronously();
    }

    /**
     * Setups the command console output filter
     */
    private void setupFilter() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new RegisterCommandFilter());
    }
}
