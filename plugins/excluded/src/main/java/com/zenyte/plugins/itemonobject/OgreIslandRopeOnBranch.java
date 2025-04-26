package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.agility.shortcut.OgreIslandEntranceRopeSwing;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 1/26/2020
 */
public class OgreIslandRopeOnBranch implements ItemOnObjectAction {
    private static final OgreIslandEntranceRopeSwing SHORTCUT = new OgreIslandEntranceRopeSwing();
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        SHORTCUT.handle(player, object,  0, null);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.ROPE };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { OgreIslandEntranceRopeSwing.BRANCH };
    }

    public void handle(final Player player, final Item item, int slot, final WorldObject object) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(OgreIslandEntranceRopeSwing.startPosition), () -> {
            player.stopAll();
            player.setFaceLocation(OgreIslandEntranceRopeSwing.destination);
            handleItemOnObjectAction(player, item, slot, object);
        }));
    }
}
