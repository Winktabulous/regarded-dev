package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 2 jun. 2018 | 19:46:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class EssenceMinePortalObject implements ObjectAction {
	private static final Location AUBURY_SHOP_LOCATION = new Location(3253, 3400, 0);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		Location location = new Location(player.getNumericAttribute("essence_mine_departure").intValue());
		System.out.println(location.getPositionHash());
		if (location.getPositionHash() == 0) {
			location = new Location(AUBURY_SHOP_LOCATION.getPositionHash());
		}
		player.setGraphics(CombatSpell.CURSE.getHitGfx());
		player.setLocation(location);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {34825};
	}
}
