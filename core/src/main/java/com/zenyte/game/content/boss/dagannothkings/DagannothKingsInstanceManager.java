package com.zenyte.game.content.boss.dagannothkings;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 18/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DagannothKingsInstanceManager {
    private static final DagannothKingsInstanceManager manager = new DagannothKingsInstanceManager();
    private final Map<String, DagannothKingInstance> instances = new Object2ObjectOpenHashMap<>();

    public void addInstance(@NotNull final String clanOwner, @NotNull final DagannothKingInstance area) {
        final DagannothKingInstance existingInstance = instances.get(clanOwner);
        Preconditions.checkArgument(existingInstance == null || existingInstance.getPlayers().isEmpty());
        instances.put(clanOwner, area);
    }

    public void removeInstance(@NotNull final String clanOwner, @NotNull final DagannothKingInstance area) {
        final DagannothKingInstance existingInstance = instances.get(clanOwner);
        Preconditions.checkArgument(existingInstance != null && existingInstance.getPlayers().isEmpty() && existingInstance.equals(area));
        instances.remove(clanOwner, area);
    }

    public Optional<DagannothKingInstance> findInstance(@NotNull final Player player) {
        final ClanChannel channel = player.getSettings().getChannel();
        Preconditions.checkArgument(channel != null);
        final String owner = channel.getOwner();
        Preconditions.checkArgument(owner != null);
        return Optional.ofNullable(instances.get(owner));
    }

    public Optional<DagannothKingInstance> findInstanceSolo(@NotNull final Player player) {
        return Optional.ofNullable(instances.get(player.getUsername()));
    }

    public static DagannothKingsInstanceManager getManager() {
        return manager;
    }
}
