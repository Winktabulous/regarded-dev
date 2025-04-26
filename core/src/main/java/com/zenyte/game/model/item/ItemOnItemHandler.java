package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 10. nov 2017 : 23:59.43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum ItemOnItemHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ItemOnItemHandler.class);
    private static final Long2ObjectMap<ItemOnItemAction> intActions = new Long2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<ItemOnItemAction> useOnAny = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<?> c) {
        try {
            final ItemOnItemAction action = (ItemOnItemAction) c.getDeclaredConstructor().newInstance();
            final boolean include = action.includeEquivalentItems();
            final ItemOnItemAction.ItemPair[] pairs = action.getMatchingPairs();
            if (pairs != null) {
                for (final ItemOnItemAction.ItemPair pair : pairs) {
                    final int left = pair.getLeft();
                    final int right = pair.getRight();
                    final int first = Math.max(left, right);
                    final int second = Math.min(left, right);
                    intActions.put((((long) (first)) << 32) | (second & 4294967295L), action);
                }
                return;
            }
            final int[] items = action.getItems();
            final int length = items.length;
            if (action.allItems()) {
                for (final int item : items) {
                    useOnAny.put(item, action);
                }
            } else {
                for (int i = length - 1; i >= 0; i--) {
                    final int itemUsed = items[i];
                    for (int a = length - 1; a >= 0; a--) {
                        final int usedWith = items[a];
                        if (itemUsed == usedWith && !include) {
                            continue;
                        }
                        final int first = Math.max(itemUsed, usedWith);
                        final int second = Math.min(itemUsed, usedWith);
                        intActions.put((((long) (first)) << 32) | (second & 4294967295L), action);
                    }
                }
            }
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    public static void handleItemOnItem(final Player player, final Item used, final Item usedWith, final int fromSlot
            , final int toSlot) {
        //player.stopAll();
        player.stop(Player.StopType.INTERFACES);
        final int itemId = used.getId();
        final int usedWithId = usedWith.getId();
        final int first = Math.max(itemId, usedWithId);
        final int second = Math.min(itemId, usedWithId);
        final long hash = (((long) first) << 32) | (second & 4294967295L);
        ItemOnItemAction action = useOnAny.get(itemId);
        if (action == null) {
            action = useOnAny.get(usedWithId);
            if (action == null) {
                action = intActions.get(hash);
            }
        }
        if (action != null) {
            log.info("[" + action.getClass().getSimpleName() + "] " + used.getName() + "(" + (used.getId() + " x " + used.getAmount()) + ") -> " + usedWith.getName() + "(" + (usedWith.getId() + " x " + usedWith.getAmount()) + ") | Slots: " + fromSlot + " -> " + toSlot);
            if (fromSlot == toSlot) {
                return;
            }

            action.handleItemOnItemAction(player, used, usedWith, fromSlot, toSlot);
            return;
        }
        player.sendMessage("Nothing interesting happens.");
    }

}
