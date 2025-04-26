package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 18/01/2019 16:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VyreWell implements ObjectAction {
    public static final int SET_RUNE_COUNT = 2700;
    public static final int SET_CHARGE_COUNT = 1000;
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch(option) {
            case "Fill":
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        plain("Pouring the power of 2700 blood runes into this well will add 1000 charges per set.").executeAction(() -> {
                            player.getDialogueManager().finish();
                            int availableCharges = 20;
                            final int setsOfBlood = player.getInventory().getAmountOf(565) / SET_RUNE_COUNT;
                            availableCharges = Math.min(setsOfBlood, availableCharges);
                            if (availableCharges <= 0) {
                                player.getDialogueManager().start(new PlainChat(player, "You haven\'t got enough " + ("blood runes") + " to fill the well."));
                                return;
                            }
                            chargeWell(player, availableCharges);
                        });
                    }
                });
                break;
            case "Check":
                {
                    final int existingCharges = player.getNumericAttribute("vyre well charges").intValue();
                    player.getDialogueManager().start(new PlainChat(player, "The well contains " + existingCharges + " charge" + (existingCharges == 1 ? "" : "s") + "."));
                    break;
                }
            case "Empty":
                {
                    final int existingCharges = Math.min(player.getNumericAttribute("vyre well charges").intValue(), 20);
                    if (existingCharges <= 0) {
                        // Should never happen, but refresh the varbit just in case if it does.
                        VarCollection.VYRE_WELL.updateSingle(player);
                        player.getDialogueManager().start(new PlainChat(player, "The well is already empty."));
                        return;
                    }
                    player.addAttribute("vyre well charges", 0);
                    VarCollection.VYRE_WELL.updateSingle(player);
                    player.getInventory().addItem(new Item(565, existingCharges * SET_RUNE_COUNT)).onFailure(item -> World.spawnFloorItem(item, player));
                    player.getDialogueManager().start(new PlainChat(player, "You empty the well."));
                    break;
                }
            case "Charge-at":
                final Inventory inventory = player.getInventory();
                for (int i = 0; i < 28; i++) {
                    final Item item = inventory.getItem(i);
                    if (item == null)
                        continue;
                    final int id = item.getId();
                    switch (id) {
                        case    ItemId.SCYTHE_OF_VITUR,
                                ItemId.SCYTHE_OF_VITUR_UNCHARGED,
                                ItemId.HOLY_SCYTHE_OF_VITUR,
                                ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED,
                                ItemId.SANGUINE_SCYTHE_OF_VITUR,
                                ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED,
                                ItemId.SANGUINESTI_STAFF,
                                ItemId.SANGUINESTI_STAFF_UNCHARGED,
                                ItemId.HOLY_SANGUINESTI_STAFF,
                                ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED
                                -> {
                            if (item.getCharges() <= 19000) {
                                appendCharges(player, item);
                                return;
                            }
                        }
                    }
                }
                player.sendMessage("You haven\'t got any weapons needing charging.");
                break;
        }
    }

    private static final void chargeWell(@NotNull final Player player, final int availableCharges) {
        player.sendInputInt("How many sets of 100 charges do you wish to add? (Up to " + availableCharges + ")", number -> {
            final int existingCharges = player.getNumericAttribute("vyre well charges").intValue();
            if (existingCharges >= 20) {
                player.getDialogueManager().start(new PlainChat(player, "The well is already full."));
                return;
            }
            int amount = Math.min(20 - existingCharges, number);
            final int setsOfBlood = player.getInventory().getAmountOf(565) / SET_RUNE_COUNT;
            amount = Math.min(setsOfBlood, amount);
            if (amount <= 0) {
                player.getDialogueManager().start(new PlainChat(player, "You haven\'t got enough " + "blood runes" + " to fill the well."));
                return;
            }
            player.getInventory().deleteItem(new Item(565, amount * SET_RUNE_COUNT));
            player.addAttribute("vyre well charges", existingCharges + amount);
            VarCollection.VYRE_WELL.updateSingle(player);
            player.getDialogueManager().start(new PlainChat(player, "You fill the well with " + amount + " charge" + (amount == 1 ? "" : "s") + "."));
        });
    }

    private static final void appendCharges(@NotNull final Player player, @NotNull final Item item) {
        final int id = item.getId();
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                boolean isScythe = switch (id) {
                    case ItemId.SCYTHE_OF_VITUR, ItemId.SCYTHE_OF_VITUR_UNCHARGED, ItemId.HOLY_SCYTHE_OF_VITUR, ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED, ItemId.SANGUINE_SCYTHE_OF_VITUR, ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED -> true;
                    default -> false;
                };
                item(item, "You lower your " + (isScythe ? "scythe" : "staff") + " into the well...").executeAction(() -> {
                    player.getDialogueManager().finish();
                    final int max = Math.min(player.getNumericAttribute("vyre well charges").intValue(), 20);
                    final int charges = (int) Math.min(max, Math.floor((20000 - item.getCharges()) / (float) SET_CHARGE_COUNT));
                    if (charges == 0) {
                        // Should never happen.
                        VarCollection.VYRE_WELL.updateSingle(player);
                        player.getDialogueManager().start(new PlainChat(player, "The well is already empty."));
                        return;
                    }
                    player.sendInputInt("How many sets of 1000 charges do you wish to apply? (Up to " + charges + ")", number -> {
                        final int existingCharges = Math.min(player.getNumericAttribute("vyre well charges").intValue(), 20);
                        final int amount = (int) Math.min(Math.min(number, existingCharges), Math.floor((20000 - item.getCharges()) / (float) SET_CHARGE_COUNT));
                        if (amount == 0) {
                            // Should never happen.
                            return;
                        }
                        switch (item.getId()) {
                            case ItemId.SCYTHE_OF_VITUR_UNCHARGED -> item.setId(ItemId.SCYTHE_OF_VITUR);
                            case ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED -> item.setId(ItemId.HOLY_SCYTHE_OF_VITUR);
                            case ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED -> item.setId(ItemId.SANGUINE_SCYTHE_OF_VITUR);
                            case ItemId.SANGUINESTI_STAFF_UNCHARGED -> item.setId(ItemId.SANGUINESTI_STAFF);
                            case ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED -> item.setId(ItemId.HOLY_SANGUINESTI_STAFF);
                        }
                        item.setCharges(item.getCharges() + (amount * SET_CHARGE_COUNT));
                        player.addAttribute("vyre well charges", existingCharges - amount);
                        VarCollection.VYRE_WELL.updateSingle(player);
                        player.getInventory().refreshAll();
                        player.getDialogueManager().start(new Dialogue(player) {

                            @Override
                            public void buildDialogue() {
                                item(item, "You apply " + StringFormatUtil.format((amount * SET_CHARGE_COUNT)) + " charges to your " + item.getName().toLowerCase() + ".");
                            }
                        });
                    });
                });
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.VYRE_WELL_32985, ObjectId.VYRE_WELL, 33085 };
    }
}
