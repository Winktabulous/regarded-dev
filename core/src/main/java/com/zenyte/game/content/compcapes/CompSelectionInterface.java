package com.zenyte.game.content.compcapes;

import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class CompSelectionInterface extends Interface {

	@Override
	public void open(Player player) {
		super.open(player);

		player.awaitInputIntNoClose(amount -> {
			System.out.println(amount);
		});
	}

	@Override
	protected void attach() {

	}

	@Override
	protected void build() {

	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.COMP_SELECTION;
	}

}
