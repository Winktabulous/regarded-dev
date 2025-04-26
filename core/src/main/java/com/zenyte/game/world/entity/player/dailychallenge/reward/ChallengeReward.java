package com.zenyte.game.world.entity.player.dailychallenge.reward;

import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 04/05/2019 | 13:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface ChallengeReward {
    void apply(final Player player);
    RewardType getType();
}
