package com.zenyte.game.content.stars;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TimeUnit;

/**
 * @author Andys1814.
 */
public final class ShootingStars {

    // "The shooting star has depleted - keep and eye out for the next one!"
    private static ScheduledShootingStarSpawn spawn;

    private static ShootingStar current;

    @Subscribe
    public static void boot(ServerLaunchEvent event) {
        init();
    }

    public static void init() {
        ScheduledShootingStarSpawn scheduler = ScheduledShootingStarSpawn.atAnyRandomLocation(25);
        WorldTasksManager.schedule(scheduler, 0, 0);
        spawn = scheduler;
    }

    public static ScheduledShootingStarSpawn getSpawn() {
        return spawn;
    }

    public static void setSpawn(ScheduledShootingStarSpawn spawn) {
        ShootingStars.spawn = spawn;
    }

    public static ShootingStar getCurrent() {
        return current;
    }

    public static void setCurrent(ShootingStar current) {
        ShootingStars.current = current;
    }
}
