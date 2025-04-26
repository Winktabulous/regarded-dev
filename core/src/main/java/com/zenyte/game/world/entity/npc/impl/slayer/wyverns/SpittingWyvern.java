package com.zenyte.game.world.entity.npc.impl.slayer.wyverns;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.Arrays;

/**
 * @author Christopher
 * @since 2/28/2020
 */
public class SpittingWyvern extends Wyvern {
    private static final Animation meleeAnim = new Animation(7658);
    private static final Animation rangedAnim = new Animation(7653);
    private static final Graphics rangedGfx = new Graphics(1393, 0, 24);
    private static final Projectile rangedProjectile = new Projectile(1394, 0, 0, 120, 0, 8, 0, 5);

    public SpittingWyvern(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        final int style = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 1);
        if (style == 0) {
            magicAttack(target);
        } else if (style == 1) {
            setAnimation(rangedAnim);
            setGraphics(rangedGfx);
            final int delay = World.sendProjectile(this, target, rangedProjectile);
            final int hitAmount = Utils.random(2) == 0 ? 4 : 1;
            final Hit originalHit = ranged(target, 4);
            final Hit hit = new Hit(originalHit.getSource(), originalHit.getDamage(), originalHit.getHitType());
            final Hit[] hits = new Hit[hitAmount];
            final MutableBoolean dealtDamage = new MutableBoolean();
            originalHit.onLand(dealtHit -> dealtDamage.setValue(dealtHit.getDamage() > 0));
            hit.setPredicate(__ -> !dealtDamage.isTrue());
            hits[0] = originalHit;
            Arrays.fill(hits, 1, hits.length, hit);
            delayHit(this, delay, target, hits);
            return getCombatDefinitions().getAttackSpeed() + delay;
        } else {
            setAnimation(meleeAnim);
            delayHit(this, 0, target, melee(target, 9));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equalsIgnoreCase("spitting wyvern");
    }
}
