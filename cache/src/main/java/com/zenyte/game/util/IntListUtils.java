package com.zenyte.game.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;

import java.util.stream.IntStream;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class IntListUtils {
    public static IntList modifiable(final int... values) {
        return IntArrayList.wrap(values);
    }

    public static IntLists.UnmodifiableList unmodifiable(final int... values) {
        return (IntLists.UnmodifiableList) IntLists.unmodifiable(IntArrayList.wrap(values));
    }

    public static IntLists.UnmodifiableList range(final int start, final int end) {
        final int[] list = IntStream.rangeClosed(start, end).toArray();
        return unmodifiable(list);
    }
}
