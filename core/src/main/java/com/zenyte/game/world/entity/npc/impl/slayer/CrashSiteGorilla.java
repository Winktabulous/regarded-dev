package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.boons.impl.UnholyIntervention;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 05/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
abstract class CrashSiteGorilla extends NPC implements CombatScript {
    static final int MELEE_SWITCH_DELAY = 5;
    static final int PROJECTILE_MAGIC_SPEED = 8;
    static final int PROJECTILE_RANGED_SPEED = 6;
    static final int PROJECTILE_MAGIC_DELAY = 12;
    static final int PROJECTILE_RANGED_DELAY = 9;
    static final List<AttackType> possibleAttackStyles = Collections.unmodifiableList(Arrays.asList(CRUSH, RANGED, MAGIC));
    static final Animation magicAnimation = new Animation(7225);
    static final Animation meleeAnimation = new Animation(7226);
    static final Animation rangedAnimation = new Animation(7227);
    static final Animation boulderAnimation = new Animation(7228);
    static final Graphics rangedGraphics = new Graphics(1303);
    static final Graphics magicGraphics = new Graphics(1305);
    static final Graphics boulderGraphics = new Graphics(305, 4 * 30, 5);
    static final Projectile rangedProjectile = new Projectile(1302, 45, 30, 50, 7, -10, 5, 5);
    static final Projectile magicProjectile = new Projectile(1304, 10, 10, 45, 7, -10, 0, 4);
    static final Projectile boulderProjectile = new Projectile(856, 200, 6, 0, 0, 4 * 30, 64, 0);
    static final SoundEffect magicAttackStartSound = new SoundEffect(3529, 5, 0);
    static final SoundEffect magicAttackLandSound = new SoundEffect(3528, 5, -1);
    static final SoundEffect rangedAttackStartSound = new SoundEffect(3191, 5, 0);
    static final SoundEffect meleeAttackSound = new SoundEffect(3350, 5, 0);
    /**
     * The boulder sound isn't exactly the same as OS but since OpenOSRS refuses to give me area sounds right now I'll stick to something that sounds near-identical.
     */
    static final SoundEffect boulderSound = new SoundEffect(360, 10, 4 * 30);

    CrashSiteGorilla(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.aggressionDistance = 3;
        this.attackDistance = 10;
        this.maxDistance = 16;
    }

    abstract int failedHitsUntilSwitch();

    protected int missedHits;
    protected long meleeSwitchTick;
    protected int meleeMovementDelay;
    protected long lastStyleSwitch;

    @Override
    public NPC spawn() {
        missedHits = 0;
        final NPC npc = super.spawn();
        npc.getCombatDefinitions().setAttackStyle(Utils.getRandomCollectionElement(possibleAttackStyles));
        return npc;
    }

    @Override
    public void processNPC() {
        final AttackType combatStyle = combatDefinitions.getAttackStyle();
        //When the gorilla switches to melee style, it will not do anything else but try and attack with melee. It will not change aggression if target leaves, it's frozen still.
        //The only way to break the freeze is to hit it ~3 seconds after it first switched to melee, if the hit is done from afar, it'll switch to another style.
        if (combatStyle.isMelee()) {
            if (meleeMovementDelay > 0) {
                meleeMovementDelay--;
                final int combatDelay = combat.getCombatDelay();
                if (combatDelay > 0) {
                    combat.setCombatDelay(combatDelay - 1);
                }
                return;
            }
            combat.process();
            return;
        }
        //meleeMovementDelay = 0;
        super.processNPC();
    }

