package com.zenyte.plugins.events;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Event;

public class ClanLeaveEvent implements Event {
    private final Player player;

    public ClanLeaveEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "ClanLeaveEvent(player=" + this.getPlayer() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ClanLeaveEvent)) return false;
        final ClanLeaveEvent other = (ClanLeaveEvent) o;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        return this$player == null ? other$player == null : this$player.equals(other$player);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ClanLeaveEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        return result;
    }
}
