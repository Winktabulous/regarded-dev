package com.zenyte.game.content.skills.afk.impl;

import com.near_reality.game.content.skills.mining.PickAxeDefinition;
import com.zenyte.game.content.skills.afk.BasicAfkAction;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

public class AfkMiningAction extends BasicAfkAction {


    private PickAxeDefinition tool;


    @Override
    public Animation actionAnimation() {
        return tool.getAnim();
    }

    @Override
    public int getSkill() {
        return SkillConstants.MINING;
    }

    @Override
    public String getMessage() {
        return "You swing your pick at the rocks";
    }

    @Override
    public boolean hasRequiredItem() {
        final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = MiningDefinitions.PickaxeDefinitions.get(player, true);
        if (axe.isEmpty()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use."));
            return false;
        }
        this.tool = axe.get().getDefinition();
        return true;
    }
}
