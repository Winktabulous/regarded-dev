package com.zenyte.game.world.entity.player.action.combat.special;

import com.zenyte.game.content.boons.impl.SliceNDice;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
public class BurningBarrageSpecial implements SpecialAttackScript {
    @Override
    public void attack(Player player, PlayerCombat combat, Entity target) {
        int probability = -1;
        for (int i = 0; i < 3; i++) {
            if (!combat.isSuccessful(player, target, 1, AttackType.SLASH)) continue;
            probability = i;
            break;
        }
        var hasBonusHit = player.getBoonManager().hasBoon(SliceNDice.class) && SliceNDice.roll();
        var ordinaryMaxHit = combat.getMaxHit(player, 1, 1, false);
        var boost = Utils.random(1);
        World.sendSoundEffect(player, SLICE_AND_DICE_SOUND);
        switch (probability) {
            case 0: {
                final int maxHit = ordinaryMaxHit - 1;
                final int minHit = (maxHit + 1) / 2;
                int firstHit = Utils.random(minHit, maxHit);
                int secondHit = firstHit / 2;
                int thirdHit = secondHit / 2;
                combat.delayHit(0, new Hit(player, firstHit, HitType.MELEE), new Hit(player, secondHit, HitType.MELEE));
                combat.delayHit(1, new Hit(player, thirdHit, HitType.MELEE));
                if (hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, boost, HitType.MELEE));
                return;
            }
            case 1: {
                final int maxHit = (int) (ordinaryMaxHit * 7.0 / 8.0);
                final int minHit = (int) (ordinaryMaxHit * 3.0 / 8.0);
                int secondHit = Utils.random(minHit, maxHit);
                int thirdHit = secondHit / 2;
                combat.delayHit(0, new Hit(player, 0, HitType.MELEE), new Hit(player, secondHit, HitType.MELEE));
                combat.delayHit(1, new Hit(player, thirdHit, HitType.MELEE));
                if (hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, boost, HitType.MELEE));
                return;
            }
            case 2: {
                final int maxHit = (int) (ordinaryMaxHit * 1.25);
                final int minHit = (int) (ordinaryMaxHit * 0.25);
                int thirdHit = Utils.random(minHit, maxHit);
                combat.delayHit(0, new Hit(player, 0, HitType.MISSED), new Hit(player, 0, HitType.MISSED));
                combat.delayHit(1, new Hit(player, thirdHit, HitType.MISSED));
                if (hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, boost, HitType.MELEE));
            }
            default:
                combat.delayHit(0, new Hit(player, 0, HitType.MISSED), new Hit(player, 0, HitType.MISSED));
                combat.delayHit(1, new Hit(player, boost, HitType.MELEE));
                if (hasBonusHit && !(combat.getTarget() instanceof Player))
                    combat.delayHit(2, new Hit(player, boost, HitType.MELEE));
        }
    }
}
