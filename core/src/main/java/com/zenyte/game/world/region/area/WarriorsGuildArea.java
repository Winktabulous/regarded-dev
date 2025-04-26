package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 27. mai 2018 : 18:09:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class WarriorsGuildArea extends DeathPlateau implements CannonRestrictionPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {
				new RSPolygon(new int[][] { { 2838, 3556 }, { 2838, 3537 }, { 2839, 3537 }, { 2840, 3536 }, { 2841, 3536 }, { 2842, 3537 },
						{ 2845, 3537 }, { 2846, 3536 }, { 2847, 3536 }, { 2848, 3537 }, { 2849, 3537 }, { 2849, 3534 }, { 2860, 3534 },
						{ 2860, 3537 }, { 2865, 3537 }, { 2866, 3536 }, { 2867, 3536 }, { 2868, 3537 }, { 2871, 3537 }, { 2872, 3536 },
						{ 2873, 3536 }, { 2874, 3537 }, { 2876, 3537 }, { 2876, 3539 }, { 2877, 3540 }, { 2877, 3541 }, { 2876, 3542 },
						{ 2876, 3544 }, { 2877, 3545 }, { 2877, 3548 }, { 2876, 3549 }, { 2876, 3551 }, { 2877, 3552 }, { 2877, 3553 },
						{ 2876, 3554 }, { 2876, 3556 }, { 2874, 3556 }, { 2873, 3557 }, { 2872, 3557 }, { 2871, 3556 }, { 2868, 3556 },
						{ 2867, 3557 }, { 2866, 3557 }, { 2865, 3556 } }, 0, 1),
				new RSPolygon(new int[][] { { 2905, 9974 }, { 2905, 9966 }, { 2912, 9966 }, { 2912, 9974 } }, 0),
				new RSPolygon(new int[][] { { 2838, 3543 }, { 2838, 3537 }, { 2839, 3537 }, { 2840, 3536 }, { 2841, 3536 }, { 2842, 3537 },
						{ 2845, 3537 }, { 2846, 3536 }, { 2847, 3536 }, { 2848, 3537 }, { 2848, 3538 }, { 2847, 3538 }, { 2847, 3543 } },
						2) };
	}

	@Override
	public String name() {
		return "Warriors' guild";
	}

}
