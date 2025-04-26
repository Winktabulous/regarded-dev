package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 23/03/2019 00:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LeverTeleport implements Teleport {

    private final Location destination;
    private final WorldObject level;
    private final String message;
    private final Runnable onArrival;

    @Override
    public TeleportType getType() {
        return TeleportType.LEVER_TELEPORT;
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
        return Integer.MIN_VALUE;
    }

    @Override
    public void onArrival(Player player) {
        if (level != null) {
            World.spawnObject(level);
            level.setLocked(false);
        }
        player.sendMessage(message);
        if (onArrival != null) {
            onArrival.run();
        }
    }

    @Override
    public void onUsage(Player player) {
        if (level != null) {
            level.setLocked(true);
            WorldTasksManager.schedule(() -> World.spawnObject(new WorldObject(88, level.getType(), level.getRotation(), level)));
        }
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    public LeverTeleport(Location destination, WorldObject level, String message, Runnable onArrival) {
        this.destination = destination;
        this.level = level;
        this.message = message;
        this.onArrival = onArrival;
    }
}
