package com.zenyte.plugins.drop.sos;

/**
 * @author Kris | 31/01/2019 02:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AnkouProcessor extends SceptreProcessor {
    @Override
    public int getId() {
        return 9008;
    }

    @Override
    public int[] ids() {
        return new int[] {
                2514, 2515, 2516, 2517, 2518, 2519
        };
    }
}
