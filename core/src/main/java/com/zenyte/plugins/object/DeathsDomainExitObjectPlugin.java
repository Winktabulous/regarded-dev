package com.zenyte.plugins.object;

import com.zenyte.game.GameInterface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14/06/2022
 */
public class DeathsDomainExitObjectPlugin  implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.DEATHS_OFFICE_ENTER_OVERLAY.open(player);
        player.setAnimation(new Animation(827, 20));
        player.sendSound(3952);
        player.getPacketDispatcher().sendClientScript(2893, 41549825, 41549826, 39504, 4128927, -1, -1);
        final int from = player.getAreaManager().getOnEnterLocation();
        final Location fromLocation = from == 0 ? new Location(3121, 3446, 0) : new Location(from);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.getPacketDispatcher().sendClientScript(2894, 41549825, 41549826, -1, -1);
            player.setLocation(fromLocation);
            WorldTasksManager.schedule(() -> {
                player.lock(1);
                player.getInterfaceHandler().closeInterface(GameInterface.DEATHS_OFFICE_ENTER_OVERLAY);
            });
        }, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.PORTAL_39549 };
    }
}
