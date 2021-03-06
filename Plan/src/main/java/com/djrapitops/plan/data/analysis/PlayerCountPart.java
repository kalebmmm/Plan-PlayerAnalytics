package main.java.com.djrapitops.plan.data.analysis;

import com.djrapitops.plugin.utilities.Verify;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Part responsible for counting players.
 * <p>
 * Placeholder values can be retrieved using the get method.
 * <p>
 * Contains following placeholders after analyzed:
 * ${playersTotal}, ${ops}
 *
 * @author Rsl1122
 * @since 3.5.2
 */
public class PlayerCountPart extends RawData {

    private final Set<UUID> uuids;
    private final Set<UUID> ops;

    public PlayerCountPart() {
        uuids = new HashSet<>();
        ops = new HashSet<>();
    }

    @Override
    public void analyse() {
        addValue("ops", ops.size());
        addValue("playersTotal", uuids.size());
    }

    public void addPlayer(UUID uuid) {
        Verify.nullCheck(uuid);
        uuids.add(uuid);
    }

    public void addPlayers(Collection<UUID> uuids) {
        Verify.nullCheck(uuids);
        this.uuids.addAll(uuids);
    }

    public void addOPs(Collection<UUID> uuids) {
        Verify.nullCheck(uuids);
        this.ops.addAll(uuids);
    }

    public void addOP(UUID uuid) {
        Verify.nullCheck(uuid);
        ops.add(uuid);
    }

    public Set<UUID> getUuids() {
        return uuids;
    }

    public int getPlayerCount() {
        return uuids.size();
    }

    public Set<UUID> getOps() {
        return ops;
    }
}
