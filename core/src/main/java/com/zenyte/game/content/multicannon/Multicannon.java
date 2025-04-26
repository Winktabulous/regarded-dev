package com.zenyte.game.content.multicannon;

import com.zenyte.game.content.boons.impl.DoubleTap;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Bonuses;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.AttackStyle;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Handles the Dwarf Multicannon object.
 *
 * @author Kris | 13. okt 2017 : 13:02.43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class Multicannon extends WorldObject {

    private static final int BROKEN_CANNON = 14916;

    private static final Projectile PROJECTILE = new Projectile(53, 36, 35, 0, 3, 25, 11, 5);

    private static final Projectile GRANITE_PROJECTILE = new Projectile(1443, 36, 35, 0, 3, 25, 11, 5);

    private static final SoundEffect CANNON_SOUND = new SoundEffect(2877, 10, 0);

    private final Location center;

    private WeakReference<Player> player;

    private MulticannonDirection direction;

    private boolean firing;

    private int decayTimer;

    private DwarfMultiCannon cannon;

    private final transient Map<MulticannonDirection, RSPolygon> polygons;

    private transient int style;

    private transient DwarfMultiCannonType cannonType;

    public Multicannon(final int id, final int type, final int rotation, final Location tile, final Player player, final DwarfMultiCannonType cannonType) {
        super(id, type, rotation, tile);
        this.player = new WeakReference<>(player);
        this.cannonType = cannonType;
        direction = MulticannonDirection.NORTH;
        decayTimer = 3000;
        center = new Location(tile.getX() + 1, tile.getY() + 1, tile.getPlane());
        cannon = player.getDwarfMulticannon();
        polygons = MulticannonDirection.create(center);
    }

    /**
     * Processes the cannon's activities - decaying, rotation, firing.
     */
    public boolean process() {
        final Player player = this.player.get();
        decayTimer--;
        if (decayTimer == 500) {
            firing = false;
            if (player != null && !player.isFinished()) {
                player.sendMessage("<col=ff0000>Your dwarf multicannon is about to decay!");
            }
            if (this.getId() == cannonType.getCannonLoc()) {
                setId(cannonType.getBrokenCannonLoc());
                World.spawnObject(this);
            }
        } else if (decayTimer == 0) {
            World.removeObject(this);
            if (player != null && !player.isFinished()) {
                player.sendMessage("<col=ff0000>Your dwarf multicannon has decayed! Speak with Nulodion to retrieve it.");
            }
            firing = false;
            direction = MulticannonDirection.NORTH;
            return false;
        }
        if (!firing) {
            return true;
        }
        if (player == null || player.isFinished()) {
            firing = false;
            return true;
        }
        World.sendSoundEffect(this, CANNON_SOUND);
        if (fire(false)) {
            World.sendObjectAnimation(this, direction.getFireAnimation(cannonType));
            if (cannon.getCannonballs() > 0) {
                cannon.setCannonballs(cannon.getCannonballs() - 1);
            } else if (cannon.getGraniteballs() > 0) {
                cannon.setGraniteballs(cannon.getGraniteballs() - 1);
            }
            if (cannon.getCannonballs() == 0 && cannon.getGraniteballs() == 0) {
                firing = false;
            }
        } else {
            World.sendObjectAnimation(this, direction.getAnimation(cannonType));
        }

        final int direction = this.direction.ordinal() + 1;
        this.direction = MulticannonDirection.values[direction == 8 ? 0 : direction];
        return true;
    }

    private void setHighestAccuracyBonus() {
        final Player player = this.player.get();
        if (player == null) {
            return;
        }
        int accuracyBonus = Integer.MIN_VALUE;
        final Bonuses bonuses = player.getBonuses();
        for (int i = 0; i < 5; i++) {
            final int bonus = bonuses.getBonus(i);
            if (bonus > accuracyBonus) {
                accuracyBonus = bonus;
                style = i;
            }
        }
    }

    private boolean success(final NPC npc) {
        final Player player = this.player.get();
        if (player == null) {
            return false;
        }
        float accuracyModifier = 1.0F;
        if (style == 3) {
            if (CombatUtilities.hasFullMagicVoid(player, false)) {
                accuracyModifier += 0.45F;
            }
        } else if (style == SkillConstants.RANGED) {
            if (CombatUtilities.hasFullRangedVoid(player, false)) {
                accuracyModifier += 0.1F;
            }
        } else {
            if (CombatUtilities.hasFullMeleeVoid(player, true)) {
                accuracyModifier += 0.1F;
            }
        }
        float effectiveLevel = player.getSkills().getLevel(style) + 8;
        effectiveLevel *= player.getPrayerManager().getSkillBoost(style == 3 ? SkillConstants.MAGIC : style == 4 ? SkillConstants.RANGED : SkillConstants.ATTACK);
        final AttackType type = player.getCombatDefinitions().getAttackType();
        final AttackStyle.AttackExperienceType attackType = player.getCombatDefinitions().getAttackExperienceType();
        effectiveLevel += attackType == AttackStyle.AttackExperienceType.ATTACK_XP ? 3 : attackType == AttackStyle.AttackExperienceType.SHARED_XP ? 1 : 0;
        final int targetRoll = PlayerCombat.getTargetDefenceRoll(player, npc, AttackType.RANGED);
        final int accuracyBoost = player.getBonuses().getBonus(type == AttackType.RANGED ? AttackType.MAGIC.ordinal() : type == AttackType.MAGIC ? AttackType.RANGED.ordinal() : type.ordinal());
        float roll = (int) (effectiveLevel * (accuracyBoost + 64.0F));
        roll *= accuracyModifier;
        float accuracy;
        if (roll > targetRoll) {
            accuracy = 1 - (targetRoll + 2.0F) / (2 * (roll + 1));
        } else {
            accuracy = roll / (2.0F * (targetRoll + 1));
        }
        return accuracy >= Utils.randomDouble();
    }

    /**
     * Gathers up all the npcs within the loaded regions and goes through numerous checks. If all of the checks come in true, breaks the
     * loop and attacks the target.
     *
     * @return whether a target was attacked or not.
     */
    private boolean fire(boolean skipRoll) {
        final Player player = this.player.get();
        if (player == null) {
            return false;
        }
        final long currentTime = System.currentTimeMillis();
        final long currentTick = WorldThread.getCurrentCycle();
        final List<NPC> targets = CharacterLoop.find(center, 20, NPC.class,
                entity -> !entity.isDead()
                        && !(entity.isMaximumTolerance()
                        || (!entity.isMultiArea()
                        && entity.getAttackedBy() != player
                        && (entity.getAttackedByDelay() > currentTick || entity.getFindTargetDelay() > currentTime))
                        || entity.isProjectileClipped(entity, false)
                        || !entity.getDefinitions().containsOption("Attack")
                        || ProjectileUtils.isProjectileClipped(null, null, center, entity, false)));
        if (targets.isEmpty())
            return false;
        NPC target = null;
        for (int i = targets.size() - 1; i >= 0; i--) {
            final NPC n = targets.get(i);
            if (canAttack(n) && n.canBeMulticannoned(player)) {
                target = n;
                break;
            }
        }
        if (target == null) {
            return false;
        }
        if(!skipRoll && player.getBoonManager().hasBoon(DoubleTap.class) && DoubleTap.roll() && !fire(true))
            return false;
        this.setHighestAccuracyBonus();
        final Projectile projectile = cannon.getCannonballs() == 0 ? GRANITE_PROJECTILE : PROJECTILE;
        World.sendProjectile(center, target, projectile);
        final NPC t = target;
        final int damage = success(target) ? Utils.random(cannon.getCannonballs() == 0 ? 35 : 30) : 0;
        player.getSkills().addXp(SkillConstants.RANGED, damage << 1);
        WorldTasksManager.schedule(() -> {
            final Hit hit = new Hit(player, damage, HitType.REGULAR);
            hit.setWeapon("Dwarf Multicannon");
            t.applyHit(hit);
            t.autoRetaliate(player);
        }, PROJECTILE.getTime(center, t));
        if(cannon.getCannonballs() == 1) {
            if(player.getMemberRank().equalToOrGreaterThan(MemberRank.RESPECTED) && cannon.getCannon().getLocation().withinDistance(player.getLocation(), 20))
                player.getDwarfMulticannon().loadCannon();
        }
        return true;
    }

    /**
     * Determines whether the target is within attack range of the cannon.
     *
     * @param npc
     *            to check
     * @return whether the target is within range.
     */
    private boolean canAttack(final NPC npc) {
        final Player player = this.player.get();
        if (player == null) {
            return false;
        }
        final RegionArea area = player.getArea();
        if ((area instanceof EntityAttackPlugin && !((EntityAttackPlugin) area).attack(player, npc, null))) {
            return false;
        }

        if (!npc.isMultiArea()) {
            Entity attacking = player.getAttackedBy();
            if (attacking != null && attacking != npc && player.getAttackedByDelay() > WorldThread.getCurrentCycle() && !attacking.isDead() && !attacking.isFinished()) {
                return false;
            }
            attacking = npc.getAttackedBy();
            if (attacking != null && attacking != player && npc.getAttackedByDelay() > WorldThread.getCurrentCycle() && !attacking.isDead() && !attacking.isFinished()) {
                return false;
            }
        }
        return polygons.get(direction).contains(npc.getLocation());
    }

    public WeakReference<Player> getPlayer() {
        return player;
    }

    public void setPlayer(WeakReference<Player> player) {
        this.player = player;
    }

    public MulticannonDirection getDirection() {
        return direction;
    }

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    public int getDecayTimer() {
        return decayTimer;
    }

    public void setDecayTimer(int decayTimer) {
        this.decayTimer = decayTimer;
    }

    public void setCannon(DwarfMultiCannon cannon) {
        this.cannon = cannon;
    }
}
