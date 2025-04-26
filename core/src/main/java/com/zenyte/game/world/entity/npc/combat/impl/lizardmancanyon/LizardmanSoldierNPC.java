package com.zenyte.game.world.entity.npc.combat.impl.lizardmancanyon;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanSoldierNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("What do you want? I'm busy.");
                options(TITLE, new DialogueOption("How many lizardmen have I killed?", key(5)), new DialogueOption("Nothing."));
                final int shamanCount = player.getNotificationSettings().getKillcount("Lizardman shaman");
                final int bruteCount = player.getNotificationSettings().getKillcount("Lizardman brute");
                final int lizardmanCount = player.getNotificationSettings().getKillcount("Lizardman");
                npc(5, "We've seen you kill " + StringFormatUtil.format(shamanCount + bruteCount + lizardmanCount) + " lizardmen since we began tracking your kills. Of those lizardmen, " + StringFormatUtil.format(shamanCount) + " were lizardman shamans.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SOLDIER_6868, 6869, 6870, 6871 };
    }
}
