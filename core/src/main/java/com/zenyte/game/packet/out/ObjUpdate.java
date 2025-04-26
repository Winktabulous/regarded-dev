package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 11. march 2018 : 19:42.07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class ObjUpdate implements GamePacketEncoder {
	private final FloorItem floorItem;
	private final int oldQuantity;

	@Override
	public void log(@NotNull final Player player) {
		final Location tile = floorItem.getLocation();
		this.log(player, "Item: " + floorItem.getId() + ", amount: " + floorItem.getAmount() + ", old quantity: " + oldQuantity + ", x: " + tile.getX() + ", y: " + tile.getY() + ", z: " + tile.getPlane());
	}

	public ObjUpdate(FloorItem floorItem, int oldQuantity) {
		this.floorItem = floorItem;
		this.oldQuantity = oldQuantity;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.OBJ_COUNT.gamePacketOut();
		final int targetLocalX = floorItem.getLocation().getX() - ((floorItem.getLocation().getX() >> 3) << 3);
		final int targetLocalY = floorItem.getLocation().getY() - ((floorItem.getLocation().getY() >> 3) << 3);
		final int offsetHash = (targetLocalX & 7) << 4 | (targetLocalY & 7);
		buffer.writeShortLE(Math.min(65535, floorItem.getAmount()));
		buffer.writeShortLE128(floorItem.getId());
		buffer.writeShortLE128(oldQuantity);
		buffer.writeByte128(offsetHash);
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
