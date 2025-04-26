package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:28:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class LocAnim implements GamePacketEncoder {
	private final int id;
	private final int type;
	private final int rotation;
	private final int x;
	private final int y;
	private final int plane;
	private final Animation animation;

	@Override
	public void log(@NotNull final Player player) {
		log(player, "Id: " + id + ", type: " + type + ", rotation: " + rotation + ", x: " + x + ", y: " + y + ", z: " + plane + ", animation id: " + animation.getId() + ", delay: " + animation.getDelay());
	}

	public LocAnim(WorldObject object, Animation animation) {
		type = object.getType();
		rotation = object.getRotation();
		x = object.getX();
		y = object.getY();
		id = object.getId();
		plane = object.getPlane();
		this.animation = animation;
	}

	public LocAnim(int id, int type, int rotation, Location location, Animation animation) {
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		x = location.getX();
		y = location.getY();
		plane = location.getPlane();
		this.animation = animation;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.LOC_ANIM.gamePacketOut();
		buffer.writeShort128(animation.getId());
		buffer.writeByte((type << 2) | rotation);
		buffer.writeByte((x & 7) << 4 | (y & 7));
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}
}
