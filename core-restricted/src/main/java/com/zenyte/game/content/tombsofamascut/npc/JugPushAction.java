package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Savions.
 */
public class JugPushAction extends NPCPlugin {

	@Override public void handle() {
		bind("Push", new MoveHandler(true));
		bind("Pull", new MoveHandler(false));
		final OptionHandler combatHandler = new OptionHandler() {

			@Override public void click(Player player, NPC npc, NPCOption option) {
				player.stopAll();
				player.setFaceEntity(npc);
				handle(player, npc);
			}

			@Override public void handle(Player player, NPC npc) {
				PlayerCombat.attackEntity(player, npc, null);
			}
		};
		bind("Hit", combatHandler);
		bind("Attack", combatHandler);
	}

	private void moveJug(Player player, NPC npc, boolean push) {
		if (npc instanceof CrondisJug crondisJug) {
			crondisJug.moveJug(player, push);
		}
	}

	@Override public int[] getNPCs() {
		return new int[] {Zebak.JUG_ID, Zebak.JUG_ID + 1};
	}

	private class MoveHandler implements OptionHandler {

		private final boolean push;

		public MoveHandler(boolean push) {
			this.push = push;
		}

		@Override public void handle(Player player, NPC npc) {
			moveJug(player, npc, push);
		}

		@Override public void click(Player player, NPC npc, NPCOption option) {
			if (player.getLocation().getTileDistance(npc.getLocation()) == 1) {
				execute(player, npc);
			} else {
				player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> execute(player, npc), true));
			}
		}
	}
}
