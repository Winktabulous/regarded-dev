package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.utilities.CollectionUtils;

import java.util.Optional;

import static com.zenyte.game.util.AccessMask.*;

/**
 * @author Kris | 21/10/2018 08:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BankDepositInterface extends Interface {
    private static final Animation DEPOSIT_ANIM = new Animation(834);
    private static final int SCRIPT_RESET_INVENTORY_TAB_SPRITE = 915;
    private static final int INVENTORY_TAB = 3;

    static {
        VarManager.appendPersistentVarp(1794);
        VarManager.appendPersistentVarbit(4430);
    }

    @Override
    protected void attach() {
        put(2, "Interact with Item");
        put(4, "Deposit inventory");
        put(6, "Deposit worn items");
        put(8, "Deposit loot");
        put(11, "Default Quantity 1");
        put(13, "Default Quantity 5");
        put(15, "Default Quantity 10");
        put(17, "Default Quantity X");
        put(19, "Default Quantity All");
    }

    @Override
    public void open(Player player) {
        if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN)) {
            player.getDialogueManager().start(new PlainChat(player, "As an Ultimate Iron Man, you cannot use the bank."));
            return;
        }
        if (TempPlayerStatePlugin.enableTempState(player, TempPlayerStatePlugin.StateType.INVENTORY)) {
            player.sendMessage("Cannot open the bank right now.");
            return;
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.INVENTORY_TAB);
        player.getInterfaceHandler().closeInterface(InterfacePosition.EQUIPMENT_TAB);
        player.getPacketDispatcher().sendClientScript(SCRIPT_RESET_INVENTORY_TAB_SPRITE, INVENTORY_TAB);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getId(), getComponent("Interact with Item"), 0, Container.getSize(ContainerType.INVENTORY), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP6, CLICK_OP9, CLICK_OP10, DRAG_DEPTH1, DRAG_TARGETABLE);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (replacement.isPresent()) return;
        GameInterface.INVENTORY_TAB.open(player);
        GameInterface.EQUIPMENT_TAB.open(player);
    }

    @Override
    protected void build() {
        bind("Interact with Item", (player, slotId, itemId, option) -> {
            final Item item = player.getInventory().getItem(slotId);
            if (item == null || item.getId() != itemId) {
                return;
            }
            final BankDepositInterface.ItemOption op = ItemOption.of(option);
            if (op.equals(ItemOption.EXAMINE)) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            if (op.equals(ItemOption.WITHDRAW_SELECTOR)) {
                final int quantity = Math.max(1, player.getVarManager().getValue(1794));
                player.getBank().deposit(player, player.getInventory().getContainer(), slotId, quantity);
                player.setAnimation(DEPOSIT_ANIM);
                return;
            }
            if (op.equals(ItemOption.WITHDRAW_X)) {
                player.sendInputInt("How many would you like to deposit?", amount -> {
                    player.getBank().deposit(player, player.getInventory().getContainer(), slotId, amount);
                    player.setAnimation(DEPOSIT_ANIM);
                });
                return;
            }
            if (op.equals(ItemOption.EMPTY)) {
                if (itemId == ItemId.COAL_BAG_12019) {
                    final int coalAmount = item.getNumericAttribute("coal").intValue();
                    if (coalAmount <= 0) {
                        player.sendMessage("Your coal bag is empty.");
                        return;
                    }
                    if (!player.getBank().hasFreeSlots()) {
                        player.sendMessage("Your bank is full.");
                        return;
                    }
                    final int amountDeposited = player.getBank().add(new Item(ItemId.COAL, coalAmount)).getSucceededAmount();
                    item.setAttribute("coal", coalAmount - amountDeposited);
                    player.sendMessage("You deposit " + amountDeposited + " coal to your bank from the coal bag.");
                }
            }
            player.getBank().deposit(player, player.getInventory().getContainer(), slotId, op.amount);
            player.setAnimation(DEPOSIT_ANIM);
        });
        bind("Deposit loot", player -> {
            final Inventory inventory = player.getInventory();
            final boolean carryingLootingBag = inventory.containsItem(ItemId.LOOTING_BAG) || inventory.containsItem(ItemId.LOOTING_BAG_22586);
            if (!carryingLootingBag) {
                player.sendMessage("You're not carrying a looting bag right now.");
                return;
            }
            final LootingBag lootingBag = player.getLootingBag();
            if (lootingBag.isEmpty()) {
                player.sendMessage("Your looting bag is already empty.");
                return;
            }
            final Container container = lootingBag.getContainer();
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                final Item item = container.get(slot);
                if (item == null) {
                    continue;
                }
                player.getBank().deposit(null, container, slot, item.getAmount());
            }
            player.getEquipment().refreshAll();
            player.getCombatDefinitions().refresh();
            player.sendMessage("You deposit the contents of your looting bag in the bank.");
            if (container.getSize() != 0) {
                player.sendMessage("Not enough space in your bank.");
            }
        });
        bind("Deposit worn items", player -> {
            final Container container = player.getEquipment().getContainer();
            if (container.getSize() == 0) {
                player.sendMessage("You're not wearing anything.");
                return;
            }
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                final Item item = container.get(slot);
                if (item == null) {
                    continue;
                }
                player.getBank().deposit(null, container, slot, item.getAmount());
            }
            player.setAnimation(DEPOSIT_ANIM);
            if (container.getSize() != 0) {
                player.sendMessage("Not enough space in your bank.");
            }
        });
        bind("Deposit inventory", player -> {
            final Container container = player.getInventory().getContainer();
            if (container.getSize() == 0) {
                player.sendMessage("Your inventory is already empty.");
                return;
            }
            for (int slot = 0; slot < container.getContainerSize(); slot++) {
                final Item item = container.get(slot);
                if (item == null) {
                    continue;
                }
                player.getBank().deposit(null, container, slot, item.getAmount());
            }
            player.setAnimation(DEPOSIT_ANIM);
            if (container.getSize() != 0) {
                player.sendMessage("Not enough space in your bank.");
            }
        });
        bind("Default Quantity 1", player -> {
            player.getSettings().setSetting(Setting.MOBILE_DEPOSIT_BOX_AMOUNT, 0);
            player.getVarManager().sendVar(1794, 1);
        });
        bind("Default Quantity 5", player -> {
            player.getSettings().setSetting(Setting.MOBILE_DEPOSIT_BOX_AMOUNT, 1);
            player.getVarManager().sendVar(1794, 5);
        });
        bind("Default Quantity 10", player -> {
            player.getSettings().setSetting(Setting.MOBILE_DEPOSIT_BOX_AMOUNT, 2);
            player.getVarManager().sendVar(1794, 10);
        });
        bind("Default Quantity X", player -> player.sendInputInt("Select quantity", value -> {
            player.getSettings().setSetting(Setting.MOBILE_DEPOSIT_BOX_AMOUNT, 3);
            player.getVarManager().sendVar(1794, value);
        }));
        bind("Default Quantity All", player -> {
            player.getSettings().setSetting(Setting.MOBILE_DEPOSIT_BOX_AMOUNT, 4);
            player.getVarManager().sendVar(1794, Integer.MAX_VALUE);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BANK_DEPOSIT_INTERFACE;
    }


    private enum ItemOption {
        WITHDRAW_SELECTOR(1, -1), WITHDRAW_1(2, 1), WITHDRAW_5(3, 5), WITHDRAW_10(4, 10), WITHDRAW_ALL(5, Integer.MAX_VALUE), WITHDRAW_X(6, -1), EMPTY(9, -1), EXAMINE(10, -1);
        private static final ItemOption[] values = values();
        private final int optionId;
        private final int amount;

        /**
         * Gets the ItemOption constant for the input integer option id.
         *
         * @param option the option id.
         * @return the ItemOption constant.
         */
        private static final ItemOption of(final int option) {
            final BankDepositInterface.ItemOption opt = CollectionUtils.findMatching(values, v -> v.optionId == option);
            if (opt == null) {
                throw new IllegalArgumentException("Option cannot be " + option + ".");
            }
            return opt;
        }

        ItemOption(int optionId, int amount) {
            this.optionId = optionId;
            this.amount = amount;
        }
    }
}
