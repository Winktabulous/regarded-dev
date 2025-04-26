package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 9. juuli 2018 : 21:50:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class AncientStructure implements TeleportStructure {

	private static final Animation START_ANIM = new Animation(1979);
	private static final Graphics START_GRAPHICS = new Graphics(392);

	@Override
	public Animation getStartAnimation() {
		return START_ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return START_GRAPHICS;
	}

}
