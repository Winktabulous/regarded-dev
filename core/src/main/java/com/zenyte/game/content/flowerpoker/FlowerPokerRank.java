package com.zenyte.game.content.flowerpoker;

import com.zenyte.game.world.entity.player.action.misc.MithrilSeedPlanting;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FlowerPokerRank {
    BURST("Burst", pairList -> has(pairList, 1, 5)),
    FOUR_OF_A_KIND("Four of a Kind", pairList -> has(pairList, 1, 4)),
    FULL_HOUSE("Full house", pairList -> has(pairList, 2, 5)),
    THREE_OF_A_KIND("Three of a Kind", pairList -> has(pairList, 1, 3)),
    TWO_DOUBLES("Two doubles", pairList -> has(pairList, 2, 4)),
    DOUBLE("Double", pairList -> has(pairList, 1, 2)),
    NONE("None", null);

    private final String rankName;
    private final Predicate<List<Pair>> predicate;

    private FlowerPokerRank(String rankName, Predicate<List<Pair>> predicate) {
        this.rankName = rankName;
        this.predicate = predicate;
    }

    public String getName() {
        return rankName;
    }

    private static boolean has(List<Pair> pairsList, int pairs, int count) {
        return pairsList.size() == pairs && pairsList.stream().mapToInt(p -> p.amount).sum() == count;
    }

    public static FlowerPokerRank rankOf(List<Pair> pairsList) {
        for (FlowerPokerRank rank : values()) {
            if (rank != NONE && rank.predicate.test(pairsList))
                return rank;
        }
        return FlowerPokerRank.NONE;
    }

    public static FlowerPokerRank bestOf(FlowerPokerRank o1, FlowerPokerRank o2) {
        if (o1.ordinal() < o2.ordinal()) {
            return o1;
        }
        if (o2.ordinal() < o1.ordinal()) {
            return o2;
        }
        return null;
    }

    public static FlowerPokerRank calculateRank(List<MithrilSeedPlanting.Flowers> flowersList) {
        List<Pair> pairs = Stream.of(MithrilSeedPlanting.Flowers.all).map(flower -> {
            int pairCount = (int) flowersList.stream().filter(f -> f == flower).count();
            return pairCount > 1 ? Pair.of(pairCount) : null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return FlowerPokerRank.rankOf(pairs);
    }
}
