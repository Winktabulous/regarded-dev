package com.zenyte.game.content.skills.construction.objects.bedroom;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Kris | 24. veebr 2018 : 21:55.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Clock implements ObjectInteraction {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CLOCK_13169, ObjectId.CLOCK_13170, ObjectId.CLOCK_13171 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        player.sendMessage("The time is " + DTF.format(LocalDateTime.now()) + ".");
    }
}
