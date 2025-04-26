package com.zenyte.game.content.boss.nightmare.area;

import com.zenyte.game.content.boss.nightmare.NightmareLobbyNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;

import java.util.function.Consumer;

public class NightmareLobbyArea extends PolygonRegionArea {

	public static NightmareLobbyNPC lobbyNPC;

	@Override
	protected RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(3798, 9749, 3818, 9769)};
	}

	@Override
	public void enter(Player player) {

	}

	@Override
	public void leave(Player player, boolean logout) {

	}

	public static void forEachPlayer(Consumer<Player> consumer) {
		for (Player player : GlobalAreaManager.getArea(NightmareLobbyArea.class).getPlayers()) {
			if (player == null) {
				continue;
			}

			consumer.accept(player);
		}
	}

	@Override
	public String name() {
		return "Nightmare lobby";
	}

}
