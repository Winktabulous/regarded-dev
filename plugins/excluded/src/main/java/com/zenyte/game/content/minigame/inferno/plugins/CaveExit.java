package com.zenyte.game.content.minigame.inferno.plugins;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Tommeh | 07/12/2019 | 21:30
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class CaveExit implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final RegionArea area = player.getArea();
        if (!(area instanceof Inferno)) {
            return;
        }
        final Inferno inferno = (Inferno) area;
        if (option.equals("Exit")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    options("Really leave?", "Yes - really leave.", "No, I\'ll stay.").onOptionOne(() -> inferno.leave(false));
                }
            });
        } else {
            inferno.leave(false);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_EXIT_30283 };
    }
}
