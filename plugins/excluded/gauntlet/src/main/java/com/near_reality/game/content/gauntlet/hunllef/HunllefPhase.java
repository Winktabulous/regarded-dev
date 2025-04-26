package com.near_reality.game.content.gauntlet.hunllef;

import it.unimi.dsi.fastutil.Pair;

import java.util.List;

/**
 * @author Andys1814.
 * @since 2/7/2022.
 */
public enum HunllefPhase {
    ONE(1),
    TWO(2),
    THREE(3);

    private final int numTornadoes;

    HunllefPhase(int numTornadoes) {
        this.numTornadoes = numTornadoes;
    }

    public int getNumTornadoes() {
        return numTornadoes;
    }

    public List<List<Pair<Integer, Integer>>> getTilePatterns() {
        switch (this) {
            case ONE:
                return HunllefTilePatterns.EASY_PATTERNS;
            case TWO:
                List<List<Pair<Integer, Integer>>> patterns = HunllefTilePatterns.EASY_PATTERNS;
                patterns.addAll(HunllefTilePatterns.MEDIUM_PATTERNS);
                return patterns;
            case THREE:
                return HunllefTilePatterns.HARD_PATTERNS;
        }
        throw new IllegalArgumentException("bruh");
    }
}
