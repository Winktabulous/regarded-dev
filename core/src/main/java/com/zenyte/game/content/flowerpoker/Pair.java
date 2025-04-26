package com.zenyte.game.content.flowerpoker;

public class Pair {
	public final int amount;
	public static Pair of(int pairs) {
		return new Pair(pairs);
	}
	public Pair(int amount) {
			this.amount = amount;
		}
}