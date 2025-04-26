package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 20:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OttoGodblessed extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    player("What of the one-handed spears of which you spoke?");
                    npc("I see you have constructed your hasta, and are<br><br>approaching readiness.");
                    player("If I may ask, readiness for what?");
                    npc("To live life to it's fullest of course - that you may be a<br><br>peaceful spirit when your time ends.");
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.OTTO_GODBLESSED };
    }
}
