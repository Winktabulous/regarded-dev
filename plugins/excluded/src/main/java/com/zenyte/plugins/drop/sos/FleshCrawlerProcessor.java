package com.zenyte.plugins.drop.sos;

/**
 * @author Kris | 31/01/2019 02:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FleshCrawlerProcessor extends SceptreProcessor {
    @Override
    public int getId() {
        return 9011;
    }

    @Override
    public int[] ids() {
        return new int[] {
                2498, 2499, 2500
        };
    }
}
