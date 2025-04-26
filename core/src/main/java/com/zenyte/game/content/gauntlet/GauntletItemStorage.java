package com.zenyte.game.content.gauntlet;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.events.InitializationEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 *
 * @author Andys1814.
 * @since 1/22/2022.
 */
public final class GauntletItemStorage {

    private final transient Player player;

    private final Container inventory;

    private final Container equipment;

    public GauntletItemStorage(@NotNull final Player player) {
        this.player = player;
        inventory = new Container(ContainerPolicy.NORMAL, ContainerType.INVENTORY, Optional.of(player));
        equipment = new Container(ContainerPolicy.NORMAL, ContainerType.EQUIPMENT, Optional.of(player));
    }

    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        Player player = event.getPlayer();
        Player saved = event.getSavedPlayer();

        GauntletItemStorage storage = saved.getGauntletItemStorage();
        if (storage == null) {
            return;
        }

        /* If our storage containers aren't empty, give them back to player. */

        if (!storage.inventory.isEmpty()) {
            player.getInventory().getContainer().setContainer(storage.inventory);
            storage.inventory.clear();
        }

        if (!storage.equipment.isEmpty()) {
            player.getEquipment().getContainer().setContainer(storage.equipment);
            storage.equipment.clear();
        }
    }

    public Container getInventory() {
        return inventory;
    }

    public Container getEquipment() {
        return equipment;
    }
}
