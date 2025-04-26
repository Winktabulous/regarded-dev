package com.zenyte.game.content.godwars;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 13/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GodwarsInstanceManager {
    private static final GodwarsInstanceManager manager = new GodwarsInstanceManager();
    private final Map<String, GodwarsInstance> clanInstances = new Object2ObjectOpenHashMap<>();

    public void addInstance(@NotNull final String clanOwner, @NotNull final GodwarsInstance area) {
        final GodwarsInstance existingInstance = clanInstances.get(clanOwner + "|" + area.getGod().name());
        Preconditions.checkArgument(existingInstance == null || existingInstance.getPlayers().isEmpty());
        clanInstances.put(clanOwner + "|" + area.getGod().name(), area);
    }

    public void removeInstance(@NotNull final String clanOwner, @NotNull final GodwarsInstance area) {
        final GodwarsInstance existingInstance = clanInstances.get(clanOwner + "|" + area.getGod().name());
        Preconditions.checkArgument(existingInstance != null && existingInstance.getPlayers().isEmpty() && existingInstance.equals(area));
        clanInstances.remove(clanOwner + "|" + area.getGod().name(), area);
    }

    public Optional<GodwarsInstance> findInstance(@NotNull final Player player, @NotNull final GodType god) {
        final ClanChannel channel = player.getSettings().getChannel();
        Preconditions.checkArgument(channel != null);
        final String owner = channel.getOwner();
        Preconditions.checkArgument(owner != null);
        return Optional.ofNullable(clanInstances.get(owner + "|" + god.name()));
    }

    public static GodwarsInstanceManager getManager() {
        return manager;
    }
}
