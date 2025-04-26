package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author Kris | 26/03/2019 15:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BottomlessCompostBucket extends ItemPlugin implements PairedItemOnItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int charges = item.getCharges();
            final int type = charges >> 16;
            assert type >= 0 && type <= 2;
            final int uses = charges & 65535;
            final String compostName = type == 0 ? "compost" : type == 1 ? "supercompost" : "ultracompost";
            player.sendMessage("Your bottomless compost bucket contains " + uses + " x " + compostName + ".");
        });
        bind("Fill/Check", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options(TITLE, new DialogueOption("Fill.", () -> getHandler("Fill").handle(player, item, container, slotId)), new DialogueOption("Check.", () -> getHandler("Check").handle(player, item, container, slotId)));
            }
        }));
        bind("Empty", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, "Emptying the bottomless compost bucket won't give you back any of the compost it holds - " + Colour.RED.wrap("it will all be lost forever") + ". Are you sure you wish to empty it?");
                options("Empty the bucket?", new DialogueOption("Yes.", () -> {
                    if (container.get(slotId) != item) {
                        return;
                    }
                    item.setCharges(0);
                    item.setId(22994);
                    player.getInventory().refreshAll();
                    setKey(25);
                }), new DialogueOption("No."));
                item(25, item, "You empty the bottomless compost bucket of all its compost.");
            }
        }));
        bind("Fill", (player, item, container, slotId) -> {
            final int charges = item.getCharges();
            if ((charges & 65535) >= 9999) {
                player.sendMessage("Your bottomless compost bucket cannot hold any more compost.");
                return;
            }
            final Inventory inventory = player.getInventory();
            final int compost = inventory.getAmountOf(6032) + inventory.getAmountOf(6033);
            final int supercompost = inventory.getAmountOf(6034) + inventory.getAmountOf(6035);
            final int ultracompost = inventory.getAmountOf(21483) + inventory.getAmountOf(21484);
            if (charges == 0) {
                int differenceCount = 0;
                if (compost != 0) {
                    differenceCount++;
                }
                if (supercompost != 0) {
                    differenceCount++;
                }
                if (ultracompost != 0) {
                    differenceCount++;
                }
                if (differenceCount == 0) {
                    player.sendMessage("You don't have any compost to fill your bottomless compost bucket with.");
                    return;
                }
                if (differenceCount == 1) {
                    fill(player, item, compost != 0 ? 0 : supercompost != 0 ? 1 : 2);
                } else {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            final ObjectArrayList<Dialogue.DialogueOption> list = new ObjectArrayList<DialogueOption>();
                            if (compost != 0) {
                                list.add(new DialogueOption("Compost", () -> fill(player, item, 0)));
                            }
                            if (supercompost != 0) {
                                list.add(new DialogueOption("Supercompost", () -> fill(player, item, 1)));
                            }
                            if (ultracompost != 0) {
                                list.add(new DialogueOption("Ultracompost", () -> fill(player, item, 2)));
                            }
                            options("Which type of compost would you like to fill your bucket with?", list.toArray(new DialogueOption[0]));
                        }
                    });
                }
            } else {
                final int type = item.getCharges() >> 16;
                if (type == 0) {
                    if (compost == 0) {
                        player.sendMessage("You haven't got any compost to fill the bucket with.");
                        return;
                    }
                } else if (type == 1) {
                    if (supercompost == 0) {
                        player.sendMessage("You haven't got any supercompost to fill the bucket with.");
                        return;
                    }
                } else {
                    if (ultracompost == 0) {
                        player.sendMessage("You haven't got any ultracompost to fill the bucket with.");
                        return;
                    }
                }
                fill(player, item, type);
            }
        });
    }

    private final void fill(final Player player, final Item item, final int type) {
        final int bucketType = item.getCharges() == 0 ? -1 : (item.getCharges() >> 16);
        if (bucketType != -1 && bucketType != type) {
            player.sendMessage("You can only fill your bottomless compost bucket with one type of compost at a time.");
            return;
        }
        final int existing = item.getCharges() & 65535;
        final Runnable runnable = () -> {
            player.getDialogueManager().finish();
            final String typeName = type == 0 ? "Compost" : type == 1 ? "Supercompost" : "Ultracompost";
            final Inventory inventory = player.getInventory();
            int compost = inventory.getAmountOf(type == 0 ? 6032 : type == 1 ? 6034 : 21483) + inventory.getAmountOf(type == 0 ? 6033 : type == 1 ? 6035 : 21484);
            if (compost == 1) {
                fill(player, item, type, 1);
                return;
            }
            if (existing + (compost * 2) > 10000) {
                compost = ((10000) - existing) / 2;
            }
            player.sendInputInt("How many " + typeName + " would you like to fill with? (1 - " + Math.min(5000, compost) + ")", value -> fill(player, item, type, Math.min(5000, value)));
        };
        if (existing != 0) {
            runnable.run();
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, "Filling the bucket with compost will make the bucket<br>untradeable. If emptied, it can be made tradeable once<br>more - however <col=cc0000>ALL COMPOST CONTAINED<br><col=cc0000>WITHIN IS LOST.</col>");
                options("Do you wish to proceed?", new DialogueOption("Yes.", runnable), new DialogueOption("No."));
            }
        });
    }

    private void fill(final Player player, final Item item, final int type, final int amount) {
        final Inventory inventory = player.getInventory();
        int requested = amount;
        final int existing = item.getCharges() & 65535;
        if (existing + (requested * 2) > 10000) {
            requested = ((10000) - existing) / 2;
        }
        requested -= inventory.deleteItem(type == 0 ? 6032 : type == 1 ? 6034 : 21483, requested).getSucceededAmount();
        requested -= inventory.deleteItem(type == 0 ? 6033 : type == 1 ? 6035 : 21484, requested).getSucceededAmount();
        if (requested == amount) {
            return;
        }
        item.setCharges(type << 16 | (existing + ((amount - requested) * 2)));
        item.setId(22997);
        inventory.refreshAll();
        player.getDialogueManager().start(new ItemChat(player, item, "You fill your bottomless compost bucket with " + (amount - requested) + " x " + (type == 0 ? "compost" : type == 1 ? "supercompost" : "ultracompost") + ". Your bottomless compost bucket now contains a total of " + (item.getCharges() & 65535) + " uses."));
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item bucket = from.getId() == 22994 || from.getId() == 22997 ? from : to;
        final Item compost = bucket == from ? to : from;
        final int compostId = compost.getId();
        fill(player, bucket, (compostId == 6032 || compostId == 6033) ? 0 : (compostId == 6034 || compostId == 6035) ? 1 : 2);
    }

    @Override
    public int[] getItems() {
        return new int[] {22994, 22997};
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(22994, 6032), ItemPair.of(22994, 6033), ItemPair.of(22994, 6034), ItemPair.of(22994, 6035), ItemPair.of(22994, 21483), ItemPair.of(22994, 21484), ItemPair.of(22997, 6032), ItemPair.of(22997, 6033), ItemPair.of(22997, 6034), ItemPair.of(22997, 6035), ItemPair.of(22997, 21483), ItemPair.of(22997, 21484)};
    }
}
