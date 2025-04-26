package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.skills.afk.AfkMasterDialogue;
import com.zenyte.game.content.skills.afk.AfkSkillingConstants;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

public class AfkMaster extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new AfkMasterDialogue(player)));
        bind("Trade", (player, npc) -> {
            player.openShop("AFK Skilling Shop");
            player.sendMessage("You currently have " + Colour.RED.wrap(player.getNumericAttribute(AfkSkillingConstants.AFK_POINTS).intValue()) + " Afk points.");
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {AfkSkillingConstants.AFK_MASTER};
    }
}
