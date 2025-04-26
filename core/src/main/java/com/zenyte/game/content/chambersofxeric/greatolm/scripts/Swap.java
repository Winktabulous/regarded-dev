package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.LeftClaw;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Kris | 16. jaan 2018 : 1:45.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Swap implements OlmCombatScript {
	private final List<PlayerPair> pairs = new ArrayList<>();
	private static final Graphics white = new Graphics(1359);
	private static final Graphics green = new Graphics(1360);
	private static final Graphics yellow = new Graphics(1361);
	private static final Graphics purple = new Graphics(1362);
	private static final Graphics teleportGraphics = new Graphics(436);
	private static final SoundEffect sound = new SoundEffect(1551, 5, 0);
	private static final SoundEffect teleportSound = new SoundEffect(200);

	@Override
	public void handle(final GreatOlm olm) {
		final List<Player> everyone = olm.everyone(GreatOlm.ENTIRE_CHAMBER);
		final int chamberSize = everyone.size();
		if (chamberSize == 0) {
			return;
		}
		final List<Graphics> colours = new ArrayList<>(Arrays.asList(white, green, yellow, purple));
		int pairCount = (int) Math.min(4, Math.max(1, Math.floor(chamberSize / 2.0F)));
		while (pairCount-- > 0) {
			final Player primary = everyone.remove(Utils.random(everyone.size() - 1));
			Player secondary;
			Location tile;
			if (!everyone.isEmpty()) {
				tile = null;
				secondary = everyone.remove(Utils.random(everyone.size() - 1));
			} else {
				secondary = null;
				tile = olm.getPortalLocation(primary, GreatOlm.ENTIRE_CHAMBER);
			}
			if (secondary == null && tile == null) {
				continue;
			}
			pairs.add(new PlayerPair(primary, secondary, tile, colours.remove(0)));
			if (secondary == null) {
				primary.sendMessage("The Olm has no one to pair you with! The magical power will enact soon...");
				primary.getTemporaryAttributes().remove("deathSpot");
			}
			else {
				primary.sendMessage("You have been paired with <col=ff0000>" + secondary.getPlayerInformation().getDisplayname() + "</col>! The magical power will enact soon.");
				secondary.sendMessage("You have been paired with <col=ff0000>" + primary.getPlayerInformation().getDisplayname() + "</col>! The magical power will enact soon.");
				primary.getTemporaryAttributes().remove("deathSpot");
				secondary.getTemporaryAttributes().remove("deathSpot");
			}
		}
		if (pairs.isEmpty()) {
			return;
		}
		olm.getScripts().add(this.getClass());
		if (olm.getRoom().getLeftClaw() != null) {
			olm.getRoom().getLeftClaw().displaySign(LeftClaw.SWIRL_SIGN);
		}
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks = 10;
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed()) {
					stop();
					return;
				}
				final int originalTicks = ticks;
				switch (ticks--) {
				case 10: 
				case 8: 
				case 6: 
				case 4: 
					pairs.removeIf(pair -> {
						if (pair.getPrimary().getTemporaryAttributes().get("deathSpot") != null || pair.getPrimary().isDead() || !olm.getRoom().inChamber(pair.getPrimary().getLocation())) {
							return true;
						}
						if (originalTicks == 10) {
							World.sendSoundEffect(pair.getPrimary().getLocation(), sound);
						}
						pair.getPrimary().setGraphics(pair.getColour());
						if (pair.getSecondary() == null) {
							World.sendGraphics(pair.getColour(), pair.getTile());
						} else {
							if (pair.getSecondary().getTemporaryAttributes().get("deathSpot") != null) {
								pair.setTile(new Location((Location) pair.getSecondary().getTemporaryAttributes().get("deathSpot")));
								pair.setSecondary(null);
							} else {
								if (pair.getSecondary().isDead() || pair.getSecondary().getLocation().getDistance(olm.getLocation()) >= 50) {
									return true;
								}
								if (originalTicks == 10) {
									World.sendSoundEffect(pair.getSecondary().getLocation(), sound);
								}
								pair.getSecondary().setGraphics(pair.getColour());
							}
						}
						return false;
					});
					break;
				case 2: 
					stop();
					olm.getScripts().remove(Swap.this.getClass());
					pairs.forEach(pair -> {
						if (pair.getPrimary().getTemporaryAttributes().get("deathSpot") != null || pair.getPrimary().isDead() || !olm.getRoom().inChamber(pair.getPrimary().getLocation())) {
							return;
						}
						final int distance = (int) pair.getPrimary().getLocation().getDistance(pair.getSecondary() == null ? pair.getTile() : pair.getSecondary().getLocation());
						if (distance == 0) {
							pair.getPrimary().sendMessage("The teleport attack had no effect on you.");
							if (pair.getSecondary() != null) {
								pair.getSecondary().sendMessage("The teleport attack had no effect on you.");
							}
						} else {
							final int damage = Math.min(95, 5 * distance);
							final Location primary = new Location(pair.getPrimary().getLocation());
							final Player secondaryPlayer = pair.getSecondary();
							final Location secondary = secondaryPlayer == null ? pair.getTile() : secondaryPlayer.getTemporaryAttributes().get("deathSpot") instanceof Location ? new Location((Location) pair.getSecondary().getTemporaryAttributes().get("deathSpot")) : new Location(secondaryPlayer.getLocation());
							if (secondary == null) {
								pair.getPrimary().sendMessage("As you had no pairing...you are taken into a random spot.");
							}
							pair.getPrimary().applyHit(new Hit(olm, damage, HitType.REGULAR));
							pair.getPrimary().sendSound(teleportSound);
							pair.getPrimary().resetWalkSteps();
							World.sendGraphics(teleportGraphics, primary);
							World.sendGraphics(teleportGraphics, secondary);
							if (pair.getSecondary() != null) {
								pair.getSecondary().applyHit(new Hit(olm, damage, HitType.REGULAR));
								pair.getPrimary().setLocation(secondary);
								pair.getSecondary().setLocation(primary);
								pair.getSecondary().resetWalkSteps();
								pair.getSecondary().sendSound(teleportSound);
							} else {
								pair.getPrimary().setLocation(pair.getTile());
							}
						}
					});
					break;
				}
			}
		}, 0, 0);
	}

	private static final class PlayerPair {
		private final Player primary;
		private final Graphics colour;
		private Player secondary;
		private Location tile;

		PlayerPair(final Player primary, final Player secondary, final Location tile, final Graphics colour) {
			this.primary = primary;
			this.secondary = secondary;
			this.tile = tile;
			this.colour = colour;
		}

		public Player getPrimary() {
			return primary;
		}

		public Player getSecondary() {
			return secondary;
		}

		public void setSecondary(Player secondary) {
			this.secondary = secondary;
		}

		public Location getTile() {
			return tile;
		}

		public void setTile(Location tile) {
			this.tile = tile;
		}

		public Graphics getColour() {
			return colour;
		}
	}
}
