package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.event.easter2020.SplittingHeirs;
import com.zenyte.game.content.event.easter2020.Stage;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 12/04/2020
 */
@SkipPluginScan
public class ImplingManager extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            
            if (SplittingHeirs.progressedAtLeast(player, Stage.IMPLINGS_GATHERED_SPEAK_TO_BUNNY_JR)) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        player("All working at full speed, and all because of me.");
                        plain("You feel innordinately proud.");
                        npc("Please do not interfere with the ovulation decoration technicians. They are required to fulfil their E.P.P for their shift.", Expression.HIGH_REV_NORMAL);
                        player("E.P.P?");
                        npc("Oh, yes. Egg Painting Points: they must fulfil no less than 1600 E.P.P per shift.", Expression.HIGH_REV_NORMAL);
                        player("What happens if they don't?");
                        npc("Have you ever been fortunate enough to own an egg with a pretty embossed chocolate impling on it?", Expression.HIGH_REV_NORMAL);
                        player("Oh... OH! That's harsh.");
                    }
                });
            } else if (SplittingHeirs.getProgress(player) == Stage.GATHER_IMPLINGS) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Please help return the implings back to the painting machine!", Expression.HIGH_REV_SAD);
                    }
                });
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        player("I should speak with the Easter Bunny's son to see what he wants me to do first.");
                    }
                });
            }
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{EasterConstants.IMPLING_MANAGER};
    }
}
