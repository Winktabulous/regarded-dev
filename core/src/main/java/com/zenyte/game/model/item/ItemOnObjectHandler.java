package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.object.WorldObjectUtils;
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
public enum ItemOnObjectHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ItemOnObjectHandler.class);
    private static final Long2ObjectMap<ItemOnObjectAction> INT_ACTIONS =
            new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnObjectAction> ITEM_STRING_ACTIONS =
            new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnObjectAction> OBJECT_STRING_ACTIONS =
            new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnObjectAction> STRING_ACTIONS =
            new Long2ObjectOpenHashMap<>();
    private static final Map<Object, ItemOnObjectAction> ALL_ITEMS_USABLE = new Object2ObjectOpenHashMap<>();

    public static void add(final Class<?> c) {
        try {
            final ItemOnObjectAction action = (ItemOnObjectAction) c.getDeclaredConstructor().newInstance();
            if (action.getItems() == null) {
                for (final Object object : action.getObjects()) {
                    if (object instanceof String) {
                        ALL_ITEMS_USABLE.put(object.toString().toLowerCase(), action);
                    } else {
                        ALL_ITEMS_USABLE.put(object, action);
                    }
                }
                return;
            }
            for (final Object item : action.getItems()) {
                for (final Object object : action.getObjects()) {
                    if (item instanceof Integer) {
                        if (object instanceof Integer) {
                            INT_ACTIONS.put((((long) ((Integer) item)) << 32) | ((Integer) object & 4294967295L),
                                    action);
                        } else if (object instanceof String) {
                            OBJECT_STRING_ACTIONS.put((((long) (Integer) item) << 32) | (((String) object).toLowerCase().hashCode() & 4294967295L), action);
                        }
                    } else if (item instanceof String) {
                        if (object instanceof String) {
                            STRING_ACTIONS.put((((long) ((String) item).toLowerCase().hashCode()) << 32) | (((String) object).toLowerCase().hashCode() & 4294967295L), action);
                        } else if (object instanceof Integer) {
                            ITEM_STRING_ACTIONS.put((((long) ((String) item).toLowerCase().hashCode()) << 32) | ((Integer) object & 4294967295L), action);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void handleItemOnObject(final Player player, final Item item, int slot, final WorldObject object) {
        final boolean isInfoEnabled = log.isInfoEnabled();
        if (player.isLocked() || player.isFullMovementLocked()) {
            if (isInfoEnabled)
                log.info(item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + object.getName() +
                        "(" + object.getId() + ")");
            return;
        }
        player.stopAll(false, true, true);
        final int itemId = item.getId();
        final int objectId = object.getId();
        final String objectName = WorldObjectUtils.getObjectNameOfPlayer(object, player).toLowerCase();
        ItemOnObjectAction action;
        if ((action = ALL_ITEMS_USABLE.get(objectId)) != null || (action = ALL_ITEMS_USABLE.get(objectName)) != null) {
            if (isInfoEnabled)
                log.info("[" + action.getClass().getSimpleName() + "] " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + object.getName() + "(" + object.getId() + ")");
            action.handle(player, item, slot, object);
            return;
        }
        final int itemHash = item.getName().toLowerCase().hashCode();
        final int objectHash = objectName.hashCode();
        long hash = (((long) itemId) << 32) | (objectId & 4294967295L);
        action = INT_ACTIONS.get(hash);
        if (action == null) {
            hash = (((long) itemId) << 32) | (objectHash & 4294967295L);
            action = OBJECT_STRING_ACTIONS.get(hash);
            if (action == null) {
                hash = (((long) itemHash) << 32) | (objectId & 4294967295L);
                action = ITEM_STRING_ACTIONS.get(hash);
                if (action == null) {
                    hash = (((long) itemHash) << 32) | (objectHash & 4294967295L);
                    action = STRING_ACTIONS.get(hash);
                }
            }
        }
        player.setFacedInteractableEntity(object);
        if (action != null) {
            if (isInfoEnabled)
                log.info("[" + action.getClass().getSimpleName() + "] " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + object.getName() + "(" + object.getId() + ")");
            action.handle(player, item, slot, object);
        } else {
            if (isInfoEnabled)
                log.info("[ItemOnObject]: " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> "
                        + object.getName() + "(" + object.getId() + "), " + object.getLocation());
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
                player.stopAll();
                player.faceObject(object);
                player.sendMessage("Nothing interesting happens.");
            }));
        }
    }

}
