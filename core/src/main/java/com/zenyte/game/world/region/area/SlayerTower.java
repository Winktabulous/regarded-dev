package com.zenyte.game.world.region.area;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;

/**
 * @author Kris | 16. mai 2018 : 16:31:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SlayerTower extends Morytania implements CannonRestrictionPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {
				new RSPolygon(new int[][] { { 3405, 3539 }, { 3405, 3535 }, { 3409, 3531 }, { 3413, 3531 }, { 3416, 3534 }, { 3419, 3534 },
						{ 3420, 3533 }, { 3424, 3533 }, { 3426, 3535 }, { 3426, 3536 }, { 3432, 3536 }, { 3432, 3535 }, { 3434, 3533 },
						{ 3438, 3533 }, { 3439, 3534 }, { 3442, 3534 }, { 3445, 3531 }, { 3449, 3531 }, { 3453, 3535 }, { 3453, 3539 },
						{ 3449, 3543 }, { 3449, 3547 }, { 3450, 3548 }, { 3450, 3552 }, { 3449, 3553 }, { 3449, 3558 }, { 3450, 3559 },
						{ 3450, 3563 }, { 3449, 3564 }, { 3449, 3568 }, { 3453, 3572 }, { 3453, 3576 }, { 3449, 3580 }, { 3445, 3580 },
						{ 3442, 3577 }, { 3439, 3577 }, { 3438, 3578 }, { 3434, 3578 }, { 3433, 3577 }, { 3425, 3577 }, { 3424, 3578 },
						{ 3420, 3578 }, { 3419, 3577 }, { 3416, 3577 }, { 3413, 3580 }, { 3409, 3580 }, { 3405, 3576 }, { 3405, 3572 },
						{ 3409, 3568 }, { 3409, 3564 }, { 3408, 3563 }, { 3408, 3559 }, { 3409, 3558 }, { 3409, 3553 }, { 3408, 3552 },
						{ 3408, 3548 }, { 3409, 3547 }, { 3409, 3543 } }),
				new RSPolygon(new int[][]{
						{ 3396, 9978 },
						{ 3396, 9924 },
						{ 3450, 9924 },
						{ 3450, 9978 }
				}, 3)};
	}

	@Override
	public void enter(final Player player) {
	    super.enter(player);
        player.getTeleportManager().unlock(PortalTeleport.SLAYER_TOWER);
	}

	@Override
	public String name() {
		return "Slayer Tower";
	}

}
