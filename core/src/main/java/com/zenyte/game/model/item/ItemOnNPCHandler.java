package com.zenyte.game.model.item;

import com.zenyte.game.content.rottenpotato.plugin.RottenPotatoItemOnNpc;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 10. nov 2017 : 23:21.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum ItemOnNPCHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ItemOnNPCHandler.class);
    private static final Long2ObjectMap<ItemOnNPCAction> intActions = new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnNPCAction> itemStringActions = new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnNPCAction> objectStringActions = new Long2ObjectOpenHashMap<>();
    private static final Long2ObjectMap<ItemOnNPCAction> stringActions = new Long2ObjectOpenHashMap<>();

    private static final Object2ObjectMap<Object, ItemOnNPCAction> allItemsUsable = new Object2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<ItemOnNPCAction> allIntActions = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<ItemOnNPCAction> allObjectStringActions = new Int2ObjectOpenHashMap<>();

    private static final RottenPotatoItemOnNpc potatoNpcPlugin = new RottenPotatoItemOnNpc();

    public static void add(final Class<?> c) {
        try {
            final ItemOnNPCAction action = (ItemOnNPCAction) c.getDeclaredConstructor().newInstance();
            final Object[] items = action.getItems();
            final Object[] objects = action.getObjects();
            if (items == null) {
                for (final Object object : objects) {
                    if (object instanceof String) {
                        allItemsUsable.put(object.toString().toLowerCase(), action);
                    } else {
                        allItemsUsable.put(object, action);
                    }
                }
                return;
            }
            for (final Object item : items) {
                if (item instanceof Integer) {
                    if ((int) item == -1) {
                        for (final Object object : objects) {
                            if (object instanceof Integer) {
                                allIntActions.put((int) object, action);
                            } else if (object instanceof String) {
                                allObjectStringActions.put(((String) object).toLowerCase().hashCode(), action);
                            }
                        }
                    } else {
                        final long itemBase = ((long) ((int) item)) << 32;
                        for (final Object object : objects) {
                            if (object instanceof Integer) {
                                intActions.put(itemBase | ((int) object & 0xffffffffL), action);
                            } else if (object instanceof String) {
                                objectStringActions.put(itemBase | (((String) object).toLowerCase().hashCode() & 0xffffffffL), action);
                            }
                        }
                    }
                } else if (item instanceof String) {
                    final long itemBase = (((long) ((String) item).toLowerCase().hashCode()) << 32);
                    for (final Object object : action.getObjects()) {
                        if (object instanceof String) {
                            stringActions.put(itemBase | (((String) object).toLowerCase().hashCode() & 0xffffffffL),
                                    action);
                        } else if (object instanceof Integer) {
                            itemStringActions.put(itemBase | ((int) object & 0xffffffffL), action);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void handleItemOnNPC(final Player player, final Item item, final int slotId, final NPC npc) {
        if (player.isLocked() || player.isFullMovementLocked()) {
            return;
        }
        player.stopAll(false, true, true);
        final int itemId = item.getId();
        if (itemId == ItemId.ROTTEN_POTATO) {
            potatoNpcPlugin.handleItemOnNPCAction(player, item, slotId, npc);
            return;
        }
        final int npcId = npc.getId();
        final String npcName = npc.getName(player).toLowerCase();
        ItemOnNPCAction action;
        if ((action = allItemsUsable.get(npcId)) != null || (action = allItemsUsable.get(npcName)) != null) {
            action.handle(player, item, slotId, npc);
            return;
        }
        action = allIntActions.get(npcId);
        if (action == null) {
            long hash = (((long) itemId) << 32) | (npcId & 0xffffffffL);
            action = intActions.get(hash);
            if (action == null) {
                final int objectHash = npcName.hashCode();
                action = allObjectStringActions.get(objectHash);
                if (action == null) {
                    hash = (((long) itemId) << 32) | (objectHash & 0xffffffffL);
                    action = objectStringActions.get(hash);
                    if (action == null) {
                        final int itemHash = item.getName().toLowerCase().hashCode();
                        hash = (((long) itemHash) << 32) | (npcId & 0xffffffffL);
                        action = itemStringActions.get(hash);
                        if (action == null) {
                            hash = (((long) itemHash) << 32) | (objectHash & 0xffffffffL);
                            action = stringActions.get(hash);
                        }
                    }
                }
            }
        }
        if (action != null) {
            if (log.isInfoEnabled())
                log.info("[" + action.getClass().getSimpleName() + "] " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + npc.getName(player) + "(" + npc.getId() + ", Index: " + npc.getIndex() + ")");
            action.handle(player, item, slotId, npc);
        } else {
            player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
                player.stopAll();
                player.faceEntity(npc);
                player.sendMessage("Nothing interesting happens.");
            }, true));
        }
    }

}
