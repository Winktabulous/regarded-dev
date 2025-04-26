package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.OptionDialogue;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 25. aug 2018 : 21:54:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Blowpipe extends ItemPlugin implements ChargeExtension {
    public static int getBlowpipeAmmunition(final Item blowpipe) {
        if (blowpipe == null) {
            return -1;
        }
        final int darts = blowpipe.getNumericAttribute("blowpipeDartType").intValue();
        if (darts == 0) {
            return -1;
        }
        return darts;
    }

    @Override
    public void handle() {
        bind("Unload", (player, item, slotId) -> {
            if (player.getDuel() != null && player.getDuel().inDuel()) {
                player.sendMessage("You can't do this during a duel.");
                return;
            }
            final int darts = item.getNumericAttribute("blowpipeDarts").intValue();
            final int type = item.getNumericAttribute("blowpipeDartType").intValue();
            if (darts == 0) {
                player.sendMessage("Your blowpipe is already empty from darts.");
                return;
            }
            if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(type, 1)) {
                player.sendMessage("You need some free space to unload the darts.");
                return;
            }
            final ContainerResult result = player.getInventory().addItem(new Item(type, darts));
            player.sendMessage("You unload the darts from your blowpipe.");
            if (result.getSucceededAmount() >= darts) {
                item.setAttribute("blowpipeDarts", 0);
                item.setAttribute("blowpipeDartType", 0);
            } else {
                item.setAttribute("blowpipeDarts", darts - result.getSucceededAmount());
            }
            if (isEmpty(item)) {
                item.setId(12924);
                player.getInventory().refresh(slotId);
            }
        });
        bind("Uncharge", (player, item, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    this.destroyItem(item, "Are you sure you want to uncharge it?", "If you uncharge the blowpipe, all scales and darts will fall out.").onYes(() -> {
                        if (player.getInventory().getItem(slotId) != item) return;
                        final int scales = item.getNumericAttribute("blowpipeScales").intValue();
                        final int darts = item.getNumericAttribute("blowpipeDarts").intValue();
                        final int type = item.getNumericAttribute("blowpipeDartType").intValue();
                        final Container container = player.getInventory().getContainer();
                        final int removedDarts = container.add(new Item(type, darts)).getSucceededAmount();
                        final int removedScales = container.add(new Item(12934, scales)).getSucceededAmount();
                        if (removedDarts >= darts) {
                            item.setAttribute("blowpipeDarts", 0);
                            item.setAttribute("blowpipeDartType", 0);
                        } else {
                            item.setAttribute("blowpipeDarts", darts - removedDarts);
                        }
                        item.setAttribute("blowpipeScales", Math.max(0, scales - removedScales));
                        if (isEmpty(item)) {
                            item.setId(12924);
                        } else {
                            player.sendMessage("Not enough free space in your inventory.");
                        }
                        container.setFullUpdate(true);
                        player.getInventory().refresh();
                    });
                }
            });
        });
        bind("Dismantle", (player, item, slotId) -> {
            if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(12934, 1)) {
                player.sendMessage("You need some free space to dismantle the blowpipe.");
                return;
            }
            player.getDialogueManager().start(new OptionDialogue(player, "Dismantle the blowpipe for 20,000 Zulrah's " +
                    "scales?", new String[] {"Yes, dismantle the blowpipe.", "No, don't dismantle it."}, new Runnable[] {() -> {
                if (player.getInventory().getItem(slotId) != item) return;
                player.getInventory().deleteItem(slotId, item);
                player.getInventory().addItem(12934, 20000).onFailure(it -> World.spawnFloorItem(it, player));
            }, null}));
        });
    }

    private boolean isEmpty(final Item blowpipe) {
        final int scales = blowpipe.getNumericAttribute("blowpipeScales").intValue();
        final int darts = blowpipe.getNumericAttribute("blowpipeDarts").intValue();
        return scales <= 0 && darts <= 0;
    }

    @Override
    public int[] getItems() {
        return new int[] {12924, 12926};
    }

    @Override
    public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
        if (item == null) {
            return;
        }
        int darts = item.getNumericAttribute("blowpipeDarts").intValue();
        final int bpstep = player.getNumericTemporaryAttribute("blowpipestep").intValue();
        if (bpstep == 3) {
            player.getTemporaryAttributes().remove("blowpipestep");
        } else {
            int scales = item.getNumericAttribute("blowpipeScales").intValue();
            if (scales > 0) {
                scales--;
            }
            item.setAttribute("blowpipeScales", scales);
            player.getTemporaryAttributes().put("blowpipestep", bpstep + 1);
        }
        if (amount == 2 && --darts <= 0) {
            item.setAttribute("blowpipeDartType", 0);
        }
        item.setAttribute("blowpipeDarts", darts);
        if (isEmpty(item)) {
            item.setId(12924);
            wrapper.refresh(slotId);
            player.getEquipment().refresh(EquipmentSlot.WEAPON.getSlot());
        }
    }

    @Override
    public void checkCharges(final Player player, final Item item) {
        final int darts = item.getNumericAttribute("blowpipeDarts").intValue();
        final int scales = item.getNumericAttribute("blowpipeScales").intValue();
        final int dartId = item.getNumericAttribute("blowpipeDartType").intValue();
        final String percentage = FORMATTER.format(scales / (float) 16383 * 100).replace(".0", "");
        player.sendMessage("Darts: <col=006000>" + (darts == 0 ? "None" : (ItemDefinitions.getOrThrow(dartId).getName() + " x " + darts + ".")) + " </col>Scales: <col=006000>" + percentage + "%.");
    }
}
