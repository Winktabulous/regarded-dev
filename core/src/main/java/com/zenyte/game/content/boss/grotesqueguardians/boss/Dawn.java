package com.zenyte.game.content.boss.grotesqueguardians.boss;

import com.zenyte.game.content.boss.grotesqueguardians.EnergySphere;
import com.zenyte.game.content.boss.grotesqueguardians.FightPhase;
import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.Gargoyle;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.types.config.AnimationDefinitions;

import java.util.ArrayList;

/**
 * @author Tommeh | 21/07/2019 | 21:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Dawn extends NPC implements CombatScript {
    private static final Animation MELEE_ATTACK_ANIM = new Animation(7769);
    private static final Animation RANGED_ATTACK_ANIM = new Animation(7770);
    private static final Animation SPHERE_ATTACK_ANIM = new Animation(7771);
    private static final Animation DEATH_FIRST_ANIM = new Animation(7776);
    private static final Animation DEATH_SECOND_ANIM = new Animation(7777);
    private static final Animation DUSK_SAD_ANIM = new Animation(7795);
    private static final Animation BOSS_FINISH_OFF = new Animation(401);
    private static final Projectile RANGED_ATTACK_PROJ = new Projectile(1444, 90, 40, 0, 32);
    private static final Projectile FREEZE_ATTACK_PROJ = new Projectile(1445, 90, 0, 50, 32);
    private static final Projectile SPHERE_ATTACK_PROJ = new Projectile(1437, 90, 0, 0, 32);
    private static final Graphics FLAME_CORNER_GFX = new Graphics(1433);
    private static final Graphics FREEZE_HIT_GFX = new Graphics(160);
    public static final int ATTACKABLE_NPC_ID = 7852;
    public static final int NON_ATTACKABLE_NPC_ID = 7853;
    private static final int DEATH_SEQUENCE_NPC_ID = 7885;
    private static final byte[][] OFFSETS = new byte[][] {new byte[] {-1, -1}, new byte[] {-1, 0}, new byte[] {-1, 1}, new byte[] {0, -1}, new byte[] {0, 1}, new byte[] {1, -1}, new byte[] {1, 0}, new byte[] {1, 1}};
    private Dusk dusk;
    private final GrotesqueGuardiansInstance instance;
    private boolean sphereAttack;
    private long sphereAttackDelay;
    private long freezeAttackDelay;
    private boolean dying;
    private int attacks;

    public Dawn(final Location tile, final GrotesqueGuardiansInstance instance) {
        super(ATTACKABLE_NPC_ID, tile, Direction.WEST, 10);
        this.instance = instance;
        this.maxDistance = 20;
    }

    @Override
    public float getXpModifier(final Hit hit) {
        return hit.getDamage() == 0 ? 0 : 1;
    }

    @Override
    public void resetWalkSteps() {
        if (instance.isPerformingLightningAttack()) {
            return;
        }
        super.resetWalkSteps();
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
        if (instance.isPerformingLightningAttack()) {
            return false;
        }
        if (id != NON_ATTACKABLE_NPC_ID && !instance.getWalkingBoundary().contains(nextX, nextY)) {
            return false;
        }
        return super.addWalkStep(nextX, nextY, lastX, lastY, check);
    }

    @Override
    public boolean canAttack(final Player source) {
        return definitions.containsOptionCaseSensitive("Attack");
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void autoRetaliate(final Entity source) {
        if (!isCantInteract()) {
            super.autoRetaliate(source);
        }
    }

    @Override
    public boolean setHitpoints(final int amount) {
        final boolean dead = isDead();
        this.hitpoints = amount;
        if (!dead && hitpoints <= 9) {
            sendDeath();
            return true;
        }
        return false;
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player) || dying || isDead()) {
            return getCombatDefinitions().getAttackSpeed();
        }
        attacks++;
        final Player player = (Player) target;
        if (sphereAttack && sphereAttackDelay <= Utils.currentTimeMillis()) {
            setAnimation(SPHERE_ATTACK_ANIM);
            final ArrayList<Location> sphereLocations = new ArrayList<Location>(3);
            int count = 1000;
            while (--count > 0 && sphereLocations.size() != 3) {
                final Location location = instance.getRandomPoint();
                if (sphereLocations.size() < 3 && !sphereLocations.contains(location)) {
                    sphereLocations.add(location);
                }
            }
            for (int index = 0; index < sphereLocations.size(); index++) {
                final EnergySphere sphere = new EnergySphere(index, sphereLocations.get(index), player, this, instance);
                instance.getEnergySpheres().put(sphere.getId(), sphere);
                World.sendProjectile(this, sphere, SPHERE_ATTACK_PROJ);
                WorldTasksManager.schedule(() -> World.spawnObject(sphere), SPHERE_ATTACK_PROJ.getTime(this, sphere));
            }
            sphereAttackDelay = Utils.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
            return getCombatDefinitions().getAttackSpeed();
        }
        if (attacks >= 3 && freezeAttackDelay <= Utils.currentTimeMillis()) {
            final Location tile = getFreezeLocation(player);
            World.sendProjectile(this, tile, FREEZE_ATTACK_PROJ);
            WorldTasksManager.schedule(() -> {
                World.sendGraphics(FREEZE_HIT_GFX, tile);
                if (target.getLocation().matches(tile)) {
                    instance.setHitByRockfall();
                    target.stun(4);
                    target.applyHit(new Hit(Utils.random(15, 29), HitType.REGULAR));
                    ((Player) target).sendMessage("You have been trapped in stone!");
                }
            }, FREEZE_ATTACK_PROJ.getTime(this, target));
            freezeAttackDelay = Utils.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30);
            return getCombatDefinitions().getAttackSpeed();
        }
        if (isWithinMeleeDistance(this, target) && Utils.random(1) == 0) {
            setAnimation(MELEE_ATTACK_ANIM);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), CRUSH, target), HitType.MELEE));
        } else {
            setAnimation(RANGED_ATTACK_ANIM);
            World.sendProjectile(this, target, RANGED_ATTACK_PROJ);
            delayHit(this, RANGED_ATTACK_PROJ.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
            WorldTasksManager.schedule(() -> {
                World.sendProjectile(this, target, RANGED_ATTACK_PROJ);
                delayHit(this, RANGED_ATTACK_PROJ.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
            });
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void setAnimation(final Animation animation) {
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
    public void handleIngoingHit(final Hit hit) {
        if (hit.getHitType().equals(HitType.MAGIC)) {
            final Entity source = hit.getSource();
            hit.setDamage(0);
            if (source instanceof Player) {
                ((Player) source).sendFilteredMessage("Dawn is immune to magic attacks!");
            }
        }
        super.handleIngoingHit(hit);
    }

    @Override
    public void sendDeath() {
        if (dying || !instance.getPhase().equals(FightPhase.PHASE_THREE)) {
            return;
        }
        final Player source = instance.getPlayer();
        final boolean isUnlocked = source.getSlayer().isUnlocked("Gargoyle smasher");
        final Object attr = getTemporaryAttributes().get("used_rock_hammer");
        if ((attr != null && (boolean) attr) || (getHitpoints() <= 9 && isUnlocked && (source.getInventory().containsItem(Gargoyle.ROCK_HAMMER) || source.getInventory().containsItem(Gargoyle.GRANITE_HAMMER) || source.getInventory().containsItem(21754, 1)))) {
            if (isUnlocked) {
                final EntityEvent routeEvent = new EntityEvent(source, new DistancedEntityStrategy(this, 1), () -> deathSequence(source), true);
                routeEvent.setOnFailure(() -> source.unlock());
                setRouteEvent(routeEvent);
            } else {
                deathSequence(source);
            }
        } else if (getHitpoints() == 0) {
            heal(1);
        }
    }

    private void deathSequence(final Player source) {
        dying = true;
        source.setAnimation(BOSS_FINISH_OFF);
        source.faceEntity(this);
        source.lock();
        onDeath(source);
        lock();
        setAnimation(DEATH_FIRST_ANIM);
        getTemporaryAttributes().remove("used_rock_hammer");
        if (!source.getInventory().containsItem(Gargoyle.ROCK_HAMMER) && !source.getInventory().containsItem(Gargoyle.GRANITE_HAMMER)) {
            source.getInventory().deleteItem(21754, 1);
        }
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                case 1: 
                    source.unlock();
                    dusk.lock();
                    dusk.faceEntity(Dawn.this);
                    dusk.setAnimation(DUSK_SAD_ANIM);
                    setTransformation(DEATH_SEQUENCE_NPC_ID);
                    setAnimation(DEATH_SECOND_ANIM);
                    for (final Location corner : getCorners(getMiddleLocation())) {
                        World.sendGraphics(FLAME_CORNER_GFX, corner);
                    }
                    for (final Location corner : getCorners(dusk.getMiddleLocation())) {
                        World.sendGraphics(FLAME_CORNER_GFX, corner);
                    }
                    if (!instance.getEnergySpheres().isEmpty()) {
                        for (final Int2ObjectMap.Entry<EnergySphere> entry : instance.getEnergySpheres().int2ObjectEntrySet()) {
                            final EnergySphere sphere = entry.getValue();
                            World.removeObject(sphere);
                        }
                    }
                    break;
                case 3: 
                    finish();
                case 13: 
                    instance.setPhase(FightPhase.PHASE_FOUR);
                    dusk.prisonAttack(true);
                    stop();
                    break;
                }
            }
        }, 0, 0);
        source.sendFilteredMessage("Dawn cracks apart.");
    }

    private Location getFreezeLocation(final Player player) {
        final Location base = player.getLocation();
        for (final byte[] offset : OFFSETS) {
            final Location location = base.transform(offset[0], offset[1], 0);
            if (World.isFloorFree(location, 1)) {
                return location;
            }
        }
        return base.transform(OFFSETS[0][0], OFFSETS[0][1], 0);
    }

    private Location[] getCorners(final Location middle) {
        final Location[] corners = new Location[4];
        corners[0] = middle.transform(-1, 2, 0);
        corners[1] = middle.transform(2, 2, 0);
        corners[2] = middle.transform(2, -1, 0);
        corners[3] = middle.transform(-1, -1, 0);
        return corners;
    }

    public void setDusk(Dusk dusk) {
        this.dusk = dusk;
    }

    public boolean isSphereAttack() {
        return sphereAttack;
    }

    public void setSphereAttack(boolean sphereAttack) {
        this.sphereAttack = sphereAttack;
    }
}
