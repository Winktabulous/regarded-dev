package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.GameConstants;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Corey
 * @since 19/12/2019
 */
public class SirAmikVarze extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final AChristmasWarble.ChristmasWarbleProgress progress = AChristmasWarble.getProgress(player);
            if (progress == null) {
                return;
            }
            if (progress == AChristmasWarble.ChristmasWarbleProgress.FIND_OUT_ABOUT_SCOURGES_PAST) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("What are you doing in those awful sheets, " + player.getName() + "?", Expression.HIGH_REV_WONDERING);
                        player("I'm pretending to be a ghost.");
                        npc("How peculiarsh...well...anyway...", Expression.HIGH_REV_JOLLY);
                        player("Er, Sir Amik Varze, sir? Have you ever met Ebenezer Scourge before?");
                        npc("Scourge? I'm glad to say I've never had the misfortune of meeting thatsh rotter up 'til " +
                                "now. Though...I have heard rumours of the ill deeds he's commited on " + GameConstants.SERVER_NAME, Expression.HIGH_REV_MAD);
                        player("What's he done?");
                        npc("He consorts with Zamorakian mages, goblins and necromancers. He'sh the sort who'd swap " +
                                "his grandma for a turnip  if he could finda buyer. Evil to the core, that man.", Expression.HIGH_REV_MAD);
                        player("Thanks, though I'm not sure if that information is exactly what I'm looking for.");
                    }
                });
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("*hick*", Expression.HIGH_REV_JOLLY);
                        plain("Sir Amik is too drunk to talk right now.");
                    }
                });
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {ChristmasConstants.AMIK_VARZE_NPC_ID};
    }
}
