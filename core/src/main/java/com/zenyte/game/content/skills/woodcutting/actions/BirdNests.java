package com.zenyte.game.content.skills.woodcutting.actions;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.farming.Seedling;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Corey
 * @since 17/11/18
 */
public class BirdNests {
	private static final NestLoot[] SEEDS = new NestLoot[] {new NestLoot(Seedling.OAK.getSeed(), 210), new NestLoot(Seedling.APPLE.getSeed(), 172), new NestLoot(Seedling.WILLOW.getSeed(), 142), new NestLoot(Seedling.BANANA.getSeed(), 109), new NestLoot(Seedling.ORANGE.getSeed(), 87), new NestLoot(Seedling.CURRY.getSeed(), 64), new NestLoot(Seedling.MAPLE.getSeed(), 55), new NestLoot(Seedling.PINEAPPLE.getSeed(), 38), new NestLoot(Seedling.PAPAYA.getSeed(), 31), new NestLoot(Seedling.PALM.getSeed(), 21), new NestLoot(Seedling.YEW.getSeed(), 29), new NestLoot(Seedling.CALQUAT.getSeed(), 13), new NestLoot(Seedling.SPIRIT.getSeed(), 11), new NestLoot(Seedling.MAGIC.getSeed(), 7), new NestLoot(Seedling.TEAK.getSeed(), 6), new NestLoot(Seedling.DRAGONFRUIT.getSeed(), 6), new NestLoot(Seedling.CELASTRUS.getSeed(), 5), new NestLoot(Seedling.MAHOGANY.getSeed(), 5), new NestLoot(Seedling.REDWOOD.getSeed(), 4)};
	private static final NestLoot[] RINGS = new NestLoot[] {new NestLoot(CraftingDefinitions.JewelleryData.GOLD_RING.getProduct(), 34), new NestLoot(CraftingDefinitions.JewelleryData.SAPPHIRE_RING.getProduct(), 28), new NestLoot(CraftingDefinitions.JewelleryData.EMERALD_RING.getProduct(), 15), new NestLoot(CraftingDefinitions.JewelleryData.RUBY_RING.getProduct(), 10), new NestLoot(CraftingDefinitions.JewelleryData.DIAMOND_RING.getProduct(), 5)};

	public static void main(String[] args) {
		final HashMap<Item, Integer> results = new HashMap<Item, Integer>();
		final BirdNests.Nests nest = Nests.SEED;
		System.out.println(Arrays.toString(nest.cumulativeProbability));
		for (int i = 0; i < 100000; i++) {
			final Item loot = nest.rollLoot();
			final Integer count = results.getOrDefault(loot, 0);
			results.put(loot, count + 1);
		}
		System.out.println("Results:");
		for (final Map.Entry<Item, Integer> result : results.entrySet()) {
			System.out.println(result.getKey().getId() + " = " + (float) result.getValue() / 1000);
		}
	}


	public enum Nests {
		// TODO birdhouse nests
		RED_EGG(5070, new NestLoot(5076, 100)), GREEN_EGG(5071, new NestLoot(5078, 100)), BLUE_EGG(5072, new NestLoot(5077, 100)), SEED(5073, SEEDS), RING(5074, RINGS), EMPTY(5075);
		public static final Nests[] VALUES = values();
		public static final Int2ObjectOpenHashMap<Nests> NESTS = new Int2ObjectOpenHashMap<>(VALUES.length);
		public static final Int2ObjectOpenHashMap<Nests> LOOTABLE_NESTS = new Int2ObjectOpenHashMap<>(VALUES.length);

		static {
			for (final BirdNests.Nests nest : VALUES) {
				NESTS.put(nest.getNestItemId(), nest);
				if (nest != EMPTY) {
					LOOTABLE_NESTS.put(nest.getNestItemId(), nest);
				}
			}
		}

		private final int nestItemId;
		private final NestLoot[] loot;
		private final int[] cumulativeProbability;

		Nests(int nestItemId, NestLoot... loot) {
			this.nestItemId = nestItemId;
			this.loot = loot;
			this.cumulativeProbability = getCumulativeProbability();
		}

		public static Nests rollRandomNest(final boolean eggs) {
			final int typeRoll = Utils.random(eggs ? 0 : 5, 100);
			if (typeRoll <= 4) {
				final BirdNests.Nests[] eggNests = new Nests[] {Nests.RED_EGG, Nests.GREEN_EGG, Nests.BLUE_EGG};
				final int eggRoll = Utils.random(eggNests.length - 1);
				return eggNests[eggRoll];
			} else if (typeRoll <= 33) {
				return Nests.RING;
			} else {
				return Nests.SEED;
			}
		}

		private int[] getCumulativeProbability() {
			if (loot.length < 1) {
				return null;
			}
			final int[] probabilities = new int[loot.length];
			int accumulator = 0;
			for (int i = 0; i < loot.length; i++) {
				accumulator += loot[i].chance;
				probabilities[i] = accumulator;
			}
			return probabilities;
		}

		public Item rollLoot() {
			if (cumulativeProbability == null) {
				return null;
			} else if (loot.length == 1) {
				return loot[0].getItem();
			}
			final int maxValue = cumulativeProbability[cumulativeProbability.length - 1];
			final int randomValue = Utils.random(maxValue - 1);
			int lootIndex = 0;
			//Fail safe incase someone fucks something up.
			int count = 100;
			while (--count > 0 && cumulativeProbability[lootIndex] <= randomValue) {
				lootIndex++;
			}
			return loot[lootIndex].getItem();
		}

		public int getNestItemId() {
			return nestItemId;
		}
	}


	private static class NestLoot {
		private final Item item;
		private final int chance;

		NestLoot(int itemId, int chance) {
			this.item = new Item(itemId, 1);
			this.chance = chance;
		}

		public NestLoot(Item item, int chance) {
			this.item = item;
			this.chance = chance;
		}

		public Item getItem() {
			return item;
		}

		public int getChance() {
			return chance;
		}
	}
}
