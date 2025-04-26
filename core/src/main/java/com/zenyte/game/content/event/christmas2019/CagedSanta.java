package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 16/12/2019
 */
public class CagedSanta implements ObjectAction {
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final String impName = ChristmasUtils.getImpName(player);
        final int scourgeId = AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_SCOURGE) ? ChristmasConstants.PAJAMAS_SCOURGE_NPC_ID : ChristmasConstants.SCOURGE_NPC_ID;
    
        player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
            @Override
            public void buildDialogue() {
                npc(ChristmasConstants.SANTA_NPC_ID, "Well, " + player.getName() + ", I've got myself into a bit of a jam here.", 1, Expression.HIGH_REV_SAD);
                npc(impName, "That Scourge fella's gonna pay for locking you up, Nick!", Expression.HIGH_REV_MAD);
                player("Santa, is there anything I can do to help?");
                plain("A voice comes echoing through the mansion...");
                npc(scourgeId, "Don't speak to him or I'll blast you! I'll blast you and turn your mother into a frog!", 5, Expression.HIGH_REV_MAD);
                npc(ChristmasConstants.SANTA_NPC_ID, "You should leave this place, it's too dangerous.", 6, Expression.HIGH_REV_SAD);
            }
        });
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{ChristmasConstants.CAGED_SANTA, ChristmasConstants.CAGED_SANTA_2};
    }
    
}
