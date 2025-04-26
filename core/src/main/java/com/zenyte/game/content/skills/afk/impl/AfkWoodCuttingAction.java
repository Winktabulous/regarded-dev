package com.zenyte.game.content.skills.afk.impl;

import com.near_reality.game.content.skills.woodcutting.AxeDefinition;
import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;

import java.util.Optional;

public class AfkWoodCuttingAction extends BasicAfkAction {

    private AxeDefinition axe;


    @Override
    public Animation actionAnimation() {
        return axe.getTreeCutAnimation();
    }

    @Override
    public int getSkill() {
        return SkillConstants.WOODCUTTING;
    }

    @Override
    public String getMessage() {
        return "You swing your axe at the tree";
    }

    @Override
    public boolean hasRequiredItem() {
        final Optional<Woodcutting.AxeResult> optionalAxe = Woodcutting.getAxe(player);
        if (!optionalAxe.isPresent()) {
            player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
            return false;
        }
        this.axe = optionalAxe.get().getDefinition();
        return true;
    }
}
