package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 25 sep. 2018 | 20:50:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class AsgarnianIceDungeon extends PolygonRegionArea {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][]{
                { 2983, 9600 },
                { 2983, 9536 },
                { 3017, 9536 },
                { 3017, 9531 },
                { 3072, 9531 },
                { 3072, 9536 },
                { 3088, 9536 },
                { 3088, 9600 },
                { 3037, 9600 },
                { 3037, 9602 },
                { 3030, 9602 },
                { 3030, 9600 }
        }) };
	}

	@Override
	public void enter(Player player) {
	    player.getTeleportManager().unlock(PortalTeleport.ASGARNIAN_ICE_CAVES);
	}

	@Override
	public void leave(Player player, boolean logout) {
	}

	@Override
	public String name() {
		return "Asgarnian Ice Dungeon";
	}

}
