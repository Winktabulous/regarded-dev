package com.zenyte.game.content.boss.magearenaii;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 21/06/2019 23:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class JusticiarZachariah extends MageArenaBossBase {
    private static final Animation magicAnimation = new Animation(7962);
    private static final Animation meleeAnimation = new Animation(7853);
    private static final Projectile specialProjectile = new Projectile(1515, 50, 10, 40, 10, 70, 64, 5);

    @Override
    Animation getMagicAnimation() {
        return magicAnimation;
    }

    @Override
    int meleeFrequency() {
        return 0;
    }

    @Override
    int meleeSpeed() {
        return 3;
    }

    @Override
    Animation getMeleeAnimation() {
        return meleeAnimation;
    }

    @Override
    boolean canUseSpecial(final Entity target) {
        return !isWithinMeleeDistance(this, target);
    }

    @Override
    Animation getSpecialAnimation() {
        return meleeAnimation;
    }

    @Override
    CombatSpell validSpell() {
        return CombatSpell.SARADOMIN_STRIKE;
    }

    @Override
    int meleeMax() {
        return 26;
    }

    @Override
    AttackType meleeAttackType() {
        return AttackType.SLASH;
    }

    @Override
    int special(final Entity target) {
        final Location tile = new Location(target.getLocation());
        final int delay = World.sendProjectile(this, tile, specialProjectile);
        setAnimation(getSpecialAnimation());
        WorldTasksManager.schedule(() -> {
            if (target.isDead() || target.isFinished() || !target.getLocation().matches(tile)) {
                return;
            }
            final CombatSpell spell = CombatSpell.WATER_BLAST;
            final Graphics graphics = spell.getHitGfx();
            final SoundEffect sound = spell.getHitSound();
            target.setGraphics(new Graphics(graphics.getId(), 0, graphics.getHeight()));
            World.sendSoundEffect(new Location(target.getLocation()), new SoundEffect(sound.getId(), sound.getRadius(), 0));
            target.setForceTalk(new ForceTalk("Nooo!"));
            target.setAnimation(Animation.KNOCKBACK);
            final Location teleTile = getFaceLocation(target, getSize() + 2);
            if (!World.isFloorFree(teleTile, 1)) {
                teleTile.setLocation(getFaceLocation(target, getSize()));
                if (!World.isFloorFree(teleTile, 1)) {
                    return;
                }
            }
            target.lock(1);
            target.setForceMovement(new ForceMovement(teleTile, 30, DirectionUtil.getFaceDirection(getX() - (target.getX() + (target.getSize() / 2.0F)), getY() - (target.getY() + (target.getSize() / 2.0F)))));
            WorldTasksManager.schedule(() -> target.setLocation(teleTile));
        }, delay);
        return delay + 2;
    }

    public JusticiarZachariah(@NotNull final Player owner, @NotNull final Location tile) {
        super(7858, owner, tile);
    }
}
