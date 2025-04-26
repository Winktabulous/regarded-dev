package com.zenyte.game.content.minigame.castlewars;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.plugins.object.CastleWarsLobbyEntrancePortal;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import kotlin.Pair;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Set;

/**
 * @author Kris | 21/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CastleWarsStartArea extends CastleWarsArea {
    private static final int TIMEOUT_MINUTES = 2;
    private static final int TIMEOUT_TICKS = (int) TimeUnit.MINUTES.toTicks(TIMEOUT_MINUTES);
    private final Set<Pair<String, MutableInt>> collection = new ObjectOpenHashSet<>();

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{2368, 3136}, {2368, 3127}, {2377, 3127}, {2377, 3136}}, 1), new RSPolygon(new int[][] {{2423, 3081}, {2423, 3072}, {2432, 3072}, {2432, 3081}}, 1)};
    }

    @Override
    public void enter(final Player player) {
        super.enter(player);
        collection.removeIf(pair -> pair.getFirst().equalsIgnoreCase(player.getUsername()));
        collection.add(new Pair<>(player.getUsername(), new MutableInt(0)));
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        super.leave(player, logout);
        collection.removeIf(pair -> pair.getFirst().equalsIgnoreCase(player.getUsername()));
    }

    @Override
    public void process() {
        super.process();
        collection.removeIf(entry -> {
            final int value = entry.getSecond().incrementAndGet();
            if (value >= TIMEOUT_TICKS) {
                World.getPlayer(entry.getFirst()).ifPresent(player -> CastleWarsLobbyEntrancePortal.joinTeam(player, CastleWars.getTeam(player)));
                return true;
            }
            return false;
        });
    }

    @Override
    public String name() {
        return "Castle Wars (Start)";
    }

}
