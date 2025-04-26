package com.zenyte.plugins.flooritem;

import com.zenyte.game.content.minigame.warriorsguild.shotput.ShotTakeD;
import com.zenyte.game.content.minigame.warriorsguild.shotput.ShotputArea;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;

/**
 * @author Kris | 29. mai 2018 : 20:53:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShotTakePlugin implements FloorItemPlugin {

	@Override
	public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
		if (option.equals("Take")) {
			player.getDialogueManager().start(new ShotTakeD(player, 6074, item));
			return;
		}
	}
	
	@Override
	public boolean overrideTake() {
		return true;
	}

	@Override
	public int[] getItems() {
		return new int[] { ShotputArea.SHOT_18LB_ITEM.getId(), ShotputArea.SHOT_22LB_ITEM.getId() };
	}

}
