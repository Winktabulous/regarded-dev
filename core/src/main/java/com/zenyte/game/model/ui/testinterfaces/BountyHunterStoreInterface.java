package com.zenyte.game.model.ui.testinterfaces;

import com.google.common.base.Preconditions;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.bountyhunter.BountyHunter;
import com.zenyte.game.content.bountyhunter.BountyHunterVar;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.ui.testinterfaces.BountyHunterRewardType;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import mgi.types.config.enums.Enums;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;

import java.util.Optional;
import java.util.OptionalInt;

import static com.zenyte.game.util.AccessMask.*;

/**
 * @author Kris | 07/05/2019 01:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BountyHunterStoreInterface extends Interface {

    @Override
    protected void attach() {
        put(2, "Item layer");
        put(3, "Scrollbar");
    }

    @Override
    public void open(final Player player) {
        /*inal PacketDispatcher dispatcher = player.getPacketDispatcher();
        final VarManager varManager = player.getVarManager();
        final BountyHunter bounty = player.getBountyHunter();
        final IntEnum layerEnum = Enums.BOUNTY_HUNTER_REWARDS;
        player.getInterfaceHandler().sendInterface(this);
        varManager.sendVarInstant(1137, bounty.getValue(BountyHunterVar.CURRENT_HUNTER_KILLS));
        varManager.sendVarInstant(1138, bounty.getValue(BountyHunterVar.CURRENT_ROGUE_KILLS));
        dispatcher.sendClientScript(23, getId() << 16 | getComponent("Item layer"), getId() << 16 | getComponent("Scrollbar"), layerEnum.getId());
        dispatcher.sendComponentSettings(getId(), getComponent("Item layer"), 0, layerEnum.getSize(), CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP10);
   */
    }

    @Override
    protected void build() {
        bind("Item layer", (player, slotId, itemId, optionId) -> {
            /*final IntEnum layerEnum = Enums.BOUNTY_HUNTER_REWARDS;
            final OptionalInt optionalItem = layerEnum.getValue(slotId);
            if (!optionalItem.isPresent()) {
                return;
            }
            int item = optionalItem.getAsInt();
            final Optional<BountyHunterRewardType> optionalReward = BountyHunterRewardType.get(item);
            if (!optionalReward.isPresent()) {
                return;
            }
            final BountyHunterRewardType reward = optionalReward.get();
            if (reward == BountyHunterRewardType.HUNTERS_HONOUR && player.getVarManager().getValue(1137) > player.getVarManager().getValue(1138)) {
                item = 12856;
            }
            final BountyHunterStoreInterface.Option option = Option.get(optionId - 1);
            if (option == Option.EXAMINE) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            if (option == Option.VALUE) {
                final ItemDefinitions itemDefinitions = ItemDefinitions.getOrThrow(item);
                player.sendMessage(itemDefinitions.getName() + ": currently costs " + StringFormatUtil.format(reward.getCost()) + " Bounty Hunter points.");
                return;
            }
            int amount = option.amount;
            final int points = player.getBountyHunter().getPoints();
            final int cost = reward.getCost();
            if (amount > (points / cost)) {
                amount = points / cost;
            }
            final Container inventory = player.getInventory().getContainer();
            int freeSlots = inventory.getFreeSlotsSize();
            final int affordableAmount = amount;
            final int inInventory = inventory.getAmountOf(item);
            if (amount + inInventory < 0) {
                amount = Integer.MAX_VALUE - inInventory;
            }
            final ItemDefinitions definitions = ItemDefinitions.getOrThrow(item);
            if (definitions.isStackable()) {
                if (freeSlots == 0 && inInventory == 0) {
                    amount = 0;
                }
            } else {
                amount = Math.min(freeSlots, amount);
            }
            final Optional<String> message = affordableAmount != option.amount ? Optional.of("You don't have enough Bounty Hunter points.") : amount != affordableAmount ? Optional.of("Not enough space in your inventory.") : Optional.empty();
            message.ifPresent(player::sendMessage);
            if (amount <= 0) {
                return;
            }
            if (reward == BountyHunterRewardType.LOOTING_BAG) {
                if (LootingBag.hasBag(player)) {
                    player.sendMessage("You can only have one looting bag with you at all times.");
                    return;
                }
                if (amount > 1) {
                    amount = 1;
                    player.sendMessage("You can only have one looting bag with you at all times.");
                }
            } else if (reward == BountyHunterRewardType.RUNE_POUCH) {
                if (player.containsItem(ItemId.RUNE_POUCH)) {
                    player.sendMessage("You can only own one rune pouch at a time!");
                    return;
                }
                if (amount > 1) {
                    amount = 1;
                    player.sendMessage("You can only own one rune pouch at a time!");
                }
            }
            player.getBountyHunter().setPoints(points - (cost * amount));
            player.getInventory().addOrDrop(new Item(item, amount));*/
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.BOUNTY_HUNTER_STORE;
    }


    private enum Option {
        VALUE(-1), BUY_1(1), BUY_5(5), BUY_10(10), BUY_50(50), EXAMINE(-1);
        private final int amount;
        private static final Option[] values = values();

        private static final Option get(final int id) {
            if (id == 9) {
                return EXAMINE;
            }
            Preconditions.checkArgument(!(id < 0 || id >= values.length));
            return values[id];
        }

        Option(int amount) {
            this.amount = amount;
        }
    }
}
