package com.zenyte.game.world.region.area.memberzones;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.plugins.itemonobject.LegendaryFountainOfRunePlugin;


public class LegendaryFountainOfRuneArea extends LegendaryZone {

    public LegendaryFountainOfRuneArea() {
		LegendaryFountainOfRunePlugin.initiate(this.players);
	}

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3379, 7598 }, { 3379, 7588 }, { 3388, 7588 }, { 3388, 7598 } }, 0) };
	}
	
	@Override
	public void enter(final Player player) {
		super.enter(player);
		VarCollection.FOUNTAIN_OF_RUNE.send(player, 1);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		super.leave(player, logout);
		VarCollection.FOUNTAIN_OF_RUNE.send(player, 0);
	}

	@Override
	public String name() {
		return "Legendary Zone Fountain of Rune";
	}
	
}
