package com.zenyte.game.content.minigame.inferno.npc.impl;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.InfernoWave;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.utils.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommeh | 29/11/2019 | 19:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalZek extends InfernoNPC {
    private static final Animation magicAnimation = new Animation(7610);
    private static final Animation meleeAnimation = new Animation(7612);
    private static final Animation reviveAnimation = new Animation(7611);
    private static final Graphics magicOnHitGfx = new Graphics(131, 0, 124);
    private static final Graphics revivalGfx = new Graphics(444, 0, 550);
    private static final SoundEffect attackSound = new SoundEffect(598);
    private static final Projectile magicProjectile = new Projectile(1376, 65, 38, 40, 17, 15, 0, 3);
    private final List<InfernoNPC> toRevive = new ArrayList<>();
    private final List<InfernoNPC> revived = new ArrayList<>();
    private long reviveDelay;

    public JalZek(final Location location, final Inferno inferno) {
        super(7699, location, inferno);
        if (inferno.getWave().equals(InfernoWave.WAVE_69)) {
            setAttackDistance(30);
        } else {
            setAttackDistance(14);
        }
        reviveDelay = Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(60);
        combat.setCombatDelay(3);
    }

    @Override
    public boolean isRevivable() {
        return true;
    }

    @Override
    public int attack(final Entity target) {
        if (reviveDelay <= Utils.currentTimeMillis() && Utils.random(2) == 0) {
            if (toRevive.isEmpty()) {
                autoAttack(target);
                reviveDelay = Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(30);
                return combatDefinitions.getAttackSpeed();
            }
            lock(4);
            setAnimation(reviveAnimation);
            setFaceLocation(inferno.getMiddle());
            WorldTasksManager.schedule(() -> {
                final InfernoNPC revival = toRevive.get(Utils.random(toRevive.size() - 1));
                revival.setLocation(inferno.getRevivalLocations()[Utils.random(inferno.getRevivalLocations().length - 1)]);
                revival.faceEntity(target);
                revival.spawn();
                revival.getCombat().setCombatDelay(4);
                revival.getCombat().setTarget(target);
                revival.setHitpoints(revival.getMaxHitpoints() / 2);
                revival.setGraphics(revivalGfx);
                inferno.add(revival);
                revived.add(revival);
                toRevive.remove(revival);
            }, 2);
            setTarget(target);
            reviveDelay = Utils.currentTimeMillis() + TimeUnit.TICKS.toMillis(60);
            return 8;
        } else {
            autoAttack(target);
            return combatDefinitions.getAttackSpeed();
        }
    }

    private void autoAttack(final Entity target) {
        if (isWithinMeleeDistance(this, target)) {
            if (Utils.random(1) == 0) {
                inferno.playSound(attackSound);
                setAnimation(meleeAnimation);
                delayHit(0, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), STAB, target), HitType.MELEE));
            } else {
                magicAttack(target);
            }
        } else {
            magicAttack(target);
        }
    }

    private void magicAttack(final Entity target) {
        inferno.playSound(attackSound);
        setAnimation(magicAnimation);
        delayHit(World.sendProjectile(this, target, magicProjectile), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(h -> target.setGraphics(magicOnHitGfx)));
    }

    public void queueRevival(final InfernoNPC npc) {
        if (!npc.isRevivable() || revived.contains(npc)) {
            return;
        }
        toRevive.add(npc);
    }

    @Override
    public void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().complete(CAType.HALF_WAY_THERE);
        }
    }

}
