package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 23. sept 2018 : 22:32:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class CombatTrainingCampArea extends KingdomOfKandarin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2511, 3387 }, { 2510, 3386 }, { 2509, 3386 }, { 2509, 3385 }, { 2508, 3384 },
				{ 2507, 3384 }, { 2503, 3380 }, { 2503, 3366 }, { 2508, 3361 }, { 2515, 3361 }, { 2516, 3360 }, { 2516, 3358 },
				{ 2517, 3357 }, { 2519, 3357 }, { 2520, 3358 }, { 2520, 3360 }, { 2521, 3361 }, { 2527, 3361 }, { 2534, 3368 },
				{ 2534, 3380 }, { 2533, 3381 }, { 2533, 3382 }, { 2530, 3385 }, { 2529, 3385 }, { 2527, 3387 } }, 0) };
	}

	@Override
	public void enter(final Player player) {
	    super.enter(player);
		player.getAchievementDiaries().update(ArdougneDiary.ENTER_COMBAT_TRAINING_CAMP);
	}

	@Override
	public String name() {
		return "Combat Training Camp";
	}

}
