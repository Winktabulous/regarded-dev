package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.crafting.actions.SpinningCrafting;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkCraftingAction extends BasicAfkAction {



    @Override
    public Animation actionAnimation() {
        return SpinningCrafting.ANIMATION;
    }

    @Override
    public int getSkill() {
        return SkillConstants.CRAFTING;
    }

    @Override
    public String getMessage() {
        return "You spin the wheel";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
