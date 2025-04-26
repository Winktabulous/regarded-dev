package com.zenyte.game.world.region.area;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 29 aug. 2018 | 20:49:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SmokeDungeonArea extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3199, 9406 }, { 3328, 9407 }, { 3329, 9344 }, { 3201, 9343 } }) };
	}

	@Override
	public void enter(final Player player) {
	    player.getTeleportManager().unlock(PortalTeleport.SMOKE_DUNGEON);
		player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 313);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
	}

	@Override
	public String name() {
		return "Smoke Dungeon";
	}

}
