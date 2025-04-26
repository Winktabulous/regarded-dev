package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;

/**
 * @author Kris | 16. nov 2017 : 2:31.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SmallScavengerRoom extends ScavengerRoom {

    public SmallScavengerRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Small Scavenger room";
    }

}
