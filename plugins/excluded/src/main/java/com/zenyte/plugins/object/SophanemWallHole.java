package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 4/12/2020
 */
public class SophanemWallHole implements ObjectAction {
    private static final ImmutableLocation DESTINATION = new ImmutableLocation(3321, 2858, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        new FadeScreen(player, () -> player.setLocation(DESTINATION)).fade(3);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.HOLE_6620};
    }
}
