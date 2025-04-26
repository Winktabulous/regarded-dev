package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.dialogue.LeaveRaidD;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

import static com.zenyte.game.content.chambersofxeric.Raid.outsideTile;

/**
 * @author Kris | 06/07/2019 03:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SurfaceSteps implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name,
                                   final int optionId, final String option) {
        final Optional<Raid> optionalRaid = player.getRaid();
        if (optionalRaid.isPresent()) {
            player.getDialogueManager().start(new LeaveRaidD(player, optionalRaid.get()));
        } else {
            player.setLocation(outsideTile);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.STEPS_29778};
    }

}
