package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 5. sept 2018 : 02:54:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SkullSceptreStructure implements TeleportStructure {

	private static final Animation START_ANIM = new Animation(714);
	private static final Graphics START_GFX = new Graphics(111, 0, 70);

	@Override
	public Animation getStartAnimation() {
		return START_ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return START_GFX;
	}
	
}
