package com.zenyte.plugins.object.memberzones;

import com.zenyte.game.content.boss.kraken.KrakenInstance;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

import static com.zenyte.game.content.boss.kraken.KrakenInstance.INSIDE_TILE;

public final class WhirlpoolPlugin implements ObjectAction {
    private static final Animation ANIMATION = new Animation(6723);

    private static final Location START = new Location(3393, 7793);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.stopAll();
        player.lock(6);
        player.setRouteEvent(new TileEvent(player, new TileStrategy(START), () -> {
            player.faceObject(object);
            player.setAnimation(ANIMATION);
            WorldTasksManager.schedule(() ->  {
                player.autoForceMovement(player.getLocation().transform(Direction.SOUTH, 6), 160);
                WorldTasksManager.schedule(() -> new FadeScreen(player, () -> createInstance(player)).fade(3), 5);
            });
        }));
    }

    private void createInstance(Player player) {
        try {
        final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(64, 64);
        final KrakenInstance instance = new KrakenInstance(player, true, allocatedArea, (9116 >> 8) << 3, (9116 & 0xFF) << 3);
        instance.constructRegion();
        player.setLocation(instance.getLocation(INSIDE_TILE));
        } catch (OutOfSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.WHIRLPOOL_25275};
    }
}
