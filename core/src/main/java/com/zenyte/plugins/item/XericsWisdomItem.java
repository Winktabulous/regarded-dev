package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.testinterfaces.GameNoticeboardInterface;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.utils.TimeUnit;

/**
 * @author Kris | 30/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class XericsWisdomItem extends ItemPlugin {
    private static final Animation READ_ANIM = new Animation(7403);

    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> {
            final String name = item.getName();
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power?");
                    options("This will consume the scroll.", new DialogueOption("Learn " + name + ".", () -> readScroll(player, item, slotId)), new DialogueOption("Cancel."));
                }
            });
        });
    }

    private final void readScroll(final Player player, final Item item, final int slotId) {
        final Inventory inventory = player.getInventory();
        final Item inSlot = inventory.getItem(slotId);
        if (inSlot != item) {
            return;
        }
        final String name = inSlot.getName();
        player.lock(5);
        player.setAnimation(READ_ANIM);
        inventory.deleteItem(slotId, item);
        player.getVariables().setRaidsBoost(player.getVariables().getRaidsBoost() + (int) TimeUnit.HOURS.toTicks(5));
        GameNoticeboardInterface.refreshXericsWisdom(player);
        player.getDialogueManager().start(new ItemChat(player, item, "You study the scroll and gain 5 hours of: <col=FF0040>" + name + "</col>"));
    }

    @Override
    public int[] getItems() {
        return new int[] {19782};
    }
}
