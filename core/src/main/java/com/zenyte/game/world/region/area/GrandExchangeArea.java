package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.LayableTrapRestrictionPlugin;

/**
 * @author Kris | 16. mai 2018 : 16:25:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class GrandExchangeArea extends KingdomOfMisthalin implements CannonRestrictionPlugin, LayableTrapRestrictionPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3139, 3473 }, { 3144, 3468 }, { 3163, 3468 }, { 3163, 3467 }, { 3167, 3467 },
				{ 3167, 3468 }, { 3187, 3468 }, { 3187, 3476 }, { 3190, 3479 }, { 3190, 3497 }, { 3196, 3503 }, { 3196, 3504 },
				{ 3198, 3506 }, { 3198, 3508 }, { 3189, 3517 }, { 3171, 3517 }, { 3168, 3514 }, { 3161, 3514 }, { 3158, 3517 },
				{ 3143, 3517 }, { 3139, 3513 }, { 3139, 3495 }, { 3142, 3492 }, { 3142, 3485 }, { 3139, 3482 } }) };
	}

	@Override
	public String name() {
		return "Grand Exchange";
	}

    @Override
    public String restrictionMessage() {
        return "The Grand Exchange staff prefer not to have heavy artillery operated around their premises.";
    }
}
