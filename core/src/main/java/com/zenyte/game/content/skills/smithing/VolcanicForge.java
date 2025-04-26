package com.zenyte.game.content.skills.smithing;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Action;

import java.util.Map;

/**
 * @author Tommeh | 30 aug. 2018 | 18:41:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VolcanicForge extends Action {
	private final int ward;
	private int ticks;
	public static final Map<Integer, Integer[]> WARDS = ImmutableMap.<Integer, Integer[]>builder().put(11924, new Integer[] {11931, 11932, 11933}).put(11926, new Integer[] {11928, 11929, 11930}).build();
	private static final Animation ANIMATION = new Animation(734, 5);

	@Override
	public boolean start() {
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public boolean interruptedByCombat() {
		return false;
	}

	@Override
	public boolean interruptedByDialogue() {
		return false;
	}

	@Override
	public int processWithDelay() {
		final Location location = new Location(player.getX(), player.getY() - 1, player.getPlane());
		switch (ticks++) {
		case 0: 
			player.setForceMovement(new ForceMovement(location, 20, ForceMovement.NORTH));
			player.setAnimation(ANIMATION);
			break;
		case 2: 
			player.setLocation(location);
			for (final Integer item : WARDS.get(ward)) {
				player.getInventory().deleteItem(item, 1);
			}
			player.getInventory().addItem(ward, 1);
			player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
			player.sendMessage("You forge the shield pieces together in the chambers of fire and are blown back by the intense heat.");
			player.unlock();
			break;
		}
		return 0;
	}

	public VolcanicForge(int ward) {
		this.ward = ward;
	}
}
