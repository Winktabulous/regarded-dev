package com.zenyte.game.content.minigame.warriorsguild.plugins;

import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;

/**
 * @author Kris | 18/04/2019 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WarriorsGuildBottomCyclopsProcessor extends DropProcessor {

    private static final int[] bottomFloorCyclops = new int[] {
            2137, 2139, 2138, 2142, 2140, 2141
    };

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(12954, 1, 1, 50));
    }

    @Override
    public int[] ids() {
        return bottomFloorCyclops;
    }
}
