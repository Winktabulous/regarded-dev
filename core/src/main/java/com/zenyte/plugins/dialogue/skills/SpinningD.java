package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.crafting.CraftingDefinitions;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.SpinningData;
import com.zenyte.game.content.skills.crafting.actions.SpinningCrafting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 14 apr. 2018 | 16:04:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SpinningD extends SkillDialogue {
	private final WorldObject object;

	public SpinningD(final Player player, final WorldObject object) {
		super(player, SpinningData.WOOL.getProduct(), SpinningData.BOW_STRING.getProduct(), SpinningData.CROSSBOW_STRING.getProduct(), SpinningData.MAGIC_STRING.getProduct(), SpinningData.ROPE.getProduct());
		this.object = object;
	}

	@Override
	public void run(final int slotId, final int amount) {
		final CraftingDefinitions.SpinningData data = SpinningData.VALUES.get(slotId);
		if (data != null) {
			player.getActionManager().setAction(new SpinningCrafting(data, object, amount));
		}
	}
}
