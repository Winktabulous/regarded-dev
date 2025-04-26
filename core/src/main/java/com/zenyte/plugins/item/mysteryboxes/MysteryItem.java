package com.zenyte.plugins.item.mysteryboxes;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MysteryItem {

	private final int id;
	private final int minAmount;
	private final int maxAmount;
	private final int weight;
	private boolean checkHasItem;
	private boolean announce;
	private int charges = 0;

	public MysteryItem(int id) {
		this(id, 1, 1, 1);
	}

	public MysteryItem(int id, int weight) {
		this(id, 1, 1, weight);
	}

	public MysteryItem(int id, int minAmount, int maxAmount, int weight) { this(id, minAmount, maxAmount, weight, 0); }

	public MysteryItem(int id, int minAmount, int maxAmount, int weight, int charges) {
		this.id = id;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.weight = weight;
		this.charges = charges;
	}

	public int getId() {
		return id;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public int getWeight() {
		return weight;
	}

	public MysteryItem setCheckHasItem() {
		this.checkHasItem = true;
		return this;
	}

	public MysteryItem announce() {
		this.announce = true;
		return this;
	}

	public MysteryItem charges(int ch) {
		this.charges = ch;
		return this;
	}

	public boolean isAnnounce() {
		return announce;
	}

	public boolean checkHasItem() {
		return checkHasItem;
	}

	public static int calculateTotalWeight(MysteryItem[] items) {
		int totalWeight = 0;

		for (MysteryItem item : items) {
			totalWeight += item.weight;
		}

		return totalWeight;
	}

	public static MysteryItem generate(MysteryItem[] items, int totalWeight) {
		final int random = Utils.random(totalWeight);
		int current = 0;
		for (final MysteryItem it : items) {
			if ((current += it.weight) >= random) {
				return it;
			}
		}
		return null;
	}

	@NotNull
	public static final MysteryItem generateItem(@NotNull final Player player, MysteryItem[] items, int totalWeight) {
		MysteryItem rewardItem;
		while (true) {
			rewardItem = generate(items, totalWeight);
			if (rewardItem.checkHasItem() && player.containsItem(rewardItem.getId())) {
				continue;
			}
			break;
		}
		return Objects.requireNonNull(rewardItem);
	}

	public static void main(String[] args) {
		final int total = 1_000_000;
		final Map<MysteryItem, Integer> count = new HashMap<>();
		for (int i = 0; i < total; i++) {
			final MysteryItem mysteryItem = generate(MysteryBox.rewards, MysteryBox.totalWeight);
			final int previous = count.getOrDefault(mysteryItem, 1);
			count.put(mysteryItem, previous + 1);
		}
		count.entrySet().stream()
				.sorted(Comparator.comparingInt(Map.Entry::getValue))
				.forEachOrdered(entry -> System.out.println(entry.getKey() + " - " + entry.getValue() / (total / 100.0)));
	}

	public int getCharges() {
		return charges;
	}
}

