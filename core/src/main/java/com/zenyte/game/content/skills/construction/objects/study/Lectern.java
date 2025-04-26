package com.zenyte.game.content.skills.construction.objects.study;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 25. veebr 2018 : 1:26.30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Lectern implements ObjectInteraction {

    private static final int[][] VALUES = new int[][] { new int[] { 0, 0 }, new int[] { 1, 0 }, new int[] { 0, 1 }, new int[] { 2, 0 }, new int[] { 0, 2 }, new int[] { 3, 0 }, new int[] { 0, 3 } };

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LECTERN_13642, ObjectId.LECTERN_13643, ObjectId.LECTERN_13644, ObjectId.LECTERN_13645, ObjectId.LECTERN_13646, ObjectId.LECTERN_13647, ObjectId.LECTERN_13648 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (construction.isBuildingMode()) {
            player.sendMessage("You can't do this in build mode.");
            return;
        }
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 79);
        final int[] values = VALUES[object.getId() - 13642];
        player.getVarManager().sendVar(261, values[0]);
        player.getVarManager().sendVar(262, values[1]);
    }
}
