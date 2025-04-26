package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 23/09/2019 | 21:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class MotherlodeMineExit implements ObjectAction {

    private static final Location DESTINATION_1 = new Location(3060, 9766, 0);

    private static final Location DESTINATION_2 = new Location(3054, 9744, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Exit")) {
            if (object.getId() == 30375 && player.getSkills().getLevel(SkillConstants.MINING) < 60) {
                player.getDialogueManager().start(new NPCChat(player, 7721, "Sorry but you're not experienced enough to go in there."));
                player.sendMessage("You need a Mining level of 60 to access the Mining Guild.");
                return;
            }
            player.setLocation(object.getId() == 26655 ? DESTINATION_1 : DESTINATION_2);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TUNNEL_26655, ObjectId.TUNNEL_30375 };
    }
}
