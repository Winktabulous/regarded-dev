package com.zenyte.game.content.hiscores;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class HiscoresScore implements Comparable<HiscoresScore> {

	private final String name;
	private final long[] values;
	private final long timestamp;

	HiscoresScore(String name, long[] values) {
		this.name = name;
		this.values = values;
		this.timestamp = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public long[] getValues() {
		return values;
	}

	@Override
	public int compareTo(@NotNull HiscoresScore o) {
		int result = Arrays.compare(values, o.values) * -1;
		if (result == 0) {
			return Long.compare(timestamp, o.timestamp);
		}

		return result;
	}

	@Override
	public String toString() {
		return "HiscoresScore{" +
				"values=" + Arrays.toString(values) +
				", timestamp=" + timestamp +
				'}';
	}

}
