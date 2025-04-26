package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

public class AfkFishingAction extends BasicAfkAction {

    private static final Animation ANIMATION = new Animation(621);

    @Override
    public Animation actionAnimation() {
        return ANIMATION;
    }

    @Override
    public int getSkill() {
        return SkillConstants.FISHING;
    }

    @Override
    public String getMessage() {
        return "You put your net in the water";
    }

    @Override
    public boolean hasRequiredItem() {
        if (!player.getInventory().containsItem(303, 1)) {
            player.getDialogueManager().start(new PlainChat(player, "You need a small fishing net to do this."));
            return false;
        }
        return true;
    }
}
