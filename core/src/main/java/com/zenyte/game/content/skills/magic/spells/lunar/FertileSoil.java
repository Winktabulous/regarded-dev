package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.skills.farming.actions.Saturating;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ObjectSpell;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlayerChat;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

/**
 * @author Kris | 17. veebr 2018 : 17:31.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FertileSoil implements ObjectSpell {
	private static final Animation ANIM = new Animation(4413);
	private static final Graphics GFX = new Graphics(724, 0, 80);

	@Override
	public int getDelay() {
		return 5000;
	}

	@Override
	public boolean spellEffect(final Player player, final WorldObject object) {
		final FarmingSpot spot = player.getFarming().getPatch(object).isPresent() ? player.getFarming().create(object) : null;
		if (spot == null) {
			player.getDialogueManager().start(new PlayerChat(player, "Um... I don't want to fertilize that!"));
			return false;
		}
		if (spot.getState().equals(PatchState.DEAD)) {
			player.getDialogueManager().start(new PlayerChat(player, "I don't think fertilizing this will bring it " +
                    "back to life."));
			return false;
		}
		if (!ArrayUtils.contains(Saturating.allowedStates, spot.getState())) {
			player.sendMessage("Composting isn't going to make it get any bigger.");
			return false;
		}
		if (spot.getPatch().getType() == PatchType.HESPORI_PATCH || spot.getPatch().getType() == PatchType.GRAPEVINE_PATCH) {
			player.sendMessage("The patch won't benefit from that kind of treatment.");
			return false;
		}
		final int type = player.getInventory().containsItem(21622, 2) ? 3 : 2;
		final Optional<PatchFlag> compost = spot.getCompostFlag();
		if (compost.isPresent()) {
			//Have to allow re-saturing phasmatys patch due to the stupid diary.
			if (!spot.getPatch().equals(FarmingPatch.PHASMATYS_HERB) && (type == 3 ? PatchFlag.ULTRACOMPOST : PatchFlag.SUPERCOMPOST).ordinal() <= compost.get().ordinal()) {
				player.sendMessage("This " + spot.getPatch().getType().getSanitizedName() + " has already been treated with " + compost.get().toString().toLowerCase() + ".");
				return false;
			}
		}
		spot.getFlags().removeIf(f -> (f == PatchFlag.COMPOST || f == PatchFlag.SUPERCOMPOST || f == PatchFlag.ULTRACOMPOST));
		spot.setFlag(type == 3 ? PatchFlag.ULTRACOMPOST : PatchFlag.SUPERCOMPOST);
		player.setAnimation(ANIM);
		World.sendGraphics(GFX, object);
		if (type == 3) {
			player.getInventory().deleteItem(21622, 2);
		}
		player.faceObject(object);
		this.addXp(player, 87);
		this.addXp(player, SkillConstants.FARMING, 18);
		player.sendFilteredMessage("You saturate the patch with " + (type == 3 ? "ultracompost" : "supercompost") + ".");
		if (spot.getPatch().equals(FarmingPatch.PHASMATYS_HERB)) {
			player.getAchievementDiaries().update(MorytaniaDiary.FERTILIZE_MORYTANIA_HERB_PATCH);
		}
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
