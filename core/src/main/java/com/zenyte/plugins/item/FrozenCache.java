package com.zenyte.plugins.item;

import com.zenyte.game.content.drops.RateWeightedDropTable;
import com.zenyte.game.content.drops.table.RollResult;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Savions.
 */
public class FrozenCache extends ItemPlugin {

	private static final RateWeightedDropTable TABLE = new RateWeightedDropTable();
	private static final SoundEffect OPEN_SOUND_EFFECT = new SoundEffect(2655, 0, 0);

	static {
		TABLE.append(ItemId.ANCIENT_ESSENCE, 8, 540, 599);
		TABLE.append(ItemId.CHAOS_RUNE, 16, 480);
		TABLE.append(ItemId.RUNE_PLATELEGS, 16, 3);
		TABLE.append(ItemId.BLACK_DHIDE_BODY, 16, 1);
		TABLE.append(ItemId.FIRE_RUNE, 16, 1949);
		TABLE.append(ItemId.CANNONBALL, 16, 666);
		TABLE.append(ItemId.DRAGON_PLATESKIRT, 16, 1);
		TABLE.append(ItemId.TORSTOL_SEED, 16, 4);
		TABLE.append(ItemId.COAL, 16, 163);
		TABLE.append(ItemId.SNAPDRAGON_SEED, 20, 5);
		TABLE.append(ItemId.DRAGON_PLATELEGS, 20, 2);
		TABLE.append(ItemId.ANCIENT_ESSENCE, 22, 885, 995);
		TABLE.append(ItemId.RUNITE_ORE, 27, 18);
		TABLE.append(ItemId.GRIMY_TOADFLAX, 27, 55);
		TABLE.append(ItemId.LIMPWURT_ROOT, 27, 21);
		TABLE.append(ItemId.RANARR_SEED, 27, 8);
		TABLE.append(ItemId.SILVER_ORE, 40, 101);
		TABLE.append(ItemId.SPIRIT_SEED, 40, 1);
		TABLE.append(ItemId.ANCIENT_ESSENCE, 50, 1970, 2060);
		TABLE.append(ItemId.RUNE_SWORD, 80, 1);
		TABLE.append(ItemId.FROZEN_CACHE, 125, 1);
		TABLE.append(ItemId.ANCIENT_ICON, 250, 1);
		TABLE.append(ItemId.VENATOR_SHARD, 500, 1);
	}

	@Override public void handle() {
		bind("Open", (player, item, slotId) -> {
			final RollResult rollResult = TABLE.roll();
			player.getInventory().deleteItem(slotId, item);
			final Item reward = new Item(rollResult.getId(), rollResult.getQuantity());
			if (reward.getId() == ItemId.FROZEN_CACHE) {
				player.getDialogueManager().start(new ItemChat(player, reward.getId(), "You open the frozen cache and find... another frozen cache inside! How peculiar."));
			} else {
				player.getDialogueManager().start(new ItemChat(player, reward.getId(), "You open the frozen cache and find " + ItemUtil.toPrettyString(reward) + " inside!"));
			}
			final ItemDefinitions defs = reward.getDefinitions();
			if (defs != null && defs.getNotedId() != -1) {
				reward.setId(defs.getNotedId());
			}
			player.getInventory().addItem(reward);
			player.getPacketDispatcher().sendSoundEffect(OPEN_SOUND_EFFECT);
		});
	}

	@Override public int[] getItems() {
		return new int[] { ItemId.FROZEN_CACHE };
	}
}
