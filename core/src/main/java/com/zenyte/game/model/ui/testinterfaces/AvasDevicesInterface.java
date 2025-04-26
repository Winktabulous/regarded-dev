package com.zenyte.game.model.ui.testinterfaces;

import com.google.common.base.Preconditions;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

import java.util.function.IntConsumer;

/**
 * @author Kris | 19/04/2019 19:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AvasDevicesInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Purchase ava's attractor");
        put(7, "Purchase ava's accumulator");
        put(12, "Purchase ava's assembler");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Purchase ava's attractor", (player, slotId, itemId, option) -> {
            Preconditions.checkArgument(option >= 0 && option <= 4);
            final IntConsumer consumer = amount -> {
                final int cost = 999;
                final int space = player.getInventory().getFreeSlots();
                final int coins = player.getInventory().getAmountOf(995);
                if (amount > space) {
                    amount = space;
                    if (coins == ((amount + 1) * cost)) {
                        amount++;
                    }
                }
                if ((amount * cost) > coins) {
                    amount = coins / cost;
                }
                if (amount <= 0) {
                    player.sendMessage("You don't have enough coins to purchase an attractor; it costs 999 gold each.");
                    return;
                }
                final int finalAmount = amount;
                player.getInventory().ifDeleteItem(new Item(995, finalAmount * cost), () -> player.getInventory().addOrDrop(new Item(10498, finalAmount)));
            };
            if (option == 3) {
                player.sendInputInt("How many would you like to purchase?", consumer::accept);
                return;
            }
            consumer.accept(option == 1 ? 1 : option == 2 ? 5 : 27);
        });
        bind("Purchase ava's accumulator", (player, slotId, itemId, option) -> {
            Preconditions.checkArgument(option >= 0 && option <= 4);
            final IntConsumer consumer = amount -> {
                final int cost = 2999;
                final int space = player.getInventory().getFreeSlots();
                final int coins = player.getInventory().getAmountOf(995);
                if (amount > space) {
                    amount = space;
                    if (coins == ((amount + 1) * cost)) {
                        amount++;
                    }
                }
                if ((amount * cost) > coins) {
                    amount = coins / cost;
                }
                if (amount <= 0) {
                    player.sendMessage("You don't have enough coins to purchase an accumulator; it costs 2,999 gold each.");
                    return;
                }
                final int finalAmount = amount;
                player.getInventory().ifDeleteItem(new Item(995, finalAmount * cost), () -> player.getInventory().addOrDrop(new Item(10499, finalAmount)));
            };
            if (option == 3) {
                player.sendInputInt("How many would you like to purchase?", consumer::accept);
                return;
            }
            consumer.accept(option == 1 ? 1 : option == 2 ? 5 : 27);
        });
        bind("Purchase ava's assembler", (player, slotId, itemId, option) -> {
            Preconditions.checkArgument(option >= 0 && option <= 4);
            final IntConsumer consumer = amount -> {
                final int cost = 4999;
                final int space = player.getInventory().getFreeSlots();
                final int coins = player.getInventory().getAmountOf(995);
                final int heads = player.getInventory().getAmountOf(21907);
                if (amount > space) {
                    amount = space;
                }
                if ((amount * cost) > coins) {
                    amount = coins / cost;
                }
                if (heads < amount) {
                    amount = heads;
                }
                if (amount <= 0) {
                    player.sendMessage("You don't have enough coins to purchase an assembler; it costs 4,999 gold & a vorkath head each.");
                    return;
                }
                final int finalAmount = amount;
                player.getInventory().deleteItemsIfContains(new Item[] {new Item(995, finalAmount * cost), new Item(21907, finalAmount)}, () -> player.getInventory().addOrDrop(new Item(22109, finalAmount)));
            };
            if (option == 3) {
                player.sendInputInt("How many would you like to purchase?", consumer::accept);
                return;
            }
            consumer.accept(option == 1 ? 1 : option == 2 ? 5 : 27);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.AVAS_DEVICES;
    }
}
