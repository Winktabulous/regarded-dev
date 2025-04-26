package com.zenyte.game.content.chompy.plugins;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 21/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SwampToadNPC extends NPC implements Spawnable {
    public SwampToadNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    private static final ForceTalk[] forceChats = new ForceTalk[] {
            new ForceTalk("Ribbit!"),
            new ForceTalk("Croak!")
    };

    private long chatDelay;

    @Override
    public void processNPC() {
        super.processNPC();
        if (!isDead() && chatDelay < Utils.currentTimeMillis() && Utils.random(100) == 0) {
            chatDelay = Utils.currentTimeMillis() + 5000;
            setForceTalk(forceChats[Utils.random(forceChats.length - 1)]);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 1473;
    }
}
