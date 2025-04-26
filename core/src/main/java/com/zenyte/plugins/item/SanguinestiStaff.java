package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 18/01/2019 18:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SanguinestiStaff extends ItemPlugin implements ChargeExtension {
    @Override
    public void handle() {
        bind("Charge", (player, item, slotId) -> {
            int current = item.getCharges();
            int existing = (int) Math.floor((20000 - item.getCharges()) / 100.0F);
            player.sendInputInt("How many sets of 100 charges do you wish to add? (max " + existing + ")", number -> {

                int amount = Math.min(existing, number);
                final int setsOfBlood = player.getInventory().getAmountOf(565) / 300;
                amount = Math.min(setsOfBlood, amount);
                if (amount <= 0) {
                    player.getDialogueManager().start(new PlainChat(player, "You haven\'t got enough " +  "blood runes" + " to charge your staff."));
                    return;
                }
                player.getInventory().deleteItem(new Item(565, amount * 300));
                item.setCharges(item.getCharges() + (amount * 100));
                switch (item.getId()) {
                    case ItemId.SANGUINESTI_STAFF_UNCHARGED -> item.setId(ItemId.SANGUINESTI_STAFF);
                    case ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED -> item.setId(ItemId.HOLY_SANGUINESTI_STAFF);
                }
                player.getInventory().refreshAll();
                player.getDialogueManager().start(new PlainChat(player, "You add " + amount * 100 + " charges" + " to your staff."));
            });
        });
        bind("Dismantle", (player, item, slotId) -> {
            if (player.getInventory().containsItem(item) && player.getInventory().hasSpaceFor(ItemId.HOLY_ORNAMENT_KIT, ItemId.SANGUINESTI_STAFF_UNCHARGED)) {
                player.getInventory().deleteItem(item);
                player.getInventory().addItem(new Item(ItemId.HOLY_ORNAMENT_KIT));
                player.getInventory().addItem(new Item(ItemId.SANGUINESTI_STAFF_UNCHARGED));
                player.sendMessage("You dismantle your staff.");
            } else {
                player.sendMessage("Not enough space in your inventory.");
            }
        });
        bind("Uncharge", (player, item, slotId) -> {
            if (player.getDuel() != null && player.getDuel().inDuel()) {
                player.sendMessage("You can't do this during a duel.");
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("You will not be able to re-obtain your vials of blood and blood runes.");
                    options("Uncharge the staff?<br><col=ff0000>Alternatively you can unload the charges back into the well.", new DialogueOption("Yes.", () -> {
                        //Verify the existence of the item.
                        if (player.getInventory().getItem(slotId) == item) {
                            item.setCharges(0);
                            if (item.getId() == ItemId.SANGUINESTI_STAFF) {
                                item.setId(ItemId.SANGUINESTI_STAFF_UNCHARGED);
                            } else if (item.getId() == ItemId.HOLY_SANGUINESTI_STAFF) {
                                item.setId(ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED);
                            }
                            player.getInventory().refresh(slotId);
                            player.sendMessage("You uncharge your staff.");
                        }
                    }), new DialogueOption("No."));
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.SANGUINESTI_STAFF_UNCHARGED, ItemId.SANGUINESTI_STAFF, ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED, ItemId.HOLY_SANGUINESTI_STAFF };
    }

    @Override
    public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
        item.setCharges(Math.max(0, item.getCharges() - amount));
        if (item.getCharges() <= 0) {
            if (item.getId() == ItemId.SANGUINESTI_STAFF) {
                item.setId(ItemId.SANGUINESTI_STAFF_UNCHARGED);
            } else if (item.getId() == ItemId.HOLY_SANGUINESTI_STAFF) {
                item.setId(ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED);
            }
            player.getEquipment().refreshAll();
            player.getCombatDefinitions().refresh();
        }
    }
}
