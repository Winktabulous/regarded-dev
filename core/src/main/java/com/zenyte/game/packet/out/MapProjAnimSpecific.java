package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

public final class MapProjAnimSpecific implements GamePacketEncoder {

	@Override
	public void log(@NotNull final Player player) {
	}

	public MapProjAnimSpecific(final Player player, final Position senderTile, final Position receiverObject, final Projectile projectile, final int duration, final int offset) {
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.PROJANIM_SPECIFIC.gamePacketOut();
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.SPAM;
	}

}
