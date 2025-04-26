package com.zenyte.game.content;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.testinterfaces.StorageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.InitializationEvent;

import java.util.Optional;

/**
 * @author Kris | 03/07/2022
 */
public class StorageRoom {
    public Container getContainer() {
        return container;
    }

    private final Container container;

    public StorageRoom() {
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.STORAGE_ROOM, Optional.empty());
    }

    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final StorageRoom storageUnit = savedPlayer.getStorageRoom();
        if (storageUnit != null && storageUnit.container != null) {
            final StorageRoom unit = player.getStorageRoom();
            unit.container.setContainer(storageUnit.container);
        }
    }

    public static void open(Player player, StorageType type) {
        player.getTemporaryAttributes().put("storage_room_type", type);
        GameInterface.STORAGE_ROOM.open(player);
        GameInterface.STORAGE_ROOM_INV.open(player);
    }
}
