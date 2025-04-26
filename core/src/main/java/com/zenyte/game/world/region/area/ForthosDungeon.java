package com.zenyte.game.world.region.area;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

public class ForthosDungeon extends PolygonRegionArea {

	@Override
	protected RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(7322), new RSPolygon(7323) };
	}

	@Override
	public void enter(Player player) {

	}

	@Override
	public void leave(Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Forthos Dungeon";
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

}
