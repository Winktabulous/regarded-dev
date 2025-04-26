package com.zenyte.game.content.boons.impl;

import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonPriceTable;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.content.skills.woodcutting.AxeDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

public class SwissArmyMan extends Boon {
    @Override
    public String name() {
        return "SwissArmyMan";
    }

    @Override
    public int price() {
        return BoonPriceTable.v_SwissArmyMan;
    }

    @Override
    public String description() {
        return "Provides the ability to chop, mine and catch without tools.";
    }

    @Override
    public int item() {
        return 1;
    }

    public static MiningDefinitions.PickaxeDefinitions pickaxeForLevel(Player p) {
        int level = p.getSkills().getLevel(SkillConstants.MINING);
        if(level < 20) {
            return MiningDefinitions.PickaxeDefinitions.IRON;
        } else if(level < 40) {
            return MiningDefinitions.PickaxeDefinitions.MITHRIL;
        } else if(level < 50) {
            return MiningDefinitions.PickaxeDefinitions.RUNE;
        } else if(level < 70) {
            return MiningDefinitions.PickaxeDefinitions.DRAGON;
        } else {
            return MiningDefinitions.PickaxeDefinitions.INFERNAL;
        }
    }

    public static AxeDefinitions getAxeForLevel(Player p) {
        int level = p.getSkills().getLevel(SkillConstants.WOODCUTTING);
        if(level < 20) {
            return AxeDefinitions.IRON;
        } else if(level < 40) {
            return AxeDefinitions.MITHRIL;
        } else if(level < 50) {
            return AxeDefinitions.RUNE;
        } else if(level < 70) {
            return AxeDefinitions.DRAGON;
        } else {
            return AxeDefinitions.INFERNAL;
        }
    }
}
