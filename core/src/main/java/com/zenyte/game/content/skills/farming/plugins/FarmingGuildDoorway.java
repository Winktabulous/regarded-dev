package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ForcedGate;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Kris | 24/04/2019 21:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FarmingGuildDoorway implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        new ForcedGate<>(player, object).handle(Optional.of(t -> {
            if (!player.inArea("Farming Guild") && player.getSkills().getLevelForXp(SkillConstants.FARMING) < 45) {
                player.getDialogueManager().start(new PlainChat(player, "You need a Farming level of at least 45 to enter the guild."));
                return false;
            }
            return true;
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_34463, ObjectId.DOOR_34464 };
    }
}
