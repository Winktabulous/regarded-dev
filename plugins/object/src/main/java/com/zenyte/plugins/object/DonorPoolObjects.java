package com.zenyte.plugins.object;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

public class DonorPoolObjects implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Drink")) {
            final int id = object.getId();
            if (id == 50050)
                drink(player, 3);
            else if (id == 50044 || id == 50043)
                drink(player, 4);
        }

        if(object.getId() == 50099 && option.equalsIgnoreCase("Drink")) {
            if(!player.getMemberRank().equalToOrGreaterThan(MemberRank.UBER)) {
                player.sendMessage("Only Uber donators can use this.");
                return;
            }
            if(player.getVariables().getTime(TickVariable.OVERLOAD) > 0) {
                player.sendMessage("You must wait until your last overload effect is over.");
                return;
            }
            ConsumableEffects.damagePlayer(player, 5);
            player.getVariables().schedule(500, TickVariable.OVERLOAD);
            player.getVariables().setOverloadType((short) 3);
            player.getVarManager().sendBit(VarCollection.OVERLOAD_REFRESHES_REMAINING.getId(), 21);
            ConsumableEffects.applyOverload(player);
        }

        if(object.getId() == 50100 && option.equalsIgnoreCase("Drink")) {
            if(!player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
                player.sendMessage("Only Legendary donators+ can use this.");
                return;
            }

            if(player.getAttributes().containsKey("DIVINE_POTION") && ((double) player.getAttributes().get("DIVINE_POTION")) > System.currentTimeMillis()) {
                player.sendMessage("You have to wait 5 minutes before using this again.");
                return;
            }
            new Consumable.Boost(SkillConstants.ATTACK, 0.15F, 5).apply(player);
            new Consumable.Boost(SkillConstants.DEFENCE, 0.15F, 5).apply(player);
            new Consumable.Boost(SkillConstants.STRENGTH, 0.15F, 5).apply(player);
            player.getVariables().schedule(500, TickVariable.DIVINE_SUPER_COMBAT_POTION);
            player.getAttributes().put("DIVINE_POTION", ((double)(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5))));
        }
    }

    // di - type 3
    // die = type 4
    // rdi = type 4

    private final void drink(final Player player, final int type) {
        if (type >= 0) {
            player.getCombatDefinitions().setSpecialEnergy(100);
        }

        if (type >= 1) {
            player.getVariables().setRunEnergy(100);
        }

        if (type >= 2) {
            player.getPrayerManager().restorePrayerPoints(99);
        }

        if (type >= 3) {
            for (int i = 0; i < 22; i++) {
                if (i == SkillConstants.HITPOINTS || i == SkillConstants.PRAYER)
                    continue;
                if (player.getSkills().getLevel(i) < player.getSkills().getLevelForXp(i))
                    player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
            }
        }

        if (type >= 4) {
            player.heal(99);
        }

        player.getToxins().reset();

        player.sendMessage("You feel replenished.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {50043, 50044, 50050, 50099, 50100};
    }
}
