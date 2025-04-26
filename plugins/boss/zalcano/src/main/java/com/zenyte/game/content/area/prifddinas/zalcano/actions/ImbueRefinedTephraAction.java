package com.zenyte.game.content.area.prifddinas.zalcano.actions;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.types.config.items.ItemDefinitions;

public class ImbueRefinedTephraAction extends Action {

    public static final Animation ANIMATION = new Animation(791);

    @Override
    public boolean start() {
        if (!canImbue()) {
            player.sendMessage("You must have " + ItemDefinitions.get(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID).getName() + " to use this Altar.");
            return false;
        }

        return true;
    }

    @Override
    public boolean process() {
        if (!canImbue()) {
            return false;
        }

        player.getInventory().deleteItem(ZalcanoConstants.REFINED_TEPHRA_ITEM_ID, 1);
        player.getInventory().addItem(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID, 1);
        player.sendFilteredMessage(ZalcanoConstants.IMBUE_TEPHRA);
        player.getSkills().addXp(SkillConstants.RUNECRAFTING, 5);
        player.setAnimation(ANIMATION);
        player.sendSound(Smelting.soundEffect);
        return true;
    }

    public boolean canImbue() {
        return player.getInventory().containsItem(ZalcanoConstants.REFINED_TEPHRA_ITEM_ID);
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
