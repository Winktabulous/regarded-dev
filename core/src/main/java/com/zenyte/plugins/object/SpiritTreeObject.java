package com.zenyte.plugins.object;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.SpiritTree;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SpiritTreeD;
import com.zenyte.plugins.dialogue.SpiritTreeMenuD;

public class SpiritTreeObject implements ObjectAction {

    private final NPC npc = new NPC(4982, new Location(0), Direction.SOUTH, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final SpiritTree tree = SpiritTree.getTree(player);
        if (option.equals("Talk-to")) {
            if (tree == SpiritTree.TREE_GNOME_VILLAGE) {
                if (TreasureTrail.talk(player, npc)) {
                    return;
                }
            }
            player.getDialogueManager().start(new SpiritTreeD(player));
        } else if (option.equals("Travel")) {
            player.getDialogueManager().start(new SpiritTreeMenuD(player));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 1293, 1294, 1295 };
    }
}
