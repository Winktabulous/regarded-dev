package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

public class KerisCombat extends MeleeCombat {

    public KerisCombat(Entity target) {
        super(target);
    }

    @Override
    public int getMaxHit(Player player, double passiveModifier, double activeModifier, boolean ignorePrayers) {
        float maxHit = (float) super.getMaxHit(player, passiveModifier, activeModifier, ignorePrayers);
        if (isKerisWeapon(player.getEquipment().getId(EquipmentSlot.WEAPON)) && CombatUtilities.isKerisAffected(target)) {
            maxHit *= (4.0F / 3.0F);
        }
        return (int) maxHit;
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier, AttackType attackType) {
        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MELEE)) {
            return maxhit;
        }

        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        int accuracy = getAccuracy(player, target, modifier);
        if (weaponId == ItemId.KERIS_PARTISAN_OF_BREACHING) {
            if (CombatUtilities.isKerisAffected(target)) {
                accuracy *= (4.0F / 3.0F);
            }
        } else if (weaponId == ItemId.KERIS_PARTISAN_OF_THE_SUN) {
            if (target.getHitpointsAsPercentage() <= 25) {//TODO inside ToA check
                accuracy *= 1.25;
            }
        }
        final int targetRoll = getTargetDefenceRoll(player, target, attackType);
        sendDebug(accuracy, targetRoll, maxhit);
        int accRoll = Utils.random(accuracy);
        int defRoll = Utils.random(targetRoll);
        if (accRoll <= defRoll) {
            return 0;
        }

        int rolled = Utils.random(maxhit);
        if (CombatUtilities.isKerisAffected(target) && Utils.random(50) == 0) {
            rolled *= 3;
            if (rolled >= 10 && isKerisDagger(weaponId)) {
                player.sendMessage("You slip your dagger through a gap in the creature's chitin, landing a vicious blow.");
            } else {
                player.sendMessage("You slip your weapon through a gap in the creature's chitin, landing a vicious blow.");
            }
        }
        return rolled;
    }

}
