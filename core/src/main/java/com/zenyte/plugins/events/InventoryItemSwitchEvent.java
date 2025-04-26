package com.zenyte.plugins.events;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Event;

public class InventoryItemSwitchEvent implements Event {
    private final Player player;
    private final int fromSlot;
    private final int toSlot;

    public InventoryItemSwitchEvent(Player player, int fromSlot, int toSlot) {
        this.player = player;
        this.fromSlot = fromSlot;
        this.toSlot = toSlot;
    }

    public Player getPlayer() {
        return player;
    }

    public int getFromSlot() {
        return fromSlot;
    }

    public int getToSlot() {
        return toSlot;
    }

    @Override
    public String toString() {
        return "InventoryItemSwitchEvent(player=" + this.getPlayer() + ", fromSlot=" + this.getFromSlot() + ", toSlot=" + this.getToSlot() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof InventoryItemSwitchEvent)) return false;
        final InventoryItemSwitchEvent other = (InventoryItemSwitchEvent) o;
        if (!other.canEqual(this)) return false;
        if (this.getFromSlot() != other.getFromSlot()) return false;
        if (this.getToSlot() != other.getToSlot()) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        return this$player == null ? other$player == null : this$player.equals(other$player);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof InventoryItemSwitchEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getFromSlot();
        result = result * PRIME + this.getToSlot();
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        return result;
    }
}
