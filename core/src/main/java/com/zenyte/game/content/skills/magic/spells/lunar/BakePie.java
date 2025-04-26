package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.skills.cooking.CookingDefinitions.CookingData;
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
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;

public class BakePie implements DefaultSpell {
	private static final Animation ANIM = new Animation(4413);
	private static final Graphics GFX = new Graphics(746, 0, 100);

	@Override
	public int getDelay() {
		return 4000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		player.setLunarDelay(getDelay());
		player.getActionManager().setAction(new PieBakingAction(this));
		return false;
	}


	private static final class PieBakingAction extends Action {
		private final BakePie spell;

		public PieBakingAction(final BakePie spell) {
			this.spell = spell;
		}

		private static final int SIZE = CookingData.PIES.length;

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
			final int level = skills.getLevel(SkillConstants.COOKING);
			final SpellState state = new SpellState(player, spell);
			if (!state.check()) {
				return -1;
			}
			//Putting inventory up for refreshing at the end of the tick.
			player.getInventory().refreshAll();
			for (int i = 0; i < SIZE; i++) {
				final CookingData pie = CookingData.PIES[i];
				final int id = pie.getRaw();
				if (level < pie.getLevel()) {
					break;
				}
				for (int slot = 0; slot < 28; slot++) {
					final Item item = inventory.getItem(slot);
					if (item == null) {
						continue;
					}
					if (item.getId() != id) {
						continue;
					}
					item.setId(pie.getCooked());
					inventory.refresh(i);
					spell.addXp(player, 60);
					spell.addXp(player, SkillConstants.COOKING, pie.getXp());
					player.setLunarDelay(4000);
					player.setAnimation(ANIM);
					player.setGraphics(GFX);
					if (pie.equals(CookingData.WILD_PIE)) {
						player.getDailyChallengeManager().update(SkillingChallenge.COOK_WILD_PIES);
					} else if (pie.equals(CookingData.SUMMER_PIE)) {
						player.getAchievementDiaries().update(VarrockDiary.BAKE_A_SUMMER_PIE);
						player.getDailyChallengeManager().update(SkillingChallenge.COOK_SUMMER_PIES);
					}
					state.remove();
					if (!new SpellState(player, spell).check()) {
						player.sendMessage("You have no more runes remaining to cast this spell.");
						return -1;
					}
					return 2;
				}
			}
			player.sendMessage("You have no pies for which you have a level to cook.");
			return -1;
		}
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
