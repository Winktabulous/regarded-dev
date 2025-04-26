package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.donation.HomeTeleport;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 13. juuli 2018 : 21:14:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class HomeStructure implements TeleportStructure {

	@Override
	public Animation getStartAnimation() {
		return null;
	}

	@Override
	public Graphics getStartGraphics() {
		return null;
	}

	@Override
	public void start(final Player player, final Teleport teleport) {
		player.getActionManager().setAction(new HomeTeleportAction(teleport));
		player.blockIncomingHits(5);
	}

	/**
	 * Ends the teleportation sequence, initiates the stop sequence based off of the duration of the ending animation.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 */
	@Override
	public void end(final Player player, final Teleport teleport) {
        updateDiaries(player, teleport);
	}

	public static final class HomeTeleportAction extends Action {

		private static final Animation[] ANIMATIONS = new Animation[]{
				new Animation(4847), new Animation(4850), new Animation(4853), new Animation(4855), new Animation(4857)
		};

		private static final SoundEffect[] sounds = new SoundEffect[]{
				new SoundEffect(193, 4, 0), new SoundEffect(196, 4, 0), new SoundEffect(194, 4, 0), new SoundEffect(195, 4, 0), null
		};

		private static final Graphics[] GRAPHICS = new Graphics[]{
				new Graphics(800), new Graphics(801), new Graphics(802), new Graphics(803), new Graphics(804)
		};

		private static final Animation STAND_UP = new Animation(4852);

		HomeTeleportAction(final Teleport teleport) {
			this.teleport = teleport;
		}

		private final Teleport teleport;
		private int ticks;

		@Override
		public boolean start() {
			return true;
		}

		@Override
		public boolean process() {
			return !player.isUnderCombat();
		}

		@Override
		public int processWithDelay() {
			if (ticks == 5) {
				HomeTeleport tp = HomeTeleport.get(player);
				Location location = tp.getLocation();
				player.setLocation(location);
                player.getInterfaceHandler().closeInterfaces();
				teleport.onArrival(player);
				teleport.getType().getStructure().updateDiaries(player, teleport);
				player.blockIncomingHits();
				WorldTasksManager.schedule(() -> {
					player.setAnimation(Animation.STOP);
					player.setGraphics(Graphics.RESET);
				});
				return -1;
			}
			player.setAnimation(ANIMATIONS[ticks]);
			if (GRAPHICS[ticks] != null) {
				player.setGraphics(GRAPHICS[ticks]);
			}
			if (sounds[ticks] != null) {
				World.sendSoundEffect(player.getPosition(), sounds[ticks]);
			}
			ticks++;
			return ticks == 1 ? 4 : ticks == 5 ? 1 : 2;
		}

		@Override
		public void stop() {
			if (player.getNextLocation() == null) {
				player.setAnimation(Animation.STOP);
				player.setGraphics(Graphics.RESET);
			}
		}

	}

}
