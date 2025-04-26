package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum SlayerMountType {
	LOCKED(0, -1),
	NONE(1, -1),
	RED(2, ItemId.RED_SLAYER_HELMET_I),
	HYDRA(3, ItemId.HYDRA_SLAYER_HELMET_I),
	TZTOK(4, ItemId.TZTOK_SLAYER_HELMET_I),
	GREEN(5, ItemId.GREEN_SLAYER_HELMET_I),
	BLACK(6, ItemId.BLACK_SLAYER_HELMET_I),
	TWISTED(7, ItemId.TWISTED_SLAYER_HELMET_I),
	REGULAR_IMBUED(8, ItemId.SLAYER_HELMET_I),
	PURPLE(9, ItemId.PURPLE_SLAYER_HELMET_I),
	TURQUOISE(10, ItemId.TURQUOISE_SLAYER_HELMET_I),
	VAMPYRIC(11, ItemId.VAMPYRIC_SLAYER_HELMET_I),
	TZKAL(12, ItemId.TZKAL_SLAYER_HELMET_I);

	private final int index, itemId;
	public static final Int2ObjectMap<SlayerMountType> helmToType = new Int2ObjectOpenHashMap<>();
	public static final Int2ObjectMap<SlayerMountType> indexToType = new Int2ObjectOpenHashMap<>();
	private final Item item;

	SlayerMountType(int index, int itemId) {
		this.index = index;
		this.itemId = itemId;
		if (itemId != -1) {
			this.item = new Item(itemId);
		} else {
			this.item = null;
		}
	}

	public int getIndex() {
		return index;
	}

	public Item getItem() {
		return item;
	}

	public int getItemId() {
		return itemId;
	}

	public boolean mounted(Player player) {
		return SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR)) == this;
	}

	public static boolean hasHelmMounted(Player player) {
		SlayerMountType slayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
		return slayerMountType != null && slayerMountType.getItemId() != -1;
	}

	static {
		for (SlayerMountType slayerMountType : values()) {
			indexToType.put(slayerMountType.getIndex(), slayerMountType);
			helmToType.put(slayerMountType.getItemId(), slayerMountType);
		}
	}

}
