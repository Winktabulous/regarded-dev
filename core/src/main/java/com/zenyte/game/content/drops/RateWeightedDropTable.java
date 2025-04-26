package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.RollResult;
import com.zenyte.game.content.drops.table.slot.BasicSlot;
import com.zenyte.game.util.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Savions.
 */
public class RateWeightedDropTable {

	private float totalWeight;
	private final ArrayList<Pair> table = new ArrayList<Pair>();
	private final Random random = Utils.getRandom();

	public final void append(int id, int weight, int amount) {
		append(id, weight, amount, amount);
	}

	public final void append(int id, int weight, int minAmount, int maxAmount) {
		assert(minAmount <= maxAmount);
		assert(id >= 1);
		assert(minAmount >= 1);

		final float inverseWeight = 1.0F / weight;
		totalWeight += inverseWeight;
		table.add(new Pair(new BasicSlot(id, minAmount, maxAmount), inverseWeight));
	}

	public final RollResult roll() {
		assert(totalWeight != 0);

		final float randomValue = random.nextFloat() * totalWeight;
		float currentSlot = 0;
		for (Pair pair : table) {
			if (randomValue <= currentSlot + pair.weight) {
				return pair.slot.evaluate(random);
			}
			currentSlot += pair.weight;
		}

		//should never happen
		return table.get(0).slot.evaluate(random);
	}

	@Override public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return super.equals(o);
	}

	@Override public int hashCode() {
		var result = random.hashCode();
		result = 31 * result + table.hashCode();
		result = 31 * result + (int) totalWeight;
		return result;
	}

	private static class Pair {

		private final BasicSlot slot;
		private final float weight;

		public Pair(final BasicSlot slot, final float weight) {
			this.slot = slot;
			this.weight = weight;
		}
	}
}