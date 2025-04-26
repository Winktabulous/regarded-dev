package com.zenyte.game.world.entity.npc;

import com.near_reality.game.content.commands.DeveloperCommands;
import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.near_reality.game.world.entity.FastCollisionCheckKt;
import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.ContentConstants;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.content.boons.impl.ClueCollector;
import com.zenyte.game.content.boons.impl.HoarderMentality;
import com.zenyte.game.content.boons.impl.SlayersFavor;
import com.zenyte.game.content.donation.DonationToggle;
import com.zenyte.game.content.skills.magic.spells.arceuus.DeathChargeKt;
import com.zenyte.game.content.skills.prayer.ectofuntus.Bonecrusher;
import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.Slayer;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.content.supplycaches.SupplyCache;
import com.zenyte.game.content.tombsofamascut.AbstractTOARaidArea;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.*;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.combatdefs.*;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorMonster;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.npc.race.Demon;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.player.*;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.*;
import com.zenyte.game.world.region.area.BrimhavenDungeon;
import com.zenyte.game.world.region.area.CatacombsOfKourend;
import com.zenyte.game.world.region.area.FremennikSlayerDungeon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.taskonlyareas.StrongholdSlayerDungeon;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.game.world.region.area.wilderness.WildernessSlayerCave;
import com.zenyte.plugins.item.RingOfWealthItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NPC extends AbstractEntity {

    public static Object2IntOpenHashMap<Class<? extends RegionArea>> areaDefinedRespawnTimers = new Object2IntOpenHashMap<>();

    /*
     * Add an area class into this static block to decrease the ticks to respawn timer
     * for each NPC in that defined polygon
     */
    static {
        areaDefinedRespawnTimers.put(StrongholdSlayerDungeon.class, 16);
        areaDefinedRespawnTimers.put(CatacombsOfKourend.class, 16);
        areaDefinedRespawnTimers.put(FremennikSlayerDungeon.class, 16);
        areaDefinedRespawnTimers.put(WildernessSlayerCave.class, 16);
        areaDefinedRespawnTimers.put(BrimhavenDungeon.class, 16);
    }

    private static final Logger log = LoggerFactory.getLogger(NPC.class);
    public static final IntOpenHashSet pendingAggressionCheckNPCs = new IntOpenHashSet();
    protected final Object2LongMap<Entity> interactingEntities = new Object2LongOpenHashMap<>();
    protected final transient Int2ObjectOpenHashMap<NPCCombatDefinitions> combatDefinitionsMap = new Int2ObjectOpenHashMap<>(1);
    protected int id;
    /**
     * Whether the NPC is manually spawned or not. Used to define whether to assign a respawning task to the NPC upon death or not. Spawned
     * NPCs by default will not respawn unless modified.
     */
    protected boolean spawned;
    protected NPCCombatDefinitions combatDefinitions;
    /**
     * The location at which the NPC was initially spawned, used to determine where the NPC should respawn.
     */
    protected ImmutableLocation respawnTile;
    /**
     * The actual combat of the NPC.
     */
    protected NPCCombat combat;
    protected int size;
    protected int interactionDistance = 3;
    /**
     * The next transformation id of the NPC. Used to transmogrify NPCs.
     */
    protected int nextTransformation = -1;
    /**
     * The radius of the walk distance of the NPC. It will only random walk within the boundaries here.
     */
    protected int radius = 5;
    /**
     * Whether the NPC ignores its combat distance checks and always enters melee distance or not.
     */
    protected boolean forceFollowClose;
    /**
     * The distance from which the NPC will be able to see you and become aggressive towards you, granted the rest of the aggression
     * requirements are met.
     */
    protected int aggressionDistance;
    /**
     * The delay in ticks between the death animation start and the call to finish().
     */
    protected int deathDelay = 2;
    /**
     * The damage cap of the NPC. By default, there is no cap AKA -1. If you wish to restrict the maximum damage that can be dealt to the
     * NPC in one blow, modify this value.
     */
    protected int damageCap = -1;
    /**
     * The maximum distance between the NPC and its target, if this value is exceeded, the NPC will end its combat task and return to its
     * normal stand-by state.
     */
    protected int maxDistance = 10;
    /**
     * The attack distance for the NPC with magic and ranged styles.
     */
    protected int attackDistance = 7;
    /**
     * The type of the targets that can be assigned through aggressivity to this NPC. Defaults to just players, so it only checks for nearby
     * players whom to aggressively attack.
     */
    protected EntityType targetType = EntityType.PLAYER;
    /**
     * The forced state of the NPC aggression. This variable is only effective if its value is true. Used to set passive NPCs aggressive for
     * a certain period.
     */
    protected boolean forceAggressive;
    /**
     * The region in which the NPC is originally spawned.
     */
    protected Region region;
    /**
     * The tile to which the NPC will force walk; by default the value is null and the NPC does no forcewalking.
     */
    protected Location forceWalk;
    protected Direction spawnDirection = Direction.SOUTH;
    protected NPCDefinitions definitions;
    protected Entity interactingWith;
    protected int ticksUntilRespawn;
    private transient int swapTicks;
    private transient NPCSpawn npcSpawn;
    protected Predicate<Entity> predicate = this::isPotentialTarget;
    protected transient boolean supplyCache = false;
    private boolean inWilderness;
    protected transient int randomWalkDelay;
    private boolean despawnWhenStuck;
    private int despawnTimer;
    protected transient long flinchTime;
    private boolean intelligent;
    private int statReduceTimer = Utils.random(100);//Randomize the timer so all npcs don't reduce their stats at the same exact time - better balances loadp ressure.
    protected boolean dropped;
    private NpcOverhead overhead;

    public void setOverhead(NpcOverhead prayer) {
        this.overhead = prayer;
        updateFlags.flag(UpdateFlag.NPC_PRAYER_OVERHEAD);
    }

    public void clearOverhead() {
        setOverhead(null);
    }

    public NpcOverhead getOverhead() {
        return overhead;
    }

    public boolean hasOverheadActive() {
        var object = getTemporaryAttributes().get("npc_overhead_prayers");
        if (!(object instanceof Boolean value)) return false;
        return value;
    }
    private int ticker = 0;

    private int combatLevelChange = -1;

    public void setCombatLevelChange(int combatLevelChange) {
        this.combatLevelChange = combatLevelChange;
        updateFlags.flag(UpdateFlag.COMBAT_LEVEL_CHANGE);
    }

    public int getCombatLevelChange() {
        return combatLevelChange;
    }

    private String nameChange;

    public void setNameChange(String nameChange) {
        this.nameChange = nameChange;
        updateFlags.flag(UpdateFlag.NAME_CHANGE);
    }

    public String getNameChange() {
        return nameChange;
    }

    private int optionMask = 31;

    public void setOptionMask(int optionMask) {
        this.optionMask = optionMask;
        updateFlags.flag(UpdateFlag.HIDE_OPTIONS);
    }

    public int getOptionMask() {
        return optionMask;
    }

    public void normalizeBoostedStats() {
        if (isDead() || isFinished() || !isCycleHealable() || statReduceTimer++ % 100 != 0) {
            return;
        }
        final int hitpoints = getHitpoints();
        final int maxHitpoints = getMaxHitpoints();
        if (hitpoints < maxHitpoints) {
            setHitpoints(hitpoints + 1);
        } else if (hitpoints > maxHitpoints) {
            setHitpoints(hitpoints - 1);
        }
        final NPCCombatDefinitions originalCombatDefinitions = NPCCDLoader.get(getId());
        if (originalCombatDefinitions == null) {
            return;
        }
        final StatDefinitions statDefinitions = combatDefinitions.getStatDefinitions();
        final StatDefinitions originalStatDefinitions = originalCombatDefinitions.getStatDefinitions();
        for (final StatType statType : StatType.levelTypes) {
            final int currentLevel = statDefinitions.get(statType);
            final int originalLevel = originalStatDefinitions.get(statType);
            if (currentLevel > originalLevel) {
                statDefinitions.set(statType, currentLevel - 1);
            } else if (currentLevel < originalLevel) {
                statDefinitions.set(statType, currentLevel + 1);
            }
        }
    }

    public void clear() {

    }

    public void flinch() {
        if (flinchTime > WorldThread.WORLD_CYCLE || !isFlinchable()) {
            return;
        }
        final int attackSpeed = combatDefinitions.getAttackSpeed();
        combat.combatDelay += attackSpeed / 2;
        flinchTime = WorldThread.WORLD_CYCLE + (attackSpeed / 2) + 8;
    }

    public boolean isFlinchable() {
        return true;
    }

    public void renewFlinch() {
        flinchTime = WorldThread.WORLD_CYCLE + combatDefinitions.getAttackSpeed() + 8;
    }

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        flinch();
    }

    public NPC(final int id, final Location tile, final Direction facing, final int radius, boolean spawned) {
        this(id, tile, true);
        spawnDirection = facing;
        if (spawnDirection != null) {
            direction = spawnDirection.getDirection();
        }
        this.radius = radius;
    }

    public NPC(final int id, final Location tile, final Direction facing, final int radius) {
        this(id, tile, false);
        spawnDirection = facing;
        if (spawnDirection != null) {
            direction = spawnDirection.getDirection();
        }
        this.radius = radius;
    }

    public NPC(final int id) {
        this.id = id;
        resetDefinitions();
        location = new Location(3440, 3440, 0);
    }

    public void spawnAt(Location location) {
        this.location = location;
    }

    private ModelOverride modelOverride;

    public ModelOverride override() {
        if(modelOverride == null)
            modelOverride = new ModelOverride(this);
        return modelOverride;
    }

    public NPC(final int id, final Location tile, final boolean spawned) {
        if (tile == null) {
            return;
        }
        forceLocation(new Location(tile));
        this.id = id;
        resetDefinitions();
        updateCombatDefinitions();
        final Animation death = combatDefinitions.getSpawnDefinitions().getDeathAnimation();
        if (death != null) {
            deathDelay = Math.max(Math.min((int) Math.ceil(AnimationUtil.getDuration(death) / 1200.0F), 10), 1);
        }
        despawnWhenStuck = definitions.containsOption("Pickpocket");
        aggressionDistance = combatDefinitions.getAggressionDistance();
        this.inWilderness = WildernessArea.isWithinWilderness(getX(), getY());
        if (inWilderness) {
            aggressionDistance /= 2;
        }
        combat = new NPCCombat(this);
        respawnTile = new ImmutableLocation(tile);
        this.spawned = spawned;
        size = getDefinitions().getSize();
        setFinished(true);
        setLastRegionId(0);
        region = World.getRegion(getLocation().getRegionId());
        postInit();
    }

    public void postInit() {

    }

    public static final void clearPendingAggressions() {
        pendingAggressionCheckNPCs.clear();
    }

    public static final int getTransformedId(final int npcId, final Player player) {
        return player.getTransmogrifiedId(NPCDefinitions.getOrThrow(npcId), npcId);
    }

    public void setId(final int id) {
        this.id = id;
        resetDefinitions();
    }

    private void resetDefinitions() {
        final NPCDefinitions definitions = NPCDefinitions.get(id);
        if (definitions == null) {
            throw new RuntimeException("Invalid NPC id: " + id);
        }
        this.definitions = definitions;
    }

    public Location getRespawnTile() {
        return respawnTile;
    }

    /**
     * @return Whether the NPC can walk through other entities (both players and NPCs) as well as whether other NPCs can walk through this
     * NPC {value false} or not {value true}.
     */
    public boolean isEntityClipped() {
        return true;
    }

    @Override
    public void setLocation(final Location tile) {
        super.setLocation(tile);
        setTeleported(true);
    }

    @Override
    public void unclip() {
        if (!isEntityClipped()) return;
        final int size = getSize();
        final int x = getX();
        final int y = getY();
        final int z = getPlane();
        int hash;
        int lastHash = -1;
        Chunk chunk = null;
//        Objects.requireNonNull(chunk);
        for (int x1 = x; x1 < (x + size); x1++) {
            for (int y1 = y; y1 < (y + size); y1++) {
                if ((hash = Chunk.getChunkHash(x1 >> 3, y1 >> 3, z)) != lastHash) {
                    chunk = World.getChunk(lastHash = hash);
                }
                assert chunk != null;
                if (FastCollisionCheckKt.collides(this, chunk.safePlayerIterator(), x1, y1))
                    continue;
                if (collides(chunk.getNPCs(), x1, y1))
                    continue;
                World.getRegion(_Location.getRegionId(x1, y1), true).removeFlag(z, x1 & 63, y1 & 63, clipFlag());
            }
        }
    }

    @Override
    public void clip() {
        if (!isEntityClipped()) return;
        if (isFinished()) {
            return;
        }
        final int size = getSize();
        final int x = getX();
        final int y = getY();
        final int z = getPlane();
        for (int x1 = x; x1 < (x + size); x1++) {
            for (int y1 = y; y1 < (y + size); y1++) {
                World.getRegion(_Location.getRegionId(x1, y1), true).addFlag(z, x1 & 63, y1 & 63, clipFlag());
            }
        }
    }

    protected int clipFlag() {
        return Flags.OCCUPIED_BLOCK_NPC;
    }

    public boolean isAttackable(final Entity e) {
        return true;
    }

    @Override
    public void processHit(final Hit hit) {
        /*if (isDead()) {
            return;
        }*/
        if (isImmune(hit.getHitType())) {
            hit.setDamage(0);
        }
        if (hit.getDamage() > Short.MAX_VALUE) {
            hit.setDamage(Short.MAX_VALUE);
        }
        if (hit.getDamage() > getHitpoints()) {
            hit.setDamage(getHitpoints());
        }
        getUpdateFlags().flag(UpdateFlag.HIT);
        getNextHits().add(hit);
        addHitbar();
        if (hit.getHitType() == HitType.HEALED || HitType.PALM_LOWER.equals(hit.getHitType())) {
            heal(hit.getDamage());
        } else if (!HitType.SHIELD_DOWN.equals(hit.getHitType()) && !HitType.CORRUPTION.equals(hit.getHitType())){
            removeHitpoints(hit);
        }
        postHitProcess(hit);
    }

    protected void postHitProcess(final Hit hit) {
    }

    public boolean isCycleHealable() {
        return true;
    }

    public boolean checkAggressivity() {
        return superCheckAggressivity();
    }

    protected boolean superCheckAggressivity() {
        if (!isAttackable()) {
            return false;
        }
        if(location.getRegionId() == 13430 || location.getRegionId() == 13431)//uber area since npc doesn't have area type like player
            return false;

        if (!forceAggressive) {
            if (!combatDefinitions.isAggressive()) {
                return false;
            }
        }
        getPossibleTargets(targetType);
        possibleTargets.removeIf(e -> !combat.attackable(e, TargetSwitchCause.AGGRESSION));
        if (!possibleTargets.isEmpty()) {
//            setForceTalk("I have "+possibleTargets.size()+" possible targets");
            this.resetWalkSteps();
            final Entity target = possibleTargets.get(Utils.randomNoPlus(possibleTargets.size()));
            final Entity previousTarget = combat.getTarget();
            setTarget(target, previousTarget == null ? TargetSwitchCause.OTHER : TargetSwitchCause.AGGRESSION);
//            if (previousTarget != target && previousTarget != null){
//                combat.setCombatDelay(1);
//            }
        }
        return true;
    }

    public void setTarget(final Entity target) {
        setTarget(target, TargetSwitchCause.OTHER);
    }

    public void setTarget(final Entity target, TargetSwitchCause cause) {
        combat.setTarget(target, cause);
    }

    /**
     * Whether the NPC is affected by tolerance(players standing in the area for 20 minutes straight)
     *
     * @return whether the npc is tolerable.
     */
    public boolean isTolerable() {
        return true;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public int getSize() {
        return size;
    }

    public final NPCDefinitions getDefinitions() {
        return definitions;
    }

    public void setTransformation(final int id) {
        nextTransformation = id;
        setId(id);
        size = definitions.getSize();
        updateFlags.flag(UpdateFlag.TRANSFORMATION);
        if (preserveStatsOnTransformation()) {
            updateTransformationalDefinitions();
        } else {
            updateCombatDefinitions();
        }
    }

    public void setTransformationPreservingStats(final int id) {
        nextTransformation = id;
        setId(id);
        size = definitions.getSize();
        updateFlags.flag(UpdateFlag.TRANSFORMATION);
        updateTransformationalDefinitions();
    }

    protected boolean preserveStatsOnTransformation() {
        return false;
    }

    protected void updateTransformationalDefinitions() {
        final NPCCombatDefinitions def = NPCCombatDefinitions.clone(getId(), NPCCDLoader.get(getId()));
        final int currentHitpoints = getHitpoints();
        final int currentMaxHitpoints = getMaxHitpoints();
        final int updatedMaxHitpoints = def.getHitpoints();
        if (currentMaxHitpoints != updatedMaxHitpoints) {
            setHitpoints((int) ((double) currentHitpoints / currentMaxHitpoints * updatedMaxHitpoints));
        }
        def.getStatDefinitions().setCombatStats(this.combatDefinitions.getStatDefinitions().getCombatStats());
        setCombatDefinitions(def);
        if (inWilderness) {
            if (this.combatDefinitions.isAggressive()) {
                this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
            }
        }
    }

    protected void updateCombatDefinitions() {
        NPCCombatDefinitions def = combatDefinitionsMap.get(getId());
        if (def == null) {
            final NPCCombatDefinitions cachedDefs = NPCCDLoader.get(getId());
            def = NPCCombatDefinitions.clone(getId(), cachedDefs);
        }
        if (combatDefinitionsMap.isEmpty()) {
            setHitpoints(def.getHitpoints());
        }
        setCombatDefinitions(def);
        if (inWilderness) {
            if (this.combatDefinitions.isAggressive()) {
                this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
            }
        }
    }

    public NPCCombatDefinitions getBaseCombatDefinitions() {
        return NPCCDLoader.get(getId());
    }

    public void setCombatDefinitions(final NPCCombatDefinitions definitions) {
        this.combatDefinitions = definitions;
        combatDefinitionsMap.put(getId(), definitions);
    }

    public boolean lockUponInteraction() {
        return true;
    }

    @Override
    public void processEntity() {
        if(this.dropped) {
            return;
        }
        if (getX() < 6400) {
            if (region == null || region.getLoadStage() != 2) {
                return;
            }
        }
        if (ticksUntilRespawn > 0) {
            if (--ticksUntilRespawn > 0) {
                return;
            }
            spawn();
        }
        if (isFinished()) return;

        if (routeEvent != null) {
            if (routeEvent.process()) {
                routeEvent = null;
            }
        }
        iterateScheduledHits();
        processReceivedHits();
        processNPC();
        processMovement();
        afterMovement();
        retreatMechanics.process();
        toxins.process();
        try {
            normalizeBoostedStats();
        } catch (Exception e) {
            log.error("", e);
        }
        checkFacedEntity();
    }

    public void afterMovement() {
        combat.processAttack();
    }

    @Override
    public boolean checkProjectileClip(Player player, boolean melee) {
      return true;
    }

    @Override
    public boolean isProjectileClipped(final Position target, final boolean closeProximity) {
        return ProjectileUtils.isProjectileClipped(target instanceof Entity ? (Entity) target : null, this, target, getNextPosition(isRun() ? 2 : 1), closeProximity);
    }

    /**
     * Whether or not the monster will be frozen on-spot by entity event(to prevent player walking side by side with the npc for extended duration, we reset their steps and stop them
     * from moving)
     *
     * @return whether or not the npc is affected.
     */
    public boolean isPathfindingEventAffected() {
        return true;
    }

    public void finish() {

        if (isFinished()) {
            writeLog("already finished");
            return;
        }

        try {
            setFinished(true);
            routeEvent = null;
            unclip();
            World.updateEntityChunk(this, true);
            setLastChunkId(-1);
            interactingWith = null;
            if (!interactingEntities.isEmpty()) {
                interactingEntities.clear();
            }
        } catch (Throwable e) {
            log.error("Failed to finish NPC {}", this, e);
            writeLog("failed to finish NPC", e);
        }
        World.removeNPC(this);
    }

    protected RetreatMechanics retreatMechanics = new RetreatMechanics(this);
    protected transient boolean forceCheckAggression;

    private boolean checkIfDespawn() {
        if (!addWalkSteps(getX() - 1, getY())) {
            if (!addWalkSteps(getX(), getY() - 1)) {
                if (!addWalkSteps(getX() + 1, getY())) {
                    return !addWalkSteps(getX(), getY() + 1);
                }
            }
        }
        return false;
    }

    public void processNPC() {
        if(this.dropped) {
            return;
        }
        if (despawnWhenStuck) {
            if (!isDead()) {
                if (++despawnTimer % 500 == 0) {
                    if (checkIfDespawn()) {
                        finish();
                        setRespawnTask();
                    }
                }
            }
        }
        final int delay = randomWalkDelay;
        if (delay > 0) {
            randomWalkDelay--;
        }
        if (combat.process()) {
            return;
        }
        if (ticker++ % 4 == 0) {
            // Only demons can be burnt this way so...
            if (Demon.isDemon(this, true)) {
                var attribute = getTemporaryAttributes().get("burningDamage");
                if (attribute != null) {
                    var burn = Integer.parseInt(attribute.toString());
                    if (burn > 0)
                        applyHit(new Hit(1, HitType.BURN));
                    getTemporaryAttributes().put("burningDamage", burn - 1);
                    if (burn == 0)
                        temporaryAttributes.remove("burningDamage");
                }
            }
        }
        if (isLocked()) {
            return;
        }
        if (this.targetType == EntityType.PLAYER) {
            if (forceCheckAggression || pendingAggressionCheckNPCs.contains(getIndex())) {
                if (checkAggressivity()) {
                    if (combat.getTarget() != null) {
                        return;
                    }
                }
            }
        } else {
            if (checkAggressivity()) {
                if (combat.getTarget() != null) {
                    return;
                }
            }
        }
        if (!interactingEntities.isEmpty()) {
            final ObjectIterator<Object2LongMap.Entry<Entity>> it = interactingEntities.object2LongEntrySet().iterator();
            final long ctms = Utils.currentTimeMillis();
            while (it.hasNext()) {
                final Object2LongMap.Entry<Entity> entry = it.next();
                final Entity e = entry.getKey();
                if (e == null) {
                    continue;
                }
                final long time = entry.getLongValue();
                if (e.getLocation().getDistance(getLocation()) > interactionDistance || e.isFinished() || ctms > time) {
                    it.remove();
                    if (e == interactingWith) {
                        setInteractingWith(null);
                    }
                }
            }
            if (!interactingEntities.isEmpty()) {
                return;
            }
        }
        if (delay > 0 || radius <= 0 || ContentConstants.SPAWN_MODE) {
            return;
        }
        if (routeEvent != null || !getWalkSteps().isEmpty()) {
            return;
        }
        if (Utils.random(5) != 0 || isFrozen() || isStunned()) {
            return;
        }
        final int moveX = Utils.random(-radius, radius);
        final int moveY = Utils.random(-radius, radius);
        final int respawnX = respawnTile.getX();
        final int respawnY = respawnTile.getY();
        addWalkStepsInteract(respawnX + moveX, respawnY + moveY, radius, getSize(), true);
    }

    public boolean isUsingMelee() {
        return combatDefinitions.isMelee();
    }

    @Override
    public void processMovement() {
        if (faceEntity >= 0) {
            final Entity target = faceEntity >= 32768 ? World.getPlayers().get(faceEntity - 32768) : World.getNPCs().get(faceEntity);
            if (target != null) {
                direction = DirectionUtil.getFaceDirection(target.getLocation().getCoordFaceX(target.getSize()) - getX(), target.getLocation().getCoordFaceY(target.getSize()) - getY());
            }
        }
        this.walkDirection = -1;
        this.runDirection = -1;
        this.crawlDirection = -1;
        if (nextLocation != null) {
            if (lastLocation == null) {
                lastLocation = new Location(location);
            } else {
                lastLocation.setLocation(location);
            }
            despawnTimer = 0;
            unclip();
            forceLocation(nextLocation);
            onMovement();
            clip();
            nextLocation = null;
            teleported = true;
            World.updateEntityChunk(this, false);
            resetWalkSteps();
            return;
        }
        teleported = false;
        if (walkSteps.isEmpty() || isLocked() && temporaryAttributes.get("ignoreWalkingRestrictions") == null) {
            return;
        }
        if (isDead() || isFinished()) {
            return;
        }
        if (lastLocation == null) {
            lastLocation = new Location(location);
        } else {
            lastLocation.setLocation(location);
        }
        final boolean isCrawling = isCrawling();
        final boolean canMove = !isCrawling || (crawlInterval & 0x1) != 0;
        if (canMove) {
            final int steps = isCrawling ? 1 : silentRun ? 1 : run ? 2 : 1;
            //TODO debug for agile scarab
            int stepCount;
            for (stepCount = 0; stepCount < steps; stepCount++) {
                final int nextStep = getNextWalkStep();
                if (nextStep == 0) {
                    break;
                }
                final int dir = WalkStep.getDirection(nextStep);
                if ((WalkStep.check(nextStep) && !canMove(getX(), getY(), dir))) {
                    resetWalkSteps();
                    break;
                }
                if (isCrawling) {
                    crawlDirection = dir;
                } else if (stepCount == 0) {
                    walkDirection = dir;
                } else {
                    runDirection = dir;
                }
                final int x = Utils.DIRECTION_DELTA_X[dir];
                final int y = Utils.DIRECTION_DELTA_Y[dir];
                unclip();
                location.moveLocation(x, y, 0);
                clip();
            }
            onMovement();
        }
        despawnTimer = 0;
        if (faceEntity < 0) {
            direction = DirectionUtil.getFaceDirection(location.getX() - lastLocation.getX(), location.getY() - lastLocation.getY());
        }
        World.updateEntityChunk(this, false);
    }

    protected boolean canMove(final int fromX, final int fromY, final int direction) {
        return World.checkWalkStep(getPlane(), fromX, fromY, direction, getSize(), isEntityClipped(), false);
    }

    public void forceWalkRespawnTile() {
        setForceWalk(respawnTile);
    }

    public boolean isUnderCombat() {
        return combat.underCombat();
    }

    public void setForceWalk(final Location tile) {
        resetWalkSteps();
        forceWalk = tile;
    }

    public void finishInteractingWith(final Entity entity) {
        if (entity == interactingWith) {
            interactingWith = null;
        }
        interactingEntities.removeLong(entity);
        if (!isUnderCombat()) {
            setFaceEntity(null);
        }
    }

    public void setInteractingWith(final Entity entity) {
        if (entity == interactingWith) {
            if (entity != null) {
                interactingEntities.put(entity, Utils.currentTimeMillis() + 60000);
            }
            return;
        }
        interactingWith = entity;
        if (!isUnderCombat()) {
            setFaceEntity(entity);
        }
        if (entity == null) {
            return;
        }
        entity.resetWalkSteps();
        if (!interactingEntities.containsKey(entity)) {
            interactingEntities.put(entity, Utils.currentTimeMillis() + 60000);
        }
    }

    @Override
    public int getMaxHitpoints() {
        return combatDefinitions.getHitpoints();
    }

    @Override
    public boolean isDead() {
        return getHitpoints() == 0;
    }

    @Override
    public int getClientIndex() {
        return getIndex();
    }

    @Override
    public Location getMiddleLocation() {
        if (middleTile == null) {
            middleTile = size == 1 ? new Location(getLocation()) : new Location(getLocation().getCoordFaceX(size), getLocation().getCoordFaceY(size), getPlane());
        } else {
            if (size == 1) {
                middleTile.setLocation(getLocation());
            } else {
                middleTile.setLocation(getLocation().getCoordFaceX(size), getLocation().getCoordFaceY(size), getPlane());
            }
        }
        return middleTile;
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (damageCap != -1 && hit.getDamage() > damageCap) {
            hit.setDamage(damageCap);
        }

        switch (hit.getHitType()) {
            case REGULAR, DEFAULT, MELEE, MAGIC, RANGED -> {
                final int wrathOfAmascutTick = getNumericTemporaryAttributeOrDefault(PlayerCombat.WRATH_OF_AMASCUT_ATT, -1).intValue();
                if (wrathOfAmascutTick >= WorldThread.getCurrentCycle()) {
                    hit.setDamage((int) (hit.getDamage() * 1.25F));
                }
            }
        }
    }

    @Override
    public int getMagicLevel() {
        NPCCombatDefinitions combatDefs = getCombatDefinitions();
        if(combatDefs == null)
            return 1;

        StatDefinitions statDefinitions = combatDefs.getStatDefinitions();
        if(statDefinitions == null)
            return 1;

        return getCombatDefinitions().getStatDefinitions().getMagicLevel();
    }

    @Override
    public void postProcessHit(final Hit hit) {
    }

    public boolean isTickEdible() {
        return true;
    }

    @Override
    public void handleOutgoingHit(final Entity target, final Hit hit) {
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0;
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return 0;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0;
    }

    protected String notificationName(@NotNull final Player player) {
        return definitions.getName().toLowerCase();
    }

    protected void sendNotifications(final Player player) {
        final String name = notificationName(player);
        final boolean isBoss = NotificationSettings.BOSS_NPC_NAMES.contains(name);
        if (this instanceof SuperiorNPC) {
            player.getNotificationSettings().increaseKill("superior creature");
        }
        if (NotificationSettings.isKillcountTracked(name)) {
            player.getNotificationSettings().increaseKill(name);
            if (isBoss) {
                player.getNotificationSettings().sendBossKillCountNotification(name);
            }
        }
    }

    public boolean isAttackableNPC() {
        return getDefinitions().containsOption("Attack");
    }

    public boolean forceKilled = false;

    protected void onFinish(final @Nullable Entity source) {
        if(!forceKilled) {
            try {
                spawnSuperior(source, this);
            } catch (Exception e) {
                log.error("Failed to spawn superior for NPC {}", this, e);
            }
            try {
                drop(getMiddleLocation());
            } catch (Exception e) {
                log.error("Failed to drop items for NPC {}", this, e);
            }
        }
        reset();
        finish();
        if (!spawned) {
            setRespawnTask();
        }
        if (source instanceof Player player) {
            sendNotifications(player);
        }
        forceKilled = false;

    }

    @Override
    public void setAnimation(final Animation animation) {
        if (animation == null) {
            this.animation = null;
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            if (!isValidAnimation(animation.getId())) {
                NPCDefinitions def = getDefinitions();
                log.info("NPC {} (\"{}\") can't play invalid animation {}",
                        getId(), def == null ? null : def.getName(), animation.getId());
                return;
            }
            this.animation = animation;
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    public boolean isValidAnimation(int animID) {
        return AnimationMap.isValidAnimation(id, animID);
    }

    @Override
    public void setInvalidAnimation(final Animation animation) {
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    @Override
    public void setUnprioritizedAnimation(final Animation animation) {
        if (lastAnimation > Utils.currentTimeMillis() || updateFlags.get(UpdateFlag.ANIMATION)) {
            return;
        }
        if (animation != null && !AnimationMap.isValidAnimation(id, animation.getId())) {
            if (log.isWarnEnabled()) {
                NPCDefinitions def = getDefinitions();
                log.warn("Invalid unprioritized animation ID {} for NPC {} (\"{}\")",
                        animation.getId(), id, def == null ? null : def.getName());
            }
            return;
        }
        this.animation = animation;
        updateFlags.set(UpdateFlag.ANIMATION, animation != null);
    }

    private transient long timeOfDeath;

    protected void onDeath(final Entity source) {

        try {
            timeOfDeath = WorldThread.WORLD_CYCLE;
            resetWalkSteps();
            combat.removeTarget();
            setAnimation(null);
            if (source instanceof Player player) {
                player.getSlayer().checkAssignment(this);
                DeathChargeKt.invokeDeathChargeEffect(player);
                checkCombatAchievements(player);
                if (player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.KERIS_PARTISAN_OF_THE_SUN && player.getArea() != null && player.getArea() instanceof AbstractTOARaidArea) {
                    final int prayerLevel = player.getPrayerManager().getPrayerPoints();
                    final int overhealAmount = (int) (player.getMaxHitpoints() * 1.20);
                    final int currentHitpoints = player.getHitpoints();
                    if (prayerLevel >= 5 && currentHitpoints <= overhealAmount) {
                        player.getPrayerManager().drainPrayerPoints(5);
                        player.setHitpoints(Math.min(overhealAmount, currentHitpoints + 12));
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void sendDeath() {
        final Player source = getMostDamagePlayerCheckIronman();
        onDeath(source);
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (ticks == 0) {
                    final SpawnDefinitions spawnDefinitions = combatDefinitions.getSpawnDefinitions();
                    setAnimation(spawnDefinitions.getDeathAnimation());
                    setOptionMask(0);
                    final SoundEffect sound = spawnDefinitions.getDeathSound();
                    if (sound != null && source != null) {
                        source.sendSound(sound);
                    }
                }
                if (ticks == deathDelay) {
                    onFinish(source);
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 1);
    }

    private void checkCombatAchievements(final Player player) {
        if (Demon.isGreaterDemon(this)) {
            player.getCombatAchievements().complete(CAType.A_GREATER_FOE);
            if (PlayerCombat.isDemonbaneWeapon(player.getEquipment().getId(EquipmentSlot.WEAPON))) {
                player.getCombatAchievements().complete(CAType.DEMONBANE_WEAPONRY);
            }
        }
        if (definitions.getLowercaseName().contains("hellhound")) {
            player.getCombatAchievements().complete(CAType.A_DEMONS_BEST_FRIEND);
        }
        if (definitions.getLowercaseName().contains("brutal black dragon")) {
            player.getCombatAchievements().complete(CAType.BRUTAL_BIG_BLACK_AND_FIREY);
        }
    }

    protected Player getDropRecipient() {
        Player killer = getMostDamagePlayer();
        if (killer == null) {
            return null;
        }
        if (killer.isIronman() && !killer.isGroupIronman() && !hasDealtEnoughDamage(killer, 0.5F)) {
            killer = getMostDamageNonIronmanPlayer();
        }
        return killer;
    }

    protected void drop(final Location tile) {
        final Player killer = getDropRecipient();
        if (killer == null) {
            return;
        }
        onDrop(killer);
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }
        if(Utils.random((int) ((double) 30000 / Math.max(1, getCombatLevel()))) == 0) {
            killer.sendFilteredMessage(Colour.GREEN.wrap("You spot a shimmer of crystal coming from the floor beneath you."));
            dropItemAtKiller(killer, new Item(ItemId.CRYSTAL_KEY));
        }
        if(Utils.random((int) ((double) 45000 / Math.max(1, getCombatLevel()))) == 0) {
            killer.sendFilteredMessage(Colour.GREEN.wrap("You spot a stack of vouchers under your feet."));
            dropItemAtKiller(killer, new Item(ItemId.REMNANT_POINT_VOUCHER_1, Math.max(10, Utils.randomNoPlus(25) + 1)));
        }
        final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
        if (drops == null) {
            return;
        }
        if(SlayerHelmetEffects.INSTANCE.getBonusDropRoll(killer))
            NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile));

        NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile));
    }

    private static final Class<?>[] superiorParams = new Class[] {Player.class, NPC.class, Location.class};

    private void spawnSuperior(@Nullable final Entity killer, @NotNull final NPC inferior) {
        if (!(killer instanceof Player)) {
            return;
        }

        final Optional<Class<? extends SuperiorNPC>> superior = SuperiorMonster.getSuperior(inferior.getDefinitions().getName());
        if (!superior.isPresent()) {
            return;
        }

        final Player player = (Player) killer;
        if (player.getTemporaryAttributes().containsKey("superior monster") || !player.getSlayer().isBiggerAndBadder()) {
            return;
        }

        int rate = player.getNumericTemporaryAttributeOrDefault("superior rate", player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED) ? 89 : 99).intValue();
        if (World.hasBoost(XamphurBoost.BONUS_SLAYER_SUPERIOR)) {
            rate = Math.max(50, rate / 2);
        }
        if (player.getVariables().getSlayerBoosterTick() > 0) {
            rate *= 0.75;
        }

        if(player.hasBoon(SlayersFavor.class))
            rate *= 0.75;

        final Slayer slayer = ((Player) killer).getSlayer();
        if (Utils.random(rate) != 0 || !slayer.isCurrentAssignment(inferior)) {
            return;
        }
        if (slayer.getMaster() == SlayerMaster.KONAR_QUO_MATEN) {
            final Assignment assignment = slayer.getAssignment();
            final Class<? extends RegionArea> area = assignment.getArea();
            if (area != null) {
                if (!player.inArea(area) && !assignment.checkExceptions(inferior, area)) {
                    return;
                }
            }
        }
        try {
            final SuperiorNPC sup = superior.get().getDeclaredConstructor(superiorParams).newInstance(killer, inferior, getLocation());
            final Optional<Location> tile = WorldUtil.findEmptySquare(getLocation(), sup.getSize() + 6, sup.getSize(), Optional.of(t -> !CollisionUtil.collides(t.getX(), t.getY(), sup.getSize(), killer.getX(), killer.getY(), killer.getSize())));
            if (tile.isPresent()) {
                sup.setLocation(tile.get());
                sup.spawn();
                player.sendMessage(Colour.RED.wrap("A superior foe has appeared..."));
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void onDrop(@NotNull final Player killer) {
        if (!supplyCache) return;
        final int level = getDefinitions().getCombatLevel();
        if (level < 50) return;

        final boolean wilderness = WildernessArea.isWithinWilderness(getX(), getY());
        final int cappedLevel = Math.min(300, level);
        int chance = 350 - cappedLevel;
        if (wilderness) {
            chance *= 0.5F;
        }

        if (!killer.isIronman() && Utils.random(chance - 1) == 0) {
            final Optional<SupplyCache> loot = SupplyCache.random();
            killer.sendMessage(Colour.RS_GREEN.wrap("The " + getName(killer).toLowerCase() + " drops you some extra supplies."));
            loot.ifPresent(cache -> {
                dropItem(killer, new Item(ItemDefinitions.getOrThrow(cache.getId()).getNotedOrDefault(), Utils.random(cache.getMin(), cache.getMax())));
                dropItem(killer, new Item(995, Utils.random(50000, 150000)));
            });
        }

        chance *= 0.5F;
        if (Utils.random(chance - 1) == 0) {
            killer.sendMessage(Colour.RS_GREEN.wrap("The " + getName(killer).toLowerCase() + " drops you some extra coins."));
            dropItem(killer, new Item(995, Utils.random(50_000, 500_000)));
        }
    }

    protected void invalidateItemCharges(@NotNull final Item item) {
        item.setCharges(DegradableItem.getFullCharges(item.getId()));
    }

    public void dropItem(final Player killer, final Item item, final Location tile, final boolean guaranteedDrop) {
        this.dropItem(killer, item, tile, guaranteedDrop, false);
    }

    protected void spawnDrop(final Item item, final Location tile, final Player killer) {
        World.spawnFloorItem(item, tile, killer, invisibleDropTicks(), visibleDropTicks());
    }

    protected int invisibleDropTicks() {
        return 100;
    }

    protected int visibleDropTicks() {
        return 200;
    }

    public void dropItem(final Player killer, final Item item) {
        this.dropItem(killer, item, getMiddleLocation(), false);
    }

    public void dropItemAtKiller(final Player killer, final Item item) {
        this.dropItem(killer, item, killer.getMiddleLocation(), false);
    }

    public void dropItem(final Player killer, final Item item, boolean bypassGround) {
        this.dropItem(killer, item, getMiddleLocation(), false, bypassGround);
    }

    public final void dropItem(final Player killer, final Drop drop, final Location location) {
        /* Don't try dropping item if the player it belongs to is null */
        if(killer == null)
            return;

        Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
        if(killer.getBoonManager().hasBoon(HoarderMentality.class) && (item != null && item.getName() != null && item.getName().contains("dragonhide") || item.getName().contains("ragon bones"))) {
            item = item.toNote();
        }
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            final Item baseItem = item;
            for (final DropProcessor processor : processors) {
                if ((item = processor.drop(this, killer, drop, item)) == null) {
                    return;
                }
                if (item != baseItem) break;
            }
        }
        //do NOT reference 'drop' after this line, rely on 'item' only!
        dropItem(killer, item, location, drop.isAlways());
    }

    public final void dropItemExcludeProcessor(final Player killer, final Drop drop, final Location location) {
        Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
        if(killer.getBoonManager().hasBoon(HoarderMentality.class) && (item != null && item.getName() != null && item.getName().contains("dragonhide") || item.getName().contains("ragon bones"))) {
            item = item.toNote();
        }
        //do NOT reference 'drop' after this line, rely on 'item' only!
        dropItem(killer, item, location, drop.isAlways());
    }


    public void dropItem(final Player killer, final Item item, final Location tile, final boolean guaranteedDrop, boolean bypassGround) {
        try {
            invalidateItemCharges(item);
            killer.getCollectionLog().add(item);
            WorldBroadcasts.broadcast(killer, BroadcastType.RARE_DROP, item, getName(killer));
            LootBroadcastPlugin.fireEvent(killer.getName(), item, tile, guaranteedDrop);
            if (item.getId() == 11941 && killer.containsItem(item)) {
                return;
            }
            //Amulet of avarice's effect inside revenant caves.
            if (killer.getEquipment().getId(EquipmentSlot.AMULET) == 22557 && GlobalAreaManager.get("Forinthry Dungeon").inside(getLocation())) {
                item.setId(item.getDefinitions().getNotedOrDefault());
            }

            if(DonationToggle.process(killer, item)) {
                return;
            }

            if(killer.getBoonManager().hasBoon(ClueCollector.class)
                    && (Arrays.stream(ClueItem.getCluesArray()).anyMatch(it -> it == item.getId())
                    || Arrays.stream(ClueItem.getBoxesArray()).anyMatch(it -> it == item.getId()))) {
                killer.getInventory().addOrDrop(item);
                return;
            }

            final int id = item.getId();
            if (id == 995 && World.hasBoost(XamphurBoost.DOUBLE_COINS)) {
                item.setAmount(item.getAmount() * 2);
            }

            if ((id == 995 || id == 21555 || id == 6529) && RingOfWealthItem.isRingOfWealth(killer.getRing()) && !killer.getBooleanSetting(Setting.ROW_CURRENCY_COLLECTOR)) {
                killer.getInventory().addOrDrop(item);
                killer.getNotificationSettings().sendDropNotification(item);
                return;
            }
            if (Bonecrusher.handle(killer, item)) {
                return;
            }
            if(bypassGround && item.getId() != 995) {
                killer.getNotificationSettings().sendDropNotification(item);

                if(!killer.tryAddInventoryThenBank(item))
                    spawnDrop(item, tile, killer);
                return;
            }
            killer.getNotificationSettings().sendDropNotification(item);
            spawnDrop(item, tile, killer);
        } catch (Throwable e) {
            log.error("Failed to drop item {} for {}", item, killer, e);
        }
    }

    public int getRespawnDelay() {
        return 60;
    }


    public void setRespawnTask() {
        if (!isFinished()) {
            reset();
            finish();
        }

        final Location respawnTile = getRespawnTile();
        final Region region = World.regions.get(respawnTile.getRegionId());

        int respawnDelay;
        RegionArea regionArea = GlobalAreaManager.getArea(respawnTile);
        if (regionArea != null && areaDefinedRespawnTimers.containsKey(regionArea.getClass()))
            respawnDelay = areaDefinedRespawnTimers.getInt(regionArea.getClass());
        else
            respawnDelay = getRespawnDelay();


        WorldTasksManager.schedule(() -> {
            if (respawnTile.getX() >= 6400 && World.regions.get(respawnTile.getRegionId()) != region) {
                return;
            }
            spawn();
        }, respawnDelay);
    }

    public boolean shouldUpdateOptionsMask = false;
    public void setShouldUpdateOptionsMask(boolean set) {
        this.shouldUpdateOptionsMask = set;
    }

    public NPC spawn() {
        if (!isFinished())
            throw new RuntimeException("The NPC has already been spawned: " + getId() + ", " + getDefinitions().getName() + ", " + getNpcSpawn() + ", " + getLocation());

        boolean added = false;
        try {
            added = World.addNPC(this);
            location.setLocation(getRespawnTile());
            setFinished(false);
            updateLocation();
            if (!combatDefinitionsMap.isEmpty()) {
                combatDefinitionsMap.clear();
            }
            updateCombatDefinitions();
            optionMask = 31;
            if(shouldUpdateOptionsMask)
                updateFlags.flag(UpdateFlag.HIDE_OPTIONS);
            writeLog("NPC.spawn("+added+")");
        } catch (Throwable e){
            log.error("Failed to spawn npc {}", this, e);
            writeLog("NPC.spawn("+added+")", e);
        }
        return this;
    }

    public void updateLocation() {
        setLastRegionId(0);
        World.loadRegion(location.getRegionId());
        World.updateEntityChunk(this, false);
        loadMapRegions(false);
        clip();
    }

    @Override
    public int getCombatLevel() {
        return getDefinitions().getCombatLevel();
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.NPC;
    }

    @Override
    public boolean isAcceptableTarget(final Entity entity) {
        return true;
    }

    @Override
    public boolean isPotentialTarget(final Entity entity) {
        final int entityX = entity.getX();
        final int entityY = entity.getY();
        final int entitySize = entity.getSize();
        final int x = getX();
        final int y = getY();
        final int size = getSize();
        final long currentTime = Utils.currentTimeMillis();
        final long currentTick = WorldThread.getCurrentCycle();
        return !entity.isMaximumTolerance() && (entity.isMultiArea() || entity.getAttackedBy() == this || (entity.getAttackedByDelay() <= currentTick && entity.getFindTargetDelay() <= currentTime)) && (!ProjectileUtils.isProjectileClipped(this, entity, getLocation(), entity.getLocation(), combatDefinitions.isMelee()) || CollisionUtil.collides(x, y, size, entityX, entityY, entitySize)) && (forceAggressive || combatDefinitions.isAlwaysAggressive() || combatDefinitions.isAggressive() && entity.getCombatLevel() <= (getCombatLevel() << 1)) && (!(entity instanceof NPC) || ((NPC) entity).getDefinitions().containsOption("Attack")) && isAcceptableTarget(entity) && (!(entity instanceof Player) || !isTolerable() || !((Player) entity).isTolerant(getLocation()));
    }

    @Override
    public void unlink() {
    }

    @Override
    public List<Entity> getPossibleTargets(final EntityType type, final int radius) {
        if (!possibleTargets.isEmpty()) {
            possibleTargets.clear();
        }
        CharacterLoop.populateEntityList(possibleTargets, this.getMiddleLocation(),
                radius, type.getClazz(), predicate);
        return possibleTargets;
    }

    @Override
    public int getPossibleTargetsDefaultRadius() {
        return aggressionDistance + (getSize() / 2);
    }

    public void remove() {
        finish();
    }

    protected void onMovement() {
    }

    @Override
    public void cancelCombat() {
        combat.setTarget(null);
    }

    @Override
    public void performDefenceAnimation(Entity attacker) {
        final BlockDefinitions blockDefinitions = getCombatDefinitions().getBlockDefinitions();
        setUnprioritizedAnimation(blockDefinitions.getAnimation());
        final SoundEffect sound = blockDefinitions.getSound();
        if (sound != null) {
            if (sound.getRadius() == 0) {
                if (attacker instanceof Player) {
                    ((Player) attacker).sendSound(sound);
                }
            } else {
                World.sendSoundEffect(this::getMiddleLocation, sound);
            }
        }
    }

    @Override
    public void performDeathAnimation() {
        final SpawnDefinitions spawnDefinitions = combatDefinitions.getSpawnDefinitions();
        setAnimation(spawnDefinitions.getDeathAnimation());
    }

    @Override
    public int drainSkill(final int skill, final double percentage) {
        if (!canDrainSkill(skill)) {
            return 0;
        }

        return combatDefinitions.drainSkill(skill, percentage, 0);
    }

    @Override
    public int drainSkill(final int skill, final double percentage, final int minimumDrain) {
        if (!canDrainSkill(skill)) {
            return 0;
        }

        return combatDefinitions.drainSkill(skill, percentage, minimumDrain);
    }

    @Override
    public int drainSkill(final int skill, final int amount) {
        if (!canDrainSkill(skill)) {
            return 0;
        }

        return combatDefinitions.drainSkill(skill, amount);
    }

    public boolean canDrainSkill(final int skill) {
        return true;
    }

    public void listenScheduleHit(Hit hit) { }

    @Override
    public boolean canAttack(final Player source) {
        if (!definitions.containsOptionCaseSensitive("Attack")) {
            source.sendMessage("You can't attack this npc.");
            return false;
        }
        return true;
    }

    /**
     * Whether or not this npc can be attacked by a multicannon from the given player.
     * @param player the player the multicannon belongs to
     */
    public boolean canBeMulticannoned(@NotNull final Player player) {
        return true;
    }

    public boolean isAttackable() {
        return definitions.containsOptionCaseSensitive("Attack");
    }

    @Override
    public void autoRetaliate(final Entity source) {
        if (combat.getTarget() == source || source == this) return;
        if (!combat.isForceRetaliate()) {
            final Entity target = combat.getTarget();
            if (target != null) {
                if (target instanceof Player) {
                    final Player player = (Player) target;
                    if (player.getActionManager().getAction() instanceof PlayerCombat) {
                        final PlayerCombat combat = (PlayerCombat) player.getActionManager().getAction();
                        if (combat.getTarget() == this) {
                            return;
                        }
                    }
                } else {
                    final NPC npc = (NPC) target;
                    if (npc.getCombat().getTarget() == this) return;
                }
            }
        }
        randomWalkDelay = 1;
        resetWalkSteps();
        final Entity previousTarget = combat.getTarget();
        combat.setTarget(source, TargetSwitchCause.AUTO_RETALIATE);

        if (previousTarget == null && combat.getCombatDelay() == 0) {
            combat.setCombatDelay(2);
        }
    }

    public boolean isAbstractNPC() {
        return respawnTile == null;
    }

    public String getName(final Player player) {
        int transformedId = getTransformedId(getId(), player);
        NPCDefinitions def = NPCDefinitions.get(transformedId);
        return def == null ? "null" : def.getName();
    }

    public String getName() {
        NPCDefinitions def = NPCDefinitions.get(getId());
        return def == null ? "null" : def.getName();
    }

    @Override
    public boolean isInitialized() {
        return true; // Always true for npcs.
    }

    @Override
    public boolean isMaximumTolerance() {
        return false;
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        final int dir = DirectionUtil.getMoveDirection(nextX - lastX, nextY - lastY);
        if (dir == -1 || !isMovableEntity() || ContentConstants.SPAWN_MODE) {
            return false;
        }
        if (check && !canMove(lastX, lastY, dir)) {
            return false;
        }
        walkSteps.enqueue(WalkStep.getHash(dir, nextX, nextY, check));
        return true;
    }

    public float getAccuracyMultiplier() { return 1F; }

    protected boolean isMovableEntity() {
        return true;
    }

    public Object2LongMap<Entity> getInteractingEntities() {
        return interactingEntities;
    }

    public int getId() {
        return id;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public NPCCombatDefinitions getCombatDefinitions() {
        return combatDefinitions;
    }

    public void setRespawnTile(ImmutableLocation respawnTile) {
        this.respawnTile = respawnTile;
    }

    public NPCCombat getCombat() {
        return combat;
    }

    public void setCombat(NPCCombat combat) {
        this.combat = combat;
    }

    public int getInteractionDistance() {
        return interactionDistance;
    }

    public void setInteractionDistance(int interactionDistance) {
        this.interactionDistance = interactionDistance;
    }

    public int getNextTransformation() {
        return nextTransformation;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public boolean isForceFollowClose() {
        return forceFollowClose;
    }

    public void setForceFollowClose(boolean forceFollowClose) {
        this.forceFollowClose = forceFollowClose;
    }

    public int getAggressionDistance() {
        return aggressionDistance;
    }

    public void setAggressionDistance(int aggressionDistance) {
        this.aggressionDistance = aggressionDistance;
    }

    public int getDeathDelay() {
        return deathDelay;
    }

    public void setDeathDelay(int deathDelay) {
        this.deathDelay = deathDelay;
    }

    public int getDamageCap() {
        return damageCap;
    }

    public void setDamageCap(int damageCap) {
        this.damageCap = damageCap;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(int attackDistance) {
        this.attackDistance = attackDistance;
    }

    public EntityType getTargetType() {
        return targetType;
    }

    public void setTargetType(EntityType targetType) {
        this.targetType = targetType;
    }

    public boolean isForceAggressive() {
        return forceAggressive;
    }

    public void setForceAggressive(boolean forceAggressive) {
        this.forceAggressive = forceAggressive;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Location getForceWalk() {
        return forceWalk;
    }

    public Direction getSpawnDirection() {
        return spawnDirection;
    }

    public void setSpawnDirection(Direction spawnDirection) {
        this.spawnDirection = spawnDirection;
    }

    public int getSwapTicks() {
        return swapTicks;
    }

    public void setSwapTicks(int swapTicks) {
        this.swapTicks = swapTicks;
    }

    public NPCSpawn getNpcSpawn() {
        return npcSpawn;
    }

    public void setNpcSpawn(NPCSpawn npcSpawn) {
        this.npcSpawn = npcSpawn;
    }

    public boolean isInWilderness() {
        return inWilderness;
    }

    public int getRandomWalkDelay() {
        return randomWalkDelay;
    }

    public void setRandomWalkDelay(int randomWalkDelay) {
        this.randomWalkDelay = randomWalkDelay;
    }

    public long getFlinchTime() {
        return flinchTime;
    }

    public void setFlinchTime(long flinchTime) {
        this.flinchTime = flinchTime;
    }

    public boolean isIntelligent() {
        return intelligent;
    }

    public void setIntelligent(boolean intelligent) {
        this.intelligent = intelligent;
    }

    public RetreatMechanics getRetreatMechanics() {
        return retreatMechanics;
    }

    public long getTimeOfDeath() {
        return timeOfDeath;
    }

    public void setTimeOfDeath(long timeOfDeath) {
        this.timeOfDeath = timeOfDeath;
    }

    private boolean applyDamageFromHitsAfterDeath = false;

    public void markApplyDamageFromHitsAfterDeath() {
        this.applyDamageFromHitsAfterDeath = true;
    }

    public boolean applyDamageFromHitsAfterDeath() {
        return this.applyDamageFromHitsAfterDeath;
    }

    public boolean isAlwaysTakeMaxHit(HitType type) { return false; }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
        writeLog("setIndex("+index+")");
    }

    @Override
    public void setFinished(boolean finished) {
        super.setFinished(finished);
        writeLog("setFinished("+finished+")");
    }

    private void writeLog(String action) {
        writeLog(action, null);
    }
    private void writeLog(String action, Throwable exception) {
        if (DeveloperCommands.INSTANCE.getNpcLogging()) {
            final Throwable finalException = exception == null ? new Exception("Stack trace") : exception;
            CoresManager.getServiceProvider().submit(() -> {
                final long cycle = WorldThread.getCurrentCycle();
                final File file = Paths.get("data/logs/npcs/" + getIndex(), "" + cycle + ".txt").toFile();
                final File directory = file.getParentFile();
                if (!directory.exists()){
                    directory.mkdirs();
                }
                try (final FileOutputStream out = new FileOutputStream(file, true)) {
                    final PrintWriter writer = new PrintWriter(out);
                    writer.println(action);
                    if (definitions != null) {
                        writer.println(definitions.getName());
                        writer.println(definitions.getId());
                    }
                    writer.println(getPosition());
                    finalException.printStackTrace(writer);
                    writer.flush();
                    writer.close();
                } catch (Exception ignored) {
                }
            });
        }
    }

    public ModelOverride getOverrides() {
        return modelOverride;
    }

}
