package com.zenyte.game.content.skills.agility.priffdinasrooftop;

import com.zenyte.game.content.skills.agility.AgilityCourseObstacle;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author R-Y-M-R
 * @date 2/11/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public final class Portal extends AgilityCourseObstacle {

    enum PortalLocation {
        AFTER_FIRST_LADDER(new Location(3257, 6111, 2), new Location(3275, 6105, 2)),
        AFTER_ROOF_EDGE(new Location(3270, 6116, 0), new Location(3292, 6141, 2)),
        AFTER_FIRST_ROPE_BRIDGE(new Location(3282, 6138, 2), new Location(3268, 6146, 2)),
        AFTER_SECOND_TIGHT_ROPE(new Location(3267, 6147, 2), new Location(3271, 6157, 2)),
        AFTER_SECOND_ROPE_BRIDGE(new Location(3272, 6157, 2), new Location(3277, 6167, 2)),
        AFTER_THIRD_ROPE_BRIDGE(new Location(3273, 6171, 2), new Location(3286, 6178, 0)),
        ;

        private final Location spawn;
        private final Location exit;

        PortalLocation(Location spawn, Location exit) {
            this.spawn = spawn;
            this.exit = exit;
        }

        public static PortalLocation getByPortalPosition(@NotNull Location loc) {
            for (PortalLocation portal : PortalLocation.values()) {
                if (portal.spawn.equals(loc)) {
                    return portal;
                }
            }
            throw new IllegalArgumentException("Tried getting a non-existant portal location with location: " + loc);
        }
    }

    public Portal() {
        super(PriffdinasRooftopCourse.class, 14);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 75;
    }

    @Override
    public int[] getObjectIds() {
        return new int[]{36240, 36241, 36242, 36243, 36244, 36245, 36246};
    }

    @Override
    public int distance(@NotNull final WorldObject object) {
        return 0;
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 1;
    }

    @Override
    public boolean preconditions(final Player player, final WorldObject portalObject) {
        return Arrays.stream(getObjectIds()).anyMatch((p -> p == portalObject.getId()));
    }

    @Override
    public void startSuccess(Player player, WorldObject portalObject) {
        final PortalLocation portal = PortalLocation.getByPortalPosition(portalObject.getPosition());
        WorldTasksManager.scheduleOrExecute(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                    case 0: {
                        player.addWalkSteps(portalObject.getX(), portalObject.getY(), -1, false);
                        break;
                    }
                    case 1: {
                        player.teleport(portal.exit);
                        awardLoot(player);
                        player.getTemporaryAttributes().put("prif_portal", 0);
                        player.getVarManager().sendBit(9298, 0);
                        stop();
                        break;
                    }
                }
            }
        }, 0, 0);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 82.0;
    }

    private void awardLoot(Player player) {
        if (Utils.random(1, 3) == 1) {
            player.getInventory().addOrDrop(23962, 1);
            player.sendFilteredMessage("You find a crystal shard on your way through the portal.");
        }
    }
}