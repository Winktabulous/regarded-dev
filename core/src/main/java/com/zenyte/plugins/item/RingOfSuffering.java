package com.zenyte.plugins.item;

import com.near_reality.game.content.imbue.DisimbueItemHandler;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.model.item.enums.ImbueableItem;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.IntArray;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

/**
 * @author Kris | 10. sept 2018 : 23:13:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class RingOfSuffering extends ItemPlugin implements PairedItemOnItemPlugin, ChargeExtension {
    private static final int RECOIL = 2550;
    private static final int NOTED_RECOIL = 2551;
    private static final int[] RECOILS = new int[] {RECOIL, NOTED_RECOIL};
    private static final int SUFFERING = 19550;
    private static final int SUFFERING_I = 19710;
    private static final int SUFFERING_R = 20655;
    private static final int SUFFERING_RI = 20657;
    private static final int[] SUFFERINGS = new int[] {SUFFERING, SUFFERING_I, SUFFERING_R, SUFFERING_RI};

    @Override
    public void handle() {
        bind("Uncharge", this::uncharge);
        bind("Recoil settings", (player, item, container, slotId) -> player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options(TITLE, new DialogueOption("Toggle recoil effect", () -> {
                    player.addAttribute("recoil effect", player.getBooleanAttribute("recoil effect") ? 0 : 1);
                    setKey(5);
                }), new DialogueOption("Discard recoil charges", () -> uncharge(player, item, container, slotId)), new DialogueOption("Cancel"));
                plain(5, !player.getBooleanAttribute("recoil effect") ? "Your ring of suffering will now reflect damage." : "Your ring of suffering will no longer reflect damage.");
            }
        }));
    }


    private void removeRecoil(final Player player, final Item item, final Container container, final int slotId) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, "Uncharging your <col=00080>" + item.getName() + "</col> will not grant you back the rings of recoil.");
                options("Are you sure you want to uncharge your <col=00080>" + item.getName() + "</col>?", "Yes, I'm " +
                        "sure.", "No.").onOptionOne(() -> {
                    if (container.get(slotId) == item) {
                        container.set(slotId, new Item(item.getId() == 20657 ? 19710 : item.getId() == 20655 ? 19550 : item.getId()));
                        container.refresh(player);
                    }
                    setKey(5);
                });
                item(5, item, "Your <col=00080>" + item.getName() + "</col> was successfully uncharged.");
            }
        });
    }

    private void uncharge(final Player player, final Item item, final Container container, final int slotId) {
        if (item.getId() == 20655 || item.getId() == 20657) {
            removeRecoil(player, item, container, slotId);
            return;
        }
        final ImbueableItem imbueable = ImbueableItem.get(item.getId());
        if (imbueable == null) {
            return;
        }
        final String name = item.getName();
        final Item uncharged = new Item(imbueable.getNormal());
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(new Item(imbueable.getImbued()), "Uncharging your <col=00080>" + name + "</col> will not grant you back the Imbue Token.");
                options("Are you sure you want to uncharge your <col=00080>" + name + "</col>?", "Yes, I'm sure.", "No.").onOptionOne(() -> {
                    if (container.get(slotId) == item) {
                        container.set(slotId, uncharged);
                        container.refresh(player);
                    }
                    setKey(5);
                });
                item(5, uncharged, "Your <col=00080>" + name + "</col> was successfully uncharged.");
            }
        });
    }


    @Override
    public int[] getItems() {
        return IntArray.of(SUFFERING_I, SUFFERING_R, SUFFERING_RI);
    }

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item recoil = ArrayUtils.contains(RECOILS, from.getId()) ? from : to;
        final int recoilId = recoil.getId();
        final Item suffering = recoil == from ? to : from;
        final int charges = suffering.getCharges();
        player.sendInputInt("How many rings do you wish to use?", value -> {
            final Inventory inventory = player.getInventory();
            final int maxAmount = (int) Math.ceil((100000 - charges) / 40.0F);
            if (maxAmount == 0) {
                player.sendMessage("Your ring of suffering is already fully charged.");
                return;
            }
            final int inInventory = (int) (inventory.getAmountOf(RECOILS) & 2147483647);
            final int amount = Math.min(maxAmount, Math.min(inInventory, value));
            if (amount == 0) {
                return;
            }
            int total = 0;
            final ContainerResult result = inventory.deleteItem(recoilId, amount);
            total += result.getSucceededAmount();
            if (result.getSucceededAmount() != amount) {
                final int remainder = amount - result.getSucceededAmount();
                final ContainerResult additionalResult = inventory.deleteItem(recoilId == RECOIL ? NOTED_RECOIL : RECOIL, remainder);
                total += additionalResult.getSucceededAmount();
            }
            int chargesToAdd = (total * 40);
            chargesToAdd -= 40 - player.getNumericAttribute("RING_OF_RECOIL").intValue();
            player.getAttributes().put("RING_OF_RECOIL", 40);
            suffering.setCharges(Math.min(100000, charges + chargesToAdd));
            player.sendMessage("You load your ring with " + total + " rings of recoil. It now has " + suffering.getCharges() + " recoil charges.");
            final int sufferingId = suffering.getId();
            if (sufferingId == SUFFERING) {
                suffering.setId(SUFFERING_R);
            } else if (sufferingId == SUFFERING_I) {
                suffering.setId(SUFFERING_RI);
            }
            player.getInventory().refreshAll();
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>();
        for (int recoil : RECOILS) {
            for (final int suffering : SUFFERINGS) {
                list.add(ItemPair.of(recoil, suffering));
            }
        }
        return list.toArray(new ItemPair[0]);
    }

    @Override
    public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
        if (item.getCharges() == 0) {
            return;
        }
        item.setCharges(Math.max(0, item.getCharges() - amount));
        if (item.getCharges() <= 0) {
            final int id = item.getId();
            if (id == SUFFERING_R) {
                item.setId(SUFFERING);
            } else if (id == SUFFERING_RI) {
                item.setId(SUFFERING_I);
            }
            player.sendMessage("<col=ff0000>Your ring of suffering has ran out of recoils.");
        }
        player.getInventory().refreshAll();
    }
}
