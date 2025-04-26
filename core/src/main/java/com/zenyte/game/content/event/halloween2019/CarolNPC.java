package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Emote;

/**
 * @author Kris | 01/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CarolNPC extends NPC {
    public CarolNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    private int nextCry;

    @Override
    public void processNPC() {
        super.processNPC();
        if (nextCry-- <= 0) {
            setAnimation(Emote.CRY.getAnimation());
            nextCry = Utils.random(5, 10);
        }
    }

}
