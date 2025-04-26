package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Corey
 */
public class WeissCaveEntrance implements ObjectAction {
    
    private static final int CAVE_ENTRANCE = 33329;
    private static final int STAIRS = 33227;
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (object.getId()) {
            case CAVE_ENTRANCE:
                new FadeScreen(player, () -> player.setLocation(new Location(2854, 3941))).fade(3);
                break;
            case STAIRS:
                new FadeScreen(player, () -> player.setLocation(new Location(2859, 3968))).fade(3);
                break;
        }
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == STAIRS) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(2854, 3941, 0)), getRunnable(player, object, name,
                    optionId, option), getDelay()));
        } else {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name,
                    optionId, option), getDelay()));
        }
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{CAVE_ENTRANCE, STAIRS};
    }
    
}
