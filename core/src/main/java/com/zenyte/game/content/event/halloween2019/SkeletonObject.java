package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import mgi.custom.halloween.HalloweenObject;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SkeletonObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Search")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    final boolean contains = player.getInventory().containsItem(ItemId.LOCKPICK, 1);
                    plain("You rummage through the skeleton...").executeAction(() -> {
                        if (!contains) {
                            player.getInventory().addOrDrop(new Item(ItemId.LOCKPICK));
                        }
                    });
                    if (!contains) {
                        item(new Item(ItemId.LOCKPICK), "You find a lockpick.");
                    } else {
                        plain("You find nothing.");
                    }
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.SKELETON.getRepackedObject()};
    }
}
