package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.plugins.itemonobject.FountainOfRunePlugin;

/**
 * @author Kris | 24. sept 2018 : 19:19:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class FountainOfRuneArea extends WildernessArea {

    public FountainOfRuneArea() {
        FountainOfRunePlugin.initiate(this.players);
    }

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3363, 3901 }, { 3363, 3885 }, { 3388, 3885 }, { 3388, 3901 } }, 0) };
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
		return "Wilderness Fountain of Rune";
	}
	
}
