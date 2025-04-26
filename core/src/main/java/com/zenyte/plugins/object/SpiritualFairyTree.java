package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.FairyRing;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SpiritTreeMenuD;
import com.zenyte.plugins.interfaces.FairyRingCombination;
import com.zenyte.plugins.interfaces.FairyRingLog;

/**
 * @author Kris | 12/04/2019 22:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpiritualFairyTree implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Tree")) {
            player.getDialogueManager().start(new SpiritTreeMenuD(player));
        } else if (option.equalsIgnoreCase("Ring-zanaris")) {
            FairyRing.handle(player, object, FairyRing.codes.get("BKS"));
        } else if (option.equalsIgnoreCase("Ring-configure")) {
            FairyRingCombination.open(player, object);
            FairyRingLog.open(player);
        } else if (option.equalsIgnoreCase("Ring-last-destination")) {
            final int number = player.getNumericAttribute("lastFairyRing").intValue();
            if (number == 0) {
                player.sendFilteredMessage("You haven't used the fairy ring teleportation system yet.");
                return;
            }
            FairyRing.handle(player, object, FairyRing.getRing(number));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SPIRITUAL_FAIRY_TREE_35003 };
    }
}
