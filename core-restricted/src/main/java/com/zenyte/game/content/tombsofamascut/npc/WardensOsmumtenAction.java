package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions
 */
public class WardensOsmumtenAction extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> dialogue(player));
        bind("Begin", (player, npc) -> handle(player));
    }

    private void dialogue(final Player player) {
        //TODO
    }

    private void handle(final Player player) {
        TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
        if (party == null) {
            return;
        }
        final TOARaidArea current = party.getCurrentRaidArea();
        if (current instanceof final WardenEncounter wardenEncounter) {
            wardenEncounter.setReady(player);
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] {WardenEncounter.OSMUMTEN_NPC_ID};
    }
}
