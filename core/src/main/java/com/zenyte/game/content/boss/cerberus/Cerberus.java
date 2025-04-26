package com.zenyte.game.content.boss.cerberus;

import com.zenyte.game.content.boss.cerberus.area.CerberusLairInstance;
import com.zenyte.game.content.boss.cerberus.area.StaticCerberusLair;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Kris | 22. march 2018 : 15:55.08
 * @version 2.0 since 07/08/2020 - Revised combat script to better replicate OS.
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Cerberus extends NPC implements CombatScript, Spawnable {
    /**
     * The number of lava pools that are sent at a time when the special attack occurs.
     */
    private static final int FLAMES_COUNT = 3;
    /**
     * The maximum number of directions an entity can face.
     */
    private static final int MAXIMUM_DIRECTIONS = 2048;
    /**
     * The number of faceable directions in one of the eight possible directions.
     */
    private static final int OCTANT_DIRECTION_SECTOR_LENGTH = MAXIMUM_DIRECTIONS >> 3;
    /**
     * The ids of the respective Cerberus NPCs.
     */
    private static final int COMBAT_CERBERUS = NpcId.CERBERUS;
    private static final int SLEEPING_CERBERUS = NpcId.CERBERUS_5863;
    /**
     * The hitpoints threshold for Cerberus to perform the summoned souls special attack. Health has to be under this value to be eligible.
     */
    private static final int SUMMONED_SOULS_HITPOINTS_THRESHOLD = 400;
    /**
     * The hitpoints threshold for Cerberus to perform the lava pools special attack. Health has to be under this value to be eligible.
     */
    private static final int LAVA_POOLS_HITPOINTS_THRESHOLD = 200;
    /**
     * The interval at which Cerberus performs the combo attack. The fight begins with this attack. Takes precedence over the summoned souls attack.
     */
    private static final int COMBO_ATTACK_FREQUENCY = 11;
    /**
     * The interval at which Cerberus performs the summoned souls attack. Takes precedence over the lava pools attack.
     */
    private static final int SUMMONED_SOULS_ATTACK_FREQUENCY = 7;
    /**
     * The interval at which Cerberus performs the lava pools attack.
     */
    private static final int LAVA_POOLS_ATTACK_FREQUENCY = 5;
    /**
     * The number of ticks before Cerberus performs another attack after the combo special.
     */
    private static final int COMBO_ATTACK_SPEED = 10;
    /**
     * The number of ticks before Cerberus performs another attack after the summoned souls special.
     */
    private static final int SUMMONED_SOULS_ATTACK_SPEED = 10;
    /**
     * The number of ticks before Cerberus performs another attack after the lava pools special.
     */
    private static final int LAVA_ATTACK_SPEED = 8;
    /**
     * The offset in number of game tiles from the head position of Cerberus, this value is altered however. The actual value is (Cerberus' size + offset) / 2, which represents the number of tiles
     * from the center of Cerberus, in the direction its head is facing. This offset is used to calculate the rough placement of the lava pools, which is then randomized around itself.
     */
    private static final int SIZE_OFFSET_LAVA_POOL = 7;
    /**
     * The radius of the marker location randomization. The original position is shifted away from the center of Cerberus in the direction of its head, it is then randomized around itself for the
     * value of this constant.
     */
    private static final int MARKER_RANDOMIZATION_DISTANCE = 2;
    /**
     * The duration of the marker in game ticks.
     */
    private static final int MARKER_DURATION = 14;
    /**
     * The marker damage frequency, how often damage is applied to players on and around it. This value cannot be below one.
     */
    private static final int MARKER_DAMAGE_FREQUENCY = 2;
    /**
     * The maximum distance from which the markers deal damage, a value of one represents a 3x3 tiles box, as it is the radius for it.
     */
    private static final int MARKER_REACH_DISTANCE = 1;
    /**
     * The minimum amount of damage applied on the people who are standing directly on top of a marker.
     */
    private static final int MARKER_DIRECT_MIN_DAMAGE = 10;
    /**
     * The maximum amount of damage applied on the people who are standing directly on top of a marker.
     */
    private static final int MARKER_DIRECT_MAX_DAMAGE = 15;
    /**
     * The minimum amount of damage applied on the people who are standing in reach of the marker, but not on top of it.
     */
    private static final int MARKER_DISTANCE_MIN_DAMAGE = 7;
    /**
     * The maximum amount of damage applied on the people who are standing in reach of the marker, but not on top of it.
     */
    private static final int MARKER_DISTANCE_MAX_DAMAGE = 7;
    /**
     * The number of game ticks it takes before Cerberus respawns.
     */
    private static final int RESPAWN_TIMER = 14;
    /**
     * The possible attack styles that Cerberus can use.
     */
    private static final List<AttackType> possibleOrderedAttackTypes = Collections.unmodifiableList(Arrays.asList(MAGIC, RANGED, STAB));
    /**
     * The animation Cerberus performs when it calls out the summoned souls.
     */
    private static final Animation summonedSoulsAttackAnim = new Animation(4494);
    /**
     * The animation Cerberus performs when it calls out the lava pools attack.
     */
    private static final Animation lavaPoolsAttackAnim = new Animation(4493);
    /**
     * The animation Cerberus performs when doing a melee attack.
     */
    private static final Animation meleeAnim = new Animation(4491);
    /**
     * The animation Cerberus performs when doing a magic attack.
     */
    private static final Animation magicAnim = new Animation(4489);
    /**
     * The animation Cerberus performs when doing a ranged attack.
     */
    private static final Animation rangedAnim = new Animation(4490);
    /**
     * The animation Cerberus performs when a player approaches it, waking it from its sleep.
     */
    private static final Animation leaningFromSleep = new Animation(4486);
    /**
     * The animation Cerberus performs when its chamber becomes empty of people within attach range, putting it back to sleep.
     */
    private static final Animation leaningToSleep = new Animation(4487);
    /**
     * The graphics effect sent on a game tile when Cerberus calls out the lava pools attack.
     */
    private static final Graphics markerGraphics = new Graphics(1246);
    /**
     * The graphics effect sent on a game tile when a marker expires, or when Cerberus disappears(after death) while markers are still alive.
     */
    private static final Graphics markerExplosionGraphics = new Graphics(1247);
    /**
     * The graphics effect played on top of the target when they get hit by Cerberus' ranged attack.
     */
    private static final Graphics rangedGraphics = new Graphics(1244);
    /**
     * The graphics effect played on top of the target when they get hit by Cerberus' magic attack.
     */
    private static final Graphics magicGraphics = new Graphics(1243);
    /**
     * The sound effect Cerberus performs when launching the magic attack.
     */
    private static final SoundEffect magicCastSound = new SoundEffect(162, 10, 0);
    /**
     * The sound effect played on top of Cerberus' target when a magic attack reaches them.
     */
    private static final SoundEffect magicLandSound = new SoundEffect(163, 10, -1);
    /**
     * The sound effect Cerberus performs when calling out the summoned souls.
     */
    private static final SoundEffect callingSummonedSoulsSound = new SoundEffect(911, 10, 0);
    /**
     * The projectile Cerberus sends out when performing its ranged attack.
     */
    private static final Projectile rangedProj = new Projectile(1245, 70, 20, 30, 15, 45, 64, 15);
    /**
     * The projectile Cerberus sends out when performing its magic attack.
     */
    private static final Projectile magicProj = new Projectile(1242, 70, 20, 30, 15, 45, 64, 15);
    /**
     * The projectiles Cerberus sends out when performing the lava pools special attack. A marker is sent out from each of Cerberus' heads.
     */
    private static final Projectile markerProj = new Projectile(1247, 0, 0, 30, 0, 30, 0, 10);
    /**
     * The cerberus room constant in which the this Cerberus lays.
     */
    private final CerberusRoom cerberusRoom;

    /**
     * A list of markers currently active on the ground. These markers are removed after they expire, or when Cerberus dies.
     */
    private final List<ImmutableLocation> markers;
    /**
     * A list of summoned souls currently spawned. These souls are removed after they finish retreating, or when Cerberus dies(and they perform their attack).
     */
    private final List<SummonedSoul> summonedSouls;
    /**
     * The lair area for this Cerberus.
     * Do not access the variable directly, instead call {@link Cerberus#getLairArea()} for type-safety.
     */
    private PolygonRegionArea _area;
    /**
     * The number of attacks Cerberus has performed since it spawned. Reset upon death.
     */
    private int attackCount;
    /**
     * The number of game ticks for which Cerberus' processing is halted, used to properly put Cerberus to and from sleep.
     */
    private int forcedDelay;
    private int summonedSoulMitigation;
    private boolean hitByMelee;
    private boolean hitByLavaPool;

    private CerberusLairInstance instance = null;

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (HitType.MELEE.equals(hit.getHitType()) && hit.getDamage() > 0) {
            hitByMelee = true;
        }
    }

    /**
     * Constructor for Cerberus boss, follows the strict parameters used to instantiate the creature dynamically. NPC cannot be spawned outside of its dedicated areas.
     *
     * @param id     the id of the Cerberus NPC.
     * @param tile   the tile upon which Cerberus spawns.
     * @param facing the direction Cerberus is facing upon spawn.
     * @param radius the walk radius of Cerberus.
     */
    public Cerberus(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        markers = new ObjectArrayList<>(3);
        this.summonedSouls = new ObjectArrayList<>(3);
        this.cerberusRoom = CollectionUtils.findMatching(CerberusRoom.getValues(), value -> tile == null || value.getRegionId() == tile.getRegionId());
        if (tile != null) {
            if (this.getRoom() == null) {
                throw new IllegalStateException("Unable to locate Cerberus' lair.");
            }
        }
        this.combat = new NPCCombat(this) {
            @SuppressWarnings("SuspiciousMethodCalls")
            @Override
            protected boolean checkAll() {
                return super.checkAll() && getLairArea().getPlayers().contains(target);
            }
        };
    }

    /**
     * Instanced Cerberus Constructor
     */
    public Cerberus(final Location tile, final CerberusLairInstance instance) {
        super(NpcId.CERBERUS, tile, Direction.SOUTH, 0);
        markers = new ObjectArrayList<>(3);
        this.summonedSouls = new ObjectArrayList<>(3);
        this.cerberusRoom = CerberusRoom.INSTANCED;
        this.instance = instance;
        this.combat = new NPCCombat(this) {
            @SuppressWarnings("SuspiciousMethodCalls")
            @Override
            protected boolean checkAll() {
                return super.checkAll() && getLairArea().getPlayers().contains(target);
            }
        };
    }

    public boolean isInstanced() {
        return this.instance != null;
    }

    private CerberusRoom getRoom() {
        return this.cerberusRoom;
    }

    /**
     * Spawns the Cerberus NPC. This method can throw an exception if it is being spawned outside of its designated areas, halting the spawn process.
     * Additionally resets the attack counter and sets the aggression distance and makes Cerberus forcibly aggressive.
     *
     * @return this NPC.
     */
    @Override
    public NPC spawn() {
        if(isInstanced()) {
            this._area = this.instance;
        } else {
            this._area = GlobalAreaManager.getArea(getRoom().getClazz());
        }
        this.attackCount = 0;
        setAggressionDistance(3);
        setForceAggressive(true);
        return super.spawn();
    }

    /**
     * Gets the lair of Cerberus, throws an exception if a lair isn't found.
     *
     * @return the lair in which Cerberus resides.
     */
    @NotNull
    private PolygonRegionArea getLairArea() {
        return Objects.requireNonNull(_area);
    }

    /**
     * @return whether or not Cerberus is currently in its sleeping form.
     */
    private boolean isSleeping() {
        return getId() == SLEEPING_CERBERUS;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    /**
     * Transforms Cerberus from and to its sleeping form. Resets all its stats and health during all transformations.
     *
     * @param id the id of the creature to transform Cerberus to.
     */
    @Override
    public void setTransformation(final int id) {
        nextTransformation = id;
        this.id = id;
        getUpdateFlags().flag(UpdateFlag.TRANSFORMATION);
        reset();
        if (preserveStatsOnTransformation()) {
            updateTransformationalDefinitions();
        } else {
            updateCombatDefinitions();
        }
    }

    @Override
    public int getRespawnDelay() {
        return RESPAWN_TIMER;
    }

    /**
     * Sets the combat definitions of Cerberus without caching the existing levels and bonuses, allowing for them to be properly reset when Cerberus falls asleep or wakes up.
     *
     * @param definitions the definitions to give this NPC.
     */
    @Override
    public void setCombatDefinitions(final NPCCombatDefinitions definitions) {
        this.combatDefinitions = definitions;
    }

    /**
     * @param source the player attempting to attack Cerberus.
     * @return whether or not the player can attack Cerberus.
     */
    @Override
    public boolean canAttack(final Player source) {
        if (getId() == SLEEPING_CERBERUS) {
            source.sendMessage("You can't attack this npc.");
            return false;
        }
        return true;
    }

    /**
     * Processes the Cerberus creature in the following order:
     * Markers are processed at all times, even while Cerberus dies. They're removed once Cerberus has been removed from the game, during its respawn cycle.
     * If there is a delay appended, Cerberus will halt processing until the delay has ran up.
     * If Cerberus is sleeping, upon approaching it, it'll wake up and attack the player. While awake, it fights whoever is in the room. If no one is left in the room, it retreats to the center and
     * goes back to sleep.
     */
    @Override
    public void processNPC() {
        if (forcedDelay > 0) {
            forcedDelay--;
            return;
        }
        if (isDead()) {
            return;
        }
        if (isSleeping()) {
            processSleepingCerberus();
        } else {
            processFightingCerberus();
        }
    }

    /**
     * Processes Cerberus while it is in its sleeping form, awaking it upon being approached, or retreating it back to the center if it isn't there yet.
     */
    private void processSleepingCerberus() {
        //If Cerberus is at its spawn location, let's see if there's anyone we can attack.
        if (location.matches(respawnTile)) {
            //Check for possible targets, if one is found, spawn fires.
            final List<Entity> possibleTargets = getPossibleTargets(targetType);
            if (!possibleTargets.isEmpty()) {
                setAnimation(leaningFromSleep);
                forcedDelay += 2;
                WorldTasksManager.schedule(() -> {
                    final ObjectArrayList<Player> possibleRemainingTargets = new ObjectArrayList<>(getLairArea().getPlayers());
                    setTransformation(COMBAT_CERBERUS);
                    spawnFlames();
                    setAnimation(Animation.STOP);
                    if (possibleRemainingTargets.isEmpty()) {
                        return;
                    }
                    final Player target = possibleRemainingTargets.get(Utils.random(possibleRemainingTargets.size() - 1));
                    if (isCancelled(target)) {
                        return;
                    }
                    setTarget(target);
                }, 1);
            }
            return;
        }
        //If Cerberus isn't at its spawn location, let's try to move it there.
        if (!hasWalkSteps()) {
            addWalkSteps(respawnTile.getX(), respawnTile.getY());
            //If the creature is about to take the last step, queue it up for facing a different direction.
            if (walkSteps.size() == 1) {
                faceSouth(true);
            }
        }
    }

    /**
     * Processes Cerberus while it is awake, fighting against whoever is in the room, past the fire.  If no one is left, resets itself and moves to the center, going back to sleep.
     */
    private void processFightingCerberus() {
        //If there's no one left in the attackable part of the lair, let's remove Cerberus' target.
        if (getLairArea().getPlayers().isEmpty() || (combat.getTarget() == null && getPossibleTargets(targetType).isEmpty())) {
            combat.reset();
            attackCount = 0;
            setAnimation(Animation.STOP);
            setCantInteract(true);
            resetWalkSteps();
            addWalkSteps(respawnTile.getX(), respawnTile.getY());
            final int delay = walkSteps.size();
            forcedDelay = delay + 3;
            WorldTasksManager.scheduleOrExecute(() -> {
                //If Cerberus is at its spawn location, let's immediately turn towards the southern exit, finishing the "reset".
                faceSouth(false);
                setAnimation(leaningToSleep);
                WorldTasksManager.schedule(() -> {
                    setAnimation(Animation.STOP);
                    setCantInteract(false);
                    removeFlames();
                    //Let's transform Cerberus to the sleeping form and append movement if it isn't at the respawn location.
                    setTransformation(SLEEPING_CERBERUS);
                }, 1);
            }, delay);
            return;
        }
        //Let's execute the normal combat code if there's someone in the lair.
        combat.process();
    }

    /**
     * Upon being removed from the game, the markers in the room explode and are removed.
     *
     * @param source the entity which killed Cerberus.
     */
    @Override
    public void onFinish(@NotNull final Entity source) {
        super.onFinish(source);
        this.id = SLEEPING_CERBERUS;
        removeFlames();
        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("cerberus", 75, CAType.CERBERUS_VETERAN);
            player.getCombatAchievements().checkKcTask("cerberus", 150, CAType.CERBERUS_MASTER);
            if (summonedSoulMitigation >= 6) {
                player.getCombatAchievements().complete(CAType.GHOST_BUSTER);
            }
            if (!hitByMelee) {
                player.getCombatAchievements().complete(CAType.ANTI_BITE_MECHANICS);
            }
            if (!hitByLavaPool) {
                player.getCombatAchievements().complete(CAType.UNREQUIRED_ANTIFIRE);
            }
        }
    }

    /**
     * Checks to see which players are within the reach of the markers, dealing damage to them at an interval.
     *
     * @param marker the marker to check.
     */
    private void checkMarkerCollisions(@NotNull final ImmutableLocation marker) {
        for (final Player player : getLairArea().getPlayers()) {
            final int distance = player.getLocation().getTileDistance(marker);
            if (distance <= MARKER_REACH_DISTANCE) {
                hitByLavaPool = true;
                applyMarkerDamage(player, distance == 0);
            }
        }
    }

    /**
     * Explodes the marker set on the ground, damaging anyone within its reach. The damage can occur outside of the normal interval, but cannot occur twice on the same tick(once through explosion,
     * and once through normal processing - one takes precedence over the other)
     *
     * @param marker the marker on the ground.
     */
    private void explodeMarker(@NotNull final ImmutableLocation marker) {
        World.sendGraphics(markerExplosionGraphics, marker);
        checkMarkerCollisions(marker);
    }

    /**
     * Applies the damage from the marker to the entity within its reach, dealing more damage if the entity is directly on top of it.
     *
     * @param target        the target whom to damage.
     * @param standingOntop whether or not they are standing directly on top of it.
     */
    private void applyMarkerDamage(@NotNull final Entity target, final boolean standingOntop) {
        delayHit(-1, target, new Hit(Utils.random(standingOntop ? MARKER_DIRECT_MIN_DAMAGE : MARKER_DISTANCE_MIN_DAMAGE, standingOntop ? MARKER_DIRECT_MAX_DAMAGE : MARKER_DISTANCE_MAX_DAMAGE), HitType.DEFAULT));
    }

    /**
     * Makes Cerberus face south so that the fight is finally properly 'reset' so to speak.
     *
     * @param withDelay whether or not to delay the execution of the faced-direction by one game tick.
     */
    private void faceSouth(final boolean withDelay) {
        WorldTasksManager.scheduleOrExecute(() -> setFaceLocation(getMiddleLocation().transform(Direction.SOUTH, 10)), withDelay ? 0 : -1);
    }

    /**
     * Spawns the flames upon approaching Cerberus.
     */
    private void spawnFlames() {
        final Location westernFire = getRoom().getWesternFireLocation();
        if(isInstanced()) {
            for (int i = 0; i < FLAMES_COUNT; i++) {
                final Location fireLocation = instance.getLocation(westernFire).transform(Direction.EAST, i);
                final WorldObject existingFire = World.getObjectWithType(fireLocation, 10);
                if (existingFire != null) {
                    continue;
                }
                World.spawnObject(new WorldObject(ObjectId.FLAMES, 10, 0, fireLocation));
            }
        } else {
            for (int i = 0; i < FLAMES_COUNT; i++) {
                final Location fireLocation = westernFire.transform(Direction.EAST, i);
                final WorldObject existingFire = World.getObjectWithType(fireLocation, 10);
                //If there's an existing fire, or any other type-10 object allocated, we shall move on.
                //The benefit of this strategy is to block access to Cerberus if something goes wrong,
                //we could spawn arbitrary objects which wouldn't get removed.
                if (existingFire != null) {
                    continue;
                }
                World.spawnObject(new WorldObject(ObjectId.FLAMES, 10, 0, fireLocation));
                //Iterate the people outside of the combat zone, to see if anyone is sitting on top of the fire, moving them south if that is the case.
                final StaticCerberusLair lair = GlobalAreaManager.getArea(StaticCerberusLair.class);
                for (final Player player : lair.getPlayers()) {
                    if (player.isNulled() || player.isFinished() || player.isDead()) {
                        continue;
                    }
                    if (player.matches(fireLocation)) {
                        player.addWalkSteps(fireLocation.getX(), fireLocation.getY() - 1, 1, true);
                    }
                }
            }
        }
    }

    /**
     * Removes the flames on the ground if there are any. Does not remove an object if it hasn't got the id of the flames.
     */
    private void removeFlames() {
        final Location westernFire = getRoom().getWesternFireLocation();
        if(isInstanced()) {
            for (int i = 0; i < FLAMES_COUNT; i++) {
                final Location fireLocation = instance.getLocation(westernFire).transform(Direction.EAST, i);
                final WorldObject existingFire = World.getObjectWithType(fireLocation, 10);
                //If there's an existing fire, we shall remove it. If the object isn't a fire, we skip it.
                if (existingFire == null || existingFire.getId() != ObjectId.FLAMES) {
                    continue;
                }
                World.removeObject(existingFire);
            }
        } else {
            for (int i = 0; i < FLAMES_COUNT; i++) {
                final Location fireLocation = westernFire.transform(Direction.EAST, i);
                final WorldObject existingFire = World.getObjectWithType(fireLocation, 10);
                //If there's an existing fire, we shall remove it. If the object isn't a fire, we skip it.
                if (existingFire == null || existingFire.getId() != ObjectId.FLAMES) {
                    continue;
                }
                World.removeObject(existingFire);
            }
        }
    }

    /**
     * Calculates the position of the projectile's start based on whichever head we need.
     *
     * @param target the target who Cerberus is attacking.
     * @param head   the head of Cerberus which is attacking.
     * @return the location from which a projectile is shot.
     */
    private Location getProjectileStartPosition(@NotNull final Entity target, @NotNull final CerberusHead head) {
        return getProjectileStartPosition(target, head, 0);
    }

    /**
     * Calculates the position of the projectile's start based on whichever head we need.
     *
     * @param target     the target who Cerberus is attacking.
     * @param head       the head of Cerberus which is attacking.
     * @param sizeOffset the offset in size to move the position further away from Cerberus.
     * @return the location from which a projectile is shot.
     */
    private Location getProjectileStartPosition(@NotNull final Entity target, @NotNull final CerberusHead head, final int sizeOffset) {
        return getFaceLocation(target, getSize() + sizeOffset, head == CerberusHead.RIGHT ? -OCTANT_DIRECTION_SECTOR_LENGTH : head == CerberusHead.LEFT ? OCTANT_DIRECTION_SECTOR_LENGTH : 0);
    }

    /**
     * @return whether or not the next attack is the combo attack.
     */
    private boolean isComboAttack() {
        return (attackCount - 1) % (COMBO_ATTACK_FREQUENCY - 1) == 0;
    }

    /**
     * @return whether or not the next attack is the summoned souls attack.
     */
    private boolean isSummonedSoulsAttack() {
        return getHitpoints() < SUMMONED_SOULS_HITPOINTS_THRESHOLD && attackCount % SUMMONED_SOULS_ATTACK_FREQUENCY == 0;
    }

    /**
     * @return whether or not the next attack is the lava pools attack.
     */
    private boolean isLavaPoolsAttack() {
        return getHitpoints() < LAVA_POOLS_HITPOINTS_THRESHOLD && attackCount % LAVA_POOLS_ATTACK_FREQUENCY == 0;
    }

    @Override
    public int attack(Entity target) {
        //Let's increment the attack count - it determines the combo attacks.
        attackCount++;
        if (isComboAttack()) {
            executeComboAttack(target);
            return COMBO_ATTACK_SPEED;
        }
        if (isLavaPoolsAttack()) {
            executeLavaPoolsAttack(target);
            return LAVA_ATTACK_SPEED;
        }
        if (isSummonedSoulsAttack()) {
            executeSummonedSoulsAttack(target);
            return SUMMONED_SOULS_ATTACK_SPEED;
        }
        final ObjectArrayList<AttackType> possibleStyles = new ObjectArrayList<>(possibleOrderedAttackTypes);
        if (!isWithinMeleeDistance(this, target)) {
            possibleStyles.remove(STAB);
        }
        final AttackType style = Utils.getRandomCollectionElement(possibleStyles);
        executeAttack(target, style, style.isMelee() ? CerberusHead.CENTER : Utils.random(1) == 0 ? CerberusHead.LEFT : CerberusHead.RIGHT);
        return combatDefinitions.getAttackSpeed();
    }

    /**
     * Removes the summoned soul from the collection of souls upon being removed from the game.
     *
     * @param summonedSoul the summoned soul to be removed.
     */
    void removeSoul(@NotNull final SummonedSoul summonedSoul) {
        summonedSouls.remove(summonedSoul);
    }

    /**
     * Checks if the soul was spawned from this Cerberus instance. The souls have a long travel distance and
     * by the time the soul reaches the entrance, Cerberus could have died and respawned.
     *
     * @param summonedSoul the soul to compare.
     * @return whether or not the soul was spawned by this Cerberus.
     */
    boolean isCurrentSoul(@NotNull final SummonedSoul summonedSoul) {
        return summonedSouls.contains(summonedSoul);
    }

    /**
     * Executes the summoned souls special attack, spawning three of them at the river of souls.
     *
     * @param target the target whom the souls are to attack.
     */
    private void executeSummonedSoulsAttack(@NotNull final Entity target) {
        setAnimation(summonedSoulsAttackAnim);
        setForceTalk("Aaarrrooooooo");
        World.sendSoundEffect(getMiddleLocation(), callingSummonedSoulsSound);
        final IntArrayList randomizedSoulIds = new IntArrayList(new int[] {NpcId.SUMMONED_SOUL, NpcId.SUMMONED_SOUL_5868, NpcId.SUMMONED_SOUL_5869});
        if(isInstanced()) {
            for (int i = randomizedSoulIds.size() - 1; i >= 0; i--) {
                final int soulId = randomizedSoulIds.removeInt(Utils.random(randomizedSoulIds.size() - 1));
                final SummonedSoul soul = new SummonedSoul(soulId, instance.getLocation(getRoom().getWesternSoulLocation()).transform(Direction.EAST, i), this, target, i);
                soul.spawn();
                summonedSouls.add(soul);
            }
        } else {
            for (int i = randomizedSoulIds.size() - 1; i >= 0; i--) {
                final int soulId = randomizedSoulIds.removeInt(Utils.random(randomizedSoulIds.size() - 1));
                final SummonedSoul soul = new SummonedSoul(soulId, getRoom().getWesternSoulLocation().transform(Direction.EAST, i), this, target, i);
                soul.spawn();
                summonedSouls.add(soul);
            }
        }
    }

    /**
     * Executes the combo special attack, performing the normal magic attack, followed by the normal ranged attack and finished off by the melee attack. The melee attack will occur regardless of
     * the distance between the two, so it can be done from a-far.
     *
     * @param target the target whom to attack.
     */
    private void executeComboAttack(@NotNull final Entity target) {
        final boolean leftHeadFirst = Utils.random(1) == 0;
        WorldTasksManager.scheduleOrExecute(() -> executeAttack(target, MAGIC, leftHeadFirst ? CerberusHead.LEFT : CerberusHead.RIGHT), -1);
        WorldTasksManager.scheduleOrExecute(() -> executeAttack(target, RANGED, !leftHeadFirst ? CerberusHead.LEFT : CerberusHead.RIGHT), 1);
        WorldTasksManager.scheduleOrExecute(() -> executeAttack(target, MELEE, CerberusHead.CENTER), 4);
    }

    /**
     * Executes the lava pools special attack, sending one marker from each of the Cerberus' heads.
     *
     * @param target the target under whom to send one of the pools.
     */
    private void executeLavaPoolsAttack(@NotNull final Entity target) {
        setAnimation(lavaPoolsAttackAnim);
        setForceTalk("Grrrrrrrrr");
        sendLavaPool(getProjectileStartPosition(target, CerberusHead.CENTER), new Location(target.getLocation()));
        final Location leftHeadPosition = getProjectileStartPosition(target, CerberusHead.LEFT);
        final Location rightHeadPosition = getProjectileStartPosition(target, CerberusHead.RIGHT);
        final Location leftHeadTargetedPosition = getProjectileStartPosition(target, CerberusHead.LEFT, SIZE_OFFSET_LAVA_POOL);
        final Location rightHeadTargetedPosition = getProjectileStartPosition(target, CerberusHead.RIGHT, SIZE_OFFSET_LAVA_POOL);
        final Location leftHeadDestination = translateRandomReachablePosition(leftHeadTargetedPosition);
        //We must add the element to the collection immediately to ensure the pool below doesn't collide in position.
        if (leftHeadDestination != null) {
            sendLavaPool(leftHeadPosition, leftHeadDestination);
        }
        final Location rightHeadDestination = translateRandomReachablePosition(rightHeadTargetedPosition);
        if (rightHeadDestination != null) {
            sendLavaPool(rightHeadPosition, rightHeadDestination);
        }
    }

    /**
     * Translates a collection of random reachable positions from the position passed, within a specific radius.
     *
     * @param location the location which marks the center of it.
     * @return a random position out of the translated collection.
     */
    @Nullable
    private Location translateRandomReachablePosition(@NotNull final Location location) {
        //Pre-define a possible list of locations where the pool could go, allocating the array at maximum capacity as more often than not, all will be filled.
        final ObjectArrayList<Location> possibleLocationsList = new ObjectArrayList<Location>((int) Math.pow(((MARKER_RANDOMIZATION_DISTANCE << 1) + 1), 2));
        for (int x = -MARKER_RANDOMIZATION_DISTANCE; x <= MARKER_RANDOMIZATION_DISTANCE; x++) {
            for (int y = -MARKER_RANDOMIZATION_DISTANCE; y <= MARKER_RANDOMIZATION_DISTANCE; y++) {
                final Location transformedLocation = location.transform(x, y, 0);
                if (!isProjectileClipped(transformedLocation, true) && CollectionUtils.findMatching(markers, marker -> marker.matches(transformedLocation)) == null) {
                    possibleLocationsList.add(transformedLocation);
                }
            }
        }
        if (possibleLocationsList.isEmpty()) {
            return null;
        }
        return possibleLocationsList.get(Utils.random(possibleLocationsList.size() - 1));
    }

    /**
     * Sends a laval pool out towards the specified tile.
     *
     * @param headLocation the location from which the projectile starts flying.
     * @param tile         the location to which the projectile flies, putting the marker here.
     */
    private void sendLavaPool(@NotNull final Location headLocation, @NotNull final Location tile) {
        final int delay = World.sendProjectile(headLocation, tile, markerProj);
        final int preciseDelay = markerProj.getProjectileDuration(headLocation, tile);
        final ImmutableLocation marker = new ImmutableLocation(tile);
        markers.add(marker);
        World.sendGraphics(new Graphics(markerGraphics.getId(), preciseDelay, markerGraphics.getHeight()), tile);
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                final int delay = --ticks;
                if (delay <= -MARKER_DURATION) {
                    explodeMarker(marker);
                    markers.remove(marker);
                    stop();
                    return;
                }
                if (delay % MARKER_DAMAGE_FREQUENCY == 0) {
                    checkMarkerCollisions(marker);
                }
            }
        }, delay - 1, 0);
    }

    /**
     * Executes a normal attack of Cerberus with the style requested, from the head requested. Melee attacks are hard-coded to come out of the center head as there is no projectile.
     *
     * @param target the target whom to attack.
     * @param style  the style using which to attack.
     * @param head   the head from which the projectile shoots out, if there is one.
     */
    private void executeAttack(@NotNull final Entity target, @NotNull final AttackType style, @NotNull final CerberusHead head) {
        if (isCancelled(target) || isDead() || isFinished()) {
            return;
        }
        if (style.isMelee()) {
            setAnimation(meleeAnim);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), style, target), HitType.MELEE));
            return;
        }
        final Location headLocation = getProjectileStartPosition(target, head);
        if (style.isRanged()) {
            final int delay = World.sendProjectile(headLocation, target, rangedProj);
            final int preciseDelay = rangedProj.getProjectileDuration(headLocation, target.getLocation());
            setAnimation(rangedAnim);
            target.setGraphics(new Graphics(rangedGraphics.getId(), preciseDelay, rangedGraphics.getHeight()));
            delayHit(this, delay, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), style, target), HitType.RANGED));
        } else {
            final Location destinationLocation = new Location(target.getLocation());
            final int delay = World.sendProjectile(headLocation, target, magicProj);
            final int preciseDelay = magicProj.getProjectileDuration(headLocation, destinationLocation);
            World.sendSoundEffect(headLocation, magicCastSound);
            World.sendSoundEffect(destinationLocation, new SoundEffect(magicLandSound.getId(), magicLandSound.getRadius(), preciseDelay));
            setAnimation(magicAnim);
            target.setGraphics(new Graphics(magicGraphics.getId(), preciseDelay, magicGraphics.getHeight()));
            delayHit(this, delay, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), style, target), HitType.MAGIC));
        }
    }

    /**
     * Whether or not an attack is cancelled on the player.
     *
     * @param target the player who is to be hit by an attack.
     * @return whether or not an attack is cancelled on the player.
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    boolean isCancelled(@NotNull final Entity target) {
        return target.isFinished() || target.isNulled() || target.isDead() || !getLairArea().getPlayers().contains(target);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == COMBAT_CERBERUS || id == SLEEPING_CERBERUS;
    }

    void addSummonedSoulMitigation() {
        summonedSoulMitigation++;
    }

    /**
     * An enum containing the three heads of Cerberus.
     */
    private enum CerberusHead {
        LEFT, CENTER, RIGHT
    }
}
