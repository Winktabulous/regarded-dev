package com.zenyte.game.content.minigame.fightcaves.npcs;

import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 27/10/2018 17:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class TokXil extends FightCavesNPC implements CombatScript {
    private static final Animation MELEE_ANIM = new Animation(2628);
    private static final Animation RANGED_ANIM = new Animation(2633);
    private static final Projectile RANGED_PROJ = new Projectile(443, 65, 20, 40, 5, 10, 0, 5);
    private static final SoundEffect meleeAttackSound = new SoundEffect(598);
    private static final SoundEffect rangedAttackSound = new SoundEffect(601);
    private static final SoundEffect rangedLandSound = new SoundEffect(1184);

    TokXil(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
        setAttackDistance(15);
    }

    @Override
    public int attack(Entity target) {
        final int style = Utils.random(1);
        if (style == 0 && isWithinMeleeDistance(this, target)) {
            playSound(meleeAttackSound);
            setAnimation(MELEE_ANIM);
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 13, MELEE, target), HitType.MELEE));
        } else {
            setAnimation(RANGED_ANIM);
            playSound(rangedAttackSound);
            delayHit(World.sendProjectile(this, target, RANGED_PROJ), target, new Hit(this, getRandomMaxHit(this, 14, RANGED, target), HitType.RANGED).onLand(hit -> playSound(rangedLandSound)));
        }
        return combatDefinitions.getAttackSpeed();
    }
}
