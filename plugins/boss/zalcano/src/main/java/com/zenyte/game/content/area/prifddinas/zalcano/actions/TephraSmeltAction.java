package com.zenyte.game.content.area.prifddinas.zalcano.actions;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.types.config.items.ItemDefinitions;

/**
 * Handles smelting tephra
 */
public class TephraSmeltAction extends Action {

    private int ticks = 3;

    @Override
    public boolean start() {
        if (!canSmelt()) {
            player.sendMessage("You must have " + ItemDefinitions.get(ZalcanoConstants.TEPHRA_ITEM_ID).getName() + " to use this furnace.");
            return false;
        }

        return true;
    }

    @Override
    public boolean process() {

        if (!canSmelt()) {
            return false;
        }

        if (ticks >= 1) {
            player.getInventory().deleteItem(ZalcanoConstants.TEPHRA_ITEM_ID, 1);
            player.getInventory().addItem(ZalcanoConstants.REFINED_TEPHRA_ITEM_ID, 1);
            player.sendFilteredMessage(ZalcanoConstants.REFINE_TEPHRA);
            player.getSkills().addXp(SkillConstants.SMITHING, 10);
            player.setAnimation(Smelting.ANIMATION);
            player.sendSound(Smelting.soundEffect);
            ticks = 0;
            return true;
        }


        ticks++;
        return true;
    }

    public boolean canSmelt() {
        return player.getInventory().containsItem(ZalcanoConstants.TEPHRA_ITEM_ID);
    }

    @Override
    public int processWithDelay() {
        return 0;
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

}
