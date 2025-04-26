package com.zenyte.game.content.partyroom;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

import java.util.Optional;

/**
 * @author Kris | 25/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyDropChestInterface extends Interface {
    @Override
    protected void attach() {
        put(4, "Add 1");
        put(5, "Add 5");
        put(6, "Add x");
        put(7, "Add all");
        put(14, "Accept");
        put(8, "Chest public deposit container");
        put(13, "Chest private deposit container");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
        GameInterface.PARTY_DROP_CHEST_INVENTORY.open(player);
        final FaladorPartyRoom partyroom = FaladorPartyRoom.getPartyRoom();
        partyroom.startViewing(player);
        final Container container = partyroom.getContainer();
        container.setFullUpdate(true);
        container.refresh(player);
        final Container privateContainer = partyroom.getPrivateContainer(player);
        privateContainer.setFullUpdate(true);
        privateContainer.refresh(player);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Chest public deposit container"), 0, 215, AccessMask.CLICK_OP10);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Chest private deposit container"), 0, 7,
                AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
        player.getVarManager().sendBitInstant(7792, 0);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        FaladorPartyRoom.getPartyRoom().stopViewing(player);
        player.getInterfaceHandler().closeInterface(GameInterface.PARTY_DROP_CHEST_INVENTORY);
        final Container privateContainer = FaladorPartyRoom.getPartyRoom().getPrivateContainer(player);
        final Inventory inventory = player.getInventory();
        privateContainer.getItems().values().forEach(inventory::addOrDrop);
        privateContainer.clear();
    }

    @Override
    protected void build() {
        bind("Add 1", player -> player.getVarManager().sendBitInstant(7792, 0));
        bind("Add 5", player -> player.getVarManager().sendBitInstant(7792, 1));
        bind("Add x", player -> player.getVarManager().sendBitInstant(7792, 3));
        bind("Add all", player -> player.getVarManager().sendBitInstant(7792, 2));
        bind("Accept", player -> {
            final FaladorPartyRoom partyroom = FaladorPartyRoom.getPartyRoom();
            final Container container = partyroom.getPrivateContainer(player);
            if (container.isEmpty()) {
                return;
            }
            if (partyroom.getVariables().isDisabled()) {
                player.sendMessage("The party room has been disabled.");
                return;
            }
            if (!player.getPrivilege().eligibleTo(partyroom.getVariables().getMinimumPrivilegeToDepositItems())) {
                player.sendMessage("You are not eligible to deposit items in the party chest right now.");
                return;
            }
            final int length = container.getContainerSize();
            final Container publicContainer = partyroom.getContainer();
            final int minimumStackValue = partyroom.getVariables().getMinimumItemStackValueThreshold();
            boolean unableToDeposit = false;
            for (int i = 0; i < length; i++) {
                final Item item = container.get(i);
                if (item == null) {
                    continue;
                }
                if (minimumStackValue > 0) {
                    final long valueOfStack = (long) item.getSellPrice() * item.getAmount();
                    if (valueOfStack < minimumStackValue) {
                        unableToDeposit = true;
                        player.sendMessage(Colour.RED.wrap(item.getAmount() + " x " + item.getName() + " could not be deposited: Value of stack under the threshold of " + minimumStackValue + "."));
                        continue;
                    }
                }
                publicContainer.deposit(null, container, i, item.getAmount());
            }
            container.setFullUpdate(true);
            container.refresh(player);
            publicContainer.setFullUpdate(true);
            partyroom.forEachViewing(publicContainer::refresh);
            if (!unableToDeposit && !container.isEmpty()) {
                player.sendMessage("Not enough space in the drop chest.");
            }
        });
        bind("Chest private deposit container", (player, slotId, itemId, option) -> {
            final Container container = FaladorPartyRoom.getPartyRoom().getPrivateContainer(player);
            final Item item = container.get(slotId);
            if (item == null) {
                return;
            }
            final Inventory inventory = player.getInventory();
            final Container inventoryContainer = inventory.getContainer();
            final PartyRoomOptionType optionType = getType(player, option);
            if (optionType == PartyRoomOptionType.EXAMINE) {
                player.sendMessage(item.getDefinitions().getExamine());
                return;
            }
            if (optionType == PartyRoomOptionType.X) {
                player.sendInputInt("How many would you like to withdraw?", value -> {
                    if (container.get(slotId) != item) {
                        return;
                    }
                    inventoryContainer.deposit(player, container, slotId, value);
                    container.refresh(player);
                    inventoryContainer.refresh(player);
                    PartyDropInventoryInterface.refreshContainer(player);
                });
                return;
            }
            final int amount = optionType.getAmount();
            inventoryContainer.deposit(player, container, slotId, amount);
            container.refresh(player);
            inventoryContainer.refresh(player);
            PartyDropInventoryInterface.refreshContainer(player);
        });
    }

    public static PartyRoomOptionType getType(Player player, int option) {
        PartyRoomOptionType optionType;
        switch (option) {
            case 1:
                final int type = player.getVarManager().getBitValue(7792);
                switch (type) {
                    case 0:
                        optionType = PartyRoomOptionType.ONE;
                        break;
                    case 1:
                        optionType = PartyRoomOptionType.FIVE;
                        break;
                    case 2:
                        optionType = PartyRoomOptionType.ALL;
                        break;
                    default:
                        optionType = PartyRoomOptionType.X;
                        break;
                }
                break;
            case 3:
                optionType = PartyRoomOptionType.FIVE;
                break;
            case 4:
                optionType = PartyRoomOptionType.X;
                break;
            case 10:
                optionType = PartyRoomOptionType.EXAMINE;
                break;
            default:
                optionType = PartyRoomOptionType.ALL;
                break;
        }
        return optionType;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PARTY_DROP_CHEST;
    }
}
