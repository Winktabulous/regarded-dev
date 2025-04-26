package com.zenyte.game.packet.out;

import com.zenyte.game.item.Item;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ItemContainer;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.net.game.ServerProt;
import com.zenyte.net.game.packet.GamePacketOut;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 28 jul. 2018 | 18:57:47
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class UpdateInvFull implements GamePacketEncoder {

	private int key;
	private int interfaceId;
	private int componentId;
	private ItemContainer container;
	private Container newContainer;
	private ContainerType type;

	@Override
	public void log(@NotNull final Player player) {
		if (type == null) {
			return;
		}
		log(player, "Type: " + type.getName());
	}

	public UpdateInvFull(final int key, final int interfaceId, final int componentId, final ItemContainer items) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.key = key;
		container = items;
	}

	public UpdateInvFull(final int key, final int interfaceId, final int componentId, final Container items) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.key = key;
		newContainer = items;
	}

	public UpdateInvFull(final Container items) {
		newContainer = items;
	}

	public UpdateInvFull(final Container items, final ContainerType type) {
		newContainer = items;
		this.type = type;
	}

	@Override
	public GamePacketOut encode() {
		final GamePacketOut buffer = ServerProt.UPDATE_INV_FULL.gamePacketOut();
		if (newContainer != null) {
			final ContainerType type = this.type == null ? newContainer.getType() : this.type;
			if (key > 0) {
				buffer.writeInt(interfaceId << 16 | componentId);
				buffer.writeShort(key);
			} else {
				buffer.writeInt(type.getInterfaceId() << 16 | type.getComponentId());
				buffer.writeShort(type.getId());
			}
			final int size = newContainer.getContainerSize();
			buffer.writeShort(size);
			for (int i = 0; i < size; i++) {
				final Item item = newContainer.get(i);
				final int amount = item == null ? 0 : item.getAmount();
				buffer.writeByte(amount > 254 ? 255 : amount);
				if (amount > 254) {
					buffer.writeInt(amount);
				}
				buffer.writeShort128(item == null ? 0 : (item.getId() + 1));
			}
			Container.AWAITING_RESET_CONTAINERS.add(newContainer);
			return buffer;
		}
		buffer.writeInt(interfaceId << 16 | componentId);
		buffer.writeShort(key);
		final Item[] items = container.getItems();
		final int size = items.length;
		buffer.writeShort(size);
		for (final Item item : items) {
			final int amount = item == null ? 0 : item.getAmount();
			buffer.writeByte(amount > 254 ? 255 : amount);
			if (amount > 254) {
				buffer.writeIntIME(amount);
			}
			buffer.writeShortLE(item == null ? 0 : (item.getId() + 1));
		}
		return buffer;
	}

	@Override
	public LogLevel level() {
		return LogLevel.LOW_PACKET;
	}

}
