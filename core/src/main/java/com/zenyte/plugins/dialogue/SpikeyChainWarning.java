package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.skills.agility.shortcut.SlayerTowerSpikeyChain;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class SpikeyChainWarning extends Dialogue {

	public SpikeyChainWarning(final Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		this.plain("A foul stench seems to be seeping down from the floor above... It could be dangerous up there without a nosepeg.");
		this.options("Go up anyway?", "Yes", "No").onOptionOne(() -> {
			SlayerTowerSpikeyChain.climb(player, SlayerTowerSpikeyChain.NOSEPEG_BOTTOM.getPositionHash());
		}).onOptionTwo(() -> {
			finish();
		});
	}

}
