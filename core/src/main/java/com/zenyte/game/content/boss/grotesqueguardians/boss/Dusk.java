package com.zenyte.game.content.boss.grotesqueguardians.boss;

import com.zenyte.game.content.boss.grotesqueguardians.FightPhase;
import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.*;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.npc.impl.slayer.Gargoyle;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.AnimationDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Tommeh | 21/07/2019 | 21:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Dusk extends NPC implements CombatScript {
    private static final Animation MELEE_ATTACK_ANIM = new Animation(7785);
    private static final Animation SECOND_MELEE_ATTACK_ANIM = new Animation(7787);
    private static final Animation MELEE_ATTACK_P4_ANIM = new Animation(7800);
    private static final Animation RANGED_ATTACK_ANIM = new Animation(7801);
    private static final Animation EXPLOSION_ATTACK_ANIM = new Animation(7802);
    private static final Animation PRISON_ATTACK_FIRST_ANIM = new Animation(7796);
    private static final Animation PRISON_ATTACK_ANIM = new Animation(7799);
    private static final Animation DEATH_FIRST_ANIM = new Animation(7803);
    private static final Animation DEATH_SECOND_ANIM = new Animation(7809);
    private static final Projectile RANGED_ATTACK_PROJ = new Projectile(1444, 90, 40, 0, 32);
    private static final Graphics PRISON_ATTACK_GFX = new Graphics(1434);
    private static final ForceTalk ARGHHH = new ForceTalk("Arghhh!");
    public static final int ATTACKABLE_NPC_ID = 7851;
    public static final int NON_ATTACKABLE_NPC_ID = 7854;
    public static final int LIGHTNING_ATTACK_NPC_ID = 7855;
    private static final int P4_NON_ATTACKABLE_NPC_ID = 7887;
    private static final int P4_ATTACKABLE_NPC_ID = 7888;
    private static final int DEATH_SEQUENCE_NPC_ID = 7889;
    private Dawn dawn;
    private final GrotesqueGuardiansInstance instance;
    private boolean explosiveAttack;
    private boolean dying;
    private long prisonAttackDelay;
    private boolean performingPrisonAttack;
    private int attacks;

    public Dusk(final Location tile, final GrotesqueGuardiansInstance instance) {
        super(ATTACKABLE_NPC_ID, tile, Direction.EAST, 10);
        this.instance = instance;
        this.maxDistance = 20;
    }

    @Override
    public float getXpModifier(final Hit hit) {
        return hit.getHitType().equals(HitType.RANGED) || hit.getHitType().equals(HitType.MAGIC) ? 0 : 1;
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
        if (instance.getPhase().equals(FightPhase.PHASE_ONE) || instance.getPhase().equals(FightPhase.PHASE_THREE)) {
            hit.setDamage(0);
        } else if (hit.getHitType().equals(HitType.MAGIC) || hit.getHitType().equals(HitType.RANGED)) {
            hit.setDamage(0);
        }
        if (instance.getPhase().equals(FightPhase.PHASE_TWO)) {
            attacks++;
        }
        super.handleIngoingHit(hit);
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player) || dying || isDead()) {
            return getCombatDefinitions().getAttackSpeed();
        }
        final Player player = (Player) target;
        if (explosiveAttack && attacks >= 2) {
            lock();
            setAnimation(EXPLOSION_ATTACK_ANIM);
            WorldTasksManager.schedule(new WorldTask() {
                int ticks;
                Location destination;
                boolean hit;
                @Override
                public void run() {
                    switch (ticks++) {
                    case 1: 
                        if (!isWithinMeleeDistance(Dusk.this, player)) {
                            return;
                        }
                        hit = true;
                        player.getPacketDispatcher().sendClientScript(64);
                        final Location middle = getMiddleLocation();
                        double degrees = Math.toDegrees(Math.atan2(player.getY() - middle.getY(), player.getX() - middle.getX()));
                        if (degrees < 0) {
                            degrees += 360;
                        }
                        final double angle = Math.toRadians(degrees);
                        final int px = (int) Math.round(middle.getX() + (getSize() + 10) * Math.cos(angle));
                        final int py = (int) Math.round(middle.getY() + (getSize() + 10) * Math.sin(angle));
                        final List<Location> tiles = LocationUtil.calculateLine(player.getX(), player.getY(), px, py, player.getPlane());
                        if (!tiles.isEmpty()) {
                            tiles.remove(0);
                        }
                        destination = new Location(player.getLocation());
                        for (final Location tile : tiles) {
                            final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
                            if (dir == -1) {
                                continue;
                            }
                            if (!World.checkWalkStep(destination.getPlane(), destination.getX(), destination.getY(), dir, player.getSize(), false, false)) break;
                            destination.setLocation(tile);
                        }
                        final int direction = DirectionUtil.getFaceDirection(player.getX() - destination.getX(), player.getY() - destination.getY());
                        if (!destination.matches(player)) {
                            player.setForceMovement(new ForceMovement(destination, 30, direction));
                            player.lock();
                        }
                        player.faceEntity(Dusk.this);
                        player.setAnimation(Animation.KNOCKBACK);
                        break;
                    case 2: 
                        if (!hit) {
                            break;
                        }
                        instance.setHitByBlinding();
                        player.unlock();
                        player.getPacketDispatcher().sendClientScript(1970);
                        player.setLocation(destination);
                        player.applyHit(new Hit(Dusk.this, 20 + Utils.random(15), HitType.REGULAR));
                        break;
                    case 7: 
                        unlock();
                        getCombat().setTarget(player);
                        instance.setDebrisAllowed(true);
                        stop();
                        break;
                    }
                }
            }, 0, 0);
            attacks = 0;
            explosiveAttack = false;
            return getCombatDefinitions().getAttackSpeed();
        }
        if (instance.getPhase().equals(FightPhase.PHASE_FOUR)) {
            if (Utils.random(2) <= 1) {
                setAnimation(RANGED_ATTACK_ANIM);
                World.sendProjectile(this, target, RANGED_ATTACK_PROJ);
                delayHit(this, RANGED_ATTACK_PROJ.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
                WorldTasksManager.schedule(() -> {
                    World.sendProjectile(this, target, RANGED_ATTACK_PROJ);
                    delayHit(this, RANGED_ATTACK_PROJ.getTime(this, target), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
                });
            } else {
                setAnimation(MELEE_ATTACK_P4_ANIM);
                delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), CRUSH, target), HitType.MELEE));
            }
        } else {
            final int attack = Utils.random(1);
            setAnimation(attack == 0 ? MELEE_ATTACK_ANIM : SECOND_MELEE_ATTACK_ANIM);
            delayHit(this, attack, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), CRUSH, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    public void prisonAttack(final boolean first) {
        //The size of the chamber is naturally 5x5 tiles-
        final int chamberSize = 5;
        //The distance between the chamber's box and dusk; 0 = can be next to eachother, 1 = needs at least 1 tile between them. Cannot be too far or it won't always find a situation.
        int tilesDistance = 3;
        Optional<Location> optionalCenter;
        final Location randomPoint = instance.getRandomPoint();
        final RSPolygon boundary = instance.getPrisonBoundary();
        while (true) {
            final int finalTilesDistance = tilesDistance;
            optionalCenter = WorldUtil.findEmptySquare(randomPoint, 30, 5, Optional.of(l -> !CollisionUtil.collides(l, chamberSize, getLocation(), 5, finalTilesDistance) && boundary.contains(l.getX(), l.getY())));
            if (optionalCenter.isPresent()) {
                break;
            }
            if (--tilesDistance < 0) {
                throw new RuntimeException("Unable to find a location for the square.");
            }
        }
        final Location center = optionalCenter.get().transform(2, 2, 0);
        final Player player = instance.getPlayer();
        performingPrisonAttack = true;
        instance.increasePrisonAttacks();
        lock();
        getCombat().setTarget(null);
        if (first) {
            setHitpoints(225); //225
        }
        setTransformation(P4_NON_ATTACKABLE_NPC_ID);
        setAnimation(first ? PRISON_ATTACK_FIRST_ANIM : PRISON_ATTACK_ANIM);
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (dying || isDead()) {
                    player.unlock();
                    stop();
                    return;
                }
                switch (ticks++) {
                case 1: 
                case 2: 
                    setFaceEntity(player);
                    break;
                case 5: 
                    player.setAnimation(Animation.KNOCKBACK);
                    player.autoForceMovement(center, 60);
                    player.setForceTalk(ARGHHH);
                    player.lock(2);
                    setFaceEntity(player);
                    break;
                case 7: 
                    createPrison(center);
                    break;
                case 11: 
                    if (player.getLocation().withinDistance(center, 1)) {
                        instance.setHitByPrison();
                        player.applyHit(new Hit(Dusk.this, Utils.random(60, 67), HitType.REGULAR));
                    }
                    break;
                case 14: 
                    performingPrisonAttack = false;
                    prisonAttackDelay = Utils.currentTimeMillis() + TimeUnit.SECONDS.toMillis(25);
                    unlock();
                    setTransformation(P4_ATTACKABLE_NPC_ID);
                    getCombat().setTarget(instance.getPlayer());
                    stop();
                    break;
                }
            }
        }, 0, 0);
    }

    private void createPrison(final Location center) {
        final Location[] edge = new Location[16];
        final Location[] corners = new Location[4];
        final Location[] middle = new Location[9];
        Location exit;
        int index = 0;
        for (int i = -2; i < 2; i++) {
            //north
            edge[index++] = center.transform(i, 2, 0);
        }
        for (int i = -2; i < 2; i++) {
            //west
            edge[index++] = center.transform(-2, i, 0);
        }
        for (int i = -1; i < 2; i++) {
            //south
            edge[index++] = center.transform(i, -2, 0);
        }
        for (int i = -2; i < 3; i++) {
            //south
            edge[index++] = center.transform(2, i, 0);
        }
        corners[0] = center.transform(-2, -2, 0);
        corners[1] = center.transform(-2, 2, 0);
        corners[2] = center.transform(2, -2, 0);
        corners[3] = center.transform(2, 2, 0);
        index = 0;
        for (int i = -1; i < 1; i++) {
            //north
            middle[index++] = center.transform(i, 1, 0);
        }
        for (int i = -1; i < 1; i++) {
            //west
            middle[index++] = center.transform(-1, i, 0);
        }
        for (int i = 0; i < 1; i++) {
            //south
            middle[index++] = center.transform(i, -1, 0);
        }
        for (int i = -1; i < 2; i++) {
            //south
            middle[index++] = center.transform(1, i, 0);
        }
        middle[index] = center;
        exit = getPrisonExitLocation(getMiddleLocation(), edge, corners);
        for (final Location location : edge) {
            if (exit.equals(location)) {
                continue;
            }
            World.sendGraphics(PRISON_ATTACK_GFX, location);
            World.spawnObject(new WorldObject(0, 10, 3, location)); //invisible, used to clip
        }
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                case 3: 
                    for (final Location location : middle) {
                        World.sendGraphics(PRISON_ATTACK_GFX, location);
                    }
                    break;
                case 6: 
                    for (final Location location : edge) {
                        World.removeObject(new WorldObject(0, 10, 3, location));
                    }
                    stop();
                    break;
                }
            }
        }, 0, 0);
    }

    @NotNull
    private Location getPrisonExitLocation(final Location from, final Location[] edge, final Location[] corners) {
        Location tile = null;
        double distance = Double.MAX_VALUE;
        for (final Location location : edge) {
            if (ArrayUtils.contains(corners, location)) {
                continue;
            }
            final double df = location.getDistance(from);
            if (df < distance) {
                tile = location;
                distance = df;
            }
        }
        if (tile == null) {
            throw new IllegalStateException();
        }
        return tile;
    }

    @Override
    protected void onFinish(final Entity source) {
        drop(getMiddleLocation());
        reset();
        finish();
        sendNotifications((Player) source);
    }

    @Override
    protected void drop(final Location tile) {
        final Player killer = instance.getPlayer();
        if (killer == null) {
            return;
        }
        final long delay = System.currentTimeMillis() - killer.getBossTimer().getCurrentTracker();
        killer.getBossTimer().inform("Grotesque Guardians", delay);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(delay);
        if (seconds < 80) {
            killer.getCombatAchievements().complete(CAType.GROTESQUE_GUARDIANS_SPEED_RUNNER);
        }
        if (seconds < 100) {
            killer.getCombatAchievements().complete(CAType.GROTESQUE_GUARDIANS_SPEED_CHASER);
        }
        if (seconds < 120) {
            killer.getCombatAchievements().complete(CAType.GROTESQUE_GUARDIANS_SPEED_TRIALIST);
        }
        onDrop(killer);
        this.id = 7888;
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }
        final NPCDrops.DropTable drops = NPCDrops.getTable(id);
        if (drops == null) {
            return;
        }
        NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile));
        NPCDrops.rollTable(killer, drops, drop -> {
            if (!drop.isAlways()) dropItem(killer, drop, tile);
        });
    }

    @Override
    protected void sendNotifications(final Player player) {
        player.getNotificationSettings().increaseKill("grotesque guardians");
        player.getNotificationSettings().sendBossKillCountNotification("grotesque guardians");
        instance.onKill();
        VarCollection.GROTESQUE_GUARDIAN_KC_FOR_BELL.updateSingle(player);
    }

    @Override
    public void sendDeath() {
        if (dying || !instance.getPhase().equals(FightPhase.PHASE_FOUR)) {
            return;
        }
        final Player source = instance.getPlayer();
        final boolean isUnlocked = source.getSlayer().isUnlocked("Gargoyle smasher");
        final Object attr = getTemporaryAttributes().get("used_rock_hammer");
        if ((attr != null && (boolean) attr) || (getHitpoints() <= 9 && isUnlocked && (source.getInventory().containsItem(Gargoyle.ROCK_HAMMER) || source.getInventory().containsItem(Gargoyle.GRANITE_HAMMER) || source.getInventory().containsItem(21754, 1)))) {
            dying = true;
            setId(P4_ATTACKABLE_NPC_ID);
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
                        setTransformation(DEATH_SEQUENCE_NPC_ID);
                        setAnimation(DEATH_SECOND_ANIM);
                        break;
                    case 4: 
                        instance.reset();
                        onFinish(source);
                        stop();
                        break;
                    }
                }
            }, 0, 0);
            source.sendMessage("Dusk cracks apart.");
        } else if (getHitpoints() == 0) {
            heal(1);
        }
    }

    public void setDawn(Dawn dawn) {
        this.dawn = dawn;
    }

    public void setExplosiveAttack(boolean explosiveAttack) {
        this.explosiveAttack = explosiveAttack;
    }

    public long getPrisonAttackDelay() {
        return prisonAttackDelay;
    }

    public boolean isPerformingPrisonAttack() {
        return performingPrisonAttack;
    }
}
