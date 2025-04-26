package com.zenyte.game.content.skills.magic.spells.arceuus;

import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchFlag;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ObjectSpell;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 15. juuli 2018 : 18:34:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ResurrectCrops implements ObjectSpell {
	private static final float MODIFIER = 0.84F;
	private static final Graphics GFX = new Graphics(1298);
	private static final Animation ANIM = new Animation(7163);

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final WorldObject object) {
		if (!player.getFarming().getPatch(object).isPresent()) {
			player.sendMessage("You can only cast this spell on dead crops.");
			return false;
		}
		final FarmingSpot spot = player.getFarming().create(object);
		if (spot.isClear()) {
			player.sendMessage("This farming patch is empty.");
			return false;
		}
		if (spot.getState() != PatchState.DEAD) {
			player.sendMessage("The crops aren't dead yet.");
			return false;
		}
		if (spot.getFlags().contains(PatchFlag.RESURRECTED)) {
			player.sendMessage("The spot has already been resurrected once.");
			return false;
		}
		final int level = Math.min(99 - getLevel(), player.getSkills().getLevel(SkillConstants.MAGIC) - getLevel());
		final int percentage = (int) (50 + (level / MODIFIER));
		player.faceObject(object);
		if (Utils.random(100) < percentage) {
			spot.cure();
			spot.refreshTimer();
			spot.getFlags().add(PatchFlag.RESURRECTED);
			player.sendMessage("The produce in this patch has been restored to its natural health.");
		} else {
			spot.clear();
			player.sendMessage("You were unable to resurrect the crops.");
		}
		player.setGraphics(GFX);
		player.setAnimation(ANIM);
		addXp(player, 90);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.ARCEUUS;
	}
}
