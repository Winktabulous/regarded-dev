package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.pathfinding.events.player.FloorItemEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.FloorItemStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Kris | 10. nov 2017 : 23:21.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum ItemOnFloorItemHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ItemOnFloorItemHandler.class);

    private static final Long2ObjectMap<ItemOnFloorItemAction> intActions =
            new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnFloorItemAction> itemStringActions =
            new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnFloorItemAction> objectStringActions =
            new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnFloorItemAction> stringActions =
            new Long2ObjectOpenHashMap<>();
    private static final Map<Object, ItemOnFloorItemAction> allItemsUsable = new Object2ObjectOpenHashMap<>();

    public static void add(final Class<?> c) {
        try {
            final ItemOnFloorItemAction action = (ItemOnFloorItemAction) c.getDeclaredConstructor().newInstance();
            if (action.getItems() == null) {
                for (final Object object : action.getFloorItems()) {
                    if (object instanceof String) {
                        allItemsUsable.put(object.toString().toLowerCase(), action);
                    } else {
                        allItemsUsable.put(object, action);
                    }
                }
                return;
            }
            for (final Object item : action.getItems()) {
                for (final Object object : action.getFloorItems()) {
                    if (item instanceof Integer) {
                        if (object instanceof Integer) {
                            intActions.put((((long) ((Integer) item)) << 32) | ((Integer) object & 4294967295L),
                                    action);
                        } else if (object instanceof String) {
                            objectStringActions.put((((long) (Integer) item) << 32) | (((String) object).toLowerCase().hashCode() & 4294967295L), action);
                        }
                    } else if (item instanceof String) {
                        if (object instanceof String) {
                            stringActions.put((((long) ((String) item).toLowerCase().hashCode()) << 32) | (((String) object).toLowerCase().hashCode() & 4294967295L), action);
                        } else if (object instanceof Integer) {
                            itemStringActions.put((((long) ((String) item).toLowerCase().hashCode()) << 32) | ((Integer) object & 4294967295L), action);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void handleItemOnFloorItem(final Player player, final Item item, final FloorItem floorItem) {
        if (player.isLocked() || player.isFullMovementLocked()) {
            return;
        }
        player.setFacedInteractableEntity(floorItem);
        player.stopAll(false, true, true);
        final int itemId = item.getId();
        final int objectId = floorItem.getId();
        final String objectName = floorItem.getDefinitions().getName().toLowerCase();
        ItemOnFloorItemAction action;
        if ((action = allItemsUsable.get(objectId)) != null || (action = allItemsUsable.get(objectName)) != null) {
            action.handle(player, item, floorItem);
            return;
        }
        final int itemHash = item.getName().toLowerCase().hashCode();
        final int objectHash = objectName.hashCode();
        long hash = (((long) itemId) << 32) | (objectId & 4294967295L);
        action = intActions.get(hash);
        if (action == null) {
            hash = (((long) itemId) << 32) | (objectHash & 4294967295L);
            action = objectStringActions.get(hash);
            if (action == null) {
                hash = (((long) itemHash) << 32) | (objectId & 4294967295L);
                action = itemStringActions.get(hash);
                if (action == null) {
                    hash = (((long) itemHash) << 32) | (objectHash & 4294967295L);
                    action = stringActions.get(hash);
                }
            }
        }
        if (action != null) {
            action.handle(player, item, floorItem);
        } else {
            player.setRouteEvent(new FloorItemEvent(player, new FloorItemStrategy(floorItem), () -> {
                player.stopAll();
                player.sendMessage("Nothing interesting happens.");
            }));
        }
    }

}
