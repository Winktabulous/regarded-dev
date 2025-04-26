package com.zenyte.game.world.entity.player.dailychallenge.challenge;

import com.zenyte.game.world.entity.player.dailychallenge.ChallengeCategory;
import com.zenyte.game.world.entity.player.dailychallenge.ChallengeDifficulty;
import com.zenyte.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 02/05/2019 | 22:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface DailyChallenge {

    @NotNull
    String getName();

    @NotNull
    ChallengeCategory getCategory();

    @NotNull
    ChallengeDifficulty getDifficulty();

    @NotNull
    ChallengeReward[] getRewards();

    int getLength();

    String name();
}
