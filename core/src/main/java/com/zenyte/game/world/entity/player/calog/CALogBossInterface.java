package com.zenyte.game.world.entity.player.calog;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.collectionlog.CLCategoryType;
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogInterface;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import static com.zenyte.game.world.entity.player.calog.CALogTaskInterface.*;
import static com.zenyte.game.world.entity.player.calog.CALogTaskInterface.CA_TASK_COMPLETED_SELECT_VARBIT;

/**
 * @author Savions.
 */
public class CALogBossInterface extends Interface {

	private static final int CA_BOSS_ENUM = 3987;
	private static final int STRUCT_POINTER_MONSTER_VAL = 1315;
	private static final Int2IntMap MONSTER_VALUES = new Int2IntOpenHashMap();

	static {
		final IntEnum bossEnum = EnumDefinitions.getIntEnum(CA_BOSS_ENUM);
		bossEnum.getValues().forEach((k, v) -> {
			final StructDefinitions subCategoryStruct = Objects.requireNonNull(StructDefinitions.get(v));
			MONSTER_VALUES.put(k.intValue(), Integer.parseInt(subCategoryStruct.getValue(STRUCT_POINTER_MONSTER_VAL).orElseThrow(RuntimeException::new).toString()));
		});
	}

	@Override protected void attach() {
		put(13, "Collection log");
		put(23, "General");
		put(24, "Select task");
		put(27, "Navigation");
	}

	@Override public void open(Player player) {
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_BOSS, 23, 0, 100, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_BOSS, 24, 0, 100, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_BOSS, 27, 0, 100, AccessMask.CLICK_OP1);
	}

	@Override protected void build() {
		bind("Collection log", (player, slotId, itemId, option) -> {
			final Optional<CABossType> optionalType = Arrays.stream(CABossType.values).
					filter(e -> player.getVarManager().getBitValue(CALogBossOverviewInterface.BOSS_SELECT_VARBIT) == e.ordinal() + 1).findFirst();
			if (optionalType.isPresent()) {
				final CABossType type = optionalType.get();
				player.getTemporaryAttributes().put(CollectionLogInterface.CATEGORY_ATTR_KEY, type.isRaid() ? CLCategoryType.RAIDS : CLCategoryType.BOSS);
				player.getTemporaryAttributes().put(CollectionLogInterface.SUB_CATEGORY_ATTR_KEY, type.getCollLogVal());
				GameInterface.COLLECTION_LOG.open(player);
			}
		});
		bind("General", (player, slotId, itemId, option) -> {
			GameInterface.CA_BOSS_OVERVIEW.open(player);
		});
		bind("Select task", (player, slotId, itemId, option) -> {
			player.getVarManager().sendBit(CA_TASK_TIER_SELECT_VARBIT, 0);
			player.getVarManager().sendBit(CA_TASK_TYPE_SELECT_VARBIT, 0);
			player.getVarManager().sendBit(CA_TASK_MONSTER_SELECT_VARBIT, MONSTER_VALUES.get(player.getVarManager().getBitValue(12862)));
			player.getVarManager().sendBit(CA_TASK_COMPLETED_SELECT_VARBIT, 0);
			GameInterface.CA_TASKS.open(player);
		});
		bind("Navigation", (player, slotId, itemId, option) -> {
			if (slotId == 10) {
				GameInterface.CA_OVERVIEW.open(player);
			} else if (slotId == 12) {
				GameInterface.CA_TASKS.open(player);
			} else if (slotId == 14) {
				GameInterface.CA_BOSS_OVERVIEW.open(player);
			} else if (slotId == 16) {
				GameInterface.CA_REWARDS.open(player);
			}
		});
	}

	@Override public GameInterface getInterface() { return GameInterface.CA_BOSS; }
}
