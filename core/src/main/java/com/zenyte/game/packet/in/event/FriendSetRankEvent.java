package com.zenyte.game.packet.in.event;

import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.clans.ClanRank;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Tommeh | 25-1-2019 | 20:17
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FriendSetRankEvent implements ClientProtEvent {
    private final String name;
    private final int rank;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Name: " + name + ", rank: " + rank);
    }

    @Override
    public void handle(Player player) {
        final ClanChannel channel = ClanManager.getChannel(player);
        if (channel == null) {
            return;
        }
        final ClanRank clanRank = ClanRank.getRank(rank);
        if (clanRank == null) {
            return;
        }
        final Optional<Player> loggedInPlayer = World.getPlayer(name);
        if (loggedInPlayer.isEmpty()) {
            channel.getRankedMembers().put(StringFormatUtil.formatUsername(name), clanRank);
            player.getPacketDispatcher().initFriendsList();
        } else {
            channel.getRankedMembers().put(StringFormatUtil.formatUsername(name), clanRank);
            if (channel.getMembers().contains(loggedInPlayer.get())) {
                ClanManager.refreshPartial(channel, loggedInPlayer.get(), true, false);
            }
            player.getPacketDispatcher().initFriendsList();
        }
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public FriendSetRankEvent(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }
}
