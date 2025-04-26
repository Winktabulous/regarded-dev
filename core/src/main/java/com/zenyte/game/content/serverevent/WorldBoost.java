package com.zenyte.game.content.serverevent;

import com.near_reality.tools.discord.community.DiscordBroadcastKt;
import com.near_reality.tools.discord.community.DiscordCommunityBot;
import com.zenyte.game.model.ui.testinterfaces.ServerEventsInterface;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;
import kotlin.time.Duration;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class WorldBoost {

    private final WorldBoostType eventType;
    private long boostEnd;
    private int durationHours;

    public WorldBoost(WorldBoostType event, long boostEnd, long durationHours) {
        this.eventType = event;
        this.boostEnd = boostEnd;
        this.durationHours = (int) durationHours;
    }

    public void activate(boolean announce) {
        activate(null, announce);
    }

    public void activate(@Nullable Player activator, boolean announce) {
        World.getWorldBoosts().add(this);
        if(announce) {
            final String prefix = activator != null ? activator.getName() + " activated" : "World Boost activated";
            WorldBroadcasts.sendMessage(prefix + " " + eventType.getMssg(), eventType.getBroadcastType(), true);
        }
        World.getPlayers().forEach(ServerEventsInterface::update);
        DiscordBroadcastKt.boostStart(DiscordCommunityBot.INSTANCE, this);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= boostEnd;
    }

    public WorldBoostType getBoostType() {
        return eventType;
    }

    public long boostEnd() {
        return boostEnd;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void extend(int hours, boolean announce) {
        extend(null, hours, announce);
    }

    public void extend(@Nullable Player extender, int hours, boolean announce) {
        durationHours += hours;
        boostEnd += TimeUnit.HOURS.toMillis(hours);
        if(announce) {
            final String prefix = extender != null ? extender.getName() + " extended " : "World Boost extended";
            WorldBroadcasts.sendMessage(prefix + " " + eventType.getMssg(), eventType.getBroadcastType(), true);
        }
        World.getPlayers().forEach(ServerEventsInterface::update);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WorldBoost) obj;
        return Objects.equals(this.eventType, that.eventType) &&
                this.boostEnd == that.boostEnd;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, boostEnd);
    }

    @Override
    public String toString() {
        return "WorldBoost[" +
                "eventType=" + eventType + ", " +
                "boostEnd=" + boostEnd + ']';
    }

}
