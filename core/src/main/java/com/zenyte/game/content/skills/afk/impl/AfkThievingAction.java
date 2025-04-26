package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkThievingAction extends BasicAfkAction {

    private static final Animation THIEVING_ANIM = new Animation(881);

    @Override
    public Animation actionAnimation() {
        return THIEVING_ANIM;
    }

    @Override
    public int getSkill() {
        return SkillConstants.THIEVING;
    }

    @Override
    public String getMessage() {
        return "You steal from the stall";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
