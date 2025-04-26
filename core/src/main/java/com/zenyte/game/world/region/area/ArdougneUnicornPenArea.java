package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 23. sept 2018 : 22:44:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class ArdougneUnicornPenArea extends EastArdougneArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 2629, 3272 }, { 2627, 3270 }, { 2627, 3265 }, { 2628, 3264 }, { 2637, 3264 },
				{ 2638, 3265 }, { 2638, 3270 }, { 2636, 3272 } }, 0) };
	}

	@Override
	public void enter(final Player player) {
		player.getAchievementDiaries().update(ArdougneDiary.ENTER_UNICORN_PEN);
	}

	@Override
	public void leave(final Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Ardougne Unicorn Pen";
	}

}
