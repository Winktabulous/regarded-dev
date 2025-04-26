package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class RopeOnBattlementAction implements ItemOnObjectAction {
    private static final int ROPE_ITEM_ID = 954;
    public static final int ROPE_OBJECT_ID = 4444;
    private static final int SARADOMIN_BATTLEMENT = 4446;
    private static final int ZAMORAK_BATTLEMENT = 4447;
    private static final Object[] ROPE = new Object[] {ROPE_ITEM_ID};
    private static final Object[] BATTLEMENTS = new Object[] {SARADOMIN_BATTLEMENT, ZAMORAK_BATTLEMENT};

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        if (!player.inArea("Castle Wars")) {
            return;
        }
        if (item.getId() != ROPE_ITEM_ID) {
            return;
        }
        final int rotation = object.getId() == SARADOMIN_BATTLEMENT ? object.getRotation() + 2 : object.getRotation() - 2;
        final int tileX = (object.getRotation() % 2 == 0) ? ((object.getRotation() == 2) ? object.getX() + 1 : object.getX() - 1) : object.getX();
        final int tileY = (object.getRotation() % 2 == 0) ? object.getY() : ((object.getRotation() == 1) ? object.getY() + 1 : object.getY() - 1);
        final Location tile = new Location(tileX, tileY, 0);
        if (World.containsObjectWithId(tile, ROPE_OBJECT_ID)) {
            player.sendMessage("There\'s already a battlement here!");
            return;
        }
        final WorldObject ropeObject = new WorldObject(ROPE_OBJECT_ID, 4, rotation % 4, tile);
        player.getInventory().deleteItem(ROPE_ITEM_ID, 1);
        World.spawnObject(ropeObject);
        CastleWars.SPAWNED_OBJECTS.add(ropeObject);
    }

    @Override
    public Object[] getItems() {
        return ROPE;
    }

    @Override
    public Object[] getObjects() {
        return BATTLEMENTS;
    }
}
