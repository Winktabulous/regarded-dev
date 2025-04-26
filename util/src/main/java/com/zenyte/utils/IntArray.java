package com.zenyte.utils;

/**
 * @author Kris | 22/10/2018 20:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IntArray {
    public static int[] of(final int... values) {
        return values;
    }

    public static int[] range(final int from, final int to) {
        if (from > to) throw new RuntimeException("'From' has to be smaller or equal to 'to'! From: " + from + ", to: " + to);
        final int[] array = new int[1 + to - from];
        for (int i = array.length - 1; i >= 0; i--) {
            array[i] = from + i;
        }
        return array;
    }
}
