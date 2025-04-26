package com.zenyte.game.content.godwars;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PortalTeleport implements Teleport {

    private final Location destination;

    @Override
    public TeleportType getType() {
        return TeleportType.HIGH_REVISION_TELEPORT;
    }

    @Override
    public Location getDestination() {
        return destination;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return 0;
    }

    @Override
    public Item[] getRunes() {
        return null;
    }

    @Override
    public int getWildernessLevel() {
        return WILDERNESS_LEVEL;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    @Override
    public void onArrival(final Player player) {
        WorldTasksManager.schedule(() -> player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(player.getLocation()), 1), null)), 2);
    }

    public PortalTeleport(Location destination) {
        this.destination = destination;
    }
}
