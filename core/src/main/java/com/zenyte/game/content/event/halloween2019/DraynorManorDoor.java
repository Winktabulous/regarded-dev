package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;

/**
 * @author Kris | 01/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DraynorManorDoor implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("The door seems to be locked.");
                player("That's odd... This door is never locked. Perhaps there's a key somewhere around here.");
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                HalloweenObject.DRAYNOR_MANOR_LEFT_DOOR.getRepackedObject(), HalloweenObject.DRAYNOR_MANOR_RIGHT_DOOR.getRepackedObject()
        };
    }
}
