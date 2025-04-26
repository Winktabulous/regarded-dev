package com.zenyte.game.world.entity.player.calog;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;

import java.util.stream.Stream;

/**
 * @author Savions.
 */
public class CALogBossOverviewInterface extends Interface {

	public static final int BOSS_SELECT_VARBIT = 12862;

	static {
		Stream.of(CABossType.values).forEach(b -> VarManager.appendPersistentVarbit(b.getTaskVarBit()));
	}

	@Override protected void attach() {
		put(15, "Select");
		put(23, "Navigation");
	}

	@Override public void open(Player player) {
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_BOSS_OVERVIEW, 15, 0, 100, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_BOSS_OVERVIEW, 23, 0, 100, AccessMask.CLICK_OP1);
	}

	@Override protected void build() {
		bind("Select", (player, slotId, itemId, option) -> {
			if (slotId >= 1 && slotId <= 50) {
				player.getVarManager().sendBitInstant(BOSS_SELECT_VARBIT, slotId);
				GameInterface.CA_BOSS.open(player);
			}
		});
		bind("Navigation", (player, slotId, itemId, option) -> {
			if (slotId == 10) {
				GameInterface.CA_OVERVIEW.open(player);
			} else if (slotId == 12) {
				GameInterface.CA_TASKS.open(player);
			} else if (slotId == 16) {
				GameInterface.CA_REWARDS.open(player);
			}
		});
	}

	@Override public GameInterface getInterface() {
		return GameInterface.CA_BOSS_OVERVIEW;
	}
}
