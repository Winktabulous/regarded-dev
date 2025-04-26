package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.ObjectSpell;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Kris | 16. veebr 2018 : 4:31.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CurePlant implements ObjectSpell {
	private static final Animation ANIM = new Animation(4432);
	private static final Graphics GFX = new Graphics(748, 0, 120);

	@Override
	public int getDelay() {
		return 6000;
	}

	@Override
	public boolean spellEffect(final Player player, final WorldObject object) {
		if (!player.getFarming().getPatch(object).isPresent()) {
			player.sendMessage("Umm... this spell won't cure that!");
			return false;
		}
		final FarmingSpot spot = player.getFarming().create(object);
		if (spot == null) {
			player.getDialogueManager().start(new PlayerChat(player, "Um... this spell won't cure that!"));
			return false;
		}
		if (spot.getState().equals(PatchState.DEAD)) {
			player.getDialogueManager().start(new PlayerChat(player, "I don't think curing this will bring it back to " +
                    "life."));
			return false;
		}
		if (!spot.getState().equals(PatchState.DISEASED)) {
			player.getDialogueManager().start(new PlayerChat(player, "The " + spot.getPatch().getType().getSanitizedName() + " is healthy enough already."));
			return false;
		}
		player.lock();
		player.faceObject(object);
		player.setGraphics(GFX);
		player.setAnimation(ANIM);
		WorldTasksManager.schedule(() -> {
			this.addXp(player, 91.5);
			spot.cure();
			player.unlock();
			player.sendMessage("The produce in this patch has been restored to its natural health.");
		}, 8);
		/*final FarmingPatch patch = player.getFarming().getPatch(object);
		if (patch == null) {
			player.sendMessage("You can only cast this spell on diseased plants.");
			return false;
		}
		final FarmingSpot spot = player.getFarming().getSpot(patch);
		if (spot == null) {
			player.sendMessage("You can only cast this spell on diseased plants.");
			return false;
		}
		if (spot.isDead()) {
			player.sendMessage("I don't think curing this will bring it back to life.");
			return false;
		}
		if (!spot.isDiseased()) {
			player.sendMessage("You can only cast this spell on diseased plants.");
			return false;
		}
		player.lock();
		player.faceObject(object);
		player.setGraphics(GFX);
		player.setAnimation(ANIM);
		WorldTasksManager.schedule(() -> {
			this.addXp(player, 91.5);
			spot.setDiseased(false);
			player.getFarming().refreshPatch(spot);
			player.unlock();
			player.sendMessage("The produce in this patch has been restored to its natural health.");
		}, 8);*/
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
