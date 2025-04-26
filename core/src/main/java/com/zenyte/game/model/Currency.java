package com.zenyte.game.model;

import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28. aug 2018 : 00:30:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum Currency {

	THOUSAND(1000),
	MILLION(1000000),
	BILLION(1000000000);

	private final int value;

	public int get(final int amount) {
		return amount * value;
	}

	Currency(int value) {
		this.value = value;
	}

	public static int get(@NotNull final String amount) {
		final StringBuilder builder = new StringBuilder(10);
		for (final char c : amount.toCharArray()) {
			if (c == 'k' || c == 'K') {
				builder.append("000");
			} else if (c == 'm' || c == 'M') {
				builder.append("000000");
			} else if (c == 'b' || c == 'B') {
				builder.append("000000000");
			} else if (c >= 30 && c <= 39) {
				builder.append(c);
			}
		}
		return Integer.parseInt(builder.toString());
	}
}
