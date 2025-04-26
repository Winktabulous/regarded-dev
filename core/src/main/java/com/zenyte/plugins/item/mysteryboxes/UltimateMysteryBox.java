package com.zenyte.plugins.item.mysteryboxes;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.plugins.PluginPriority;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * @author Kris | 10/06/2019 06:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@PluginPriority(9990)
public class UltimateMysteryBox extends ItemPlugin {

	public static int totalWeight;
	public static MysteryItem[] rewards;
	public static MysterySupplyItem[] supplies;

	public static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();
	public static ObjectArrayList<DropViewerEntry> toEntries() {
		if(entries.size() == 0) {
			calculateEntries();
		}
		return entries;
	}

	private static void calculateEntries() {
		for (final MysteryItem reward : rewards) {
			OtherDropViewerEntry entry = new OtherDropViewerEntry(reward.getId(), reward.getMinAmount(), reward.getMaxAmount(), reward.getWeight(), totalWeight, "");
			entries.add(entry);
		}
	}


	@Override
	public void handle() {
		bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, supplies));
		bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, supplies));

		rewards = new MysteryItem[] {
				// Common = 1000
//                new MysteryItem(32149, 5, 10, 1000), // Larran's Booster
//                new MysteryItem(32150, 5, 10, 1000), // Ganodermic Booster
//                new MysteryItem(32151, 5, 10, 1000), // Slayer Booster
//                new MysteryItem(32152, 5, 10, 1000), // Pet Booster
//                new MysteryItem(32153, 5, 10, 1000), // Gauntlet Booster
//                new MysteryItem(32154, 5, 10, 1000), // Blood Money Booster
//                new MysteryItem(32155, 5, 10, 1000), // Clue Scroll Booster
//                new MysteryItem(32156, 5, 10, 1000), // ToB Booster
//                new MysteryItem(32157, 3, 7, 1000), // Slayer Task Picker
//                new MysteryItem(32158, 5, 10, 1000), // Slayer Task Reset
//				new MysteryItem(32166, 5, 10, 1000), // Rev Booster
//				new MysteryItem(32167, 5, 10, 1000), // Nex Booster
//                new MysteryItem(19782, 4, 8, 1000), // Xeric's Wisdom
                new MysteryItem(20724, 1, 1, 1000), // Imbued Heart
                new MysteryItem(ItemId.DRAGON_CROSSBOW, 1, 1, 1000), // Dragon Crossbow
                new MysteryItem(11770, 1, 1, 1000), // Seers (i)
                new MysteryItem(11771, 1, 1, 1000), // Archers (i)
                new MysteryItem(11773, 1, 1, 1000), // Berserker (i)
                new MysteryItem(30031, 1, 1, 1000), // pet mystery box
                new MysteryItem(11806, 1, 1, 1000), // Saradomin godsword
                new MysteryItem(11804, 1, 1, 1000), // Bandos godsword
                new MysteryItem(11808, 1, 1, 1000), // Zamorak godsword
                new MysteryItem(12692, 1, 1, 1000), // treasonous ring (i)
                new MysteryItem(12691, 1, 1, 1000), // tyrannical ring (i)
                new MysteryItem(13202, 1, 1, 1000), // ring of the gods (i)
                new MysteryItem(12006, 1, 1, 1000).charges(10_000), // Tentacle Whip
                new MysteryItem(12002, 1, 1, 1000), // Occult
                new MysteryItem(13265, 1, 1, 1000), // Abyssal Dagger
                new MysteryItem(11791, 1, 1, 1000), // Staff of the dead

				//Rare+ = 250
                new MysteryItem(11802, 1, 1, 500), // Armadyl godsword
                new MysteryItem(11785, 1, 1, 500), // ACB
                new MysteryItem(19481, 1, 1, 500), // Heavy Ballista
                new MysteryItem(13239, 1, 1, 500), // Primordial Boots
                new MysteryItem(13237, 1, 1, 500), // Pegasian Boots
                new MysteryItem(13235, 1, 1, 500), // Eternal Boots
                new MysteryItem(19547, 1, 1, 500), // Anguish
                new MysteryItem(19550, 1, 1, 500), // Suffering
                new MysteryItem(19544, 1, 1, 500), // Tormented
                new MysteryItem(19553, 1, 1, 500), // Torture
                new MysteryItem(12902, 1, 1, 500), // Toxic staff
				new MysteryItem(12932, 1, 1, 500), // Magic fang
				new MysteryItem(12922, 1, 1, 500), // Tanz fang
				new MysteryItem(11832, 1, 1, 500), // bcp
				new MysteryItem(11834, 1, 1, 500), // tassets

				// Super Rare = 125
				new MysteryItem(13576, 1, 1, 250).announce(), // DWH
                new MysteryItem(13036, 1, 1, 250), // Gilded (lg)
                new MysteryItem(13038, 1, 1, 250), // Gilded (sk)
                new MysteryItem(962, 1, 1, 250).announce(), // Christmas Cracker
				new MysteryItem(23258, 1, 1, 250), // Gilded coif
				new MysteryItem(23261, 1, 1, 250), // Gilded vambs
				new MysteryItem(23264, 1, 1, 250), // Gilded body
				new MysteryItem(23267, 1, 1, 250), // Gilded chaps
				new MysteryItem(23276, 1, 1, 250), // Gilded pickaxe
				new MysteryItem(23279, 1, 1, 250), // Gilded axe
				new MysteryItem(23282, 1, 1, 250), // Gilded spade

				// Rare+- = 50
				new MysteryItem(32066, 1, 1, 150).announce(), // Pink partyhat
				new MysteryItem(32068, 1, 1, 150).announce(), // Orange partyhat
				new MysteryItem(5607, 1, 1, 150).announce(), // Grain
				new MysteryItem(32146, 1, 1, 150).announce(), // Bandos Ornament Kit
                new MysteryItem(22840, 1, 1, 150).announce(), // Golden Tench
                new MysteryItem(10330, 1, 1, 150).announce(), // 3a r top
                new MysteryItem(10332, 1, 1, 150).announce(), // 3a r bottoms
                new MysteryItem(10334, 1, 1, 150).announce(), // 3a r coif
                new MysteryItem(10336, 1, 1, 150).announce(), // 3a r vambs
                new MysteryItem(10338, 1, 1, 150).announce(), // 3a mage top
                new MysteryItem(10340, 1, 1, 150).announce(), // 3a mage robe
                new MysteryItem(10342, 1, 1, 150).announce(), // 3a mage hat
                new MysteryItem(10344, 1, 1, 150).announce(), // 3a ammy
                new MysteryItem(10346, 1, 1, 150).announce(), // 3a m platelegs
                new MysteryItem(23242, 1, 1, 150).announce(), // 3a m plateskirt
                new MysteryItem(10348, 1, 1, 150).announce(), // 3a m platebody
                new MysteryItem(10350, 1, 1, 150).announce(), // 3a m full helm
                new MysteryItem(10352, 1, 1, 150).announce(), // 3a m kiteshield
                new MysteryItem(23185, 1, 1, 150).announce(), // ring of 3a
                new MysteryItem(32060, 1, 1, 150).announce(), // lime whip
				new MysteryItem(11847, 1, 1, 150).announce(), // Black h'ween

				// Rarity = 15
				new MysteryItem(11863, 1, 1, 75).announce(), // rainbow partyhat
                new MysteryItem(12422, 1, 1, 75).announce(), // 3a wand
                new MysteryItem(12424, 1, 1, 75).announce(), // 3a bow
                new MysteryItem(12426, 1, 1, 75).announce(), // 3a sword
                new MysteryItem(20011, 1, 1, 75).announce(), // 3a axe
                new MysteryItem(20014, 1, 1, 75).announce(), // 3a pickaxe
                new MysteryItem(23336, 1, 1, 75).announce(), // 3a druidic top
                new MysteryItem(23339, 1, 1, 75).announce(), // 3a druidic bottoms
                new MysteryItem(23342, 1, 1, 75).announce(), // 3a druidic staff
                new MysteryItem(23345, 1, 1, 75).announce() // 3a druidic cloak
		};

		totalWeight = MysteryItem.calculateTotalWeight(rewards);

		supplies = new MysterySupplyItem[]{
				new MysterySupplyItem(11232, 400, 800),
				new MysterySupplyItem(22804, 300, 600),
				new MysterySupplyItem(20849, 300, 600),
				new MysterySupplyItem(13440, 200, 400),
				new MysterySupplyItem(3143, 200, 400),
				new MysterySupplyItem(21976, 100, 200),
				new MysterySupplyItem(2, 1500, 3000),
				new MysterySupplyItem(11959, 200, 400),
				new MysterySupplyItem(21946, 200, 400),
				new MysterySupplyItem(21944, 200, 400),
				new MysterySupplyItem(21948, 200, 300),
				new MysterySupplyItem(12696, 50, 100),
				new MysterySupplyItem(10926, 50, 100),
				new MysterySupplyItem(22462, 40, 90),
				new MysterySupplyItem(ItemId.ZULRAHS_SCALES, 1000, 7500),
				new MysterySupplyItem(23962, 50, 100),
				new MysterySupplyItem(ItemId.ANCIENT_SHARD, 1, 7)
		};
	}

	@Override
	public int[] getItems() {
		return new int[]{32165};
	}
}
