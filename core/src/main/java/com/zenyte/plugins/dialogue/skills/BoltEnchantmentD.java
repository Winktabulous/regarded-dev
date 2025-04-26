package com.zenyte.plugins.dialogue.skills;

import com.zenyte.game.content.skills.magic.actions.BoltEnchantment;
import com.zenyte.game.content.skills.magic.actions.BoltEnchantment.BoltEnchantmentData;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.SkillDialogue;

import java.util.List;

public class BoltEnchantmentD extends SkillDialogue {
	private final List<BoltEnchantmentData> data;
	private final MagicSpell spell;

	public BoltEnchantmentD(final Player player, final MagicSpell spell, final List<BoltEnchantmentData> data, final Item... items) {
		super(player, "How many sets of bolts to enchant?", items);
		this.spell = spell;
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		final BoltEnchantment.BoltEnchantmentData data = this.data.get(slotId);
		player.getActionManager().setAction(new BoltEnchantment(spell, data, amount));
	}
}
