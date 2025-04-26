package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

public final class CrystalOfMemoriesStructure implements TeleportStructure {

	private static final Animation START_ANIM = new Animation(8794);

	private static final Graphics START_GRAPHICS = new Graphics(1824);
	private static final Graphics END_GRAPHICS = new Graphics(1825);

	@Override
	public Animation getStartAnimation() {
		return START_ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return START_GRAPHICS;
	}

	@Override
	public Graphics getEndGraphics() {
		return END_GRAPHICS;
	}
}
