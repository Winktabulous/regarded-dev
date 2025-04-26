package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

public final class WildernessStructure implements TeleportStructure {

	private static final Animation START_ANIM = new Animation(3945);

	private static final Graphics START_GRAPHICS = new Graphics(661);

	@Override
	public Animation getStartAnimation() {
		return START_ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return START_GRAPHICS;
	}

}
