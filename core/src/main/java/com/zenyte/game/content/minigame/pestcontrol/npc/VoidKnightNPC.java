package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;

/**
 * @author Kris | 26. juuni 2018 : 19:24:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class VoidKnightNPC extends PestNPC {

	public VoidKnightNPC(final PestControlInstance instance) {
		super(instance, null, PestControlUtilities.getRandomKnight(), instance.getLocation(PestControlUtilities.VOID_KNIGHT_LOCATION));
	}
	
	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		return false;
	}

	@Override
	public void processNPC() {}
	
	@Override
	public void sendDeath() {
		finish();
		instance.finish(false);
	}
	
	@Override
	public boolean setHitpoints(final int amount) {
		final int health = getHitpoints();
		final boolean dead = super.setHitpoints(amount);
		if (instance != null && health != amount) {
			instance.flag(PestControlInstance.KNIGHT_FLAG);
		}
		return dead;
	}

}
