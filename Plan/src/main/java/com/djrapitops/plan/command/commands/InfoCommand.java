package com.djrapitops.plan.command.commands;

import com.djrapitops.plan.Phrase;
import com.djrapitops.plan.Plan;
import com.djrapitops.plan.command.CommandType;
import com.djrapitops.plan.command.SubCommand;
import com.djrapitops.plan.utilities.MiscUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class InfoCommand extends SubCommand {

    private Plan plugin;

    public InfoCommand(Plan plugin) {
        super("info", "plan.info", "View Version of Plan", CommandType.CONSOLE, "");

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ChatColor oColor = Phrase.COLOR_MAIN.color();
        ChatColor tColor = Phrase.COLOR_SEC.color();
        ChatColor hColor = Phrase.COLOR_TER.color();

        String[] messages = {
            hColor + Phrase.ARROWS_RIGHT.toString() + oColor + "Player Analytics - Info",
            tColor + " " + Phrase.BALL.toString() + oColor + "Version: " + tColor + plugin.getDescription().getVersion(),
            tColor + " " + Phrase.BALL.toString() + tColor + MiscUtils.checkVersion(),
            hColor + Phrase.ARROWS_RIGHT.toString()
        };
        sender.sendMessage(messages);
        return true;
    }

}