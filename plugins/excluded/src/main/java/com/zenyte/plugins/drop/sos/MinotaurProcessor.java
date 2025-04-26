package com.zenyte.plugins.drop.sos;

/**
 * @author Kris | 31/01/2019 01:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MinotaurProcessor extends SceptreProcessor {

    @Override
    public int getId() {
        return 9007;
    }

    @Override
    public int[] ids() {
        return new int[] {
                2481, 2482, 2483
        };
    }
}
