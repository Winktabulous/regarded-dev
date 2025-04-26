package com.zenyte.game.world.entity;

import com.near_reality.game.queue.GameQueueStack;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.player.ClockMap;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.areatype.AreaType;
import com.zenyte.game.world.region.areatype.AreaTypes;
import com.zenyte.utils.IntLinkedList;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface Entity extends Position, CharacterLoop, InteractableEntity {

    default void delay(int ticks, final WorldTask task) {
        WorldTasksManager.schedule(task, ticks);
    }
    Logger getLogger();

    int getSize();

    int getClientIndex();

    int getMaxHitpoints();

    boolean isDead();

    /**
     * Transforms an incoming {@link Hit}, this can be to negate it,
     * or reduce/buff its damage.
     *
     * @see AbstractEntity#removeHitpoints(Hit) the subtraction of health by the hit's damage.
     *
     * @param hit the {@link Hit} to be applied to this entity.
     */
    void handleIngoingHit(Hit hit);

    void postProcessHit(Hit hit);

    void sendDeath();

    void autoRetaliate(final Entity source);

    boolean canAttack(final Player source);

    default boolean canAttackInSingleZone(final Entity target) {
        if (target.isForceAttackable()) return true;
        final Entity attacking = getAttackedBy();
        return attacking == null
                || attacking == target
                || getAttackedByDelay() <= WorldThread.getCurrentCycle()
                || attacking.isDead()
                || attacking.isFinished();
    }

    default boolean canAttackSingles(final Entity target, final boolean message) {
        if (target.isForceAttackable()) return true;
        if (target.isMultiArea()) return true;

        final AreaType areaType = target.getAreaType();
        if (AreaTypes.MULTIWAY.equals(areaType)) return true;

        final Entity attacker = getAttackedBy();
        if (attacker != null
                && attacker != target
                && isUnderCombat(0)) {
            return false;
        }

        final Entity attacker_ = target.getAttackedBy();
        return attacker_ == null
                || attacker_ == this
                || !target.isUnderCombat(0);
    }

    int getCombatLevel();

    AbstractEntity.EntityType getEntityType();

    void cancelCombat();

    void processMovement();

    /**
     * Gets the current middle position of the NPC. NOTE: It reuses the existing tile object and sets its value to
     * the current middle tile,
     * therefore modifications to this object aren't suggested, but instead if you wish to obtain a tile you could
     * modify - such as set its
     * X location to something else, construct a new tile object using this.
     *
     * @return the middle tile of the NPC.
     */
    Location getMiddleLocation();

    void handleOutgoingHit(final Entity target, final Hit hit);

    void performDefenceAnimation(Entity attacker);

    void performDeathAnimation();

    List<Entity> getPossibleTargets(final EntityType type, final int radius);

    int getPossibleTargetsDefaultRadius();

    default List<Entity> getPossibleTargets(final EntityType type) {
        return getPossibleTargets(type, getPossibleTargetsDefaultRadius());
    }

    int drainSkill(final int skill, final double percentage, final int minimumDrain);

    int drainSkill(final int skill, final double percentage);

    default int getHitpointsAsPercentage() {
        final int max = getMaxHitpoints();
        if (max == 0) {
            return 0;
        }

        return getHitpoints() * 100 / max;
    }

    int drainSkill(final int skill, final int amount);

    boolean isInitialized();

    boolean isMaximumTolerance();

    boolean isAcceptableTarget(final Entity entity);

    boolean isCrawling();

    void setCrawling(boolean crawling);

    ClockMap getClocks();

    boolean isPotentialTarget(final Entity entity);

    void forceLocation(Location location);

    /**
     * Determines whether this entity will trigger opponent auto retaliate if target is a player.
     */
    default boolean triggersAutoRetaliate() {
        return true;
    }

    default boolean isMovementRestricted() {
        return isFrozen() || isStunned();
    }

    boolean setHitpoints(final int amount);

    void unlink();

    boolean isFacing(final Entity target);

    boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check);

    /**
     * Adds immunity on the character from all damage inflicted by the type specified in parameters.
     *
     * @param type         the hit type.
     * @param milliseconds the duration of the effect in milliseconds.
     */
    void addImmunity(final HitType type, final long milliseconds);

    boolean isImmune(final HitType type);

    int[] getLastWalkTile();

    /**
     * Gets the next position of the entity based on its current walksteps list and run mode.
     *
     * @return next walkstep position, or current position if none is present.
     */
    Location getNextPosition(final int amount);

    void unclip();

    void clip();

    boolean collides(final List<? extends Entity> list, final int x, final int y);

    /**
     * Whether to check if this npc is projectile clipped or not. Used for entities that are ontop of clipped tiles
     * (such as objects),
     * allows players to actually attack those.
     */
    default boolean checkProjectileClip(final Player player, boolean melee) {
        return true;
    }

    int getNextWalkStep();

    void stun(final int ticks);

    void removeStun();

    boolean isStunned();

    boolean isFreezeImmune();

    default boolean isFreezeable() {
        return true;
    }

    boolean isFrozen();

    void resetFreeze();

    void forceFreezeDelay(final int ticks);

    boolean freeze(final int freezeTicks);

    default boolean freeze(final int freezeTicks, final int immunityTicks) {
        return freeze(freezeTicks, immunityTicks, null);
    }

    Consumer<Entity> freezeConsumer = entity -> {
        if (entity instanceof Player player) {
            player.sendMessage("<col=ef1020>You have been frozen!</col>");
        }
    };

    default boolean freezeWithNotification(final int freezeTicks, final int immunityTicks) {
        return freeze(freezeTicks, immunityTicks, freezeConsumer);
    }

    default boolean freezeWithNotification(final int freezeTicks) {
        return freeze(freezeTicks, 0, freezeConsumer);
    }

    void addFreezeImmunity(final int immunityTicks);

    boolean freeze(final int freezeTicks, final int immunityTicks, @Nullable final Consumer<Entity> onFreezeConsumer);

    default boolean isNulled() {
        return false;
    }

    default boolean addWalkSteps(final int destX, final int destY) {
        return addWalkSteps(destX, destY, -1);
    }

    default boolean addWalkSteps(final int destX, final int destY, final int maxStepsCount) {
        return addWalkSteps(destX, destY, maxStepsCount, true);
    }

    double getMagicPrayerMultiplier();

    double getRangedPrayerMultiplier();

    double getMeleePrayerMultiplier();

    void heal(final int amount);

    void applyHit(final Hit hit);

    void addHitbar();

    void processReceivedHits();

    default boolean isDying() {
        return isDead();
    }

    void checkMultiArea();

    void setLocation(final Location tile);

    /**
     * Subtracts the {@link Hit#getDamage()} from this entities {@link #getHitpoints() hitpoints}.
     *
     * @param hit the {@link Hit} to subtract from the hitpoints.
     */
    void removeHitpoints(final Hit hit);

    void processHit(final Hit hit);

    default void faceEntity(final Entity target) {
        setFaceLocation(new Location(target.getLocation().getCoordFaceX(target.getSize()),
                target.getLocation().getCoordFaceY(target.getSize()), target.getPlane()));
    }

    void addReceivedDamage(final Entity source, final int amount, final HitType type);

    default boolean addWalkSteps(final int destX, final int destY, final int maxStepsCount, final boolean check) {
        return WalkStep.addWalkSteps(this, destX, destY, maxStepsCount, check);
    }

    default void appendHitEntry(final HitEntry hitEntry) {
        if (!hitEntry.isFreshEntry()) {
            return;
        }
        hitEntry.setFreshEntry(true);
    }

    void scheduleHit(final Entity source, @NotNull final Hit hit, final int delay);

    void iterateScheduledHits();

    default boolean ignoreUnderneathProjectileCheck() {
        return false;
    }

    default void processEntity() {
        iterateScheduledHits();
        processReceivedHits();
        queueCycle();
        getToxins().process();
        processMovement();
    }

    void queueCycle();

    void checkFacedEntity();

    void resetMasks();

    void reset();

    void setForceMultiArea(final boolean forceMultiArea);

    boolean needMapUpdate();

    int getChunkXInScene(final Location location);

    int getChunkYInScene(final Location location);

    int getXInScene(final Location location);

    int getYInScene(final Location location);

    void faceObject(final WorldObject object);

    default void faceDirection(final Direction direction) {
        final Location middle = getMiddleLocation();
        final int size = getSize() / 2;
        switch (direction) {
            case SOUTH:
                setFaceLocation(new Location(middle.getX() + size, middle.getY() - 15, middle.getPlane()));
                return;
            case EAST:
                setFaceLocation(new Location(middle.getX() + 15, middle.getY() + size, middle.getPlane()));
                return;
            case SOUTH_WEST:
                setFaceLocation(new Location(middle.getX() - 15, middle.getY() - 15, middle.getPlane()));
                break;
            case WEST:
                setFaceLocation(new Location(middle.getX() - 15, middle.getY() + size, middle.getPlane()));
                return;
            case NORTH:
                setFaceLocation(new Location(middle.getX() + size, middle.getY() + 15, middle.getPlane()));
                return;
            case NORTH_WEST:
                setFaceLocation(new Location(middle.getX() - 15, middle.getY() + 15, middle.getPlane()));
                return;
            case NORTH_EAST:
                setFaceLocation(new Location(middle.getX() + 15, middle.getY() + 15, middle.getPlane()));
                break;
            case SOUTH_EAST:
                setFaceLocation(new Location(middle.getX() + 15, middle.getY() - 15, middle.getPlane()));
                break;
        }
    }

    void setFacedInteractableEntity(final InteractableEntity facedInteractableEntity);

    void setFaceLocation(final Location tile);

    void loadMapRegions(final boolean init);

    default Location getFaceLocation(final Entity target) {
        return getFaceLocation(target, getSize());
    }

    /**
     * Gets the coordinate of the NPC's head, used for large npcs.
     *
     * @return head's location.
     */
    default Location getFaceLocation(final Entity target, final int npcSize) {
        if (target == null) {
            return getLocation();
        }
        final Location middle = getMiddleLocation();
        final float size = npcSize >> 1;
        double degrees = Math.toDegrees(Math.atan2(target.getY() - middle.getY(), target.getX() - middle.getX()));
        if (degrees < 0) {
            degrees += 360;
        }
        final double angle = Math.toRadians(degrees);
        final int px = (int) Math.round(middle.getX() + size * Math.cos(angle));
        final int py = (int) Math.round(middle.getY() + size * Math.sin(angle));
        return new Location(px, py, middle.getPlane());
    }

    default int getRoundedDirection() {
        return getRoundedDirection(0);
    }

    default int getRoundedDirection(final int offset) {
        return getRoundedDirection(getDirection(), offset);
    }

    default int getRoundedDirection(final int baseDirection, final int offset) {
        return Entities.getRoundedDirection(baseDirection, offset);
    }

    default Location getFaceLocation(final Entity target, final int npcSize, final int offset) {
        if (target == null) {
            return getLocation();
        }
        final Location middle = getMiddleLocation();
        final float size = (float) (npcSize >> 1);
        final Location targetMiddle = target.getMiddleLocation();
        double degrees = Math.toDegrees(((int) ((Math.atan2(targetMiddle.getY() - middle.getY(),
                targetMiddle.getX() - middle.getX()) * 325.949) + offset) & 2047) / 325.949);
        if (degrees < 0) {
            degrees += 360;
        }
        final double angle = Math.toRadians(degrees);
        final int tileX = (int) Math.round(middle.getX() + size * Math.cos(angle));
        final int tileY = (int) Math.round(middle.getY() + size * Math.sin(angle));
        return new Location(tileX, tileY, middle.getPlane());
    }

    default boolean calcFollow(final Position target, final int maxStepsCount, final boolean calculate,
                               final boolean intelligent, final boolean checkEntities) {
        return WalkStep.calcFollow(this, target, maxStepsCount, calculate, intelligent, checkEntities);
    }

    default boolean isProjectileClipped(final Position target, final boolean closeProximity) {
        return ProjectileUtils.isProjectileClipped(this, target instanceof Entity ? (Entity) target : null, this,
                target, closeProximity);
    }

    default boolean addWalkStepsInteract(final int destX, final int destY, final int maxStepsCount, final int size,
                                         final boolean calculate) {
        return WalkStep.addWalkStepsInteract(this, destX, destY, maxStepsCount, size, size, calculate);
    }

    int getNextWalkStepPeek();

    int getX();

    int getY();

    int getPlane();

    void resetWalkSteps();

    void setAnimation(final Animation animation);

    void setInvalidAnimation(final Animation animation);

    void setUnprioritizedAnimation(final Animation animation);

    void setGraphics(final Graphics graphics);

    default float getXpModifier(Hit hit) {
        return 1.0F;
    }

    long getFindTargetDelay();

    void setFindTargetDelay(long findTargetDelay);

    Player getMostDamagePlayer();

    /**
     * Locks the entity for the requested amount of ticks.
     *
     * @param time in ticks.
     */
    void lock(final int time);

    void unlock();

    boolean hasWalkSteps();

    Player getMostDamageNonIronmanPlayer();

    boolean hasDealtEnoughDamage(@NotNull final Player killer, float thresholdPercentage);

    void setRunSilent(final boolean run);

    default void setRunSilent(final int ticks) {
        setRunSilent(true);
        WorldTasksManager.schedule(() -> setRunSilent(false), ticks);
    }

    boolean isLocked();

    void lock();

    Toxins getToxins();

    @Override
    default Location getPosition() {
        return getLocation();
    }

    long getFreezeDelay();

    long getFreezeImmunity();

    long getLockDelay();

    Location getLocation();

    public RouteEvent<?, ?> getRouteEvent();

    public void setRouteEvent(final RouteEvent<?, ?> event);

    public long getProtectionDelay();

    void setProtectionDelay(long protectionDelay);

    boolean isForceAttackable();

    void setForceAttackable(boolean forceAttackable);

    int getHitpoints();

    int getMapSize();

    void setMapSize(int mapSize);

    int getIndex();

    void setIndex(int index);

    int getLastRegionId();

    void setLastRegionId(int lastRegionId);

    int getLastChunkId();

    void setLastChunkId(int lastChunkId);

    int getDirection();

    void setDirection(int direction);

    int getLastMovementType();

    AreaType getAreaType();

    void setAreaType(AreaType areaType);

    default boolean isMultiArea() {
        return AreaTypes.MULTIWAY.equals(getAreaType());
    }

    boolean isForceMultiArea();

    boolean isTeleported();

    void setTeleported(boolean teleported);

    boolean isFinished();

    void setFinished(boolean finished);

    boolean isAtDynamicRegion();

    Location getLastLoadedMapRegionTile();

    void setLastLoadedMapRegionTile(Location lastLoadedMapRegionTile);

    Location getLastLocation();

    void setLastLocation(Location lastLocation);

    Location getNextLocation();

    Entity getAttacking();

    void setAttacking(Entity attacking);

    Entity getAttackedBy();

    void setAttackedBy(Entity attackedBy);

    long getAttackedByDelay();

    void setAttackedByDelay(long attackedByDelay);

    default void resetAttackedByDelay() {
        setAttackedByDelay(0);
    }

    long getAttackedAtTick();

    void setAttackedAtTick(long attackedTick);

    default boolean isUnderCombat() {
        return isUnderCombat(7);
    }

    default boolean isUnderCombat(final int ticksAfterLastAttack) {
        return (getAttackedByDelay() + ticksAfterLastAttack) > WorldThread.getCurrentCycle();
    }

    /**
     * The tick that the entity was actually attacked. This is used for logout timer.
     * <p>
     * This is different than {@link #getAttackedByDelay()} because it's the actual tick the entity was attacked,
     * rather than the tick that their delay will expire.
     */
    long getAttackedTick();

    /**
     * Sets the tick that the entity was actually attacked. This is used for logout timer.
     * <p>
     * This is different than {@link #setAttackingDelay(long)} because it's the actual tick the entity was attacked,
     * rather than the tick that their delay will expire.
     */
    void setAttackedTick(long attackedTick);

    default boolean isLogoutPrevented(final int ticksAfterAttacked) {
        return getAttackedTick() + ticksAfterAttacked > WorldThread.getCurrentCycle();
    }

    default boolean isLogoutPrevented() {
        return isLogoutPrevented(17); // logout timer is 17 ticks!
    }

    long getLastReceivedHit();

    void setLastReceivedHit(long lastReceivedHit);

    long getLastAnimation();

    void setLastAnimation(long lastAnimation);

    long getAttackingDelay();

    void setAttackingDelay(long attackingDelay);

    Location getFaceLocation();

    IntSet getMapRegionsIds();

    IntSet getLastMapRegionsIds();

    IntLinkedList getWalkSteps();

    List<Hit> getReceivedHits();

    List<Hit> getNextHits();

    Object2ObjectMap<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>> getReceivedDamage();

    Map<Object, Object> getTemporaryAttributes();

    UpdateFlags getUpdateFlags();

    List<HitBar> getHitBars();

    int getFaceEntity();

    void setFaceEntity(final Entity entity);

    Animation getAnimation();

    Graphics getGraphics();

    ForceMovement getForceMovement();

    void setForceMovement(final ForceMovement movement);

    ForceTalk getForceTalk();

    Tinting getTinting();

    void setTinting(final Tinting tinting);

    @Override
    default int getLength() {
        return getSize();
    }

    @Override
    default int getWidth() {
        return getSize();
    }

    default void setForceTalk(final String string) {
        setForceTalk(ForceTalk.get(string));
    }

    void setForceTalk(final ForceTalk talk);

    int getWalkDirection();

    void setWalkDirection(int walkDirection);

    int getRunDirection();

    void setRunDirection(int runDirection);

    int getCrawlDirection();

    void setCrawlDirection(int crawlDirection);

    int getSceneBaseChunkId();

    void setSceneBaseChunkId(int sceneBaseChunkId);

    boolean isRun();

    void setRun(final boolean run);

    boolean isSilentRun();

    boolean isCantInteract();

    void setCantInteract(boolean cantInteract);

    long getLastFaceEntityDelay();

    void setLastFaceEntityDelay(long lastFaceEntityDelay);

    GameQueueStack getQueueStack();

    default int getMagicLevel(){
        return 1;
    }



    public enum EntityType {
        PLAYER(Player.class),
        NPC(com.zenyte.game.world.entity.npc.NPC.class),
        BOTH(Entity.class);
        private final Class<? extends Entity> clazz;

        EntityType(Class<? extends Entity> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends Entity> getClazz() {
            return clazz;
        }
    }

}
