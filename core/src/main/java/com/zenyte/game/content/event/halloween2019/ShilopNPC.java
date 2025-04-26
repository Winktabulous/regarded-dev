package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShilopNPC extends NPC {

    public ShilopNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    private static final ForceTalk[] messages = new ForceTalk[] {
            new ForceTalk("Help me!"), new ForceTalk("Get me out of here!"), new ForceTalk("I want my mommy!")
    };

    private int lastShout;

    @Override
    public void processNPC() {
        super.processNPC();
        if (--lastShout <= 0) {
            lastShout = Utils.random(10, 25);
            setForceTalk(Utils.getRandomElement(messages));
        }
    }
}
