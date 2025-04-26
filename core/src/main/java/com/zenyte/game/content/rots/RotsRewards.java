package com.zenyte.game.content.rots;

import com.zenyte.game.item.ItemId;
import com.zenyte.plugins.item.mysteryboxes.MysteryItem;

public class RotsRewards {

	public static final MysteryItem[] supplies;

	static {
		double mod = 1.5;
		supplies = new MysteryItem[] {
				new MysteryItem(ItemId.COINS_995, 1, (int) (10612 * mod), 1),
				new MysteryItem(ItemId.MIND_RUNE, 1, (int) (2889 * mod), 1),
				new MysteryItem(ItemId.CHAOS_RUNE, 1, (int) (885 * mod), 1),
				new MysteryItem(ItemId.DEATH_RUNE, 1, (int) (578 * mod), 1),
				new MysteryItem(ItemId.BLOOD_RUNE, 1, (int) (236 * mod), 1),
				new MysteryItem(ItemId.BOLT_RACK, 1, (int) (231 * mod), 1),
				new MysteryItem(ItemId.WRATH_RUNE, 1, (int) (236 * mod), 1),
				new MysteryItem(ItemId.LOOP_HALF_OF_KEY, 1),
				new MysteryItem(ItemId.TOOTH_HALF_OF_KEY, 1),
				new MysteryItem(ItemId.DRAGON_MED_HELM, 1),
				new MysteryItem(ItemId.DRAGON_DART_TIP, 25, 75, 1),
				new MysteryItem(ItemId.DRAGON_BONES + 1, 5, 25, 1),
				new MysteryItem(ItemId.GRIMY_TOADFLAX + 1, 5, 25, 1),
				new MysteryItem(ItemId.GRIMY_RANARR_WEED + 1, 5, 25, 1),
				new MysteryItem(ItemId.GRIMY_SNAPDRAGON + 1, 5, 20, 1),
				new MysteryItem(ItemId.MANTA_RAY + 1, 10, 40, 1),
				new MysteryItem(ItemId.EGG_POTATO + 1, 10, 40, 1),
				new MysteryItem(ItemId.SARADOMIN_BREW4 + 1, 5, 10, 1),
				new MysteryItem(ItemId.SUPER_RESTORE4 + 1, 5, 10, 1),
				new MysteryItem(ItemId.SUPER_COMBAT_POTION4 + 1, 5, 10, 1),
		};
	}

}
