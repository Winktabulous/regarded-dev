package com.zenyte.game.content.stars;

import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

import java.util.EnumSet;
import java.util.Set;

import static com.zenyte.game.world.broadcasts.BroadcastType.LOTTERY;

/**
 * @author Andys1814
 */
public final class ScheduledShootingStarSpawn extends TickTask {

    /**
     * The location that a new star will be spawned at upon completion of this scheduled tick task.
     * This lives here so players can pre-emptively look up the location of the next spawn.
     */
    private final ShootingStarLocation location;

    private boolean notified = false;

    public ScheduledShootingStarSpawn(int ticks, ShootingStarLocation location) {
        this.ticks = ticks;
        this.location = location;
    }

    @Override
    public void run() {
        if (ticks == 0) {
            if (ShootingStars.getCurrent() != null) {
                ShootingStars.getCurrent().remove();
                ShootingStars.setCurrent(null);
            }

            ShootingStar star = new ShootingStar(ShootingStarLevel.NINE, location, true);
            World.spawnObject(star);
            ShootingStars.setCurrent(star);

            WorldBroadcasts.sendMessage("<img=51><col=2980B9><shad=000000>A shooting star just crashed " + location.getLocation() + "!", LOTTERY, true);

            // Schedule the next shooting star spawn
            int ticks = (int) TimeUnit.HOURS.toTicks(1) + Utils.random((int) TimeUnit.MINUTES.toTicks(30));
            ScheduledShootingStarSpawn scheduler = atRandomLocationExcept(location, ticks);
            WorldTasksManager.schedule(scheduler, 0, 0);
            ShootingStars.setSpawn(scheduler);

            stop();
            return;
        }

        if (ticks == TimeUnit.MINUTES.toTicks(5)) {
            WorldBroadcasts.sendMessage("<img=51><col=2980B9><shad=000000>A shooting star will crash-land in 5 minutes. Keep an eye out for more information!", LOTTERY, true);
        }

        ticks--;
    }

    public static ScheduledShootingStarSpawn atRandomLocationExcept(ShootingStarLocation except, int ticks) {
        Set<ShootingStarLocation> locations = EnumSet.copyOf(ShootingStarLocation.LOCATIONS);
        if (except != null) {
            locations.remove(except);
        }

        ShootingStarLocation location = Utils.getRandomCollectionElement(locations);
        if (location == null) {
            throw new IllegalStateException("Unable to spawn shooting star: null when attempting to randomly generate a spawn location.");
        }

        return new ScheduledShootingStarSpawn(ticks, location);
    }

    public static ScheduledShootingStarSpawn atAnyRandomLocation(int ticks) {
        return atRandomLocationExcept(null, ticks);
    }

    public static ScheduledShootingStarSpawn atLocation(ShootingStarLocation location, int ticks) {
        return new ScheduledShootingStarSpawn(ticks, location);
    }

    public long getTicks() {
        return ticks;
    }

    public ShootingStarLocation getLocation() {
        return location;
    }
}
