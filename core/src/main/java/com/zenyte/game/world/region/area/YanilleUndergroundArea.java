package com.zenyte.game.world.region.area;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;

/**
 * @author Kris | 29. mai 2018 : 01:08:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class YanilleUndergroundArea extends PolygonRegionArea implements EntityAttackPlugin {
	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{2344, 9467}, {2337, 9441}, {2388, 9406}, {2423, 9404}, {2439, 9459}, {2411, 9474}, {2367, 9473}}, 0)};
	}

	@Override
	public void enter(final Player player) {
		if (player.getBooleanAttribute("SmokeOverlay")) {
			player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 313);
		}
	}

	@Override
	public void leave(final Player player, boolean logout) {
		if (player.getBooleanAttribute("SmokeOverlay")) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
		}
	}

	@Override
	public boolean attack(Player player, Entity entity, PlayerCombat combat) {
		if (entity instanceof NPC) {
			final String name = ((NPC) entity).getDefinitions().getName();
			if (name.equals("Smoke devil")) {
				if (!player.getSlayer().isCurrentAssignment(entity)) {
					player.sendMessage("You can only kill Smoke devils while you're on a slayer task.");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String name() {
		return "Smoke Devil Dungeon";
	}
}
