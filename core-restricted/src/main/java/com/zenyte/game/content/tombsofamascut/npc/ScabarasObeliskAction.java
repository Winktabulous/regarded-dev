package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.content.tombsofamascut.raid.ScabarasPuzzleType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCHandler;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Savions.
 */
public class ScabarasObeliskAction extends NPCPlugin {

	@Override public void handle() {
		bind("Hit", new OptionHandler() {

			@Override public void click(Player player, NPC npc, NPCOption option) {
				player.stopAll();
				player.setFaceEntity(npc);
				handle(player, npc);
			}

			@Override public void handle(Player player, NPC npc) {
				PlayerCombat.attackEntity(player, npc, null);
			}
		});
	}

	@Override public int[] getNPCs() {
		return new int[] {ScabarasObelisk.ID};
	}
}
