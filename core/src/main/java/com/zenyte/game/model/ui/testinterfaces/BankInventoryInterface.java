package com.zenyte.game.model.ui.testinterfaces;

import com.near_reality.game.world.entity.player.container.impl.BankUtil;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.containers.GemBag;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.SwitchPlugin;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import com.zenyte.plugins.item.CoalBag;
import com.zenyte.plugins.item.HerbSack;
import com.zenyte.plugins.item.RunecraftingPouch;
import kotlin.Unit;
import mgi.utilities.CollectionUtils;

import static com.zenyte.game.GameInterface.BANK_INVENTORY;
import static com.zenyte.game.util.AccessMask.*;

/**
 * @author Kris | 21/10/2018 09:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BankInventoryInterface extends Interface implements SwitchPlugin {

    @Override
    protected void attach() {
        put(3, "Interact with Item");
        put(4, "Wear");
        put(8, "Empty Looting Bag");
        put(13, "Deposit Looting Bag Item");
    }

    @Override
    public void open(Player player) {
        if (!player.getInterfaceHandler().isPresent(GameInterface.BANK))
            throw new RuntimeException("Bank inventory overlay cannot be opened without the presence of bank itself.");

        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        final int inventorySize = Container.getSize(ContainerType.INVENTORY);
        dispatcher.sendComponentSettings(BANK_INVENTORY, getComponent("Interact with Item"), 0, inventorySize, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP6, CLICK_OP7, CLICK_OP8, CLICK_OP9, CLICK_OP10, DRAG_DEPTH1, DRAG_TARGETABLE);
        dispatcher.sendComponentSettings(BANK_INVENTORY, getComponent("Wear"), 0, inventorySize, CLICK_OP1, CLICK_OP10);
        transmitSlotVarp(player);
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    public static void transmitSlotVarp(Player player) {
        int value = 0;
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < Inventory.SIZE; i++) {
            final Item item = inventory.getItem(i);
            if (item != null && item.isWieldable()) {
                value |= 1 << i;
            }
        }

        player.getVarManager().sendVar(262, value);
    }

    @Override
    protected void build() {
        bind("Deposit Looting Bag Item", (player, slotId, itemId, option) -> {
            final LootingBag lootingBag = player.getLootingBag();
            final Item item = lootingBag.getItem(slotId);
            if (item == null) {
                return;
            }
            if (option == 9)
                player.sendMessage(item.getDefinitions().getExamine());
            else if (option == 4) {
                player.sendInputInt("Amount to deposit:", amount -> BankUtil.safePermutation(player, bank -> {
                    bank.deposit(player, lootingBag.getContainer(), slotId, amount);
                    return Unit.INSTANCE;
                }));
            } else {
                BankUtil.safePermutation(player, bank -> {
                    final int amount = option == 1 ? 1 : option == 2 ? 5 : lootingBag.getContainer().getAmountOf(item.getId());
                    bank.deposit(player, lootingBag.getContainer(), slotId, amount);
                    return Unit.INSTANCE;
                });
            }
        });
        bind("Empty Looting Bag", player -> {
            final LootingBag lootingBag = player.getLootingBag();
            BankUtil.safePermutation(player, bank -> {
                for (int slotId = 0; slotId < 28; slotId++) {
                    final Item item = lootingBag.getItem(slotId);
                    if (item == null) {
                        continue;
                    }
                    bank.deposit(player, lootingBag.getContainer(), slotId, item.getAmount());
                }
                return Unit.INSTANCE;
            });
        });
        bind("Interact with Item", "Interact with Item", ((player, fromSlot, toSlot) -> {
            if (fromSlot < 0 || toSlot < 0 || fromSlot >= Container.getSize(ContainerType.INVENTORY) || toSlot >= Container.getSize(ContainerType.INVENTORY))
                return;
            player.getInventory().switchItem(fromSlot, toSlot);
            transmitSlotVarp(player);
        }));
        bind("Wear", ((player, slotId, itemId, option) -> {
            final Item item = player.getInventory().getItem(slotId);
            if (item == null || item.getId() != itemId) {
                return;
            }

            if (option == 1) {
                player.getEquipment().wear(slotId);
            } else if (option == 10) {
                ItemUtil.sendItemExamine(player, item.getId());
            }
        }));
        bind("Interact with Item", ((player, slotId, itemId, option) -> {
            final Item item = player.getInventory().getItem(slotId);
            if (item == null || item.getId() != itemId) {
                return;
            }
            if (option == 1 && !player.getLootingBag().isEmpty() && LootingBag.isBag(item.getId())) {
                player.getLootingBag().refresh();
                player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Deposit Looting Bag Item"), 0, Container.getSize(ContainerType.INVENTORY), AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP10);
                return;
            }
            if (option == 9) {
                BankUtil.safePermutation(player, bank -> {
                    switch (item.getId()) {
                        case ItemId.HERB_SACK:
                        case ItemId.OPEN_HERB_SACK:
                            player.getHerbSack().empty(bank.getContainer());
                            break;
                        case ItemId.SEED_BOX:
                        case ItemId.OPEN_SEED_BOX:
                            player.getSeedBox().emptyToBank();
                            break;
                        case 26304:
                        case 26306:
                            player.getBonePouch().empty(player.getBank().getContainer());
                            break;
                        case 26300:
                        case 26302:
                            player.getDragonhidePouch().empty(player.getBank().getContainer());
                            break;
                        case ItemId.COAL_BAG_12019:
                            CoalBag.emptyBagToBank(item, player);
                            break;
                        case ItemId.GEM_BAG_12020:
                            player.getGemBag().empty(bank.getContainer());
                            break;
                        case ItemId.RUNE_POUCH:
                        case ItemId.DIVINE_RUNE_POUCH:
                        {
                            final Container container = player.getRunePouch().getContainer();
                            if (container.isEmpty()) {
                                player.sendMessage("Your rune pouch is already empty.");
                                return Unit.INSTANCE;
                            }
                            player.getRunePouch().emptyRunePouch();
                            break;
                        }
                        case ItemId.NEST_BOX_EMPTY:
                        case ItemId.NEST_BOX_SEEDS:
                        case ItemId.NEST_BOX_RING: {
                            final int charges = item.getCharges();
                            final int id = item.getId();
                            final int nestId = id == 12792 ? 5075 : id == 12793 ? 5073 : 5074;
                            final int amount = bank.add(new Item(nestId, charges)).getSucceededAmount();
                            bank.refreshContainer();
                            if (amount >= charges) {
                                player.getInventory().deleteItem(slotId, item);
                            } else {
                                item.setCharges(charges - amount);
                                player.sendMessage("Not enough space in your bank.");
                            }
                            break;
                        }
                        default: {
                            if (RunecraftingPouch.pouches.contains(item.getId())) {
                                RunecraftingPouch.fill(player, item, bank.getContainer(), slotId);
                            } else {
                                player.getEquipment().wear(slotId);
                            }
                        }
                    }
                    return Unit.INSTANCE;
                });
                return;
            }
            final BankInventoryInterface.ItemOption op = ItemOption.of(option);
            if (op.equals(ItemOption.EXAMINE)) {
                player.sendMessage(item.getDefinitions().getExamine());
                return;
            }
            if (op.equals(ItemOption.WITHDRAW_X)) {
                player.sendInputInt("How many would you like to deposit?", amount -> {
                    BankUtil.safePermutation(player, bank -> {
                        bank.setLastDepositAmount(amount);
                        player.getVarManager().sendBit(BankInterface.VAR_LAST_DEPOSIT_AMOUNT, bank.getLastDepositAmount());
                        bank.deposit(player, player.getInventory().getContainer(), slotId, amount);
                        return Unit.INSTANCE;
                    });

                });
                return;
            }
            BankUtil.safePermutation(player, bank -> {
                final int amount = op.equals(ItemOption.WITHDRAW_1_OR_SELECTED)
                        ? bank.getCurrentQuantity()
                        : op.equals(ItemOption.WITHDRAW_LAST_AMOUNT)
                        ? bank.getLastDepositAmount()
                        : op.amount;
                bank.deposit(player, player.getInventory().getContainer(), slotId, amount);
                return Unit.INSTANCE;
            });
        }));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BANK_INVENTORY;
    }


    public enum ItemOption {

        WITHDRAW_1_OR_SELECTED(2, 1, 1),
        WITHDRAW_1(3, 2, 1),
        WITHDRAW_5(4, 3, 5),
        WITHDRAW_10(5, 4, 10),
        WITHDRAW_LAST_AMOUNT(6, 5, -1),
        WITHDRAW_X(7, 6, -1),
        WITHDRAW_ALL(8, 7, Integer.MAX_VALUE),
        EXAMINE(10, 10, -1);

        private static final ItemOption[] values = values();
        private final int optionId;
        private final int optionIdShared;
        public final int amount;

        /**
         * Gets the ItemOption constant for the input integer option id.
         *
         * @param option the option id.
         * @return the ItemOption constant.
         */
        public static ItemOption of(final int option) {
            final BankInventoryInterface.ItemOption opt = CollectionUtils.findMatching(values, v -> v.optionId == option);
            if (opt == null) {
                throw new IllegalArgumentException("Option cannot be " + option + ".");
            }
            return opt;
        }

        public static ItemOption ofShared(final int option) {
            final BankInventoryInterface.ItemOption opt = CollectionUtils.findMatching(values, v -> v.optionIdShared == option);
            if (opt == null) {
                throw new IllegalArgumentException("Option cannot be " + option + ".");
            }
            return opt;
        }

        ItemOption(final int optionId, final int optionIdShared, final int amount) {
            this.optionId = optionId;
            this.optionIdShared = optionIdShared;
            this.amount = amount;
        }

    }
}
