package com.zenyte.game.content.boss.magearenaii;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 22/06/2019 13:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Derwen extends MageArenaBossBase {
    private static final Projectile energyBall = new Projectile(1512, 50, 10, 40, 10, 70, 64, 5);


    private static final class EnergyBall extends NPC implements CombatScript {
        private static final Projectile healProjectile = new Projectile(1513, 10, 40, 0, 10, 70, 64, 5);

        private EnergyBall(final Derwen derwen, final Location tile) {
            super(7514, tile, Direction.SOUTH, 0);
            freeze(Integer.MAX_VALUE);
            spawned = true;
            this.derwen = derwen;
        }

        @Override
        public boolean isForceAttackable() {
            return true;
        }

        private final Derwen derwen;
        private int ticks;

        @Override
        public void setAnimation(final Animation animation) {
        }

        @Override
        public void setUnprioritizedAnimation(final Animation animation) {
        }

        @Override
        public void onFinish(final Entity source) {
            super.onFinish(source);
            derwen.energyBalls.remove(this);
        }

        @Override
        public void processNPC() {
            super.processNPC();
            if (ticks++ % 8 == 0 && ticks != 1) {
                World.scheduleProjectile(this, derwen, healProjectile).schedule(() -> derwen.applyHit(new Hit(5, HitType.HEALED)));
            }
        }

        @Override
        public void applyHit(final Hit hit) {
            super.applyHit(hit);
            if (hit.getWeapon() != CombatSpell.CLAWS_OF_GUTHIX) {
                hit.setDamage(0);
            }
        }

        @Override
        public float getXpModifier(final Hit hit) {
            if (hit.getWeapon() != CombatSpell.CLAWS_OF_GUTHIX) {
                return 0;
            }
            return 1;
        }

        @Override
        public int attack(final Entity target) {
            return Integer.MAX_VALUE;
        }
    }

    private static final Animation magicAnimation = new Animation(7849);
    private static final Animation meleeAnimation = new Animation(7848);

    public Derwen(@NotNull final Player owner, @NotNull final Location tile) {
        super(7859, owner, tile);
    }

    @Override
    Graphics hitGraphics() {
        return new Graphics(1511, 0, 96);
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        for (final Derwen.EnergyBall ball : new ArrayList<>(energyBalls)) {
            ball.finish();
        }
    }

    private final List<EnergyBall> energyBalls = new ObjectArrayList<>();

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
        return magicAnimation;
    }

    @Override
    CombatSpell validSpell() {
        return CombatSpell.CLAWS_OF_GUTHIX;
    }

    @Override
    int meleeFrequency() {
        return 2;
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
        return AttackType.CRUSH;
    }

    @Override
    boolean canUseSpecial(final Entity target) {
        return energyBalls.isEmpty();
    }

    @Override
    int special(final Entity target) {
        final Location tile = target.getLocation().transform(Utils.random(3), Utils.random(3), 0);
        setAnimation(getMagicAnimation());
        World.scheduleProjectile(this, tile, energyBall).schedule(() -> {
            if (isDead() || isFinished()) {
                return;
            }
            final Derwen.EnergyBall ball = new EnergyBall(this, tile);
            ball.spawn();
            energyBalls.add(ball);
        });
        return 6;
    }
}
