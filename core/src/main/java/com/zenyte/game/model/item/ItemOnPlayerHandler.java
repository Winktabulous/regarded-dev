package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 15/06/2019 10:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ItemOnPlayerHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ItemOnPlayerHandler.class);
    private static final Int2ObjectMap<ItemOnPlayerPlugin> plugins = new Int2ObjectOpenHashMap<>();

    public static void add(final Class<? extends ItemOnPlayerPlugin> clazz) {
        try {
            final ItemOnPlayerPlugin o = clazz.getDeclaredConstructor().newInstance();
            for (final int item : o.getItems()) {
                plugins.put(item, o);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addUnsafe(Class<?> clazz) {
        add((Class<? extends ItemOnPlayerPlugin>) clazz);
    }

    public static void handleItemOnPlayer(final Player player, final Item item, final int slotId, final Player target) {
        if (player.isLocked() || player.isFullMovementLocked()) {
            return;
        }
        player.stopAll(false, true, true);
        if (player.isFrozen()) {
            player.sendMessage("A magical force stops you from moving.");
            return;
        }
        if (player.isStunned()) {
            player.sendMessage("You're stunned.");
            return;
        }
        if (player.isMovementLocked(true)) {
            return;
        }
        final ItemOnPlayerPlugin action = plugins.get(item.getId());
        if (action != null) {
            if (log.isInfoEnabled())
                log.info("[" + action.getClass().getSimpleName() + "] " + item.getName() + "(" + item.getId() + " x " + item.getAmount() + ") -> " + target);
            action.handle(player, item, slotId, target);
        } else {
            player.setRouteEvent(new EntityEvent(player, new EntityStrategy(target), () -> {
                player.stopAll();
                player.faceEntity(target);
                player.sendMessage("Nothing interesting happens.");
            }, true));
        }
    }

}
