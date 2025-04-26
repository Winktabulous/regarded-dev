package com.zenyte.plugins.events;

import com.zenyte.plugins.Event;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 27/07/2019 06:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ItemDefinitionsLoadedEvent implements Event {

    private final ItemDefinitions itemDefinitions;

    public ItemDefinitionsLoadedEvent(ItemDefinitions itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
    }

    public ItemDefinitions getItemDefinitions() {
        return itemDefinitions;
    }

    @Override
    public String toString() {
        return "ItemDefinitionsLoadedEvent()";
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof ItemDefinitionsLoadedEvent;
    }

}
