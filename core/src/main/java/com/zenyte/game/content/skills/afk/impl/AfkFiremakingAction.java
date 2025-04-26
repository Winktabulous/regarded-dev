package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

public class AfkFiremakingAction extends BasicAfkAction {


    @Override
    public Animation actionAnimation() {
        return new Animation(733);
    }

    @Override
    public int getSkill() {
        return SkillConstants.FIREMAKING;
    }

    @Override
    public String getMessage() {
        return "You add to the fire";
    }

    @Override
    public boolean hasRequiredItem() {
        if(!player.getInventory().containsItem(ItemId.TINDERBOX)) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Tinderbox to do this!"));
            return false;
        }
        return true;
    }
}
