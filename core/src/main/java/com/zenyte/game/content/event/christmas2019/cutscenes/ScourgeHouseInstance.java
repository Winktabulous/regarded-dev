package com.zenyte.game.content.event.christmas2019.cutscenes;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 18/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScourgeHouseInstance extends DynamicArea {

    private static final Location doorTile = new ImmutableLocation(2471, 5405, 1);

    public ScourgeHouseInstance(AllocatedArea allocatedArea) {
        super(allocatedArea, 306, 674);
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
        player.setFaceLocation(player.getLocation().transform(Direction.SOUTH, 1));
    }

    @Override
    public String name() {
        return "Scourge's house cutscene area";
    }

    @Override
    public Location onLoginLocation() {
        return new Location(2465, 5403, 0);
    }
}
