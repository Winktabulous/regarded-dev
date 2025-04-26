package com.zenyte.game.world.entity.player.dailychallenge.reward.impl;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import com.zenyte.game.world.entity.player.dailychallenge.reward.RewardType;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 04/05/2019 | 14:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ExperienceReward implements ChallengeReward {
    private final int skill;
    private final int experience;

    @Override
    public void apply(Player player) {
        int givenXp = experience * player.getExperienceRate(skill);
        if (player.getSkillingXPRate() == 10) {
            givenXp *= 1.15;
        } else if (player.getSkillingXPRate() == 5) {
            givenXp *= 1.25;
        }
        player.getSkills().addXp(skill, experience);
        player.sendMessage("<col=ce8500><shad=000000>You have been awarded with " + StringFormatUtil.format(givenXp) + " XP in " + Skills.getSkillName(skill) + "!");
    }

    public int getExperience(final Player player) {
        int experience = this.experience;
        if (player.getSkillingXPRate() == 10) {
            experience *= 1.15;
        } else if (player.getSkillingXPRate() == 5) {
            experience *= 1.25;
        }
        return experience;
    }

    @Override
    public RewardType getType() {
        return RewardType.EXPERIENCE;
    }

    public ExperienceReward(int skill, int experience) {
        this.skill = skill;
        this.experience = experience;
    }

    public int getSkill() {
        return skill;
    }
}
