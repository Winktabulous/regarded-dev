package com.zenyte.game.content.boss.cerberus;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * @author Kris | 07/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SummonedSoul extends NPC {
    /**
     * The number of steps the summoned soul will perform upon spawn, which is followed by an attack.
     */
    private static final int WALK_DISTANCE = 9;
    /**
     * The projectile the summoned soul sends out when performing the melee attack.
     */
    private static final Projectile soulProjectile = new Projectile(1248, 25, 20, 0, 75, 10, 0, 5);
    /**
     * The projectile the summoned soul sends out when performing the ranged attack.
     */
    private static final Projectile dartProjectile = new Projectile(34, 25, 20, 0, 20, 10, 0, 5);
    /**
     * The projectile the summoned soul sends out when performing the magic attack.
     */
    private static final Projectile fireProjectile = new Projectile(100, 25, 20, 27, 20, 10, 0, 5);
    /**
     * The animation the soul performs when doing the melee attack.
     */
    private static final Animation meleeAnimation = new Animation(4505);
    /**
     * The animation the soul performs when doing the magic attack.
     */
    private static final Animation magicAnimation = new Animation(4504);
    /**
     * The animation the soul performs when doing the ranged attack.
     */
    private static final Animation rangedAnimation = new Animation(4503);
    /**
     * A weak reference to the Cerberus NPC which summoned the soul.
     */
    @NotNull
    private final WeakReference<Cerberus> cerberus;
    /**
     * A weak reference to the player whom the soul is meant to attack.
     */
    @NotNull
    private final WeakReference<Entity> target;
    /**
     * The current stage of the soul.
     */
    @NotNull
    private Stage stage;
    /**
     * The delay for which summoned soul processing is halted after the approach stage.
     */
    private int forcedDelay;
    /**
     * The delay for which summoning soul processing is halted upon spawn.
     */
    private int initialDelay;

    SummonedSoul(final int id, @NotNull final Location tile, @NotNull final Cerberus cerberus, @NotNull final Entity target, final int index) {
        super(id, tile, Direction.SOUTH, 0);
        this.cerberus = new WeakReference<>(cerberus);
        this.target = new WeakReference<>(target);
        stage = Stage.APPROACHING;
        this.initialDelay = 1;
        this.forcedDelay = 2 + (index * 2);
        this.combat = new NPCCombat(this) {
            @Override
            public void addAttackedByDelay(final Entity target) {
            }
        };
    }

    @Override
    public void processNPC() {
        if (initialDelay > 0) {
            initialDelay--;
            return;
        }
        if (stage != Stage.APPROACHING && forcedDelay > 0) {
            forcedDelay--;
            return;
        }
        final boolean isMoving = hasWalkSteps();
        switch (stage) {
        case APPROACHING:
            if (getY() == respawnTile.getY() - WALK_DISTANCE) {
                stage = Stage.ATTACKING;
                return;
            }
            if (!isMoving) {
                addWalkSteps(respawnTile.getX(), respawnTile.getY() - WALK_DISTANCE, WALK_DISTANCE, false);
                return;
            }
            break;
        case RETREATING:
            if (getY() == respawnTile.getY()) {
                finish();
                return;
            }
            if (!isMoving) {
                addWalkSteps(respawnTile.getX(), respawnTile.getY(), WALK_DISTANCE, false);
                return;
            }
            break;
        case ATTACKING:
            attack();
            stage = Stage.RETREATING;
            forcedDelay = 2;
            break;
        default:
            throw new IllegalStateException("Invalid stage: " + stage);
        }
    }

    /**
     * Performs an attack towards the target if they're still within the fight, of the style this soul uses.
     */
    private void attack() {
        final SummonedSoul.SpiritShieldType spiritShieldType = hasElysianShield() ? SpiritShieldType.ELYSIAN : hasSpectralShield() ? SpiritShieldType.SPECTRAL : SpiritShieldType.NONE;
        final Entity targetPlayer = target.get();
        final Cerberus cerberus = this.cerberus.get();
        if (targetPlayer == null || cerberus == null || cerberus.isCancelled(targetPlayer) || !(targetPlayer instanceof Player)) {
            return;
        }
        if (cerberus.isDead() || cerberus.isFinished() || !cerberus.isCurrentSoul(this)) {
            finish();
            return;
        }
        final Player player = ((Player) targetPlayer);
        faceEntity(player);
        if (isMelee()) {
            meleeAttack(spiritShieldType, player);
        } else if (isMagic()) {
            magicAttack(spiritShieldType, player);
        } else if (isRanged()) {
            rangedAttack(spiritShieldType, player);
        } else {
            throw new IllegalStateException("Unknown state: " + getId());
        }
    }

    /**
     * Performs the melee attack against the player.
     *
     * @param spiritShieldType the shield type that the player has equipped.
     * @param player           the player being attacked.
     */
    private void meleeAttack(@NotNull final SpiritShieldType spiritShieldType, @NotNull final Player player) {
        setAnimation(meleeAnimation);
        final int delay = World.sendProjectile(this, player, soulProjectile);
        scheduleHit(delay, player, Prayer.PROTECT_FROM_MELEE, spiritShieldType);
    }

    /**
     * Performs the magic attack against the player.
     *
     * @param spiritShieldType the shield type that the player has equipped.
     * @param player           the player being attacked.
     */
    private void magicAttack(@NotNull final SpiritShieldType spiritShieldType, @NotNull final Player player) {
        setAnimation(magicAnimation);
        final int delay = World.sendProjectile(this, player, fireProjectile);
        scheduleHit(delay, player, Prayer.PROTECT_FROM_MAGIC, spiritShieldType);
    }

    /**
     * Performs the ranged attack against the player.
     *
     * @param spiritShieldType the shield type that the player has equipped.
     * @param player           the player being attacked.
     */
    private void rangedAttack(@NotNull final SpiritShieldType spiritShieldType, @NotNull final Player player) {
        setAnimation(rangedAnimation);
        final int delay = World.sendProjectile(this, player, dartProjectile);
        scheduleHit(delay, player, Prayer.PROTECT_FROM_MISSILES, spiritShieldType);
    }

    /**
     * Schedules a hit against the player, ot drains their prayer if they have the correct prayer up at the time.
     *
     * @param delay            the delay until the hit reaches the player.
     * @param player           the player being hit.
     * @param prayer           the prayer the player is meant to have up to switch the damage up for prayer drain.
     * @param spiritShieldType the shield type that the player has equipped.
     */
    private void scheduleHit(final int delay, @NotNull final Player player, @NotNull final Prayer prayer, @NotNull final SpiritShieldType spiritShieldType) {
        player.addPostProcessRunnable(() -> {
            final boolean hasPrayerUp = player.getPrayerManager().isActive(prayer);
            if (hasPrayerUp) {
                final Cerberus cerberus = this.cerberus.get();
                if (cerberus != null) {
                    cerberus.addSummonedSoulMitigation();
                }
                player.getPrayerManager().drainPrayerPoints(spiritShieldType == SpiritShieldType.SPECTRAL ? 15 : 30);
            } else {
                CombatUtilities.delayHit(this, delay, player, new Hit(spiritShieldType == SpiritShieldType.ELYSIAN ? 15 : 30, HitType.REGULAR));
            }
        });
    }

    /**
     * Removes the soul from the parent Cerberus' collection after being removed from the game.
     *
     * @param source the entity which killed the soul.
     */
    @Override
    public void onFinish(@NotNull final Entity source) {
        super.onFinish(source);
        final Cerberus cerberus = this.cerberus.get();
        if (cerberus != null) {
            cerberus.removeSoul(this);
        }
    }

    /**
     * @return whether or not this summoned soul is of melee type.
     */
    private boolean isMelee() {
        return getId() == NpcId.SUMMONED_SOUL_5869;
    }

    /**
     * @return whether or not this summoned soul is of ranged type.
     */
    private boolean isRanged() {
        return getId() == NpcId.SUMMONED_SOUL;
    }

    /**
     * @return whether or not this summoned soul is of magic type.
     */
    private boolean isMagic() {
        return getId() == NpcId.SUMMONED_SOUL_5868;
    }

    /**
     * @return whether or not the player has the elysian spirit shield equipped.
     */
    private boolean hasElysianShield() {
        final Entity player = target.get();
        if (player instanceof Player) {
            return CombatUtilities.isElysianSpiritShield(((Player) player).getEquipment().getId(EquipmentSlot.SHIELD));
        }
        return false;
    }

    /**
     * @return whether or not the player has the spectral spirit shield equipped.
     */
    private boolean hasSpectralShield() {
        final Entity player = target.get();
        if (player instanceof Player) {
            return ((Player) player).getEquipment().getId(EquipmentSlot.SHIELD) == ItemId.SPECTRAL_SPIRIT_SHIELD;
        }
        return false;
    }


    /**
     * The three stages of the summoned souls in the order they go through.
     */
    private enum Stage {
        APPROACHING, ATTACKING, RETREATING
    }


    /**
     * The types of the spirit shields relevant to the summoned souls that the player could have equipped.
     */
    private enum SpiritShieldType {
        NONE, SPECTRAL, ELYSIAN
    }
}
