package com.zenyte.game.world.entity.player.teleportsystem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Objects;

/**
 * @author Kris | 29/03/2019 18:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TeleportScrollPlugin extends ItemPlugin {
    private static final Animation READ_ANIM = new Animation(7403);

    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> {
            final int id = item.getId();
            final TeleportScroll scroll = Objects.requireNonNull(TeleportScroll.map.get(id));
            final String name = scroll.getName().replaceAll(" scroll", "");
            if (player.getTeleportManager().getUnlockedTeleports().contains(scroll.getTeleport())) {
                player.getDialogueManager().start(new PlainChat(player, "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods. However there's nothing more for you to learn."));
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "You can make out some faded words on the ancient parchment. It appears to be an archaic invocation of the gods! Would you like to absorb its power?");
                    options("This will consume the scroll.", new DialogueOption("Learn " + name + ".", () -> readScroll(player, item, slotId)), new DialogueOption("Cancel."));
                }
            });
        });
    }

    private void readScroll(final Player player, final Item item, final int slotId) {
        final Inventory inventory = player.getInventory();
        final Item inSlot = inventory.getItem(slotId);
        if (inSlot != item) {
            return;
        }
        final int id = item.getId();
        final TeleportScroll scroll = Objects.requireNonNull(TeleportScroll.map.get(id));
        final String name = scroll.getName().replaceAll(" teleport scroll", "");
        player.lock(5);
        player.setAnimation(READ_ANIM);
        inventory.deleteItem(slotId, item);
        player.getTeleportManager().getUnlockedTeleports().add(scroll.getTeleport());
        player.getDialogueManager().start(new ItemChat(player, item, "You study the scroll and learn a new teleport: <col=FF0040>" + name + "</col>"));
    }

    @Override
    public int[] getItems() {
        return TeleportScroll.map.keySet().toIntArray();
    }
}
