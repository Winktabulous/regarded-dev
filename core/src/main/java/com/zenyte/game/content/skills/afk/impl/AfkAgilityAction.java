package com.zenyte.game.content.skills.afk.impl;

import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.SkillConstants;

public class AfkAgilityAction extends BasicAfkAction {


    //			player.lock();
    //			player.addFreezeImmunity(getDelay());
    //			player.getTemporaryAttributes().put("courseRun", player.isRun());
    //			player.setRunSilent(true);


    private static final RenderAnimation RENDER = new RenderAnimation(RenderAnimation.STAND, 762, 762);

    @Override
    public boolean start() {
        if(!super.check())
            return false;

        player.getTemporaryAttributes().put("courseRun", player.isRun());
        player.setRunSilent(true);
        return true;
    }


    public boolean getiDirection() {
        return player.getLocation().getX() < 3107;
    }

    @Override
    public int processWithDelay() {
        super.processWithDelay();
        player.getAppearance().setRenderAnimation(RENDER);
        if(getiDirection())
            player.addWalkSteps(3107, 3476, -1, false);
        else
            player.addWalkSteps(3102, 3476, -1, false);

        return 5;
    }

    @Override
    public void stop() {
        super.stop();
        player.setRunSilent(false);
        player.getAppearance().resetRenderAnimation();
    }

    @Override
    public Animation actionAnimation() {
        return null;
    }

    @Override
    public int getSkill() {
        return SkillConstants.AGILITY;
    }

    @Override
    public String getMessage() {
        return "You cross the log";
    }

    @Override
    public boolean hasRequiredItem() {
        return true;
    }
}
