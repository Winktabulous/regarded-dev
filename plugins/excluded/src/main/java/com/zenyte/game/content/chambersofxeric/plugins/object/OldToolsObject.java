package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 06/07/2019 04:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OldToolsObject implements ObjectAction {
    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some free inventory space to take the tools.");
                return;
            }
            if (player.getInventory().containsItem(ItemId.RAKE, 1) && player.getInventory().containsItem(ItemId.SPADE, 1) && player.getInventory().containsItem(ItemId.SEED_DIBBER, 1)) {
                player.sendMessage("You have nothing more to take from here.");
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Select a Tool", new DialogueOption("Rake.", () -> give(player, ItemId.RAKE)),
                            new DialogueOption("Spade.", () -> give(player, ItemId.SPADE)),
                            new DialogueOption("Seed dibber.", () -> give(player, ItemId.SEED_DIBBER)),
                            new DialogueOption("Take all.", () -> give(player, -1)),
                            new DialogueOption("Cancel."));
                }
            });
        });
    }

    private final void give(@NotNull final Player player, final int item) {
        give(player, item, true);
    }

    private final void give(@NotNull final Player player, final int item, final boolean message) {
        player.getDialogueManager().finish();
        if (item == -1) {
            give(player, ItemId.RAKE, false);
            give(player, ItemId.SPADE, false);
            give(player, ItemId.SEED_DIBBER, false);
            return;
        }
        if (player.getInventory().containsItem(item, 1)) {
            player.getDialogueManager().start(new ItemChat(player, new Item(item), "You already have a " + ItemDefinitions.getOrThrow(item).getName().toLowerCase() + "."));
            return;
        }
        player.getInventory().addOrDrop(new Item(item));
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{
                29771
        };
    }
}
