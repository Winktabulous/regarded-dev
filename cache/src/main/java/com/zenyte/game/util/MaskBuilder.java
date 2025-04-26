package com.zenyte.game.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kris | 1. apr 2018 : 23:51.21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class MaskBuilder {

	public MaskBuilder(final AccessMask... masks) {
		this.masks = Arrays.asList(masks);
	}
	
	/**
	 * The list of enabled access masks.
	 */
	private final List<AccessMask> masks;
	
	/**
	 * Adds an access mask to the list of enabled masks.
	 * @param mask the access mask to enqueue.
	 */
	public void addMask(final AccessMask mask) {
		if (!masks.contains(mask))
			masks.add(mask);
	}
	
	/**
	 * Removes an access mask from the list of enabled masks.
	 * @param mask the access mask to remove.
	 */
	public void removeMask(final AccessMask mask) {
		masks.remove(mask);
	}
	
	/**
	 * Gets the value of all the enabled access masks combined.
	 * @return value of all the masks combined.
	 */
	public int getValue() {
		int value = 0;
		for (final AccessMask mask : masks)
			value |= mask.getValue();
		return value;
	}
	
	public static final int getValue(final AccessMask... masks) {
		int value = 0;
		for (final AccessMask mask : masks)
			value |= mask.getValue();
		return value;
	}

}
