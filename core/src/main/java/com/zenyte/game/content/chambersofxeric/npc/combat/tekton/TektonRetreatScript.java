package com.zenyte.game.content.chambersofxeric.npc.combat.tekton;

import com.zenyte.game.content.chambersofxeric.npc.Tekton;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;

/**
 * @author Kris | 10. mai 2018 : 00:44:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TektonRetreatScript implements TektonScript {
	@Override
	public void execute(final Tekton tekton) {
		tekton.animate("leave combat stance");
		World.removeObject(tekton.getBlock());
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			@Override
			public void run() {
				if (tekton.getRoom().getRaid().isDestroyed() || tekton.isDead() || tekton.isFinished()) {
					stop();
					return;
				}
				if (ticks == 0) {
					final Location respawnTile = tekton.getRespawnTile();
					final RouteResult steps = RouteFinder.findRoute(tekton.getX(), tekton.getY(), tekton.getPlane(), tekton.getSize(), new TileStrategy(respawnTile.getX(), respawnTile.getY()), true);
					final int[] bufferX = steps.getXBuffer();
					final int[] bufferY = steps.getYBuffer();
					tekton.resetWalkSteps();
					for (int i = steps.getSteps() - 1; i >= 0; i--) {
						if (!tekton.addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
							break;
						}
					}
					if (smith(tekton)) {
						stop();
						return;
					}
				} else {
					if (smith(tekton)) {
						stop();
					}
				}
				ticks++;
			}
		}, 0, 0);
	}

	private boolean smith(final Tekton tekton) {
		if (!tekton.hasWalkSteps()) {
			tekton.faceDirection(tekton.getSpawnDirection());
			tekton.setEnrageTime(0);
			tekton.execute("smith at anvil");
			return true;
		}
		return false;
	}
}
