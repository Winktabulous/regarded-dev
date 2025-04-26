package com.zenyte.game.world.entity.player.dailychallenge.challenge;

import com.zenyte.game.world.entity.player.dailychallenge.ChallengeCategory;
import com.zenyte.game.world.entity.player.dailychallenge.ChallengeDifficulty;
import com.zenyte.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.world.entity.player.dailychallenge.ChallengeDifficulty.EASY;

/**
 * @author Tommeh | 04/05/2019 | 13:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum CombatChallenge implements DailyChallenge {

    KILL_5_BEARS("Kill 5 Bears", EASY, 5, "bear");

    private final String name;
    private final ChallengeDifficulty difficulty;
    private final ChallengeReward[] rewards;
    private final int length;
    private final String npc;

    CombatChallenge(final String name, final ChallengeDifficulty difficulty, final int length, final String npc, final ChallengeReward... rewards) {
        this.name = name;
        this.difficulty = difficulty;
        this.length = length;
        this.npc = npc;
        this.rewards = rewards;
    }

    public static final CombatChallenge[] all = values();

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public ChallengeCategory getCategory() {
        return ChallengeCategory.COMBAT;
    }

    @NotNull
    @Override
    public ChallengeDifficulty getDifficulty() {
        return difficulty;
    }

    @NotNull
    @Override
    public ChallengeReward[] getRewards() {
        return rewards;
    }

    @Override
    public int getLength() {
        return length;
    }

    CombatChallenge(String name, ChallengeDifficulty difficulty, ChallengeReward[] rewards, int length, String npc) {
        this.name = name;
        this.difficulty = difficulty;
        this.rewards = rewards;
        this.length = length;
        this.npc = npc;
    }

    public String getNpc() {
        return npc;
    }
}