package com.zenyte.game.content.event.easter2020;

import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.SkipPluginScan;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 11/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class RedCarpetInstance extends DynamicArea {

    private static final Location doorTile = new ImmutableLocation(2199, 4395, 0);

    public RedCarpetInstance(AllocatedArea allocatedArea) {
        super(allocatedArea, 274, 548);
    }

    @Override
    public void constructed() {

    }

    @Override
    public void enter(Player player) {

    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    public void spawnAtDoor(@NotNull final Player player) {
        player.setLocation(getLocation(doorTile));
    }

    @Override
    public String name() {
        return "Easter event red carpet cutscene area";
    }

    @Override
    public Location onLoginLocation() {
        return new Location(2208, 4394, 0);
    }
}