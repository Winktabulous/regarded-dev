package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.object.CorporealBeastPrivatePortalOA.InstanceLeaveDialogue;

/**
 * @author Kris | 10. veebr 2018 : 2:56.55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CorporealBeastCaveExitOA implements ObjectAction {

    private static final int PUBLIC_HASH = 585421086;

    public static final Location OUTSIDE = new Location(3206, 3681, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean publicCavern = object.getPositionHash() == PUBLIC_HASH;
        if (publicCavern) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("This exit leads to the Wilderness, are you sure?", new DialogueOption("Yes.", () -> leave(player)), new DialogueOption("No."));
                }
            });
            return;
        }
        player.getDialogueManager().start(new InstanceLeaveDialogue(player, OUTSIDE));
    }

    private void leave(final Player player) {
        player.lock(1);
        player.setLocation(OUTSIDE);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_EXIT };
    }
}
