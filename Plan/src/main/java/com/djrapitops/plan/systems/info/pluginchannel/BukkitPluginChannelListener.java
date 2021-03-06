/* 
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package main.java.com.djrapitops.plan.systems.info.pluginchannel;

import main.java.com.djrapitops.plan.Log;
import main.java.com.djrapitops.plan.Plan;
import main.java.com.djrapitops.plan.Settings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * //TODO Class Javadoc Comment
 *
 * @author Rsl1122
 */
public class BukkitPluginChannelListener implements PluginMessageListener {

    private static String accessKey;

    private final Plan plugin;

    public BukkitPluginChannelListener(Plan plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (plugin.getInfoManager().isUsingAnotherWebServer() || Settings.BUNGEE_OVERRIDE_STANDALONE_MODE.isTrue()) {
            return;
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subChannel = in.readUTF();
            Log.debug("Received plugin message, channel: " + subChannel);
            String[] data = in.readUTF().split("<!>");
            String address = data[0];
            accessKey = data[1];

            if ("bungee_address".equals(subChannel)) {
                plugin.getServerInfoManager().saveBungeeConnectionAddress(address);
                Log.info("-----------------------------------");
                Log.info("Received Bungee WebServer address through plugin channel, restarting Plan.");
                Log.info("-----------------------------------");
                plugin.restart();
                notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static void usedAccessKey() {
        accessKey = null;
    }
}