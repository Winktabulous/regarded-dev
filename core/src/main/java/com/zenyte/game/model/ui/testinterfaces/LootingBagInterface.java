package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.containers.LootingBag;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 27-2-2019 | 16:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LootingBagInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Close");
        put(5, "Store/Examine");
        put(6, "Value");
    }

    @Override
    public void open(Player player) {
        final long value = player.getLootingBag().getTotalValue();
        final String text = value >= Integer.MAX_VALUE || value < 0 ? "Lots!" : player.getLootingBag().getContainer().isEmpty() ? "-" : StringFormatUtil.format(value) + " coins";
        final Object obj = player.getTemporaryAttributes().get("deposit_looting_bag");
        if (obj instanceof Item) {
            final Item bag = (Item) obj;
            if (player.getBooleanAttribute("received_looting_bag_warning")) {
                player.getPacketDispatcher().sendUpdateItemContainer(player.getInventory().getContainer(), ContainerType.LOOTING_BAG);
                player.getPacketDispatcher().sendClientScript(495, "Add to bag", 1);
                player.getInterfaceHandler().sendInterface(getInterface());
                player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Store/Examine"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP9);
                player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Value"), "Bag value: " + text);
                player.getPacketDispatcher().sendClientScript(1235, getValues(player.getInventory().getContainer()));
                int slot = player.getInventory().getContainer().getSlotOf(LootingBag.CLOSED.getId());
                if (slot == -1) {
                    slot = player.getInventory().getContainer().getSlotOf(LootingBag.OPENED.getId());
                }
                if (slot != -1) {
                    player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Store/Examine"), slot, slot, AccessMask.CLICK_OP9);
                }
            } else {
                LootingBag.sendWarning(player, bag);
            }
            return;
        }
        player.getLootingBag().refresh();
        player.getPacketDispatcher().sendClientScript(495, "Looting Bag", 0);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Store/Examine"), 0, 27, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Value"), "Value: " + text);
        if (!player.getLootingBag().getContainer().isEmpty()) {
            player.getPacketDispatcher().sendClientScript(1235, getValues(player.getLootingBag().getContainer()));
        }
    }

    @Override
    protected void build() {
        bind("Close", player -> close(player));
        bind("Store/Examine", (player, slotId, itemId, option) -> {
            if (option == 9 || option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }
            final Item item = player.getInventory().getItem(slotId);
            if (item == null) {
                return;
            }
            if (!item.isTradable()) {
                player.sendMessage("Only tradeable items can be put in the bag.");
                return;
            }
            if (!WildernessArea.isWithinWilderness(player)) {
                player.sendMessage("You can't put items in the looting bag unless you're in the Wilderness.");
                return;
            }
            final long value = player.getLootingBag().getTotalValue();
            final String text = value >= Integer.MAX_VALUE || value < 0 ? "Lots!" : player.getLootingBag().getContainer().isEmpty() ? "-" : StringFormatUtil.format(value) + " coins";
            if (option < 4) {
                final int amount = option == 1 ? 1 : option == 2 ? 5 : player.getInventory().getAmountOf(item.getId());
                player.getLootingBag().deposit(slotId, amount);
            } else {
                player.sendInputInt("Amount to deposit:", amount -> {
                    player.getLootingBag().deposit(slotId, amount);
                });
            }
            player.getPacketDispatcher().sendClientScript(1235, getValues(player.getInventory().getContainer()));
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Value"), "Bag value: " + text);
        });
    }

    private static Object[] getValues(final Container container) {
        final Integer[] values = new Integer[container.getContainerSize() + 1];
        values[0] = ContainerType.LOOTING_BAG.getId(); //inventory id
        for (int index = 1; index < values.length; index++) {
            final Item item = container.get(index - 1);
            values[index] = item == null ? -1 : item.getSellPrice();
        }
        return values;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.LOOTING_BAG;
    }
}
