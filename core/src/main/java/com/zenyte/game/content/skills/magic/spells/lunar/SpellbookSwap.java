package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Kris | 19. veebr 2018 : 2:27.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SpellbookSwap implements DefaultSpell {
	private static final Animation ANIM = new Animation(6299);
	private static final Graphics GFX = new Graphics(1062, 0, 10);

	@Override
	public int getDelay() {
		return 6000;
	}

	@Override
	public boolean spellEffect(final Player player, int optionId, final String option) {
		if (!hasDefenceRequirement(player)) {
			return false;
		}
		if (optionId > 1) {
			optionId -= 2;
			if (optionId == 2) {
				optionId = 3;
			}
			handleSpell(player, optionId);
			return false;
		}
		player.getDialogueManager().start(new OptionDialogue(player, "Select a Spellbook", new String[] {"Regular spellbook", "Ancient spellbook", "Arceuus spellbook"}, new Runnable[] {() -> handleSpell(player, 0), () -> handleSpell(player, 1), () -> handleSpell(player, 3)}));
		return false;
	}

	private void handleSpell(final Player player, final int option) {
		player.getDialogueManager().finish();
		final SpellState state = new SpellState(player, this);
		if (!state.check()) {
			return;
		}
		player.setAnimation(ANIM);
		player.setGraphics(GFX);
		this.addXp(player, 130);
		player.setLunarDelay(6000);
		state.remove();
		WorldTasksManager.schedule(new WorldTask() {
			private boolean reset;
			@Override
			public void run() {
				if (player.isFinished()) {
					return;
				}
				if (!reset) {
					player.getCombatDefinitions().setSpellbook(Spellbook.getSpellbook(option), true);
					player.getTemporaryAttributes().put("SPELLBOOK_SWAP", Utils.currentTimeMillis() + 120000);
					player.getCombatDefinitions().resetAutocast();
					player.sendMessage("You have 2 minutes before your spellbook changes back!");
					reset = true;
				} else {
					final long value = player.getNumericTemporaryAttribute("SPELLBOOK_SWAP").longValue();
					if (value != 0 && value < Utils.currentTimeMillis()) {
						player.getCombatDefinitions().setSpellbook(Spellbook.LUNAR, true);
						player.getCombatDefinitions().resetAutocast();
						player.sendMessage("Your spellbook has been reset to lunars.");
					}
					stop();
				}
			}
		}, 1, 200);
	}

	public static final void checkSpellbook(final Player player) {
		if (player.getNumericTemporaryAttribute("SPELLBOOK_SWAP").longValue() < Utils.currentTimeMillis()) {
			return;
		}
		player.getCombatDefinitions().setSpellbook(Spellbook.LUNAR, true);
		player.getCombatDefinitions().resetAutocast();
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
