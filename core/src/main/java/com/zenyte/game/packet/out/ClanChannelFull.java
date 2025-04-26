package com.zenyte.game.packet.out;

import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanChannelBuilder;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:09:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ClanChannelFull implements GamePacketEncoder {

	private final ClanChannelBuilder builder;

	@Override
	public void log(@NotNull final Player player) {
		final ClanChannel channel = builder.getChannel();
		this.log(player, "Channel owner: " + channel.getOwner() + ", prefix: " + channel.getPrefix() + ", members: " + channel.getMembers().size());
	}

	public ClanChannelFull(ClanChannelBuilder builder) {
		builder.retain();
		this.builder = builder;
	}

	@Override
	public GamePacketOut encode() {
		return builder.encode();
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

}
