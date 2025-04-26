package com.zenyte.game.content.boss.magearenaii;

import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 21/06/2019 16:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Porazdir extends MageArenaBossBase {
    private static final Animation magicAnimation = new Animation(7838);
    private static final Animation meleeAnimation = new Animation(7840);
    private static final Animation specialAnimation = new Animation(7841);
    private static final Projectile specialProjectile = new Projectile(1514, 50, 43, 40, 10, 170, 64, 5);

    public Porazdir(@NotNull final Player owner, @NotNull final Location tile) {
        super(7860, owner, tile);
    }

    @Override
    CombatSpell validSpell() {
        return CombatSpell.FLAMES_OF_ZAMORAK;
    }

    @Override
    Animation getMagicAnimation() {
        return magicAnimation;
    }

    @Override
    Animation getMeleeAnimation() {
        return meleeAnimation;
    }

    @Override
    Animation getSpecialAnimation() {
        return specialAnimation;
    }

    @Override
    int meleeFrequency() {
        return 2;
    }

    @Override
    boolean canUseSpecial(final Entity target) {
        return true;
    }

    @Override
    int meleeSpeed() {
        return 6;
    }

    @Override
    int meleeMax() {
        return 16;
    }

    @Override
    AttackType meleeAttackType() {
        return AttackType.SLASH;
    }

    @Override
    int special(final Entity target) {
        setAnimation(getMagicAnimation());
        if (!(target instanceof Player)) {
            return 6;
        }
        final int delay = World.sendProjectile(this, target, specialProjectile);
        final Player player = ((Player) target);
        player.sendMessage(Colour.RED.wrap("Porazdir fires a ball of energy directly linked to his power!"));
        WorldTasksManager.schedule(() -> {
            final double distance = target.getLocation().getDistance(getLocation());
            final int damage = (int) ((distance >= 8 ? 0 : ((25 / distance) + Utils.random(5))) * (!player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC) ? 1.5F : 1.0F));
            target.setGraphics(new Graphics(131, 0, 124));
            delayHit(-1, target, new Hit(this, damage, HitType.REGULAR));
            if (damage > 3) {
                player.getPrayerManager().drainPrayerPoints(damage / 3);
                player.sendMessage("Your prayer is drained.");
            }
            if (damage > 0) {
                player.sendMessage("Porazdir was able to focus substantial power into the energy ball.");
            }
        }, delay);
        return 6;
    }
}
