package com.zenyte.plugins.item;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;

public class CoinPouch extends ItemPlugin {
    private static final int COIN_PICKPOCKETING_MULTIPLIER = 3;

    @Override
    public void handle() {
        bind("Open", (player, item, slotId) -> {
            final com.zenyte.game.content.skills.thieving.CoinPouch pouch = com.zenyte.game.content.skills.thieving.CoinPouch.ITEMS.get(item.getId());
            open(player, pouch, 1);
        });
        bind("Open-all", (player, item, slotId) -> {
            final com.zenyte.game.content.skills.thieving.CoinPouch pouch = com.zenyte.game.content.skills.thieving.CoinPouch.ITEMS.get(item.getId());
            open(player, pouch, item.getAmount());
        });
    }

    private void open(final Player player, final com.zenyte.game.content.skills.thieving.CoinPouch pouch, final int amount) {
        int totalCoins = getCoinAmount(pouch, amount);
        player.getInventory().deleteItem(pouch.getItemId(), amount);
        player.getInventory().addOrDrop(new Item(995, totalCoins));
        player.sendFilteredMessage("You open all of the pouches and receive a total of " + totalCoins + " coins.");
        player.getPacketDispatcher().sendSoundEffect(new SoundEffect(10));
    }

    public static int getCoinAmount(final com.zenyte.game.content.skills.thieving.CoinPouch pouch, final int amount) {
        return Utils.random(pouch.getReward().getMinAmount(), pouch.getReward().getMaxAmount()) * amount * COIN_PICKPOCKETING_MULTIPLIER;
    }

    @Override
    public int[] getItems() {
        return com.zenyte.game.content.skills.thieving.CoinPouch.ITEMS.keySet().toIntArray();
    }
}
