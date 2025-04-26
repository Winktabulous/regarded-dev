package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;

import java.util.Optional;

/**
 * @author Kris | 03/05/2019 00:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CasketPlugin extends ItemPlugin {

    private enum CasketReward {
        COINS_20(new Item(995, 20), 10), COINS_40(new Item(995, 40), 10), COINS_80(new Item(995, 80), 10), COINS_160(new Item(995, 160), 10), COINS_320(new Item(995, 320), 10), COINS_640(new Item(995, 640), 10), UNCUT_SAPPHIRE(new Item(1623, 1), 32), UNCUT_EMERALD(new Item(1621, 1), 16), UNCUT_RUBY(new Item(1619, 1), 8), UNCUT_DIAMOND(new Item(1617, 1), 2), COSMIC_TALISMAN(new Item(1454, 1), 8), LOOP_HALF_OF_KEY(new Item(987, 1), 1), TOOTH_HALF_OF_KEY(new Item(985, 1), 1);
        private final Item item;
        private final int weight;
        private static final CasketReward[] values = values();

        private static final Optional<CasketReward> getReward() {
            final int roll = Utils.random(128);
            int current = 0;
            for (final CasketPlugin.CasketReward reward : values) {
                if ((current += reward.weight) >= roll) {
                    return Optional.of(reward);
                }
            }
            return Optional.empty();
        }

        CasketReward(Item item, int weight) {
            this.item = item;
            this.weight = weight;
        }
    }

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> player.getInventory().ifDeleteItem(item, () -> CasketReward.getReward().ifPresent(reward -> {
            player.sendMessage("You open the casket and find " + reward.item.getAmount() + " x " + reward.item.getName() + ".");
            player.getInventory().addOrDrop(new Item(reward.item));
        })));
    }

    @Override
    public int[] getItems() {
        return new int[] {405};
    }
}
