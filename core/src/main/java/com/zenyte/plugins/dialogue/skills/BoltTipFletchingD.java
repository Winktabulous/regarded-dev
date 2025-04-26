package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.fletching.FletchingDefinitions.BoltTipFletchingData;
import com.zenyte.game.content.skills.fletching.actions.BoltTipFletching;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 19:08:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BoltTipFletchingD extends SkillDialogue {

	private final BoltTipFletchingData data;

	public BoltTipFletchingD(final Player player, final BoltTipFletchingData data) {
		super(player, "How many sets of " + data.getProduct().getAmount() + " will you make?", data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new BoltTipFletching(data, amount));
		}
	}

}
