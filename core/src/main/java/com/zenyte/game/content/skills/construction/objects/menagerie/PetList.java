package com.zenyte.game.content.skills.construction.objects.menagerie;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 23:15.59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PetList implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.PET_LIST };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 210);
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i <= 40; i++) {
            b.append("Insured|");
        }
        player.getPacketDispatcher().sendClientScript(647, b.toString());
        player.getPacketDispatcher().sendComponentSettings(210, 3, 0, 40, AccessMask.CLICK_OP10);
    }
}
