package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Kris | 27/11/2018 11:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IslandSquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Would you like me to take you back to the outpost?");
                    options("Leave the game?", new DialogueOption("Yes.", () -> {
                        RegionArea area = player.getArea();
                        if (!(area instanceof PestControlInstance)) {
                            return;
                        }
                        PestControlInstance instance = (PestControlInstance) area;
                        player.setLocation(instance.getType().getExitPoint());
                    }), new DialogueOption("No."));
                }
            });
        });
        bind("Leave", (player, npc) -> {
            RegionArea area = player.getArea();
            if (!(area instanceof PestControlInstance)) {
                return;
            }
            PestControlInstance instance = (PestControlInstance) area;
            player.setLocation(instance.getType().getExitPoint());
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SQUIRE_2949 };
    }
}
