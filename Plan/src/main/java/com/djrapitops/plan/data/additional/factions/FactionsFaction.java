package main.java.com.djrapitops.plan.data.additional.factions;

import com.massivecraft.factions.entity.MPlayer;
import java.io.Serializable;
import java.util.UUID;
import main.java.com.djrapitops.plan.data.additional.PluginData;

/**
 *
 * @author Rsl1122
 */
public class FactionsFaction extends PluginData {

    public FactionsFaction() {
        super("Factions", "faction");
        super.setIcon("flag");
        super.setPrefix("Faction: ");
    }

    @Override
    public String getHtmlReplaceValue(String modifierPrefix, UUID uuid) {
        MPlayer mPlayer = MPlayer.get(uuid);
        String faction = mPlayer.getFactionName();
        if (faction.isEmpty()) {
            return parseContainer("", "No Faction.");
        }
        return parseContainer("", faction);
    }

    @Override
    public Serializable getValue(UUID uuid) {
        MPlayer mPlayer = MPlayer.get(uuid);
        return mPlayer.getFactionName();
    }

}