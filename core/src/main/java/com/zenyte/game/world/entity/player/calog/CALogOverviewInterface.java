package com.zenyte.game.world.entity.player.calog;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogCategoryType;

import static com.zenyte.game.world.entity.player.calog.CALogTaskInterface.*;

/**
 * @author Savions.
 */
public class CALogOverviewInterface extends Interface {

	@Override protected void attach() {
		put(7, "Task selection");
		put(16, "Navigation");
	}

	@Override public void open(Player player) {
		for (CollectionLogCategoryType type : CollectionLogCategoryType.values) {
			if (type.function != null && type.function.length > 0) {
				for (int fi = 0; fi < type.function.length; fi++) {
					if (type.varpIds.length <= fi || type.varpIds[fi] == -1) {
						continue;
					}
					player.getVarManager().sendVar(type.varpIds[fi], type.function[fi].apply(player));
				}
			}
		}
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_OVERVIEW, 7, 0, 100, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_OVERVIEW, 16, 0, 100, AccessMask.CLICK_OP1);
	}

	@Override protected void build() {
		bind("Navigation", (player, slotId, itemId, option) -> {
			if (slotId == 12) {
				GameInterface.CA_TASKS.open(player);
			} else if (slotId == 14) {
				GameInterface.CA_BOSS_OVERVIEW.open(player);
			} else if (slotId == 16) {
				GameInterface.CA_REWARDS.open(player);
			}
		});
		bind("Task selection", (player, slotId, itemId, option) -> {
			if (slotId >= 0 && slotId <= 5) {
				player.getVarManager().sendBit(CA_TASK_TIER_SELECT_VARBIT, slotId + 1);
				player.getVarManager().sendBit(CA_TASK_TYPE_SELECT_VARBIT, 0);
				player.getVarManager().sendBit(CA_TASK_MONSTER_SELECT_VARBIT, 0);
				player.getVarManager().sendBit(CA_TASK_COMPLETED_SELECT_VARBIT, 0);
				GameInterface.CA_TASKS.open(player);
			}
		});
	}

	@Override public GameInterface getInterface() {
		return GameInterface.CA_OVERVIEW;
	}
}
