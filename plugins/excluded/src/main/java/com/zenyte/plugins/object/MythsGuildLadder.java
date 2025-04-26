package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

import static com.zenyte.plugins.object.LadderOA.CLIMB_DOWN;
import static com.zenyte.plugins.object.LadderOA.CLIMB_UP;

/**
 * @author Kris | 24/04/2019 00:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MythsGuildLadder implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        switch(option) {
            case "Climb":
                player.getDialogueManager().start(new OptionDialogue(player, "Which way do you want to go?", new String[] { "Climb up", "Climb down", "Cancel." }, new Runnable[] { () -> climb(player, object, new Location(player.getX(), player.getY(), object.getPlane() + 1), CLIMB_UP), () -> climb(player, object, new Location(player.getX(), player.getY(), object.getPlane() - 1), CLIMB_DOWN), null }));
                break;
            case "Climb-up":
                climb(player, object, new Location(player.getX(), player.getY(), (player.getPlane() + 1) & 0x3), CLIMB_UP);
                break;
            case "Climb-down":
                climb(player, object, new Location(player.getX(), player.getY(), (player.getPlane() - 1) & 0x3), CLIMB_DOWN);
                break;
        }
    }

    private void climb(final Player player, final WorldObject originalObject, final Location tile, final Animation anim) {
        player.lock(2);
        player.setAnimation(anim);
        WorldTasksManager.schedule(() -> player.setLocation(tile));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_31612 };
    }
}
