package com.zenyte.game.world.entity;

import com.google.gson.annotations.Expose;
import com.near_reality.game.queue.GameQueueStack;
import com.near_reality.game.world.Boundary;
import com.near_reality.game.world.PlayerEvent;
import com.near_reality.game.world.entity.FastCollisionCheckKt;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.player.ClockMap;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicRegion;
import com.zenyte.game.world.region.areatype.AreaType;
import com.zenyte.game.world.region.areatype.AreaTypes;
import com.zenyte.utils.IntLinkedList;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Pair;
import mgi.types.config.ObjectDefinitions;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractEntity implements Entity {
    private static final Logger log = LoggerFactory.getLogger(AbstractEntity.class);
    public static final int[] MAP_SIZES = {104, 120, 136, 168, 72};

    protected transient RouteEvent<?, ?> routeEvent;
    @Expose
    protected Toxins toxins = new Toxins(this);
    @Expose
    protected Location location;
    protected transient HitEntryList scheduledHits = new HitEntryList();
    /**
     * A delay that's referenced in {@link CombatUtilities#processHit} - if this value is above the current time in
     * milliseconds, the hit will
     * not be processed - it's used to prevent entities from being hit when insta-leaving areas like raids.
     */
    protected transient long protectionDelay;
    protected transient Location middleTile;
    @Expose
    protected transient int hitpoints;
    protected transient int direction;
    protected transient int lastMovementType;
    @Expose
    protected boolean run;
    @Expose
    protected transient boolean silentRun;
    protected transient HitBar hitBar = new EntityHitBar(this);
    /**
     * A list of possible entities. It's better to keep one list and clear it on request, rather than re-create a list
     * every time this is
     * called.
     */
    protected transient List<Entity> possibleTargets = new ObjectArrayList<>();
    private transient long freezeDelay;
    private transient long freezeImmunity;
    private transient long lockDelay;
    /**
     * Sets a temporary delay during which the entity will not be added to the list of possible targets.
     */
    private transient long findTargetDelay;
    private transient boolean forceAttackable;
    private transient long lastFaceEntityDelay;
    private transient int mapSize;
    private transient int index;
    private transient int lastRegionId;
    private transient int lastChunkId;
    private transient AreaType areaType;
    //private transient boolean multiArea;
    private transient boolean cantInteract;

    private final transient int[] lastSteps = new int[2];
    private transient InteractableEntity facedInteractableEntity = null;

    private transient boolean forceMultiArea;
    protected transient boolean teleported;
    private transient boolean finished;
    private transient boolean isAtDynamicRegion;
    private transient Location lastLoadedMapRegionTile;
    protected transient Location lastLocation;
    protected transient Location nextLocation;
    private transient Entity attacking;
    private transient Entity attackedBy;
    private transient long attackedByDelay;
    private transient long attackedTick;
    private transient long attackedAtTick;
    private transient long lastReceivedHit;
    protected transient long lastAnimation;
    private transient long attackingDelay;
    private transient Location faceLocation;
    protected transient IntSet mapRegionsIds = new IntOpenHashSet();
    private final transient IntSet lastMapRegionsIds = new IntOpenHashSet();
    protected transient IntLinkedList walkSteps = new IntLinkedList();
    protected transient List<Hit> receivedHits = new ObjectArrayList<>();
    protected transient List<Hit> nextHits = new ObjectArrayList<>();
    private final transient Object2ObjectMap<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>>
            receivedDamage = new Object2ObjectOpenHashMap<>();
    protected transient Map<Object, Object> temporaryAttributes = new HashMap<>();
    protected transient UpdateFlags updateFlags = new UpdateFlags(this);
    protected transient List<HitBar> hitBars = new ObjectArrayList<>();
    protected transient int faceEntity = -1;
    protected transient Animation animation;
    private transient Graphics graphics;
    private transient ForceMovement forceMovement;
    private transient ForceTalk forceTalk;
    private transient Tinting tinting;
    protected transient int walkDirection;
    protected transient int runDirection;
    protected transient int crawlDirection;

    @Override
    public boolean isCrawling() {
        return crawlInterval > 0;
    }

    @Override
    public void setCrawling(boolean crawling) {
        this.crawlInterval = crawling ? 1 : 0;
    }

    protected transient int crawlInterval;
    private transient int sceneBaseChunkId;

    @Override
    public ClockMap getClocks() {
        return clocks;
    }

    private final transient ClockMap clocks = new ClockMap();

    @Override
    public void forceLocation(final Location location) {
        this.location = new Location(location);
    }

    @Override
    public boolean setHitpoints(final int amount) {
        final boolean dead = isDead();
        this.hitpoints = amount;
        if (!dead && hitpoints <= 0) {
            sendDeath();
            return true;
        }
        return false;
    }

    public void overrideHitbarType(int type) {
        if(hitBar instanceof EntityHitBar ehp) {
            ehp.setOverrideType(type);
        }
    }

    @Override
    public boolean isFacing(final Entity target) {
        return this.faceEntity == target.getClientIndex();
    }

    private final transient long[] immunities = new long[HitType.values.length];

    @Override
    public void addImmunity(final HitType type, final long milliseconds) {
        immunities[type.ordinal()] = System.currentTimeMillis() + milliseconds;
    }

    @Override
    public boolean isImmune(final HitType type) {
        return immunities[type.ordinal()] > System.currentTimeMillis();
    }

    @Override
    public final int[] getLastWalkTile() {
        if (walkSteps.size() == 0) {
            lastSteps[0] = getX();
            lastSteps[1] = getY();
            return lastSteps;
        }
        final int hash = walkSteps.getLast();
        lastSteps[0] = WalkStep.getNextX(hash);
        lastSteps[1] = WalkStep.getNextY(hash);
        return lastSteps;
    }

    @Override
    public Location getNextPosition(final int amount) {
        final int size = Math.min(walkSteps.size(), amount);
        if (size == 0) {
            return location;
        }
        final int nextTileHash = walkSteps.nthPeek(size - 1);
        final int x = WalkStep.getNextX(nextTileHash);
        final int y = WalkStep.getNextY(nextTileHash);
        return new Location(x, y, location.getPlane());
    }

    /**
     * Blocks all incoming hits that were scheduled prior to the method call, as well as one tick afterwards for
     * extra protection.
     */
    public void blockIncomingHits(int ticks) {
        protectionDelay = WorldThread.getCurrentCycle() + ticks;
        receivedHits.clear();
        receivedDamage.clear();
    }

    public void blockIncomingHits() {
        blockIncomingHits(1);
    }

    @Override
    public boolean collides(final List<? extends Entity> list, final int x, final int y) {
        if (list.isEmpty()) return false;
        return FastCollisionCheckKt.collides(this, list.iterator(), x, y);
    }

    @Override
    public int getNextWalkStep() {
        if (walkSteps.isEmpty()) {
            return 0;
        }
        return walkSteps.remove();
    }

    private transient long stunDelay;

    @Override
    public void stun(final int ticks) {
        if (isStunned()) {
            return;
        }
        stunDelay = WorldThread.getCurrentCycle() + ticks;
        resetWalkSteps();
        setRouteEvent(null);
    }

    @Override
    public void removeStun() {
        stunDelay = 0;
    }

    @Override
    public boolean isStunned() {
        return stunDelay > WorldThread.getCurrentCycle();
    }

    @Override
    public boolean isFreezeImmune() {
        return freezeImmunity > WorldThread.getCurrentCycle();
    }

    @Override
    public boolean isFrozen() {
        return freezeDelay > WorldThread.getCurrentCycle();
    }

    @Override
    public void resetFreeze() {
        freezeDelay = 0;
        freezeImmunity = 0;
    }

    @Override
    public void forceFreezeDelay(final int ticks) {
        this.freezeDelay = WorldThread.getCurrentCycle() + ticks;
    }

    @Override
    public boolean freeze(final int freezeTicks) {
        return freeze(freezeTicks, 0);
    }

    @Override
    public void addFreezeImmunity(final int immunityTicks) {
        freezeImmunity = WorldThread.getCurrentCycle() + immunityTicks;
    }

    @Override
    public boolean freeze(final int freezeTicks, final int immunityTicks,
                          @Nullable final Consumer<Entity> onFreezeConsumer) {
        if (!isFreezeable() || isFreezeImmune()) {
            return false;
        }
        final long currentCycle = WorldThread.getCurrentCycle();
        freezeImmunity = currentCycle + freezeTicks + immunityTicks;
        freezeDelay = currentCycle + freezeTicks;
        resetWalkSteps();
        setRouteEvent(null);
        if (onFreezeConsumer != null) {
            onFreezeConsumer.accept(this);
        }
        return true;
    }

    @Override
    public void heal(final int amount) {
        setHitpoints(Math.min((hitpoints + amount), (getMaxHitpoints())));
    }

    @Override
    public void applyHit(final Hit hit) {
        if (isDead() && hit.getHitType() != HitType.HEALED || isFinished()) {
            return;
        }
        handleIngoingHit(hit);
        receivedHits.add(hit);
        addHitbar();
    }

    @Override
    public void addHitbar() {
        if (!getHitBars().contains(hitBar)) {
            getHitBars().add(hitBar);
        }
    }

    private final transient BiPredicate<Boolean, Hit> hitPredicate = (locked, hit) -> {
        if (locked && !hit.executeIfLocked()) {
            return false;
        }
        final Predicate<Hit> predicate = hit.getPredicate();
        if (predicate != null) {
            if (predicate.test(hit)) {
                return true;
            }
        }
        this.postProcessHit(hit);
        processHit(hit);
        return true;
    };

    @Override
    public void processReceivedHits() {
        if (receivedHits.isEmpty())
            return;
        final var locked = isLocked();
        var toRemove = new ObjectArrayList<>(receivedHits);
        toRemove.removeIf(hit -> !hitPredicate.test(locked, hit));
        receivedHits.removeAll(toRemove);
    }


    @Override
    public void checkMultiArea() {
        areaType = forceMultiArea
                ? AreaTypes.MULTIWAY
                : World.checkAreaType(getLocation());
    }

    @Override
    public void setLocation(final Location tile) {
        if (tile == null) {
            return;
        }
        nextLocation = new Location(tile);
    }

    @Override
    public void removeHitpoints(final Hit hit) {
        if (isDead()) {
            return;
        }
        int damage = hit.getDamage();
        if (damage > hitpoints) {
            damage = hitpoints;
        }
        addReceivedDamage(hit.getSource(), damage, hit.getHitType());
        setHitpoints(hitpoints - damage);
    }

    @Override
    public void processHit(final Hit hit) {
        if (hit.getScheduleTime() < protectionDelay) {
            return;
        }
        if (hit.getDamage() > Short.MAX_VALUE) {
            hit.setDamage(Short.MAX_VALUE);
        }
        getUpdateFlags().flag(UpdateFlag.HIT);
        nextHits.add(hit);
        addHitbar();
        lastReceivedHit = Utils.currentTimeMillis();
        if (hit.getHitType() == HitType.HEALED) {
            heal(hit.getDamage());
        } else if (!HitType.SHIELD_DOWN.equals(hit.getHitType()) && !HitType.CORRUPTION.equals(hit.getHitType())){
            removeHitpoints(hit);
        }
    }

    private static final long TWENTY_MINUTES_IN_MILLIS = TimeUnit.MINUTES.toMillis(20);

    @Override
    public void addReceivedDamage(final Entity source, final int amount, final HitType type) {
        if (!(source instanceof final Player player) || player.getPlayerInformation() == null) {
            return;
        }
        World.postEvent(new PlayerEvent.DamageDealt(player, this, amount));
        final Pair<String, GameMode> pair = new Pair<>(player.getUsername(), player.getGameMode());
        ObjectArrayList<ReceivedDamage> list = receivedDamage.get(pair);
        if (list == null) {
            receivedDamage.put(pair, list = new ObjectArrayList<>());
        }
        if (amount >= 0) {
            list.add(new ReceivedDamage(System.currentTimeMillis() + TWENTY_MINUTES_IN_MILLIS, amount, type));
        }
    }

    private final transient Predicate<HitEntry> hitEntryPredicate = hit -> {
        if (isDead()) {
            return true;
        }
        appendHitEntry(hit);
        final int delay = hit.getAndDecrement();
        if (delay <= 0) {
            performDefenceAnimation(hit.getHit().getSource());
        }
        if (delay < 0) {
            CombatUtilities.processHit(this, hit.getHit());
            return true;
        }
        return false;
    };

    @Override
    public void scheduleHit(final Entity source, @NotNull final Hit hit, final int delay) {
        scheduledHits.add(new HitEntry(source, delay, hit));
    }

    public void scheduleHit(final Entity source, @NotNull final Hit hit, final int delay, boolean freshEntry) {
        scheduledHits.add(new HitEntry(source, delay, hit, freshEntry));
    }

    @Override
    public final void iterateScheduledHits() {
        final Iterator<HitEntry> each = scheduledHits.iterator();
        while (each.hasNext()) {
            if (hitEntryPredicate.test(each.next())) {
                each.remove();
            }
        }
    }

    @Override
    public void queueCycle() {
        /* flag whether or not a new queue should be polled this cycle */
        boolean pollQueue = queueStack.getIdle();

        GameQueueStack queueStack = getQueueStack();
        try {
            queueStack.processCurrent();
        } catch (Throwable t) {
            queueStack.discardCurrent();
            log.error("Queue process error (" + this + ")", t);
        }

        if (pollQueue) {
            try {
                queueStack.pollPending();
            } catch (Throwable t) {
                log.error("Queue poll error (" + this + ")", t);
            }
        }
    }

    @Override
    public void checkFacedEntity() {
        if (walkDirection != -1 || runDirection != -1 || crawlDirection != -1) return;
        InteractableEntity interactableEntity = this.facedInteractableEntity;
        if (interactableEntity == null) return;
        double centerX = interactableEntity.getLocation().getX() + (interactableEntity.getWidth() / 2f - 0.5f);
        double centerY = interactableEntity.getLocation().getY() + (interactableEntity.getLength() / 2f - 0.5f);
        double offsetX = centerX - getX();
        double offsetY = centerY - getY();
        final Location location = new Location((int) (getX() + offsetX * 2), (int) (getY() + offsetY * 2), getPlane());
        this.facedInteractableEntity = null;
        if (this.location.matches(location)) return;
        setFaceLocation(location);
    }

    @Override
    public void resetMasks() {
        if (updateFlags.isUpdateRequired()) {
            updateFlags.reset();
        }
        if (!hitBars.isEmpty()) {
            hitBars.clear();
        }
        if (!nextHits.isEmpty()) {
            nextHits.clear();
        }
        if (isCrawling()) {
            this.crawlInterval++;
        }
        this.walkDirection = -1;
        this.runDirection = -1;
        this.crawlDirection = -1;
    }

    @Override
    public void reset() {
        setHitpoints(getMaxHitpoints());
        receivedHits.clear();
        walkSteps.clear();
        toxins.reset();
        receivedDamage.clear();
        hitBars.clear();
        nextHits.clear();
    }

    @Override
    public void setForceMultiArea(final boolean forceMultiArea) {
        this.forceMultiArea = forceMultiArea;
        checkMultiArea();
    }

    @Override
    public final boolean needMapUpdate() {
        if (lastLoadedMapRegionTile == null) {
            return false;
        }
        return Math.abs(lastLoadedMapRegionTile.getChunkX() - location.getChunkX()) >= 5 || Math.abs(lastLoadedMapRegionTile.getChunkY() - location.getChunkY()) >= 5;
    }

    @Override
    public int getChunkXInScene(final Location location) {
        ////(x) | (y << 11) | (plane << 22)
        return location.getChunkX() - (sceneBaseChunkId & 2047);
        //MapUtils.decode(Structure.CHUNK, entity.getSceneBaseChunkId())[0];
    }

    @Override
    public int getChunkYInScene(final Location location) {
        return location.getChunkY() - (sceneBaseChunkId >> 11 & 2047);
        //MapUtils.decode(Structure.CHUNK, entity.getSceneBaseChunkId())[1];
    }

    @Override
    public int getXInScene(final Location location) {
        return location.getX() - ((sceneBaseChunkId & 2047) << 3);
        //MapUtils.decode(Structure.CHUNK, entity.getSceneBaseChunkId())[0] * 8;
    }

    @Override
    public int getYInScene(final Location location) {
        return location.getY() - ((sceneBaseChunkId >> 11 & 2047) << 3);
        //MapUtils.decode(Structure.CHUNK, entity.getSceneBaseChunkId())[1] * 8;
    }

    @Override
    public final void faceObject(final WorldObject object) {
        final ObjectDefinitions objectDef = object.getDefinitions();
        final float preciseX = object.getPreciseCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(),
                object.getRotation());
        final float preciseY = object.getPreciseCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(),
                object.getRotation());
        if (preciseX == getX() && preciseY == getY()) {
            return;
        }
        faceLocation = new Location((int) preciseX, (int) preciseY, getPlane());
        direction = DirectionUtil.getFaceDirection(preciseX - getX(), preciseY - getY());
        getUpdateFlags().flag(UpdateFlag.FACE_COORDINATE);
    }

    @Override
    public void setFacedInteractableEntity(final InteractableEntity facedInteractableEntity) {
        final Location location = new Location(facedInteractableEntity.getLocation());
        final int width = facedInteractableEntity.getWidth();
        final int length = facedInteractableEntity.getLength();
        this.facedInteractableEntity = new InteractableEntity() {
            @NotNull
            @Override
            public Location getLocation() {
                return location;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getLength() {
                return length;
            }
        };
    }

    @Override
    public void setFaceLocation(final Location tile) {
        faceLocation = tile;
        this.facedInteractableEntity = null;
        final Location middle = getMiddleLocation();
        direction = DirectionUtil.getFaceDirection(tile.getX() - middle.getX(), tile.getY() - middle.getY());
        getUpdateFlags().flag(UpdateFlag.FACE_COORDINATE);
    }

    @Override
    public void loadMapRegions(final boolean init) {
        lastMapRegionsIds.clear();
        lastMapRegionsIds.addAll(mapRegionsIds);
        mapRegionsIds.clear();
        isAtDynamicRegion = false;
        final int sceneChunksRadio = MAP_SIZES[mapSize] / 16;
        final int chunkX = location.getChunkX();
        final int chunkY = location.getChunkY();
        final int mapHash = MAP_SIZES[mapSize] >> 4;
        final int minRegionX = (chunkX - mapHash) / 8;
        final int minRegionY = (chunkY - mapHash) / 8;
        final int sceneBaseChunkX = (chunkX - sceneChunksRadio);
        final int sceneBaseChunkY = (chunkY - sceneChunksRadio);
        final boolean isPlayer = this instanceof Player;
        for (int xCalc = Math.max(minRegionX, 0); xCalc <= ((chunkX + mapHash) / 8); xCalc++) {
            for (int yCalc = Math.max(minRegionY, 0); yCalc <= ((chunkY + mapHash) / 8); yCalc++) {
                final int regionId = yCalc + (xCalc << 8);
                if (DynamicRegion.isDynamicRegion(World.getRegion(regionId, isPlayer))) {
                    isAtDynamicRegion = true;
                }
                mapRegionsIds.add(regionId);
            }
        }
        lastLoadedMapRegionTile = new Location(getX(), getY(), getPlane());
        sceneBaseChunkId = sceneBaseChunkX | (sceneBaseChunkY << 11);
    }

    @Override
    public int getNextWalkStepPeek() {
        if (walkSteps.isEmpty()) {
            return 0;
        }
        return walkSteps.peek();
    }

    @Override
    public int getX() {
        return location.getX();
    }

    @Override
    public int getY() {
        return location.getY();
    }

    @Override
    public int getPlane() {
        return location.getPlane();
    }

    @Override
    public void resetWalkSteps() {
        if (!walkSteps.isEmpty()) {
            walkSteps.clear();
        }
    }

    @Override
    public void setGraphics(final Graphics graphics) {
        this.graphics = graphics;
        updateFlags.flag(UpdateFlag.GRAPHICS);
    }

    @Override
    public long getFindTargetDelay() {
        return findTargetDelay;
    }

    @Override
    public void setFindTargetDelay(long findTargetDelay) {
        this.findTargetDelay = findTargetDelay;
    }

    @Override
    public final Player getMostDamagePlayer() {
        final MutableInt damage = new MutableInt();
        final MutableObject<String> player = new MutableObject<>();
        final MutableInt currentDamage = new MutableInt();
        try {
            receivedDamage.object2ObjectEntrySet().removeIf(entry -> {
                final String source = entry.getKey().getFirst();
                currentDamage.setValue(0);
                entry.getValue().removeIf(pair -> {
                    if (pair.getTime() < System.currentTimeMillis()) {
                        return true;
                    }
                    currentDamage.add(pair.getDamage());
                    return false;
                });
                if (currentDamage.longValue() > damage.longValue()) {
                    damage.setValue(currentDamage.intValue());
                    player.setValue(source);
                }
                return false;
            });
        } catch (Exception e) {
            log.error("", e);
        }
        final String value = player.getValue();
        return value == null ? null : World.getPlayer(value).orElse(null);
    }

    public final ArrayList<Player> getPlayersWithKillCredit() {
        ArrayList<Player> players = new ArrayList<Player>();
        receivedDamage.object2ObjectEntrySet().forEach(entry -> World.getPlayer(String.valueOf(entry.getKey())).ifPresent(players::add));
        return players;
    }

    @Override
    public void lock(final int time) {
        lockDelay = Utils.currentTimeMillis() + (time * 600L);
    }

    @Override
    public void unlock() {
        lockDelay = 0;
    }

    @Override
    public boolean hasWalkSteps() {
        return !walkSteps.isEmpty();
    }

    @Override
    public final Player getMostDamageNonIronmanPlayer() {
        final MutableInt damage = new MutableInt();
        final MutableObject<String> player = new MutableObject<>();
        final MutableInt currentDamage = new MutableInt();
        try {
            receivedDamage.object2ObjectEntrySet().removeIf(entry -> {
                final String source = entry.getKey().getFirst();
                final Optional<Player> optionalPlayer = World.getPlayer(source);
                if (optionalPlayer.isEmpty() || optionalPlayer.get().isIronman()) {
                    return false;
                }
                currentDamage.setValue(0);
                entry.getValue().removeIf(pair -> {
                    if (pair.getTime() < System.currentTimeMillis()) {
                        return true;
                    }
                    currentDamage.add(pair.getDamage());
                    return false;
                });
                if (currentDamage.longValue() > damage.longValue()) {
                    damage.setValue(currentDamage.intValue());
                    player.setValue(source);
                }
                return false;
            });
        } catch (Exception e) {
            log.error("", e);
        }
        final String value = player.getValue();
        return value == null ? null : World.getPlayer(value).orElse(null);
    }

    @Override
    public final boolean hasDealtEnoughDamage(@NotNull final Player killer, float thresholdPercentage) {
        return hasDealtEnoughDamage(this, killer, thresholdPercentage);
    }

    public static boolean hasDealtEnoughDamage(AbstractEntity abstractEntity, @NotNull Player killer, float thresholdPercentage) {
        final MutableInt playerDamage = new MutableInt();
        final MutableInt totalDamage = new MutableInt();
        try {
            abstractEntity.getReceivedDamage().object2ObjectEntrySet().removeIf(entry -> {
                final String source = entry.getKey().getFirst();
                final boolean isPlayer = source.equals(killer.getUsername());
                entry.getValue().removeIf(pair -> {
                    if (pair.getTime() < System.currentTimeMillis()) {
                        return true;
                    }
                    final int operand = pair.getDamage();
                    totalDamage.add(operand);
                    if (isPlayer) {
                        playerDamage.add(operand);
                    }
                    return false;
                });
                return false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerDamage.intValue() > (Math.min(totalDamage.intValue(), abstractEntity.getMaxHitpoints()) * thresholdPercentage);
    }

    @Override
    public void setRunSilent(final boolean run) {
        silentRun = run;
    }

    @Override
    public boolean isLocked() {
        return lockDelay > Utils.currentTimeMillis();
    }

    @Override
    public void lock() {
        lockDelay = Long.MAX_VALUE;
    }

    @Override
    public Toxins getToxins() {
        return toxins;
    }

    @Override
    public long getFreezeDelay() {
        return freezeDelay;
    }

    @Override
    public long getFreezeImmunity() {
        return freezeImmunity;
    }

    @Override
    public long getLockDelay() {
        return lockDelay;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public RouteEvent<?, ?> getRouteEvent() {
        return routeEvent;
    }

    @Override
    public void setRouteEvent(final RouteEvent<?, ?> event) {
        if (event == null) {
            if (routeEvent != null) {
                final Runnable failure = routeEvent.getOnFailure();
                if (failure != null) {
                    failure.run();
                }
            }
        }
        routeEvent = event;
    }

    @Override
    public long getProtectionDelay() {
        return protectionDelay;
    }

    @Override
    public void setProtectionDelay(long protectionDelay) {
        this.protectionDelay = protectionDelay;
    }

    @Override
    public boolean isForceAttackable() {
        return forceAttackable;
    }

    @Override
    public void setForceAttackable(boolean forceAttackable) {
        this.forceAttackable = forceAttackable;
    }

    @Override
    public int getHitpoints() {
        return hitpoints;
    }

    @Override
    public int getMapSize() {
        return mapSize;
    }

    @Override
    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getLastRegionId() {
        return lastRegionId;
    }

    @Override
    public void setLastRegionId(int lastRegionId) {
        this.lastRegionId = lastRegionId;
    }

    @Override
    public int getLastChunkId() {
        return lastChunkId;
    }

    @Override
    public void setLastChunkId(int lastChunkId) {
        this.lastChunkId = lastChunkId;
    }

    @Override
    public int getDirection() {
        return direction;
    }

    @Override
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public int getLastMovementType() {
        return lastMovementType;
    }

    @Override
    public AreaType getAreaType() {
        return areaType;
    }

    @Override
    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
    }

    @Override
    public boolean isForceMultiArea() {
        return forceMultiArea;
    }

    @Override
    public boolean isTeleported() {
        return teleported;
    }

    @Override
    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean isAtDynamicRegion() {
        return isAtDynamicRegion;
    }

    @Override
    public Location getLastLoadedMapRegionTile() {
        return lastLoadedMapRegionTile;
    }

    @Override
    public void setLastLoadedMapRegionTile(Location lastLoadedMapRegionTile) {
        this.lastLoadedMapRegionTile = lastLoadedMapRegionTile;
    }

    @Override
    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    @Override
    public Location getNextLocation() {
        return nextLocation;
    }

    @Override
    public Entity getAttacking() {
        return attacking;
    }

    @Override
    public void setAttacking(Entity attacking) {
        this.attacking = attacking;
    }

    @Override
    public Entity getAttackedBy() {
        return attackedBy;
    }

    @Override
    public void setAttackedBy(Entity attackedBy) {
        this.attackedBy = attackedBy;
    }

    @Override
    public long getAttackedByDelay() {
        return attackedByDelay;
    }

    @Override
    public void setAttackedByDelay(long attackedByDelay) {
        this.attackedByDelay = attackedByDelay;
    }

    @Override
    public long getAttackedTick() {
        return attackedTick;
    }

    @Override
    public void setAttackedTick(long attackedTick) {
        this.attackedTick = attackedTick;
    }

    @Override
    public long getAttackedAtTick() {
        return attackedAtTick;
    }

    @Override
    public void setAttackedAtTick(long attackedAtTick) {
        this.attackedAtTick = attackedAtTick;
    }

    @Override
    public long getLastReceivedHit() {
        return lastReceivedHit;
    }

    @Override
    public void setLastReceivedHit(long lastReceivedHit) {
        this.lastReceivedHit = lastReceivedHit;
    }

    @Override
    public long getLastAnimation() {
        return lastAnimation;
    }

    @Override
    public void setLastAnimation(long lastAnimation) {
        this.lastAnimation = lastAnimation;
    }

    @Override
    public long getAttackingDelay() {
        return attackingDelay;
    }

    @Override
    public void setAttackingDelay(long attackingDelay) {
        this.attackingDelay = attackingDelay;
    }

    @Override
    public Location getFaceLocation() {
        return faceLocation;
    }

    @Override
    public IntSet getMapRegionsIds() {
        return mapRegionsIds;
    }

    @Override
    public IntSet getLastMapRegionsIds() {
        return lastMapRegionsIds;
    }

    @Override
    public IntLinkedList getWalkSteps() {
        return walkSteps;
    }

    @Override
    public List<Hit> getReceivedHits() {
        return receivedHits;
    }

    @Override
    public List<Hit> getNextHits() {
        return nextHits;
    }

    @Override
    public Object2ObjectMap<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>> getReceivedDamage() {
        return receivedDamage;
    }

    @Override
    public Map<Object, Object> getTemporaryAttributes() {
        return temporaryAttributes;
    }

    @Override
    public UpdateFlags getUpdateFlags() {
        return updateFlags;
    }

    @Override
    public List<HitBar> getHitBars() {
        return hitBars;
    }

    @Override
    public int getFaceEntity() {
        return faceEntity;
    }

    @Override
    public void setFaceEntity(final Entity entity) {
        this.lastFaceEntityDelay = System.currentTimeMillis();
        faceEntity = entity == null ? -1 : entity.getClientIndex();
        updateFlags.flag(UpdateFlag.FACE_ENTITY);
    }

    @Override
    public Animation getAnimation() {
        return animation;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public ForceMovement getForceMovement() {
        return forceMovement;
    }

    @Override
    public void setForceMovement(final ForceMovement movement) {
        forceMovement = movement;
        updateFlags.flag(UpdateFlag.FORCE_MOVEMENT);
    }

    @Override
    public ForceTalk getForceTalk() {
        return forceTalk;
    }

    @Override
    public Tinting getTinting() {
        return tinting;
    }

    @Override
    public void setTinting(final Tinting tinting) {
        this.tinting = tinting;
        updateFlags.flag(UpdateFlag.TINTING);
    }

    @Override
    public void setForceTalk(final ForceTalk talk) {
        forceTalk = talk;
        updateFlags.flag(UpdateFlag.FORCED_CHAT);
    }

    @Override
    public int getWalkDirection() {
        return walkDirection;
    }

    @Override
    public void setWalkDirection(int walkDirection) {
        this.walkDirection = walkDirection;
    }

    @Override
    public int getRunDirection() {
        return runDirection;
    }

    @Override
    public void setRunDirection(int runDirection) {
        this.runDirection = runDirection;
    }

    @Override
    public int getCrawlDirection() {
        return crawlDirection;
    }

    @Override
    public void setCrawlDirection(int crawlDirection) {
        this.crawlDirection = crawlDirection;
    }

    @Override
    public int getSceneBaseChunkId() {
        return sceneBaseChunkId;
    }

    @Override
    public void setSceneBaseChunkId(int sceneBaseChunkId) {
        this.sceneBaseChunkId = sceneBaseChunkId;
    }

    @Override
    public boolean isRun() {
        return run;
    }

    @Override
    public void setRun(final boolean run) {
        this.run = run;
    }

    @Override
    public boolean isSilentRun() {
        return silentRun;
    }

    @Override
    public boolean isCantInteract() {
        return cantInteract;
    }

    @Override
    public void setCantInteract(boolean cantInteract) {
        this.cantInteract = cantInteract;
    }

    @Override
    public long getLastFaceEntityDelay() {
        return lastFaceEntityDelay;
    }

    @Override
    public void setLastFaceEntityDelay(long lastFaceEntityDelay) {
        this.lastFaceEntityDelay = lastFaceEntityDelay;
    }

    private final transient GameQueueStack queueStack = new GameQueueStack();

    @Override
    public GameQueueStack getQueueStack() {
        return queueStack;
    }

    public final Player getMostDamagePlayerCheckIronman() {
        return getMostDamagePlayer();
    }

    public Number getNumericTemporaryAttribute(final String key) {
        final Object object = getTemporaryAttributes().get(key);
        if (!(object instanceof Number)) {
            return 0;
        }
        return (Number) object;
    }

    public Number getNumericTemporaryAttributeOrDefault(final String key, final int defaultValue) {
        final Object object = getTemporaryAttributes().get(key);
        if (!(object instanceof Number)) {
            return defaultValue;
        }
        return (Number) object;
    }

    public Boundary getBoundary() {
        return getBoundary(0);
    }

    public Boundary getBoundary(int offset) {
        return Boundary.Companion.createSurroundingBoundary(this, offset);
    }
}
