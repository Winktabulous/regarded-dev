package com.zenyte.plugins.events;

import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.Event;

public class FloorItemSpawnEvent implements Event {
    private final FloorItem item;

    public FloorItemSpawnEvent(FloorItem item) {
        this.item = item;
    }

    public FloorItem getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "FloorItemSpawnEvent(item=" + this.getItem() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof FloorItemSpawnEvent)) return false;
        final FloorItemSpawnEvent other = (FloorItemSpawnEvent) o;
        if (!other.canEqual(this)) return false;
        final Object this$item = this.getItem();
        final Object other$item = other.getItem();
        return this$item == null ? other$item == null : this$item.equals(other$item);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FloorItemSpawnEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $item = this.getItem();
        result = result * PRIME + ($item == null ? 43 : $item.hashCode());
        return result;
    }
}
