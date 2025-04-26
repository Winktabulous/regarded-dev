package com.zenyte.plugins.events;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerDeathEvent implements Event {
    @NotNull
    private Player player;
    @Nullable
    private Entity source;

    public PlayerDeathEvent() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Entity getSource() {
        return source;
    }

    public void setSource(Entity source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "PlayerDeathEvent(player=" + this.getPlayer() + ", source=" + this.getSource() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerDeathEvent)) return false;
        final PlayerDeathEvent other = (PlayerDeathEvent) o;
        if (!other.canEqual(this)) return false;
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        if (this$player == null ? other$player != null : !this$player.equals(other$player)) return false;
        final Object this$source = this.getSource();
        final Object other$source = other.getSource();
        return this$source == null ? other$source == null : this$source.equals(other$source);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerDeathEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $player = this.getPlayer();
        result = result * PRIME + ($player == null ? 43 : $player.hashCode());
        final Object $source = this.getSource();
        result = result * PRIME + ($source == null ? 43 : $source.hashCode());
        return result;
    }
}
