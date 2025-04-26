package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.world.entity.EntityHitBar;

/**
 * @author Jire
 */
public final class ZalcanoBossHitBar extends EntityHitBar {

	private final ZalcanoBoss zalcanoBoss;

	public ZalcanoBossHitBar(ZalcanoBoss zalcanoBoss) {
		super(zalcanoBoss);
		this.zalcanoBoss = zalcanoBoss;
	}

	@Override
	public int getType() {
		return zalcanoBoss.isShieldPhase() ? 29 : 21;
	}

}
