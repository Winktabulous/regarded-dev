package com.zenyte.game.content.minigame.fightcaves.npcs;

import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 27/10/2018 17:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class TzKih extends FightCavesNPC implements CombatScript {

    private static final SoundEffect attackSound = new SoundEffect(291);

    TzKih(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
    }

    @Override
    public int attack(Entity target) {
        animate();
        playSound(attackSound);
        delayHit(0, target, melee(target, 4).onLand(hit -> {
            if (target instanceof Player) {
                ((Player) target).getPrayerManager().drainPrayerPoints(1);
            }
        }));
        return combatDefinitions.getAttackSpeed();
    }
}
