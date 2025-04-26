package com.zenyte.game.world.region.area;

import com.zenyte.game.content.skills.afk.AfkSkilling;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.LoginPlugin;

public final class MeiyerditchLaboratoriesArea extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {
				{ 3616, 9667 },
				{ 3603, 9679 },
				{ 3596, 9727 },
				{ 3524, 9724 },
				{ 3510, 9823 },
				{ 3562, 9843 },
				{ 3641, 9766 },
				{ 3643, 9668 }})};
	}

	@Override
	public void enter(final Player player) {
	}

	@Override
	public void leave(final Player player, boolean logout) {
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

	@Override
	public String name() {
		return "Meiyerditch Labratories";
	}

}
