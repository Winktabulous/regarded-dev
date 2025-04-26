package com.zenyte.game.world.entity.npc;

import com.zenyte.game.content.skills.magic.spells.arceuus.DarkLureKt;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 02/03/2019 13:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RetreatMechanics {
    /**
     * The constructor for retreating mechanics.
     *
     * @param npc The npc owning this.
     */
    public RetreatMechanics(@NotNull final NPC npc) {
        this.npc = npc;
    }

    /**
     * The npc owning these mechanics.
     */
    protected NPC npc;
    protected Entity target;

    /**
     * Gets the maximum allowed distance for the npc, from the respawn location.
     *
     * @return maximum distance, if exceeded, npc retreats.
     */
    private int distance() {
        return npc.getMaxDistance();
    }

    /**
     * Processes the mechanics, retreats if necessary.
     *
     * @param target the target from whom the npc is retreating.
     * @return whether or not the npc will retreat.
     */
    public boolean process(@NotNull final Entity target) {
        this.target = target;
        final Location npcLocation = npc.getLocation();
        if (DarkLureKt.getUnderDarkLure(npc)) {
            return false;
        }
        if (outOfBounds(npcLocation)) {
            retreat(npc.getRespawnTile(), target, false);
            return true;
        }
        final Location targetLocation = target.getLocation();
        if (outOfBounds(targetLocation)) {
            if (!npc.combat.outOfRange(target, npc.getCombatDefinitions().isMelee() ? 0 : npc.getMaxDistance(), target.getSize(), npc.getCombatDefinitions().isMelee())) {
                return false;
            }
            final double distance = npcLocation.getDistance(targetLocation);
            if (distance > 16) {
                retreat(npc.getRespawnTile(), target, false);
            } else {
                retreat(retreatDestination(target), target, true);
            }
            return true;
        }
        return false;
    }

    void process() {
        //Recent face entity when the npc has finished retreating. Sort of.
        if (npc.getFaceEntity() != -1) {
            if (npc.getCombat().getTarget() == null && target != null) {
                if (!npc.hasWalkSteps() || npc.getLocation().getDistance(target.getLocation()) >= 16) {
                    npc.setFaceEntity(target = null);
                }
            }
        }
    }

    /**
     * Forces the npc to reset their combat and retreat to the requested location.
     *
     * @param tile      the tile where the npc retreats to.
     * @param target    the target causing the retreating.
     * @param backwards whether or not the npc should retreat in a backwards-motion.
     */
    public void retreat(@NotNull final Location tile, @NotNull final Entity target, final boolean backwards) {
        npc.combat.removeTarget();
        if (backwards) {
            npc.setFaceEntity(target);
        }
        npc.resetWalkSteps();
        if (npc.isFrozen() || npc.isStunned()) {
            return;
        }
        npc.addWalkStepsInteract(tile.getX(), tile.getY(), -1, npc.getSize(), true);
    }

    /**
     * Checks if the requested location is outside of the npc's movement boundaries.
     *
     * @param other the tile to test.
     * @return whether or not the tile is out of bounds.
     */
    protected boolean outOfBounds(@NotNull final Location other) {
        final int distance = distance();
        final Location tile = npc.getRespawnTile();
        final int size = npc.getSize();
        final int dx = other.getX() - tile.getX();
        final int dy = other.getY() - tile.getY();
        return dx > size + distance || dx < -1 - distance || dy > size + distance || dy < -1 - distance;
    }

    /**
     * Gets the retreat location for the npc based on the location of the target - moving directly away from them in
     * a backwards motion.
     *
     * @param target the target from whom the npc retreats.
     * @return the tile to where the npc retreats.
     */
    protected Location retreatDestination(@NotNull final Entity target) {
        final int distance = distance();
        final Location middle = npc.getMiddleLocation();
        final double degrees = degrees(middle.getY() - target.getY(), middle.getX() - target.getX());
        final double angle = Math.toRadians(degrees);
        final int x = (int) Math.round(middle.getX() + (distance * Math.cos(angle)));
        final int y = (int) Math.round(middle.getY() + (distance * Math.sin(angle)));
        return new Location(x, y, middle.getPlane());
    }

    /**
     * Gets the degrees based off of the x/y offsets requested.
     *
     * @param offsetY the y distance.
     * @param offsetX the x distance.
     * @return the degrees - value from 0 to 360.
     */
    private double degrees(final int offsetY, final int offsetX) {
        final double degrees = Math.toDegrees(Math.atan2(offsetY, offsetX));
        return degrees < 0 ? (degrees + 360) : degrees;
    }
}
