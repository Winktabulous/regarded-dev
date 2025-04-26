package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Corey
 * @since 20/12/2019
 */
public class Santa extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (AdventCalendarManager.adventEnabled() && npc.getPosition().getRegionId() != 8276) {
                GameInterface.ADVENT_CALENDAR.open(player);
                return;
            }

            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    final String impName = ChristmasUtils.getImpName(player);
                    npc("Thank you for getting me out of that fix. I must say that cage was an awfully tight fit.", Expression.HIGH_REV_HAPPY);
                    player("Our pleasure.");
                    if (AChristmasWarble.getProgress(player) == AChristmasWarble.ChristmasWarbleProgress.SANTA_FREED) {
                        npc("Such kindness and bravery deserves a reward! Let me think...", Expression.HIGH_REV_HAPPY);
                        npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Yay presents!", Expression.HIGH_REV_HAPPY);
                        npc("I've left you and your friend, " + impName + ", a small present under the Christmas tree. It's wrapped up in green and red paper.", Expression.HIGH_REV_NORMAL)
                                .executeAction(() -> AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.CAN_OPEN_PRESENT));
                        player("Thanks!");
                    }
                }
            });
        });
    }
    
    @Override
    public int[] getNPCs() {
        return new int[]{ChristmasConstants.SANTA_NPC_ID};
    }
}
