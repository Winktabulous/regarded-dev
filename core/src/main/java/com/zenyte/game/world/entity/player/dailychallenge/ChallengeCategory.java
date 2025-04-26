package com.zenyte.game.world.entity.player.dailychallenge;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.utils.TextUtils;

import static com.zenyte.game.world.entity.player.dailychallenge.ChallengeDifficulty.*;

/**
 * @author Tommeh | 02/05/2019 | 22:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum ChallengeCategory {
    SKILLING(player -> {
        int skill = 0;
        while (Skills.isCombatSkill(skill) || skill == 22) {
            skill = Utils.random(22);
        }
        final int level = player.getSkills().getLevelForXp(skill);
        final ChallengeDifficulty difficulty = level >= 75 ? ELITE : level <= 74 && level >= 45 ? HARD : level <= 44 && level >= 20 ? MEDIUM : EASY;
        return new ChallengeDetails(difficulty, skill);
    }), COMBAT(player -> {
        final int combatLevel = player.getCombatLevel();
        final ChallengeDifficulty difficulty = combatLevel >= 3 && combatLevel <= 40 ? EASY : combatLevel >= 41 && combatLevel <= 75 ? MEDIUM : combatLevel >= 76 && combatLevel <= 100 ? HARD : ELITE;
        return new ChallengeDetails(difficulty, combatLevel);
    }), MINIGAME(player -> new ChallengeDetails(EASY));
    private final DetailsDetermination determination;
    public static final ChallengeCategory[] all = values();

    public ChallengeDetails getDetails(final Player player) {
        return determination.getDetails(player);
    }


    private interface DetailsDetermination {
        ChallengeDetails getDetails(final Player player);
    }

    @Override
    public String toString() {
        return TextUtils.formatName(name().toLowerCase());
    }

    ChallengeCategory(DetailsDetermination determination) {
        this.determination = determination;
    }
}
