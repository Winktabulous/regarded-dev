package com.zenyte.game.content.chambersofxeric.skills;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 3. mai 2018 : 01:37:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidRakingAction extends Action {
	/**
	 * The rake item.
	 */
	private static final Item rakeItem = new Item(5341);
	/**
	 * The animation played when raking a farming patch.
	 */
	private static final Animation rakingAnimation = new Animation(2273);
	/**
	 * The sound effect played when raking a patch.
	 */
	private static final SoundEffect rakingSound = new SoundEffect(2442);
	/**
	 * The three possible raids seeds that a player can farm from it. Every time they farm a seed, they can no longer get that specific seed back to back.
	 */
	private static final int[] seeds = new int[] {20903, 20906, 20909};
	/**
	 * The amount of seeds they can rake out of the patch, decreasing with each successful raking.
	 */
	private int amount = 5;
	/**
	 * The item id of the last seed received, used to prevent getting back-to-back identical seeds.
	 */
	private int lastSeed;

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(rakeItem)) {
			player.sendMessage("You need a rake to do that.");
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			for (final int seed : seeds) {
				if (!player.getInventory().containsItem(seed)) {
					player.sendMessage("You need some free inventory space to do this.");
					return false;
				}
			}
		}
		player.setAnimation(rakingAnimation);
		player.sendSound(rakingSound);
		delay(4);
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		if (!player.getInventory().hasFreeSlots()) {
			for (final int seed : seeds) {
				if (!player.getInventory().containsItem(seed)) {
					player.sendMessage("You need some free inventory space to do this.");
					player.setAnimation(Animation.STOP);
					return -1;
				}
			}
		}
		player.setAnimation(rakingAnimation);
		final int lastSeed = this.lastSeed;
		final IntArrayList availableSeeds = new IntArrayList(seeds);
		availableSeeds.rem(lastSeed);
		this.lastSeed = Utils.getRandomCollectionElement(availableSeeds);
		player.getInventory().addItem(this.lastSeed, Utils.random(1, 3));
		if (--amount <= 0) {
			player.setAnimation(Animation.STOP);
			return -1;
		}
		player.sendSound(rakingSound);
		return 3;
	}
}
