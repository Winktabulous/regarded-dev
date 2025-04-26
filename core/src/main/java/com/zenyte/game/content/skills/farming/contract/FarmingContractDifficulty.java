package com.zenyte.game.content.skills.farming.contract;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/7/2020
 */
public enum FarmingContractDifficulty {
    EASY(0, 45), MEDIUM(1, 65), HARD(2, 85);
    private static final ObjectList<FarmingContractDifficulty> difficulties = ObjectArrayList.wrap(values());
    private final int tierBonus;
    private final int levelRequirement;

    public static FarmingContractDifficulty getByName(@NotNull final String name) {
        for (FarmingContractDifficulty difficulty : difficulties) {
            if (difficulty.name().equals(name)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Tried getting a non-existent contract difficulty with name: " + name);
    }

    public FarmingContractDifficulty decrease(@Positive final int amount) {
        final int newIndex = Math.max(0, difficulties.indexOf(this) - amount);
        return difficulties.get(newIndex);
    }

    FarmingContractDifficulty(int tierBonus, int levelRequirement) {
        this.tierBonus = tierBonus;
        this.levelRequirement = levelRequirement;
    }

    public int getTierBonus() {
        return tierBonus;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }
}
