package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Kris | 27/04/2019 01:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShiloVillageCart implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Board")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Travel to the north of the island?", new DialogueOption("Yes. (200 gp)", () -> {
                        player.getDialogueManager().finish();
                        travel(player);
                    }), new DialogueOption("Nevermind."));
                }
            });
        } else if (option.equals("Pay-fare")) {
            travel(player);
        }
    }

    private static final void travel(final Player player) {
        if (!player.getInventory().containsItem(new Item(995, 200))) {
            player.getDialogueManager().start(new ItemChat(player, new Item(995, 200), "You need at least 200 gold to use the cart."));
            return;
        }
        player.getInventory().deleteItem(new Item(995, 200));
        new FadeScreen(player, () -> player.setLocation(new Location(2777, 3209, 0))).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TRAVEL_CART_2265 };
    }
}
