package com.zenyte.plugins.object;

import com.zenyte.game.content.chambersofxeric.party.RaidingPartiesInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15. nov 2017 : 21:23.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RecruitingBoardObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getNumericAttribute("aware of raids layouts").intValue() == 0) {
            player.sendMessage(Colour.RED.wrap("Before you start a party - perhaps you should speak with Captain Rimor about the new layouts feature."));
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("Before you start a party - perhaps you should speak with Captain Rimor about the new layouts feature.").executeAction(() -> player.addAttribute("aware of raids layouts", 1));
                }
            });
            return;
        }
        RaidingPartiesInterface.refresh(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.RECRUITING_BOARD };
    }
}
