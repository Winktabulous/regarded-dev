package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.donation.HomeTeleport;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.InvalidLocationException;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.TeleportPlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.utils.TimeUnit;

import java.util.OptionalInt;

import static com.zenyte.utils.TimeUnit.MILLISECONDS;
import static com.zenyte.utils.TimeUnit.SECONDS;

/**
 * @author Kris | 8. juuli 2018 : 23:46:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface TeleportStructure {
	/**
	 * The duration the player has to be out of combat for in seconds, before they may teleport if the teleport is prevented under combat.
	 */
	int COMBAT_PREVENTION_DELAY = (int) SECONDS.toSeconds(10);
	/**
	 * The amount of times it will try to find a randomized location that the player can be teleported to until giving up and returning the
	 * default value.
	 */
	int RANDOMIZATION_ATTEMPT_COUNT = 100;
	/**
	 * Extra delay in {@link TimeUnit#TICKS } for how long the player will be locked after the teleportation finishes.
	 */
	int EXTRA_DELAY = 1;
	SoundEffect DEFAULT_SOUND = new SoundEffect(200, 10, 0);

	/**
	 * The beginning animation of the teleport.
	 */
	default Animation getStartAnimation() {
		return null;
	}

	/**
	 * The ending animation of the teleport.
	 */
	default Animation getEndAnimation() {
		return null;
	}

	/**
	 * The beginning graphics of the teleport.
	 */
	default Graphics getStartGraphics() {
		return null;
	}

	/**
	 * The ending graphics of the teleport.
	 */
	default Graphics getEndGraphics() {
		return null;
	}

	/**
	 * The start sound effect for the teleport.
	 */
	default SoundEffect getStartSound() {
		return DEFAULT_SOUND;
	}

	/**
	 * The end sound effect for the teleport.
	 */
	default SoundEffect getEndSound() {
		return null;
	}

	/**
	 * Attempts to start the teleportation sequence for this given player.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 */
	default void teleport(final Player player, final Teleport teleport) {
		if (isTeleportPrevented(player, teleport) || isAreaPrevented(player, teleport) || isTeleblockRestricted(player) || isRestricted(player, teleport)) {
			return;
		}
		final SpellState state = new SpellState(player, teleport.getLevel(), teleport.getRunes());
		if (!state.check()) {
			return;
		}
		player.stopAll();
		SpellbookSwap.checkSpellbook(player);
		state.remove();
		start(player, teleport);
	}

	/**
	 * Whether the player is currently teleblocked or not.
	 *
	 * @param player
	 *            the player who is attempting to teleport.
	 * @return whether the player is teleblocked or not.
	 */
	default boolean isTeleblockRestricted(final Player player) {
		int time = player.getVariables().getTime(TickVariable.TELEBLOCK);
		if (time > 0) {
			final int totalSeconds = (int) (time * 0.6F);
			final int seconds = totalSeconds % 60;
			final int minutes = totalSeconds / 60;
			if (minutes > 0) {
				player.sendMessage("A teleport block has been cast on you. It should wear off in " + minutes + " minute" + (minutes > 1 ? "s, " : ", ") + seconds + " second" + (seconds > 1 ? "s." : "."));
			} else {
				player.sendMessage("A teleport block has been cast on you. It should wear off in " + seconds + " second" + (seconds > 1 ? "s." : "."));
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the player will be restricted by either the under combat delay restriction, or the wilderness level restriction.
	 * Default wilderness restriction for teleports that don't have the restriction annotation is 20.
	 *
	 * @param player the player attempting to teleprot
	 * @param teleport the teleport the player is attempting to use
	 * @return whether the teleport is restricted for the player or not.
	 */
	default boolean isRestricted(final Player player, final Teleport teleport) {
		if (teleport.isCombatRestricted() && player.isUnderCombat()) {
			player.sendMessage("You need to be out of combat for " + COMBAT_PREVENTION_DELAY + " seconds to use this teleport.");
			return true;
		}
		final OptionalInt level = WildernessArea.getWildernessLevel(player.getLocation());
		if (!level.isPresent()) {
			return false;
		}
		final int restriction = teleport.getWildernessLevel();
		if (restriction != Integer.MIN_VALUE && level.getAsInt() > restriction) {
			player.sendMessage("A mysterious force prevents you from teleporting.");
			return true;
		}
		return false;
	}

	/**
	 * Begins the teleportation sequence, initiates the ending sequence based off of the duration of the starting animation.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 */
	default void start(final Player player, final Teleport teleport) {
		final double experience = teleport.getExperience();
		final Animation startAnimation = Utils.getOrDefault(getStartAnimation(), Animation.STOP);
		final Graphics startGraphics = Utils.getOrDefault(getStartGraphics(), Graphics.RESET);
		player.lock();
		final SoundEffect sound = getStartSound();
		if (sound != null) {
			World.sendSoundEffect(player, sound);
		}
		if (experience != 0) {
			player.getSkills().addXp(SkillConstants.MAGIC, experience);
		}
		if(teleport.getType() != TeleportType.CRYSTAL_OF_MEMORIES) {
			if(player.getInventory().containsItem(ItemId.CRYSTAL_OF_MEMORIES)) {
				if(player.getArea() != null)
					player.sendMessage("Your crystal of memories stores a memory of your last teleport from <col=fc2e00>"+player.getArea().name()+".");
				else
					player.sendMessage("Your crystal of memories stores a memory of your last teleport from your current location");
				player.getAttributes().put("CRYSTAL_OF_MEMORIES", player.getLocation().getPositionHash());
			}
		}
		teleport.onUsage(player);
		player.setInvalidAnimation(startAnimation);
		player.setGraphics(startGraphics);
		int teleportDurationInTicks = (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(startAnimation)) - 1;
		player.blockIncomingHits(teleportDurationInTicks);
		WorldTasksManager.scheduleOrExecute(() -> end(player, teleport), teleportDurationInTicks);
	}

	/**
	 * Ends the teleportation sequence, initiates the stop sequence based off of the duration of the ending animation.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 */
	default void end(final Player player, final Teleport teleport) {
		if (isTeleportPrevented(player, teleport) || isAreaPrevented(player, teleport) || isRestricted(player, teleport)) {
			stop(player, teleport);
			return;
		}
		final Animation endAnimation = Utils.getOrDefault(getEndAnimation(), Animation.STOP);
		final Graphics endGraphics = Utils.getOrDefault(getEndGraphics(), Graphics.RESET);
		final SoundEffect endSound = getEndSound();
		final Location location = getRandomizedLocation(player, teleport);
		verifyLocation(player, location);
		player.getInterfaceHandler().closeInterfaces();
		teleport.onArrival(player);
		player.setLocation(location);
		player.getChunksInScope().clear();
		player.setInvalidAnimation(endAnimation);
		player.setGraphics(endGraphics);
		if (endSound != null) {
			//Need to send this individually for the player as well.
			player.sendSound(endSound);
			World.sendSoundEffect(location, endSound);
		}
		player.updateScopeInScene();
		WorldTasksManager.scheduleOrExecute(() -> stop(player, teleport), (int) MILLISECONDS.toTicks(AnimationUtil.getCeiledDuration(endAnimation)) - 1 + EXTRA_DELAY);
//		updateDiaries(player, teleport);
	}

	default void updateDiaries(final Player player, final Teleport teleport) {

	}
	/*default void updateDiaries(final Player player, final Teleport teleport) {
		if (teleport.equals(VARROCK_TELEPORT)) {
			player.getAchievementDiaries().update(VarrockDiary.CAST_VARROCK_TELEPORT);
		} else if (teleport.equals(FALADOR_TELEPORT)) {
			player.getAchievementDiaries().update(FaladorDiary.TELEPORT_FALADOR);
		} else if (teleport.equals(ARDOUGNE_TELEPORT)) {
			player.getAchievementDiaries().update(ArdougneDiary.CAST_ARDOUGNE_TELEPORT_SPELL);
		} else if (teleport.equals(PADDEWWA_TELEPORT)) {
			player.getAchievementDiaries().update(VarrockDiary.TELEPORT_TO_PADDEWWA);
		} else if (teleport.equals(LUMBRIDGE_TELEPORT)) {
			player.getAchievementDiaries().update(LumbridgeDiary.CAST_LUMBRIDGE_TELEPORT);
		} else if (teleport.equals(GHORROCK_TELEPORT)) {
			player.getAchievementDiaries().update(WildernessDiary.TELEPORT_TO_GHORROCK);
		} else if (teleport.equals(TROLLHEIM_TELEPORT)) {
			player.getAchievementDiaries().update(FremennikDiary.TELEPORT_TO_TROLLHEIM);
		} else if (teleport.equals(WATERBIRTH_TELEPORT)) {
			player.getAchievementDiaries().update(FremennikDiary.TELEPORT_TO_WATERBIRTH_ISLAND);
		} else if (teleport.equals(CAMELOT_TELEPORT)) {
			player.getAchievementDiaries().update(KandarinDiary.TELEPORT_TO_CAMELOT);
		} else if (teleport.equals(CATHERBY_TELEPORT)) {
			player.getAchievementDiaries().update(KandarinDiary.TELEPORT_TO_CATHERBY);
		} else if (teleport.equals(WATCHTOWER_TELEPORT)) {
			player.getAchievementDiaries().update(ArdougneDiary.TELEPORT_WATCHTOWER);
		} else if (teleport.equals(APE_ATOLL_TELEPORT_REG)) {
			player.getAchievementDiaries().update(WesternProvincesDiary.TELEPORT_TO_APE_ATOLL);
		} else if (teleport.equals(PEST_CONTROL)) {
			player.getAchievementDiaries().update(WesternProvincesDiary.TELEPORT_TO_PEST_CONTROL)
		}
	}*/

	/**
	 * Verifies if the location the player is attempting to teleport to is valid; if not, throws an exception and cancels the teleport.
	 *
	 * @param player
	 *            the player who is attempting to teleport.
	 * @param location
	 *            the location to which the player teleports.
	 */
	default void verifyLocation(final Player player, final Location location) {
		World.loadRegion(location.getRegionId());
		if (!World.isFloorFree(location, player.getSize())) {
			invalidTeleport(player, location);
		}
	}

	/**
	 * Finishes the teleport, clearing the player of all received hits and unlocking them entirely.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 */
	default void stop(final Player player, final Teleport teleport) {
		player.blockIncomingHits();
		player.resetFreeze();
		player.unlock();
	}

	/**
	 * Gets a random location within the {@link Teleport#getRandomizationDistance()} radius to the {@link Teleport#getDestination()}
	 * location.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 * @return a random destination location within the randomization radius.
	 */
	default Location getRandomizedLocation(final Player player, final Teleport teleport) {
		final int randomization = teleport.getRandomizationDistance();

		Location destination = teleport.getDestination();

		if(destination != null && teleport.getDestination().equals(3087, 3490, 0))
			destination = HomeTeleport.get(player).getLocation();

		if (destination == null) {
			return invalidTeleport(player, null);
		}
		if (randomization <= 0) {
			return destination;
		}
		int count = RANDOMIZATION_ATTEMPT_COUNT;
		while (--count > 0) {
			final Location tile = RandomLocation.random(destination, randomization);
			World.loadRegion(tile.getRegionId());
			if (ProjectileUtils.isProjectileClipped(player, null, destination, tile, true) || !World.isFloorFree(tile, player.getSize())) continue;
			return tile;
		}
		return destination;
	}

	default Location invalidTeleport(final Player player, final Location location) throws InvalidLocationException {
		player.setAnimation(Animation.STOP);
		player.setGraphics(Graphics.RESET);
//		player.sendMessage("Invalid teleport!");
		player.sendMessage("Teleport not currently available.");
		player.unlock();
		throw new InvalidLocationException(player, location);
	}

	/**
	 * Checks for additional custom teleportation preventions, if applicable. By default there will be none.
	 *
	 * @param player
	 *            the player who is attempting to teleport.
	 * @param teleport
	 *            the teleport that's being executed.
	 * @return whether or not the player will be blocked from teleporting.
	 */
	default boolean isTeleportPrevented(final Player player, final Teleport teleport) {
		return false;
	}

	/**
	 * Checks if the area the player currently is in prevents the player from teleporting.
	 *
	 * @param player
	 *            the player who is teleporting.
	 * @param teleport
	 *            the teleport that's being executed.
	 * @return whether or not the teleport is blocked by the area.
	 */
	default boolean isAreaPrevented(final Player player, final Teleport teleport) {
		final RegionArea area = player.getArea();
		if (area instanceof TeleportPlugin) {
			final TeleportPlugin plugin = (TeleportPlugin) area;
			return !plugin.canTeleport(player, teleport);
		}
		return false;
	}
}
