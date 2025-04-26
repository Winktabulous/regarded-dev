package com.zenyte.game.content.minigame.pestcontrol.area;

import com.zenyte.game.content.minigame.pestcontrol.PestControlGameType;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 26. juuni 2018 : 19:51:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class IntermediateLanderArea extends AbstractLanderArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2638, 2648 }, { 2638, 2642 }, { 2642, 2642 }, { 2642, 2648 } }, 0) };
	}

	@Override
	public String name() {
		return "Pest Control Intermediate Lander";
	}

	@Override
	public PestControlGameType getType() {
		return PestControlGameType.INTERMEDIATE;
	}

}
