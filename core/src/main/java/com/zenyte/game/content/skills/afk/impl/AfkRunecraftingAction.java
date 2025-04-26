package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkRunecraftingAction extends BasicAfkAction {

    private static final Animation RUNECRA = new Animation(2040);

    @Override
    public Animation actionAnimation() {
        return RUNECRA;
    }

    @Override
    public int getSkill() {
        return SkillConstants.RUNECRAFTING;
    }

    @Override
    public String getMessage() {
        return "You infuse runes";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