    @Override
    public void applyHit(@NotNull final Hit hit) {
        super.applyHit(hit);
        final AttackType combatStyle = combatDefinitions.getAttackStyle();
        if (combatStyle.isMelee()) {
            if (meleeSwitchTick < (WorldThread.WORLD_CYCLE - MELEE_SWITCH_DELAY)) {
                final ObjectArrayList<AttackType> availableAttackStyle = new ObjectArrayList<>(Arrays.asList(MAGIC, RANGED));
                final Entity source = hit.getSource();
                if (source instanceof Player) {
                    final PrayerManager prayers = ((Player) source).getPrayerManager();
                    if (prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                        availableAttackStyle.remove(MAGIC);
                    } else if (prayers.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                        availableAttackStyle.remove(RANGED);
                    }
                }
                switchStyleTo(Utils.getRandomCollectionElement(availableAttackStyle), availableAttackStyle.size() == 1);
            }
        }
    }

    @Override
    public int attack(Entity target) {
        if (!combatDefinitions.isMelee() && Utils.random(9) == 0) {
            boulderAttack(target);
        } else {
            switch (getCombatDefinitions().getAttackStyle()) {
            case MAGIC: 
                magicAttack(target);
                break;
            case RANGED: 
                rangedAttack(target);
                break;
            default: 
                meleeAttack(target);
                break;
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    private final int getDistance2D(@NotNull final Location source, final int sourceSize, @NotNull final Location target, final int targetSize) {
        final Location p1 = getAxisDistances(source, sourceSize, target);
        final Location p2 = getAxisDistances(target, targetSize, source);
        return Math.max(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
    }

    private final Location getAxisDistances(@NotNull final Location source, final int sourceSize, @NotNull final Location target) {
        int x;
        int y;
        if (target.getX() <= source.getX()) {
            x = source.getX();
        } else x = Math.min(target.getX(), source.getX() + sourceSize - 1);
        if (target.getY() <= source.getY()) {
            y = source.getY();
        } else y = Math.min(target.getY(), source.getY() + sourceSize - 1);
        return new Location(x, y);
    }

    private final void onFailedCloseAttack(@NotNull final Entity target) {
        //If the third "missed" hit is not currently prayed against, the gorilla will either keep using the same attack style for the next 3 attacks or switch to another style. If the third
        //"missed" hit was either a ranged or magic hit, however, the following attack will always be the same as the one just used, or a melee attack.
        final boolean isCorrectlyPrayedAgainst = target instanceof Player && ((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE);
        final ObjectArrayList<AttackType> nextPossibleAttacks = new ObjectArrayList<AttackType>();
        nextPossibleAttacks.addAll(possibleAttackStyles);
        if (isCorrectlyPrayedAgainst) {
            nextPossibleAttacks.remove(combatDefinitions.getAttackStyle());
        }
        switchStyleTo(Utils.getRandomCollectionElement(nextPossibleAttacks), isCorrectlyPrayedAgainst);
    }

    private final void onFailedDistancedAttack(@NotNull final Entity target, @NotNull final Prayer prayer) {
        //If the third "missed" hit is not currently prayed against, the gorilla will either keep using the same attack style for the next 3 attacks or switch to another style. If the third
        //"missed" hit was either a ranged or magic hit, however, the following attack will always be the same as the one just used, or a melee attack.
        final boolean isCorrectlyPrayedAgainst = target instanceof Player && ((Player) target).getPrayerManager().isActive(prayer);
        final ObjectArrayList<AttackType> nextPossibleAttacks = new ObjectArrayList<AttackType>();
        if (isCorrectlyPrayedAgainst) {
            nextPossibleAttacks.addAll(possibleAttackStyles);
            nextPossibleAttacks.remove(combatDefinitions.getAttackStyle());
        } else {
            nextPossibleAttacks.add(AttackType.CRUSH);
            nextPossibleAttacks.add(combatDefinitions.getAttackStyle());
        }
        switchStyleTo(Utils.getRandomCollectionElement(nextPossibleAttacks), isCorrectlyPrayedAgainst);
    }

    private final void switchStyleTo(@NotNull final AttackType type, final boolean correctlyPrayed) {
        missedHits = 0;
        final AttackType previousType = combatDefinitions.getAttackType();
        combatDefinitions.setAttackStyle(type);
        if (type.isMelee()) {
            meleeSwitchTick = WorldThread.WORLD_CYCLE;
            if (!correctlyPrayed && !previousType.equals(type)) {
                meleeMovementDelay = 2;
            }
        }
        lastStyleSwitch = WorldThread.WORLD_CYCLE;
    }

    private void meleeAttack(final Entity target) {
        setAnimation(meleeAnimation);
        final Hit hit = new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE);
        if (hit.getDamage() == 0 || (target instanceof Player) && ((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
            if (++missedHits >= failedHitsUntilSwitch()) {
                onFailedCloseAttack(target);
            }
        }
        World.sendSoundEffect(getLocation(), meleeAttackSound);
        delayHit(this, 0, target, hit);
        meleeSwitchTick = WorldThread.WORLD_CYCLE;
    }

    private void rangedAttack(final Entity target) {
        setAnimation(rangedAnimation);
        final Hit hit = new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED);
        if (hit.getDamage() == 0 || (target instanceof Player) && ((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MISSILES)) {
            if (++missedHits >= failedHitsUntilSwitch()) {
                onFailedDistancedAttack(target, Prayer.PROTECT_FROM_MISSILES);
            }
        }
        final int preciseDelay = rangedProjectile.getProjectileDuration(getLocation(), target.getLocation());
        World.sendSoundEffect(target.getLocation(), new SoundEffect(rangedAttackStartSound.getId(), rangedAttackStartSound.getRadius(), 0));
        target.setGraphics(new Graphics(rangedGraphics.getId(), preciseDelay, 0));
        World.sendProjectile(getLocation(), target, rangedProjectile);
        delayHit(this, ((getDistance2D(getLocation(), getSize(), target.getLocation(), target.getSize()) + PROJECTILE_RANGED_DELAY) / PROJECTILE_RANGED_SPEED) - 1, target, hit);
    }

    private void magicAttack(final Entity target) {
        setAnimation(magicAnimation);
        final Hit hit = new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC);
        if (hit.getDamage() == 0 || (target instanceof Player) && ((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
            if (++missedHits >= failedHitsUntilSwitch()) {
                onFailedDistancedAttack(target, Prayer.PROTECT_FROM_MAGIC);
            }
        }
        final int preciseDelay = magicProjectile.getProjectileDuration(getLocation(), target.getLocation());
        target.setGraphics(new Graphics(magicGraphics.getId(), preciseDelay, 0));
        World.sendSoundEffect(getLocation(), magicAttackStartSound);
        World.sendSoundEffect(target.getLocation(), new SoundEffect(magicAttackLandSound.getId(), magicAttackLandSound.getRadius(), preciseDelay));
        World.sendProjectile(getLocation(), target, magicProjectile);
        delayHit(this, ((getDistance2D(getLocation(), getSize(), target.getLocation(), target.getSize()) + PROJECTILE_MAGIC_DELAY) / PROJECTILE_MAGIC_SPEED) - 1, target, hit);
    }

    private void boulderAttack(final Entity target) {
        final Location collisionTile = new Location(target.getX(), target.getY(), target.getPlane());
        final Location from = collisionTile.transform(Direction.NORTH);
        setAnimation(boulderAnimation);
        World.sendGraphics(boulderGraphics, collisionTile);
        World.sendProjectile(from, collisionTile, boulderProjectile);
        World.sendSoundEffect(collisionTile, boulderSound);
        WorldTasksManager.schedule(() -> {
            if (target.matches(collisionTile)) {
                if(target instanceof Player player && player.getBoonManager().hasBoon(UnholyIntervention.class)) {
                    player.heal(Utils.random(UnholyIntervention.MIN_HEAL, UnholyIntervention.MAX_HEAL));
                } else {
                    delayHit(this, -1, target, new Hit(this, Math.min(99, target.getHitpoints() / 3), HitType.REGULAR));
                }
            }
        }, 3);
    }
}
