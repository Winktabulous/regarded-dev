package com.zenyte.game.world.entity.player.action.combat.melee;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;

public class BlisterwoodFlailCombat extends MeleeCombat {

    public BlisterwoodFlailCombat(Entity target) {
        super(target);
    }

    @Override
    public int getAccuracy(Player player, Entity target, double resultModifier) {
        if(target instanceof NPC)
            return super.getAccuracy(player, target, resultModifier * (isVampyre((NPC) target) ? 1.05D : 1D));
        return super.getAccuracy(player, target, resultModifier);
    }

    @Override
    public Hit getHit(Player player, Entity target, double accuracyModifier, double passiveModifier, double activeModifier, boolean ignorePrayers) {
        if(target instanceof NPC && isVampyre((NPC) target)) {
            accuracyModifier += 0.05D;
            activeModifier += 0.45D;
        }
        return super.getHit(player, target, accuracyModifier, passiveModifier, activeModifier, ignorePrayers);
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier, AttackType attackType) {
        return super.getRandomHit(player, target, maxhit, modifier, attackType);
    }

    public boolean isVampyre(NPC npc) {
        String name = npc.getName();
        return name.equalsIgnoreCase("Vampyre Juvinate") || name.equalsIgnoreCase("Vampyre Juvenile")
                || name.equalsIgnoreCase("Vyrewatch Sentinel") || name.equalsIgnoreCase("Vyre") || name.equalsIgnoreCase("Vyrewatch");
    }
}
