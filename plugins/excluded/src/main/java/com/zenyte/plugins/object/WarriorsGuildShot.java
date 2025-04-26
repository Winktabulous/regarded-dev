package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.warriorsguild.shotput.ShotputArea;
import com.zenyte.game.content.minigame.warriorsguild.shotput.ShotputD;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16. dets 2017 : 22:33.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorsGuildShot implements ObjectAction {

    private static final Animation ANIM = new Animation(827);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getWeapon() != null || player.getShield() != null || player.getGloves() != null) {
            player.sendMessage("You must have both your hands completely free to throw a shot.");
            return;
        }
        if (player.getTemporaryAttributes().containsKey("shotput")) {
            player.sendMessage("You're already holding a shot.");
            return;
        }
        player.lock();
        player.getTemporaryAttributes().put("shotput", true);
        player.setAnimation(ANIM);
        WorldTasksManager.schedule(() -> {
            player.unlock();
            player.getDialogueManager().start(new ShotputD(player));
            player.setFaceLocation(object.getId() == 15665 ? ShotputArea.SHOT_22LB_FACE : ShotputArea.SHOT_18LB_FACE);
        }, 1);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object.getId() == 15665 ? ShotputArea.SHOT_22LB : ShotputArea.SHOT_18LB), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, object.getName(), optionId, option);
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHOT, ObjectId.SHOT_15665 };
    }
}
