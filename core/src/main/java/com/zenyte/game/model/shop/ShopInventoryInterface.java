package com.zenyte.game.model.shop;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;

import static com.zenyte.game.util.AccessMask.*;

/**
 * @author Kris | 23/11/2018 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShopInventoryInterface extends Interface {
    private static final int INVENTORY_OPTIONS_SCRIPT = 149;

    private Object[] getParams() {
        return new Object[] {getInterface().getId() << 16 | getComponent("Interact with item"), ContainerType.INVENTORY.getId(), 4, 7, 0, -1, "Value<col=ff9040>", "Sell 1<col=ff9040>", "Sell 5<col=ff9040>", "Sell 10<col=ff9040>", "Sell 50<col=ff9040>"};
    }

    @Override
    protected void attach() {
        put(0, "Interact with item");
    }

    @Override
    public void open(Player player) {
        final Object shopAttr = player.getTemporaryAttributes().get("Shop");
        if (!(shopAttr instanceof Shop)) {
            throw new RuntimeException("Unable to open the shop inventory directly.");
        }
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        player.getInterfaceHandler().sendInterface(getInterface());
        dispatcher.sendClientScript(INVENTORY_OPTIONS_SCRIPT, getParams());
        dispatcher.sendComponentSettings(getInterface(), getComponent("Interact with item"), 0, Container.getSize(ContainerType.INVENTORY), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP10);
    }

    @Override
    protected void build() {
        bind("Interact with item", ((player, slotId, itemId, option) -> {
            final Object shopAttr = player.getTemporaryAttributes().get("Shop");
            if (!(shopAttr instanceof Shop)) {
                return;
            }
            final Shop shop = (Shop) shopAttr;
            final Item item = player.getInventory().getItem(slotId);
            if (ItemDefinitions.isInvalid(itemId) || item == null || item.getId() != itemId) return;
            final ShopInventoryInterface.ItemOption op = ItemOption.of(option);
            if (op.is(ItemOption.EXAMINE)) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            if (op.is(ItemOption.VALUE)) {
                final int price = shop.getSellPrice(player, item.getId());
                if ((item.getId() == 995 || !item.isTradable()) && !shop.getName().toLowerCase().contains("grace's graceful clothing") && !shop.getName().toLowerCase().contains("prospector percy's nugget shop")) {
                    player.sendMessage("You can't sell this item.");
                } else if (price <= -1) {
                    player.sendMessage("You can't sell this item to this shop.");
                } else {
                    player.sendMessage(item.getName() + ": shop will buy for " + StringFormatUtil.format(price) + " " + shop.getCurrency() + ".");
                }
                return;
            }
            shop.sell(player, op, slotId);
        }));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SHOP_INVENTORY;
    }


    enum ItemOption {
        VALUE(1, -1), BUY_1(2, 1), BUY_5(3, 5), BUY_10(4, 10), BUY_50(5, 50), EXAMINE(10, -1);
        private static final ItemOption[] values = values();
        final int optionId;
        final int amount;

        ItemOption(final int optionId, final int amount) {
            this.optionId = optionId;
            this.amount = amount;
        }

        /**
         * Gets the ItemOption constant for the input integer option id.
         *
         * @param option the option id.
         * @return the ItemOption constant.
         */
        private static ItemOption of(final int option) {
            final ShopInventoryInterface.ItemOption constant = CollectionUtils.findMatching(values, value -> value.optionId == option);
            if (constant == null) {
                throw new IllegalArgumentException("Option cannot be " + option + ".");
            }
            return constant;
        }

        /**
         * Whether the input enum constant is identical to this option-wise, necessary because
         * {@code SkeletonEnum#equals(final Object other)} is final, thus preventing us from overriding it,
         * and because the options {@code CLEAR_ALL} and {@code WITHDRAW_ALL_BUT_1} are identical
         * option id wise.
         *
         * @param other the other constant to compare against.
         * @return whether the constants are identical option id wise.
         */
        public boolean is(final ItemOption other) {
            return other.optionId == optionId;
        }
    }
}
