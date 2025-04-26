package com.zenyte.game.model.shop;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;

import java.util.Optional;

import static com.zenyte.game.util.AccessMask.*;

/**
 * @author Kris | 23/11/2018 17:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShopInterface extends Interface {
    private static final Object[] params = new Object[] {GameInterface.SHOP_INVENTORY.getId() << 16, ContainerType.INVENTORY.getId(), 4, 7, 0, -1, "Value<col=ff9040>", "Sell 1<col=ff9040>", "Sell 5<col=ff9040>", "Sell 10<col=ff9040>", "Sell 50<col=ff9040>"};

    private static Shop getShopAttr(final Player player) {
        final Object shopAttr = player.getTemporaryAttributes().get("Shop");
        if (!(shopAttr instanceof Shop)) {
            throw new RuntimeException("Unable to open the shop directly.");
        }
        return (Shop) shopAttr;
    }

    @Override
    protected void attach() {
        put(16, "Interact with item");
    }

    @Override
    public void open(Player player) {
        final Shop shop = getShopAttr(player);
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        final Shop.ShopContainer container = shop.getContainer();
        player.getInterfaceHandler().sendInterface(getInterface());
        GameInterface.SHOP_INVENTORY.open(player);
        dispatcher.sendClientScript(1074, shop.getName(), container.getType().getId());
        dispatcher.sendClientScript(149, params);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Interact with item"), 0, container.getContainerSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP6, CLICK_OP7, CLICK_OP10);
        shop.getPlayers().add(player);
        container.setFullUpdate(true);
        container.refresh(player);
    }

    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (replacement.isPresent()) {
            //Don't remove shop attribute if opening another shop because the attribute will be the new shop.
            if (replacement.get() == GameInterface.SHOP) {
                return;
            }
        }
        final Shop shop = getShopAttr(player);
        shop.getPlayers().remove(player);
        player.getTemporaryAttributes().remove("Shop");
    }

    @Override
    protected void build() {
        bind("Interact with item", ((player, interfaceSlotId, itemId, option) -> {
            final int slotId = interfaceSlotId - 1;
            final Shop shop = getShopAttr(player);
            final Item item = shop.getContainer().get(slotId);
            if (ItemDefinitions.isInvalid(itemId) || item == null || item.getId() != itemId) return;
            final int mobileBuyAmount = player.getNumericTemporaryAttribute("mobile buy amount").intValue();
            if (mobileBuyAmount != 0 && option == ItemOption.VALUE.optionId) {
                // if the first option (value) was replaced with buy-x shortcut on mobile
                option = mobileBuyAmount;
            }
            final ShopInterface.ItemOption op = ItemOption.of(option);
            if (op.is(ItemOption.EXAMINE)) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            if (op.is(ItemOption.VALUE) || op.is(ItemOption.SHIFTED_MOBILE_VALUE)) {
                final int price = shop.getBuyPrice(player, item.getId());
                if (price <= -1) {
                    player.sendMessage(item.getName() + ": currently unavailable.");
                } else if (price == 0) {
                    player.sendMessage(item.getName() + ": currently costs nothing.");
                } else {
                    player.sendMessage(item.getName() + ": currently costs " + StringFormatUtil.format(price) + " " + shop.getCurrency() + ".");
                }
                return;
            }
            if (op.is(ItemOption.BUY_X)) {
                player.sendInputInt("Enter amount:", amount -> shop.purchase(player, amount, slotId));
                return;
            }
            shop.purchase(player, op, slotId);
        }));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SHOP;
    }


    enum ItemOption {
        VALUE(1, -1), BUY_1(2, 1), BUY_5(3, 5), BUY_10(4, 10), BUY_50(5, 50), SHIFTED_MOBILE_VALUE(6, -1), BUY_X(7, -1), EXAMINE(10, -1);
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
            final ShopInterface.ItemOption constant = CollectionUtils.findMatching(values, value -> value.optionId == option);
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
