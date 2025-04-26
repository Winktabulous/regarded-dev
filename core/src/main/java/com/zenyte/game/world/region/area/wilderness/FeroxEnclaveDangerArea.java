package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.RSPolygon;

public class FeroxEnclaveDangerArea extends WildernessArea {

	@Override
	protected RSPolygon[] polygons() {
		return new RSPolygon[]{
				new RSPolygon(3155, 3634, 3161, 3636),
				new RSPolygon(3129, 3610, 3141, 3617),
				new RSPolygon(3140, 3616, 3145, 3619),
				new RSPolygon(3124, 3616, 3130, 3622),
				new RSPolygon(3120, 3621, 3124, 3624),
				new RSPolygon(3118, 3623, 3123, 3635),
				new RSPolygon(3120, 3635, 3125, 3640),
				new RSPolygon(3124, 3640, 3138, 3644),
		};
	}

	@Override
	public void enter(Player player) {
		super.enter(player);

		if (player.getVariables().getTime(TickVariable.TELEBLOCK) <= 0) {
			player.getVarManager().sendBit(10530, 1);
		}
	}

	@Override
	public void leave(Player player, boolean logout) {
		super.leave(player, logout);

		if (player.getVariables().getTime(TickVariable.TELEBLOCK) <= 0) {
			player.getVarManager().sendBit(10530, 0);
		}
	}

	@Override
	public boolean attack(Player player, Entity entity, PlayerCombat combat) {
		if (entity instanceof Player) {
			Player t = (Player) entity;
			if (t.getVariables().getTime(TickVariable.TELEBLOCK) <= 0 && player.getVariables().getTime(TickVariable.TELEBLOCK) <= 0) {
				player.sendMessage("You cannot fight another player whilst next to the Enclave, please move further out.");
				return false;
			}
		}

		return super.attack(player, entity, combat);
	}

	@Override
	public String name() {
		return "Wilderness: Ferox Enclave danger area";
	}

}
