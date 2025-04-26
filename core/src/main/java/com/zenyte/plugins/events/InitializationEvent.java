package com.zenyte.plugins.events;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Event;

public class InitializationEvent implements Event {
    private final Player player;
    private final Player savedPlayer;

    public InitializationEvent(Player player, Player savedPlayer) {
        this.player = player;
        this.savedPlayer = savedPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getSavedPlayer() {
        return savedPlayer;
    }

    @Override
    public String toString() {
        return "InitializationEvent(player=" + this.getPlayer() + ", savedPlayer=" + this.getSavedPlayer() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof InitializationEvent)) return false;
        final InitializationEvent other = (InitializationEvent) o;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (this$player == null ? other$player != null : !this$player.equals(other$player)) return false;
        final Object this$savedPlayer = this.getSavedPlayer();
        final Object other$savedPlayer = other.getSavedPlayer();
        return this$savedPlayer == null ? other$savedPlayer == null : this$savedPlayer.equals(other$savedPlayer);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof InitializationEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $savedPlayer = this.getSavedPlayer();
        result = result * PRIME + ($savedPlayer == null ? 43 : $savedPlayer.hashCode());
        return result;
    }
}
