package com.zenyte.game.content.chompy.plugins;

import com.zenyte.game.content.chompy.BellowsAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public class SwampBubbles implements ObjectAction {
    public static final int SWAMP_BUBBLES_ID = 684;
    public static final Animation suckingAnim = new Animation(1026);
    public static final Graphics bellowsGfx = new Graphics(241, 0, 90);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new BellowsAction());
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{SWAMP_BUBBLES_ID};
    }
}