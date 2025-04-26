package com.zenyte.game.content.minigame.fightcaves.npcs;

import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.minigame.fightcaves.FightCaves;
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
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 27/10/2018 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class KetZek extends FightCavesNPC implements CombatScript {
    private static final Animation MELEE_ANIM = new Animation(2644);
    private static final Animation MAGIC_ANIM = new Animation(2647);
    private static final Graphics GFX = new Graphics(446);
    private static final Projectile MAGIC_PROJ = new Projectile(445, 120, 20, 40, 5, 10, 0, 5);
    private static final SoundEffect attackSound = new SoundEffect(598);

    KetZek(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
        setAttackDistance(15);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KaramjaDiary.KILL_KET_ZEK);
        }
    }

    @Override
    public int attack(Entity target) {
        final int style = Utils.random(1);
        playSound(attackSound);
        if (style == 0 && isWithinMeleeDistance(this, target)) {
            setAnimation(MELEE_ANIM);
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 49, AttackType.STAB, target), HitType.MELEE));
        } else {
            setAnimation(MAGIC_ANIM);
            delayHit(World.sendProjectile(this, target, MAGIC_PROJ), target, new Hit(this, getRandomMaxHit(this, 49, MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(GFX)));
        }
        return combatDefinitions.getAttackSpeed();
    }
}
