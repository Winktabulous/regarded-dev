package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;

public class OsmumtenFangCombat extends MeleeCombat {

    public OsmumtenFangCombat(Entity target) {
        super(target);
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier, AttackType attackType) {
        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MELEE)) {
            return maxhit;
        }

        final int accuracy = getAccuracy(player, target, modifier);
        final int targetRoll = getTargetDefenceRoll(player, target, attackType);
        sendDebug(accuracy, targetRoll, maxhit);
        int accRoll = Utils.random(accuracy);
        int defRoll = Utils.random(targetRoll);
        if (accRoll <= defRoll) {
            accRoll = Utils.random(accuracy);
            //if monster.hasAttribute("toa")://TODO do we add the ToA reroll??
            defRoll = Utils.random(targetRoll);
            if (accRoll <= defRoll) {
                return 0;
            }
        }

        int fifteen = maxhit * 15 / 100;
        return fifteen + Utils.random(maxhit - fifteen * (player.getCombatDefinitions().isUsingSpecial() ? 1 : 2));
    }

}
