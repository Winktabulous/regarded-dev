package com.zenyte.game.world.entity.player.calog;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class CALogTaskInterface extends Interface {

	public static final int CA_TASK_TIER_SELECT_VARBIT = 12858;
	public static final int CA_TASK_TYPE_SELECT_VARBIT = 12859;
	public static final int CA_TASK_MONSTER_SELECT_VARBIT = 12860;
	public static final int CA_TASK_COMPLETED_SELECT_VARBIT = 12861;

	@Override protected void attach() {
		put(25, "Navigation");
		put(30, "Tier");
		put(31, "Type");
		put(32, "Monster");
		put(33, "Completed");
	}

	@Override protected void build() {
		bind("Navigation", (player, slotId, itemId, option) -> {
			if (slotId == 10) {
				GameInterface.CA_OVERVIEW.open(player);
			} else if (slotId == 14) {
				GameInterface.CA_BOSS_OVERVIEW.open(player);
			} else if (slotId == 16) {
				GameInterface.CA_REWARDS.open(player);
			}
		});
		bind("Tier", (player, slotId, itemId, option) -> {
			if (slotId >= 1 && slotId <= 7) {
				player.getVarManager().sendBit(CA_TASK_TIER_SELECT_VARBIT, slotId - 1);
			}
		});
		bind("Type", (player, slotId, itemId, option) -> {
			if (slotId >= 1 && slotId <= 7) {
				player.getVarManager().sendBit(CA_TASK_TYPE_SELECT_VARBIT, slotId - 1);
			}
		});
		bind("Monster", (player, slotId, itemId, option) -> {
			if (slotId >= 1 && slotId <= 70) {
				player.getVarManager().sendBit(CA_TASK_MONSTER_SELECT_VARBIT, slotId - 1);
			}
		});
		bind("Completed", (player, slotId, itemId, option) -> {
			if (slotId >= 1 && slotId <= 3) {
				player.getVarManager().sendBit(CA_TASK_COMPLETED_SELECT_VARBIT, slotId - 1);
			}
		});
	}

	@Override public void open(Player player) {
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_TASKS, 25, 0, 100, AccessMask.CLICK_OP1);
		for (int i = 0; i < 4; i++) {
			player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_TASKS, 30 + i, 0, 100, AccessMask.CLICK_OP1);
		}
	}

	@Override public GameInterface getInterface() { return GameInterface.CA_TASKS; }
}
