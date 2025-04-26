package com.zenyte.game.model.item.containers;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 27-2-2019 | 18:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LootingBag extends ItemPlugin {

    public static final Item OPENED = new Item(ItemId.LOOTING_BAG_22586);
    public static final Item CLOSED = new Item(ItemId.LOOTING_BAG);

    @Override
    public void handle() {
        bind("Open", (player, item, slotId) -> {
            player.getLootingBag().setOpen(true);
            player.getInventory().set(slotId, OPENED);
            player.sendMessage("You open your looting bag, ready to fill it.");
        });
        bind("Close", (player, item, slotId) -> {
            player.getLootingBag().setOpen(false);
            player.getInventory().set(slotId, CLOSED);
            player.sendMessage("You close your looting bag.");
        });
        bind("Check", (player, item, slotId) -> {
            player.getTemporaryAttributes().remove("deposit_looting_bag");
            GameInterface.LOOTING_BAG.open(player);
        });
        bind("Deposit", (player, item, slotId) -> {
            player.addTemporaryAttribute("deposit_looting_bag", item);
            GameInterface.LOOTING_BAG.open(player);
        });
        bind("Settings", (player, item, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("When using items on the bag...", "... ask how many to store.", "... always store as many as possible.")
                        .onOptionOne(() -> {
                            player.addAttribute("looting_bag_amount_prompt", 1);
                            player.sendMessage("When using items on the bag, you will be asked how many of that item you wish to store in the bag.");
                        })
                        .onOptionTwo(() -> {
                            player.addAttribute("looting_bag_amount_prompt", 0);
                            player.sendMessage("When using items on the bag, you will immediately store as many of that item as possible in the bag.");
                         });
            }
        }));
    }

    public static void sendWarning(final Player player, final Item bag) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(bag, "Items can be withdrawn from the bag <col=b30000>only at a bank</col>. The bag and its contents are <col=b30000>always lost on death</col>.");
                options("Do you still wish to store items in the bag?", "Yes, I understand how it works.", "No, cancel.")
                        .onOptionOne(() -> {
                            player.addAttribute("received_looting_bag_warning", 1);
                            GameInterface.LOOTING_BAG.open(player);
                        });
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.LOOTING_BAG, ItemId.LOOTING_BAG_22586 };
    }
}
