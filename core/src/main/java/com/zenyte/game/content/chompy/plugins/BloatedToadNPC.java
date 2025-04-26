package com.zenyte.game.content.chompy.plugins;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BloatedToadNPC extends NPC {
    public BloatedToadNPC(Location tile) {
        super(BloatedToad.BLOATED_TOAD_NPC_ID, tile, Direction.SOUTH, 0);
    }

    private int counter = 100;

    @Override
    public void processNPC() {
        super.processNPC();
        if (--counter <= 0) {
            finish();
        }
    }
}
