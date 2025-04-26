package com.zenyte.game.content.partyroom;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Kris | 02/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyAnnouncementNPC extends NPC implements Spawnable {
    public PartyAnnouncementNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    private int ticks = Utils.random(150);

    @Override
    public void processNPC() {
        super.processNPC();
        final FaladorPartyRoom partyRoom = FaladorPartyRoom.getPartyRoom();
        final int frequency = partyRoom.getAnnouncementFrequency();
        if (frequency <= 0 || !partyRoom.getVariables().isAnnouncements()) {
            return;
        }
        if (ticks++ % frequency == 0) {
            final String announcement = partyRoom.pollAnnouncement();
            if (announcement != null) {
                setForceTalk(new ForceTalk(partyRoom.pollAnnouncementColour() + announcement));
            }
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 1315 || id == 1316 || (id >= 3269 && id <= 3283 && name.equals("guard")) || name.equals("banker");
    }
}
