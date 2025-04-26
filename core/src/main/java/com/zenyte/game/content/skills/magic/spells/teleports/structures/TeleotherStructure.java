package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;

/**
 * @author Kris | 16. juuli 2018 : 01:04:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class TeleotherStructure implements TeleportStructure {

	private static final Animation ANIM = new Animation(1816);
	private static final Graphics GFX = new Graphics(342);
	
	@Override
	public Animation getStartAnimation() {
		return ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return GFX;
	}

}
