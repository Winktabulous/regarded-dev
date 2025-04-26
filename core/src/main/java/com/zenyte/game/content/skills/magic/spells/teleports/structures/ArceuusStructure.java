package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 13. juuli 2018 : 22:13:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ArceuusStructure implements TeleportStructure {

	private static final Animation ANIMATION = new Animation(3865);
	private static final Graphics GRAPHICS = new Graphics(1296);
	
	@Override
	public Animation getStartAnimation() {
		return ANIMATION;
	}

	@Override
	public Graphics getStartGraphics() {
		return GRAPHICS;
	}

}
