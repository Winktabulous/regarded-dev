package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Tommeh | 28 jul. 2018 | 18:47:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RunClientScript implements GamePacketEncoder {

	private final int scriptId;
	private final Object[] arguments;
	private String type;
	private final boolean blank;

	@Override
	public void log(@NotNull final Player player) {
		this.log(player, "Script: " + scriptId + ", params: " + Arrays.toString(arguments));
	}

	public RunClientScript(int scriptId, boolean blank, Object... arguments) {
		this.scriptId = scriptId;
		this.blank = blank;
		this.arguments = arguments;
		char[] chars = new char[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			chars[i] = arguments[i] instanceof String ? 's' : 'i';
			type += arguments[i] instanceof String ? arguments[i].toString().length() + 1 : 4;
		}
		type = new String(chars);
	}

	public RunClientScript(int scriptId, boolean blank) {
		this.scriptId = scriptId;
		this.blank = blank;
		this.arguments = null;
		this.type = "";
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.RUNCLIENTSCRIPT.gamePacketOut();
		buffer.writeString(blank ? "" : type);
		if (!blank) {
			for (int i = arguments.length - 1; i >= 0; i--) {
				if (arguments[i] instanceof String) {
					buffer.writeString((String) arguments[i]);
				} else {
					buffer.writeInt((Integer) arguments[i]);
				}
			}
		}
		buffer.writeInt(scriptId);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
