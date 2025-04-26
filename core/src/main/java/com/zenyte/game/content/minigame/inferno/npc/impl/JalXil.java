package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.InfernoWave;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;

/**
 * @author Tommeh | 29/11/2019 | 19:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalXil extends InfernoNPC {
    private static final Animation meleeAnimation = new Animation(7604);
    private static final Animation rangedAnimation = new Animation(7605);
    private static final SoundEffect attackSound = new SoundEffect(598);
    private static final Projectile rangedProjectile = new Projectile(1377, 72, 33, 35, 17, 30, 0, 4);

    public JalXil(final Location location, final Inferno inferno) {
        super(7698, location, inferno);
        setAttackDistance(inferno.getWave().equals(InfernoWave.WAVE_69) ? 30 : 14);
        combat.setCombatDelay(3);
    }

    @Override
    public boolean isRevivable() {
        return true;
    }

    @Override
    public int attack(final Entity target) {
        if (isWithinMeleeDistance(this, target)) {
            if (Utils.random(1) == 0) {
                inferno.playSound(attackSound);
                setAnimation(meleeAnimation);
                delayHit(0, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), CRUSH, target), HitType.MELEE));
            } else {
                rangedAttack(target);
            }
        } else {
            rangedAttack(target);
        }
        return combatDefinitions.getAttackSpeed();
    }

    private void rangedAttack(final Entity target) {
        final Location left = getFaceLocation(target, getSize(), 512);
        final Location right = getFaceLocation(target, getSize(), 1536);
        setAnimation(rangedAnimation);
        World.sendProjectile(left, target, rangedProjectile);
        inferno.playSound(attackSound);
        delayHit(World.sendProjectile(right, target, rangedProjectile), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.RANGED));
    }
}
