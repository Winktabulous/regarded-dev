package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.DefaultSpell;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.variables.TickVariable;

/**
 * @author Kris | 29. dets 2017 : 3:36.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Vengeance implements DefaultSpell {
	private static final Graphics GFX = new Graphics(726, 0, 92);
	private static final Animation ANIM = new Animation(8316);
	private static final Animation NON_BLOCKING_ANIM = new Animation(8316);
	private static final SoundEffect sound = new SoundEffect(2907, 10, 66);

	@Override
	public int getDelay() {
		return 0;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (!hasDefenceRequirement(player)) {
			return false;
		}
		final int vengDelay = player.getVariables().getTime(TickVariable.VENGEANCE);
		if (vengDelay > 0) {
			final int seconds = (int) Math.ceil(vengDelay * 0.6F);
			player.sendMessage("You need to wait another " + Math.min(30, seconds) + " second" + (seconds == 1 ? "" : "s") + " to cast a vengeance.");
			return false;
		}
		player.getVariables().schedule(50, TickVariable.VENGEANCE);
		player.getVarManager().sendBit(2451, 1);
		this.addXp(player, 112);
		player.getAttributes().put("vengeance", true);
		player.setGraphics(GFX);
		player.setAnimation(player.isCanPvp() ? ANIM : NON_BLOCKING_ANIM);
		World.sendSoundEffect(player, sound);
		return true;
	}
	@Override
	public void execute(final Player player, final int optionId, final String option) {
		if (!canCast(player)) {
			player.sendMessage("You cannot cast that spell on this spellbook.");
			return;
		}
		if (!canUse(player)) {
			return;
		}
		final SpellDefinitions definitions = SpellDefinitions.SPELLS.get(getSpellName());
		if (definitions == null) {
			return;
		}
		final long spellDelay = player.getNumericTemporaryAttribute("spellDelay").longValue();
		if (spellDelay > Utils.currentTimeMillis()) {
			return;
		}
		if (player.isLocked()) {
			return;
		}
		final SpellState state = new SpellState(player, this);
		if (!state.check()) {
			return;
		}
		player.getInterfaceHandler().closeInterfaces();
		if (spellEffect(player, optionId, option)) {
			player.setLunarDelay(getDelay());
			state.remove();
			SpellbookSwap.checkSpellbook(player);
		}
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	public String getSpellName() {
		return "vengeance";
	}
}
