package com.zenyte.game.packet.out;

import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:31:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class MapProjAnim implements GamePacketEncoder {

	private final Player player;
	private final Projectile projectile;
	private final Position fromPosition;
	private final Position target;
	private final int duration;
	private final int offset;

	@Override
	public void log(@NotNull final Player player) {
		final Location fromTile = fromPosition.getPosition();
		final Location toTile = target.getPosition();
		this.log(player, "Id: " + projectile.getGraphicsId() + ", height: " + projectile.getStartHeight() + " -> " + projectile.getEndHeight() + ", delay: " + projectile.getDelay() + ", angle: " + projectile.getAngle() + ", duration: " + projectile.getDuration() + ", offset: " + projectile.getDistanceOffset() + ", multiplier: " + projectile.getMultiplier() + ", tile: [" + fromTile.getX() + ", " + fromTile.getY() + ", " + fromTile.getPlane() + "] -> [" + toTile.getX() + ", " + toTile.getY() + ", " + toTile.getPlane() + "]");
	}

	public MapProjAnim(final Player player, final Position senderTile, final Position receiverObject, final Projectile projectile, final int duration, final int offset) {
		this.player = player;
		this.projectile = projectile;
		target = receiverObject;
		fromPosition = senderTile;
		this.duration = duration;
		this.offset = offset;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.MAPPROJ_ANIM.gamePacketOut();
		final Location lastTile = player.getLastLoadedMapRegionTile();
		final Location from = fromPosition.getPosition();
		final Location to = target.getPosition();
		final int srcHash = ((from.getLocalX(lastTile) & 7) << 4) | (from.getLocalY(lastTile) & 7);
		final int delay = projectile.getDelay();
		final int projSpeed = duration != Integer.MIN_VALUE ? duration : projectile.getProjectileDuration(from, target);
		final int index = target instanceof Player ? (-(((Player) target).getIndex() + 1)) : target instanceof NPC ? (((NPC) target).getIndex() + 1) : 0;
		buffer.writeByte(projectile.getAngle());
		buffer.write128Byte(srcHash);
		buffer.writeShort128(projectile.getGraphicsId());
		buffer.writeByte(offset);
		buffer.writeByte128((to.getY() - from.getY()));
		buffer.writeShortLE128(delay);
		buffer.writeByteC(projectile.getEndHeight());
		buffer.writeShortLE128(index);
		buffer.writeByte128(projectile.getStartHeight());
		buffer.writeShortLE(projSpeed);
		buffer.write128Byte((to.getX() - from.getX()));
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

}
