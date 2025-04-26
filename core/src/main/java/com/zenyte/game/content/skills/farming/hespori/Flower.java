package com.zenyte.game.content.skills.farming.hespori;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 24/02/2019 15:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Flower extends NPC implements CombatScript {

    /**
     * An enum containing the positions where the flowers spawn.
     */
    enum FlowerPosition {
        SOUTH_WESTERN(new Location(1243, 10083, 0)), NORTH_WESTERN(new Location(1243, 10091, 0)),
        NORTH_EASTERN(new Location(1251, 10091, 0)), SOUTH_EASTERN(new Location(1251, 10083, 0));

        static final FlowerPosition[] values = values();
        @NotNull
        private final Location tile;

        FlowerPosition(Location tile) {
            this.tile = tile;
        }
    }

    /**
     * The animation the npc performs when it enters the bloom mode.
     */
    private static final Animation bloomAnimation = new Animation(8229);

    /**
     * The animation the npc performs when it dies in the bloom mode.
     */
    private static final Animation stillAnimation = new Animation(8230);

    /**
     * The npc's ids for bloom and still modes.
     */
    private static final int BLOOMING = 8584, STILL = 8585;

    private final HesporiInstance instance;

    /**
     * The package-level constructor for the flowers. Sets the npc to face south & starts it off in the still mode.
     * Random walk radius is set to 0, as the flowers cannot walk.
     *
     * @param instance the Hespori's instance that spawns the flowers.
     * @param position the position of the flowers where they spawn.
     */
    Flower(@NotNull final HesporiInstance instance, @NotNull final FlowerPosition position) {
        this(STILL, instance.getLocation(position.tile), instance, Direction.SOUTH, 0);
    }

    /**
     * The default constructor for the flowers, as they also exist in the static map.
     *
     * @param id     the id of the npc.
     * @param tile   the tile where the npc spawns.
     * @param facing the facing of the npc.
     * @param radius the radius of the npc.
     */
    private Flower(final int id, final Location tile, final HesporiInstance instance, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        this.instance = instance;
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        return false;
    }

    /**
     * Custom attack method, implemented to not allow the flowers to attack targets back, thus resulting in
     * {@link Integer#MAX_VALUE } delay.
     *
     * @param target the target entity.
     * @return the delay.
     */
    @Override
    public int attack(Entity target) {
        return Integer.MAX_VALUE;
    }

    /**
	 * THe Hespori stands on-top of a clipped farming patch, meaning we must override the projectile clip method to
	 * not check for clipping whilst attacking it.
	 *
	 * @param player the player attacking Hespori.
	 * @param melee
	 * @return whether or not to check for projectile clipping underneath the NPC.
	 */
    @Override
    public boolean checkProjectileClip(final Player player, boolean melee) {
        return false;
    }

    /**
     * The flower npcs cannot walk around under any circumstances.
     *
     * @return whether or not the npc can walk.
     */
    @Override
    public boolean isMovableEntity() {
        return false;
    }

    /**
     * Sets the flower to bloom, and puts it back to life.
     */
    void bloom() {
        assert getId() != BLOOMING;
        reset();
        setAnimation(bloomAnimation);
        WorldTasksManager.schedule(() -> setTransformation(BLOOMING), 1);
    }

    /**
     * Checks to see whether the flower is blooming or not, based on their current id.
     *
     * @return whether or not the flower is blooming.
     */
    boolean isBlooming() {
        return getId() == BLOOMING;
    }

    /**
     * Sends the npc to "death", during which phase the flower stands still.
     *
     */
    @Override
    public void sendDeath() {
        assert getId() != STILL;
        setAnimation(stillAnimation);
        combat.removeTarget();
        WorldTasksManager.schedule(() -> {
            setTransformation(STILL);
            instance.onFlowerDeath(getMostDamagePlayer());
        });
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        super.handleIngoingHit(hit);
        hit.setDamage(10);
    }

}
