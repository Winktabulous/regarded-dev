package com.zenyte.plugins.item.mysteryboxes;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author William Fuhrman | 11/28/2022 1:39 AM
 * @since 05/07/2022
 */
public class SkillingMysteryBox extends ItemPlugin {

	public static MysteryItem[] rewards;
	public static MysterySupplyItem[] supplies;

	public static void openBox(Player player, int boxId, MysteryItem[] rewards, MysterySupplyItem[] supplies) {
		Item box = new Item(boxId);
		if (!player.getInventory().deleteItem(box).isFailure()) {
			MysteryItem jackpot = null;
            for (MysteryItem mysteryItem : rewards) {
                if (Utils.randomBoolean(mysteryItem.getWeight())) {
                    jackpot = mysteryItem;
                    break;
                }
            }

            if (jackpot != null) {
                final Item reward = new Item(jackpot.getId(), Utils.random(jackpot.getMinAmount(), jackpot.getMaxAmount()));

                player.getInventory().addOrDrop(reward);
                if (jackpot.isAnnounce()) {
                    WorldBroadcasts.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, reward.getId(), box.getName());
                }
            }

			if (supplies != null) {
				for (int i = 0; i < 3; i++) {
					MysterySupplyItem supplyItem = Utils.random(supplies);
					final Item supplyReward = new Item(supplyItem.getId(), Utils.random(supplyItem.getMinAmount(), supplyItem.getMaxAmount()));
					player.getInventory().addOrDrop(supplyReward);
				}
			}
		}
	}

	@Override
	public void handle() {
		bind("Open", (player, item, container, slotId) -> openBox(player, item.getId(), rewards, supplies));

		rewards = new MysteryItem[]{
				//OTHER
				new MysteryItem(6571, 1, 1, 50).announce(), // Onyx
				new MysteryItem(19529, 1, 1, 100).announce(), // Zenyte
				new MysteryItem(ItemId.SMOULDERING_STONE, 1, 1, 100).announce(), // Smouldering Stone
				new MysteryItem(ItemId.ZALCANO_SHARD, 1, 1, 100).announce(), // Zalcano Shard
		};

		supplies = new MysterySupplyItem[]{
				new MysterySupplyItem(1618, 9, 22), // diamond
				new MysterySupplyItem(1620, 11, 32), // ruby
				new MysterySupplyItem(1622, 17, 40), // emerald
				new MysterySupplyItem(1624, 22, 54), // sapphire
				new MysterySupplyItem(1626, 29, 63), // opal
				new MysterySupplyItem(1628, 33, 70), // jade
				new MysterySupplyItem(1630, 39, 79), // red topaz
				new MysterySupplyItem(1632, 6, 13), // dragon stones
				new MysterySupplyItem(1748, 20, 40), // black dhide
				new MysterySupplyItem(1750, 25, 50), // red dhide
				new MysterySupplyItem(1752, 30, 60), // blue dhide
				new MysterySupplyItem(1754, 40, 80), // green dhide
				new MysterySupplyItem(445, 100, 200), // gold ore
				new MysterySupplyItem(448, 75, 150), // mithril ore
				new MysterySupplyItem(450, 50, 100), // addy ore
				new MysterySupplyItem(452, 10, 30), // rune ore
				new MysterySupplyItem(2354, 200, 400), // steel bar
				new MysterySupplyItem(2358, 150, 300), // gold bar
				new MysterySupplyItem(2360, 100, 200), // mithril bar
				new MysterySupplyItem(2362, 50, 100), // addy bar
				new MysterySupplyItem(2364, 10, 30), // runite bar
				new MysterySupplyItem(21348, 25, 50), // amethyst
				new MysterySupplyItem(208, 10, 30), // ranarr g
				new MysterySupplyItem(210, 10, 40), // irit g
				new MysterySupplyItem(212, 10, 45), // avantoe g
				new MysterySupplyItem(214, 10, 35), // kwuarm g
				new MysterySupplyItem(216, 10, 35), // cadantine g
				new MysterySupplyItem(218, 10, 35), // dwarf weed g
				new MysterySupplyItem(220, 10, 35), // torstol g
				new MysterySupplyItem(232, 15, 25), // snape grass
				new MysterySupplyItem(2971, 15, 25), // mort myre fungus
				new MysterySupplyItem(246, 15, 25), // wine of zamorak
				new MysterySupplyItem(20783, 15, 25), // bird nest
				new MysterySupplyItem(244, 15, 25), // blue dragon scale
				new MysterySupplyItem(1520, 50, 150), // willow logs
				new MysterySupplyItem(1518, 50, 125), // maple logs
				new MysterySupplyItem(1516, 30, 100), // yew logs
				new MysterySupplyItem(1514, 25, 75), // magic logs
				new MysterySupplyItem(19670, 30, 100), // red wood logs
				new MysterySupplyItem(7937, 300, 1000), // pure essence
				new MysterySupplyItem(11232, 30, 60), // ddart tip
				new MysterySupplyItem(11237, 45, 90), // dragon arrow tips
				new MysterySupplyItem(537, 15, 25), // dragon bones
				new MysterySupplyItem(21976, 5, 20) // superior bones
		};
	}

	@Override
	public int[] getItems() {
		return new int[]{32212};
	}
}
