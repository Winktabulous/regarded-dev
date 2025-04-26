package com.zenyte.game.world.region.area.wizardsguild;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.Yanille;

/**
 * @author Kris | 24. juuni 2018 : 16:21:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WizardsGuildArea extends Yanille {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2589, 3094 }, { 2585, 3090 }, { 2585, 3086 }, { 2589, 3082 }, { 2593, 3082 },
				{ 2597, 3086 }, { 2597, 3090 }, { 2593, 3094 } }, 0, 1, 2) };
	}

	@Override
	public String name() {
		return "Wizards guild";
	}

}
