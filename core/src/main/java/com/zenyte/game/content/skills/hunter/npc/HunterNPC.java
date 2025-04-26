package com.zenyte.game.content.skills.hunter.npc;

import com.zenyte.ContentConstants;
import com.zenyte.game.content.boons.impl.DoubleChins;
import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.PreyInteractState;
import com.zenyte.game.content.skills.hunter.TrapState;
import com.zenyte.game.content.skills.hunter.node.HunterPrey;
import com.zenyte.game.content.skills.hunter.node.TrapInteractType;
import com.zenyte.game.content.skills.hunter.node.TrapPrey;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.CharacterLoop;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Kris | 25/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
abstract class HunterNPC extends NPC {
    /**
     * Constructor for the abstract {@link HunterNPC} hunter base monster.
     * All classes implementing {@link com.zenyte.game.world.entity.npc.Spawnable} must inherit this fixed-type constructor.
     * @param id the id of the npc
     * @param tile the tile upon which it spawns
     * @param facing the direction the monster shall be facing
     * @param radius the radius of the monster
     */
    HunterNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.radius = 6;
        //Small workaround to nullability and exceptions due to reflection.
        this.prey = TrapPrey.get(id == -1 ? TrapPrey.CRIMSON_SWIFT.getNpcId() : id).orElseThrow(RuntimeException::new);
        this.trapType = prey.getTrap();
        this.forceCheckAggression = true;
        if (prey.getTrap() == TrapType.PITFALL) {
            this.maxDistance = 50;
        }
    }

    @NotNull
    final TrapPrey prey;
    @NotNull
    final TrapType trapType;
    @Nullable
    HunterTrap trap;
    @Nullable
    TrapInteractType interactType;
    @Nullable
    PreyInteractState interactState;
    int delay;
    /**
     * A mutable tile object which is only used by the {@link HunterNPC#canMove(int, int, int)} method.
     * Idea is to prevent creating a new {@link Location} object whenever the {@link NPC} takes another step.
     * So in order to do so, we create a temporary re-usable {@link Location} object whose coordinates we
     * continuously modify as the {@link NPC} continues to move.
     */
    @NotNull
    private final Location nextMovementTile = new Location(0);

    /**
     * Override the movement restriction method so that {@link HunterNPC} which are flying can move over
     * thins such as fences and water, which normally creatures would not be able to cross.
     * @param lastX the x coordinate of the tile the {@link HunterNPC} is standing on at the moment of the call.
     * @param lastY the y coordinate of the tile the {@link HunterNPC} is standing on at the moment of the call.
     * @param dir the direction the {@link HunterNPC} is moving towards.
     * @return whether or not the {@link HunterNPC} can move in the requested direction from the specified location.
     */
    @Override
    protected boolean canMove(final int lastX, final int lastY, final int dir) {
        if (!trapType.equals(TrapType.BIRD_SNARE)) {
            return super.canMove(lastX, lastY, dir);
        }
        final byte x = Utils.DIRECTION_DELTA_X[dir];
        final byte y = Utils.DIRECTION_DELTA_Y[dir];
        nextMovementTile.setLocation(lastX + x, lastY + y, getPlane());
        return !ProjectileUtils.isProjectileClipped(null, null, getLocation(), nextMovementTile, false);
    }

    @Override
    public void processMovement() {
        checkTraps();
        super.processMovement();
    }

    @Override
    public int getRespawnDelay() {
        return 5;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public NPC spawn() {
        stopChasingTrap();
        return super.spawn();
    }

    @Override
    public void processNPC() {
        if (trap != null) {
            //If the player object no longer exists, let's forget about this trap.
            final Player player = trap.getPlayer().get();
            if (player != null && !player.isNulled()) {
                final TrapState state = trap.getState();
                //Only continue chasing after this trap if the trap is still valid. If the trap is being dismantled, failure sequence will be played on it.
                if (state == TrapState.ACTIVE || state == TrapState.PROCESSING || state == TrapState.DISMANTLING) {
                    processTrap();
                    return;
                }
            }
            stopChasingTrap();
        }
        final int delay = randomWalkDelay;
        if (delay > 0) {
            randomWalkDelay--;
        }
        if (combat.process()) {
            return;
        }
        if (isLocked()) {
            return;
        }
        if (delay > 0 || radius <= 0 || ContentConstants.SPAWN_MODE) {
            return;
        }
        if (routeEvent != null || !getWalkSteps().isEmpty() || trap != null) {
            return;
        }
        if (Utils.random(4) != 0 || isFrozen() || isStunned()) {
            return;
        }
        final int moveX = Utils.random(-radius, radius);
        final int moveY = Utils.random(-radius, radius);
        final int respawnX = respawnTile.getX();
        final int respawnY = respawnTile.getY();
        addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius << 1, getSize(), true);
    }

    protected void checkTraps() {
        //If there are no players nearby, there's really no reason to find traps for this given hunter NPC.
        if (!pendingAggressionCheckNPCs.contains(getIndex())) {
            return;
        }
        if (trap != null || Utils.random(6) != 0) {
            return;
        }
        final List<Entity> possibleTargets = getPossibleTargets(EntityType.NPC);
        if (possibleTargets.isEmpty()) {
            return;
        }
        final Entity target = possibleTargets.get(Utils.random(possibleTargets.size() - 1));
        if (!(target instanceof HunterDummyNPC)) {
            return;
        }
        final HunterDummyNPC dummy = (HunterDummyNPC) target;
        final WeakReference<HunterTrap> trapReference = dummy.getTrap();
        if (trapReference == null) {
            return;
        }
        final HunterTrap randomTrap = trapReference.get();
        if (randomTrap == null || randomTrap.isFreshTrap()) {
            return;
        }
        resetWalkSteps();
        stopChasingTrap();
        trap = randomTrap;
        interactType = success(randomTrap, prey) ? TrapInteractType.SUCCESS : TrapInteractType.FAILURE;
        trap.setState(TrapState.PROCESSING);
        interact();
    }

    @Override
    public List<Entity> getPossibleTargets(final EntityType type) {
        if (!possibleTargets.isEmpty()) {
            possibleTargets.clear();
        }
        CharacterLoop.populateEntityList(possibleTargets, getLocation(), HunterUtils.SEEK_DISTANCE, type.getClazz(), predicate);
        return possibleTargets;
    }

    abstract void processTrap();

    void stopChasingTrap() {
        if (trap != null) {
            trap = null;
            setFaceEntity(null);
            interactState = null;
            resetWalkSteps();
            setAnimation(Animation.STOP);
        }
    }

    @Override
    public boolean isPotentialTarget(final Entity entity) {
        if (!(entity instanceof HunterDummyNPC)) {
            return false;
        }
        final HunterDummyNPC npc = (HunterDummyNPC) entity;
        if (npc.getId() == trapType.getActiveDummyNpcId()) {
            final WeakReference<HunterTrap> trapReference = npc.getTrap();
            if (trapReference == null) {
                return false;
            }
            final HunterTrap trap = trapReference.get();
            if (trap == null || !trap.getState().equals(TrapState.ACTIVE)) {
                return false;
            }
            if (ProjectileUtils.isProjectileClipped(this, entity, getLocation(), entity.getLocation(), !trapType.equals(TrapType.BIRD_SNARE))) {
                return false;
            }
            final Player player = trap.getPlayer().get();
            return player != null && !player.isNulled() && player.getSkills().getLevel(SkillConstants.HUNTER) >= prey.getLevel();
        }
        return false;
    }

    public abstract void interact();

    protected static double successProbability(Player player, final int level, @NotNull final HunterPrey prey) {
        final int baseRequirement = prey.baseRequirement();
        final float baseChance = prey.baseCatchRate() / 255.0F;
        int neverFailLevel = prey.neverFailLevel();
        if(player.hasBoon(DoubleChins.class) && prey instanceof BoxTrapHunterNPC boxTrapPrey && boxTrapPrey.prey.isChinchompa()) {
            neverFailLevel -= 20;
        }

        final float adjustmentPercentage = 1 - baseChance;
        final float successPerLevel = adjustmentPercentage / ((float) neverFailLevel - baseRequirement);
        return baseChance + Math.max(0, (level - baseRequirement)) * successPerLevel;
    }

    protected static boolean success(@NotNull final HunterTrap trap, @NotNull final HunterPrey prey) {
        final Player player = trap.getPlayer().get();
        if (player == null || player.isNulled() || player.getSkills().getLevel(SkillConstants.HUNTER) < prey.baseRequirement()) {
            return false;
        }
        return Utils.randomDouble() < successProbability(player, player.getSkills().getLevel(SkillConstants.HUNTER), prey);
    }
}
