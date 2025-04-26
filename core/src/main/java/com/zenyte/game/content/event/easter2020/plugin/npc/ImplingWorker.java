package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 12/04/2020
 */
@SkipPluginScan
public class ImplingWorker extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Gotta paint!", Expression.HIGH_REV_JOLLY);
                    player("I should probably let them on with their work.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.IMPLING_WORKER_15192, NpcId.IMPLING_WORKER };
    }
}
