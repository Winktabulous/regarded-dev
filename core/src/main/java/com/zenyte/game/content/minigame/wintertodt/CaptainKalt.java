package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class CaptainKalt extends NPCPlugin {
    
    @Override
    public void handle() {
        bind("Check Scores", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain(String.format("You have subdued the Wintertodt %d times.<br>Your lifetime score: %d<br>Your highest score: %d",
                                player.getNotificationSettings().getKillcount("Wintertodt"),
                                player.getNumericAttribute("wintertodt_total_accumulated_points").intValue(),
                                player.getNumericAttribute("wintertodt_highest_points").intValue()
                        ), 25);
                    }
                });
            }
            
            @Override
            public void execute(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{7377};
    }
    
}
