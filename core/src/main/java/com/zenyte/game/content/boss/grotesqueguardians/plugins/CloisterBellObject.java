package com.zenyte.game.content.boss.grotesqueguardians.plugins;

import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Tommeh | 21/07/2019 | 22:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class CloisterBellObject implements ObjectAction {

    private static final Animation PLAYER_RING_ANIMATION = new Animation(390);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Optional<GrotesqueGuardiansInstance> optionalInstance = player.getGrotesqueGuardiansInstance();
        if (!optionalInstance.isPresent()) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        final GrotesqueGuardiansInstance instance = optionalInstance.get();
        if (instance.isStarted()) {
            player.sendMessage("Concentrate on your fight!");
            return;
        }
        if (instance.getDusk() != null) {
            instance.getDusk().finish();
        }
        if (instance.getDawn() != null) {
            instance.getDawn().finish();
        }
        instance.setStarted(true);
        player.setAnimation(PLAYER_RING_ANIMATION);
        WorldTasksManager.schedule(() -> World.sendObjectAnimation(instance.getBell(), GrotesqueGuardiansInstance.BELL_ANIMATION));
        if (option.equals("Ring")) {
            player.getCutsceneManager().play(new BossFightCutscene(instance));
        } else if (option.equals("Quick-start")) {
            WorldTasksManager.schedule(() -> instance.start(false), 3);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31669 };
    }
}
