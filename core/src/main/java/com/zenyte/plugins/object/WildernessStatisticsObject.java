package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 18 feb. 2018 : 23:05:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class WildernessStatisticsObject implements ObjectAction {

    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object, 1), getRunnable(player, object, name,
                optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.lock(1);
        player.addWalkSteps(object.getX(), object.getY(), 1, false);
        WorldTasksManager.schedule(() -> {
            player.setFaceLocation(new Location(3084, 3509, 0));
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Toggle K/D", "On", "Off").onOptionOne(() -> {
                        player.sendMessage("K/D Ratio overlay - enabled");
                        player.getSettings().setSetting(Setting.WILDERNESS_KD, 1);
                        finish();
                    }).onOptionTwo(() -> {
                        player.sendMessage("K/D Ratio overlay - disabled");
                        player.getSettings().setSetting(Setting.WILDERNESS_KD, 0);
                        finish();
                    });
                }
            });
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Wilderness Statistics" };
    }

}
