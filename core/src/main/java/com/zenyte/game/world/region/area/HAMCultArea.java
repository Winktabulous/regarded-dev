package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 2 okt. 2018 | 14:20:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class HAMCultArea extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3160, 9607 }, { 3167, 9615 }, { 3176, 9608 }, { 3187, 9605 }, { 3191, 9609 }, { 3177, 9623 }, { 3189, 9628 }, { 3188, 9637 }, { 3178, 9636 }, { 3178, 9640 }, { 3182, 9644 }, { 3177, 9661 }, { 3167, 9655 }, { 3170, 9641 }, { 3162, 9643 }, { 3151, 9660 }, { 3143, 9649 }, { 3155, 9637 }, { 3138, 9620 }, { 3145, 9613 }, { 3153, 9618 }, { 3156, 9616 }, { 3152, 9612 }, { 3158, 9606 } }) };
	}

	@Override
	public void enter(Player player) {
		player.getAchievementDiaries().update(LumbridgeDiary.ENTER_HAM_HIDEOUT);
	}

	@Override
	public void leave(Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "H.A.M. Cult";
	}

}
