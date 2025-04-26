package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkSmithingAction extends BasicAfkAction {

    @Override
    public Animation actionAnimation() {
        return Smelting.ANIMATION;
    }

    @Override
    public int getSkill() {
        return SkillConstants.SMITHING;
    }

    @Override
    public String getMessage() {
        return "You mess with the furnace";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
