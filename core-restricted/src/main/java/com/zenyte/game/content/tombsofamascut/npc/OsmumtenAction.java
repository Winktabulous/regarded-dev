package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class OsmumtenAction extends NPCPlugin {

	@Override public void handle() {
		bind("Talk-to", (player, npc) -> handle(player));
		bind("Proceed", (player, npc) -> handle(player));
	}

	private void handle(final Player player) {
		TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
		if (party == null) {
			return;
		}
		final EncounterType current = party.getCurrentEncounterType();
		player.getTOAManager().enter(false, EncounterType.MAIN_HALL);
		if (!party.getCurrentEncounterType().equals(current)) {
			party.getPlayers().stream().filter(p -> !player.getUsername().equals(p.getUsername())).forEach(
					p -> p.sendMessage(player.getUsername() + " has returned to the Nexus. Join " + (player.getAppearance().isMale() ? "him" : "her") + "..."));
		}
	}

	@Override public int[] getNPCs() {
		return new int[] {11689};
	}
}
