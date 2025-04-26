package com.zenyte.plugins.item;

import com.zenyte.game.content.minigame.barrows.BarrowsWight;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

/**
 * @author Kris | 25. aug 2018 : 22:48:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Spade extends ItemPlugin {
	private static final int BRINE_RATS_DIG_SPOT_HASH = new Location(2748, 3733, 0).getPositionHash();
	private static final Animation DIG_ANIM = new Animation(830);
	private static final SoundEffect soundEffect = new SoundEffect(1470);
	private static final int[] GIANT_MOLE_HOLE_HASHES = {new Location(2984, 3387, 0).getPositionHash(), new Location(2987, 3387, 0).getPositionHash(), new Location(2989, 3378, 0).getPositionHash(), new Location(2996, 3377, 0).getPositionHash(), new Location(2999, 3375, 0).getPositionHash(), new Location(3005, 3376, 0).getPositionHash()};

	@Override
	public void handle() {
		bind("Dig", (player, item, slotId) -> {
			player.resetWalkSteps();
			player.setAnimation(DIG_ANIM);
			player.sendSound(soundEffect);
			WorldTasksManager.schedule(() -> {
				if (TreasureTrail.dig(player)) {
					return;
				}
				final Optional<BarrowsWight> mound = player.getBarrows().getMound();
				if (mound.isPresent()) {
					player.getBarrows().enter(mound.get());
					return;
				}
				if (player.getLocation().getPositionHash() == BRINE_RATS_DIG_SPOT_HASH) {
					player.setLocation(new Location(2696, 10118, 0));
					player.sendMessage("You fall down into a cavern.");
					return;
				}
				if (ArrayUtils.contains(GIANT_MOLE_HOLE_HASHES, player.getLocation().getPositionHash())) {
					player.setLocation(new Location(1752, 5237, 0));
					return;
				}
				player.sendMessage("Nothing interesting happens.");
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[] {952};
	}
}
