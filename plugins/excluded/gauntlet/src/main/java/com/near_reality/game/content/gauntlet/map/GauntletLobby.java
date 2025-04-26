package com.near_reality.game.content.gauntlet.map;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;

public class GauntletLobby extends PolygonRegionArea implements LootBroadcastPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[]{new RSPolygon(new int[][]{{3008, 6080},
				{3008, 6143},
				{3072, 6144},
				{3072, 6080}})};
	}

	@Override
	public void enter(Player player) {

	}

	@Override
	public void leave(Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Gauntlet Lobby";
	}
}
