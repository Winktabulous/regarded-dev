package com.zenyte.game.content.skills.farming;

import org.apache.commons.lang3.ArrayUtils;

import java.util.stream.IntStream;

/**
 * @author Kris | 23/02/2019 11:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CompostBinType {

    COMPOST(FarmingProduct.COMPOST, IntStream.range(1, 16), IntStream.range(16, 31), IntStream.range(80, 95),
            IntStream.range(63, 78), IntStream.range(78, 93)),
    SUPERCOMPOST(FarmingProduct.SUPERCOMPOST, IntStream.range(33, 48), IntStream.range(48, 63), IntStream.range(112, 127),
            IntStream.range(161, 176), IntStream.range(100, 115)),
    ULTRACOMPOST(FarmingProduct.ULTRACOMPOST, IntStream.empty(), IntStream.range(176, 191), IntStream.empty(),
            IntStream.empty(), IntStream.range(191, 206)),
    TOMATOES(FarmingProduct.TOMATOES, IntStream.range(129, 144), IntStream.range(144, 159), IntStream.range(208, 223),
            IntStream.range(223, 238), IntStream.range(207, 222));

    private final FarmingProduct product;
    private final int[] compostableItems, compost, readyCompost;

    CompostBinType(final FarmingProduct product, final IntStream compostableItems, final IntStream compost,
                   final IntStream readyCompost, final IntStream bigCompostableItems, final IntStream bigCompost) {
        this.product = product;
        this.compostableItems = ArrayUtils.addAll(compostableItems.toArray(), bigCompostableItems.toArray());
        this.compost = ArrayUtils.addAll(compost.toArray(), bigCompost.toArray());
        this.readyCompost = readyCompost.toArray();
    }

    public FarmingProduct getProduct() {
        return product;
    }

    public int[] getCompostableItems() {
        return compostableItems;
    }

    public int[] getCompost() {
        return compost;
    }

    public int[] getReadyCompost() {
        return readyCompost;
    }
}
