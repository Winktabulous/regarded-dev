package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;

public class DaeyaltMineNpc extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) ->
                player.getDialogueManager().start(new NPCChat(player, NpcId.NORANNA_TYTANIN, "Use your Daeyalt shards on me in exchange for essence.")));
    }


    @Override
    public int[] getNPCs() {
        return new int[] {NpcId.NORANNA_TYTANIN};
    }

}
