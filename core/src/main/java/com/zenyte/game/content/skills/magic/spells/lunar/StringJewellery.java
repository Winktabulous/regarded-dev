package com.zenyte.game.content.skills.magic.spells.lunar;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 17. veebr 2018 : 16:17.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class StringJewellery implements DefaultSpell {
	private static final Animation ANIM = new Animation(4412);
	private static final Graphics GFX = new Graphics(730, 0, 100);
	private static final ImmutableMap<Integer, Integer> STRINGABLES = ImmutableMap.<Integer, Integer>builder().put(1673, 1692).put(1675, 1694).put(1677, 1696).put(1679, 1698).put(1681, 1700).put(1683, 1702).put(6579, 6581).put(19501, 19541).put(21099, 21108).put(21102, 21111).put(21105, 21114).put(4082, 4081).put(1714, 1716).put(1720, 1722).build();

	@Override
	public int getDelay() {
		return 4000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		player.setLunarDelay(getDelay());
		player.getActionManager().setAction(new JewelleryStringingAction(this));
		return false;
	}


	private static final class JewelleryStringingAction extends Action {
		public JewelleryStringingAction(final StringJewellery spell) {
			this.spell = spell;
		}

		private final StringJewellery spell;

		@Override
		public boolean start() {
			skills = player.getSkills();
			inventory = player.getInventory();
			return true;
		}

		private Skills skills;
		private Inventory inventory;

		@Override
		public boolean process() {
			return true;
		}

		@Override
		public int processWithDelay() {
			for (int slot = 0; slot < 28; slot++) {
				final Item item = inventory.getItem(slot);
				if (item == null) {
					continue;
				}
				final Integer response = STRINGABLES.get(item.getId());
				if (response == null) {
					continue;
				}
				final SpellState state = new SpellState(player, spell);
				if (!state.check()) {
					return -1;
				}
				state.remove();
				item.setId(response);
				inventory.refresh(slot);
				spell.addXp(player, 83);
				player.getSkills().addXp(SkillConstants.CRAFTING, 4);
				player.setLunarDelay(4000);
				player.setAnimation(ANIM);
				player.setGraphics(GFX);
				return 3;
			}
			player.sendMessage("You have no unstrung amulets left to string.");
			return -1;
		}
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
