package com.zenyte.game.world.entity.player;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.model.music.MusicLoader;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.region.area.plugins.MusicPlugin;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.enums.Enums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * @author Kris | 21. veebr 2018 : 4:27.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class MusicHandler {

	private static final Logger logger = LoggerFactory.getLogger(MusicHandler.class);

	public static final int[] VARP_IDS = new int[] {20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721, 906, 1009, 1338, 1681, 2065, 2237, 2950, 3418, 3575};
	/**
	 * The given player who owns this handler.
	 */
	private final transient Player player;
	/**
	 * An int2int map containing the varps and their values.
	 */
	private final Int2IntOpenHashMap unlockedTracks;
	/**
	 * The music track that's currently playing.
	 */
	private transient Music currentlyPlaying;
	/**
	 * The amount of ticks the current song has played for, and the amount of ticks at which this song ends.
	 */
	private transient int ticks;
	private transient int nextSongAtTicks;
	/**
	 * Whether the music player is currently stopped or not.
	 */
	private transient boolean stopped;
	private transient boolean forcePlayNext;

	MusicHandler(final Player player) {
		this.player = player;
		unlockedTracks = new Int2IntOpenHashMap(VARP_IDS.length);
	}

	/**
	 * Refreshes the varps to portray on the music tab interface.
	 */
	void refreshListConfigs() {
		final VarManager manager = player.getVarManager();
		final boolean reset = isDefaultsReset();
		if (!reset) {
			setDefaultsReset();
		}
		for (int i = VARP_IDS.length - 1; i >= 0; i--) {
			final int varp = VARP_IDS[i];
			if (!reset) {
				unlockedTracks.put(varp, unlockedTracks.get(varp) - (unlockedTracks.get(varp) & MusicLoader.getExcludedVarpValue(i)));
			}
			final int value = unlockedTracks.get(varp) | MusicLoader.DEFAULT_VARP_VALUES[i];
			manager.sendVar(varp, value);
		}
		playRandomTrack();
	}

	public void stop() {
		setStopped(true);
		player.getPacketDispatcher().sendMusic(-1);
	}

	private boolean isDefaultsReset() {
		return player.getBooleanAttribute("reset default music tracks");
	}

	private void setDefaultsReset() {
		player.addAttribute("reset default music tracks", 1);
	}

	/**
	 * Restarts the currently playing track again(after setting music volume from 0 to > 0)
	 */
	public void restartCurrent() {
		final Music music = currentlyPlaying;
		if (music == null) {
			return;
		}
		if (stopped) stopped = false;
		nextSongAtTicks = music.getDuration();
		currentlyPlaying = music;
		ticks = 0;
	}

	/**
	 * Plays a random track.
	 */
	private void playRandomTrack() {
		Set<Music> list = null;
		if (player.getArea() != null && player.getArea() instanceof final MusicPlugin plugin) {
			list = plugin.getMusics(player);
		}

		if (list == null || list.isEmpty()) {
			list = World.getRegion(player.getLocation().getRegionId()).getMusicTracks();
			if (list == null || list.isEmpty()) {
				return;
			}
		}
		final Music randomTrack = Utils.getRandomCollectionElement(list);
		if (randomTrack == null || randomTrack == currentlyPlaying) {
			return;
		}
		final String trackName = randomTrack.getName();
		final OptionalInt slot = Enums.MUSIC_SLOT_NAME_ENUM.getKey(trackName);
		if (slot.isPresent()) {
			try {
				play(slot.getAsInt());
			} catch (Exception e) {
				logger.error("Music failed to play " + randomTrack, e);
			}
		} else {
			logger.debug("Slot not found for track name \"" + trackName + "\""+ " "+randomTrack.getMusicId());
		}
	}

	/**
	 * Unlocks all the music tracks associated with the given region id.
	 */
	public void unlock(final int regionId) {
		final Set<Music> list = World.getRegion(regionId).getMusicTracks();
		if (list == null || list.isEmpty()) {
			return;
		}
		for (final Music music : list) {
			unlock(music, false);
		}
		if (!player.getBooleanSetting(Setting.AUTO_MUSIC)) {
			return;
		}
		playRandomTrack();
	}

	public void unlock(final Music music) {
		unlock(music, true);
	}

	public void unlock(final Music music, final boolean play) {
		final OptionalInt optionalSlot = Enums.MUSIC_SLOT_NAME_ENUM.getKey(music.getName());
		if (!optionalSlot.isPresent()) return;
		final int slot = optionalSlot.getAsInt();
		final OptionalInt optionalMusicIndex = Enums.MUSIC_SLOT_INTERFACE_ENUM.getValue(slot);
		if (!optionalMusicIndex.isPresent()) {
			return;
		}
		final int musicIndex = optionalMusicIndex.getAsInt();
		final int index = (musicIndex >> 14 & 16383) - 1;
		if (index >= VARP_IDS.length) {
			return;
		}
		final int varp = VARP_IDS[index];
		final int value = player.getVarManager().getValue(varp) | (1 << (musicIndex & 16383));
		if (!isUnlocked(slot)) {
			unlockedTracks.put(varp, value - (value & MusicLoader.DEFAULT_VARP_VALUES[index]));
			player.getVarManager().sendVar(varp, value);
			final boolean unlocked = player.getEmotesHandler().isUnlocked(Emote.AIR_GUITAR);
			if (!unlocked && unlockedMusicCount() >= 500) {
				player.getEmotesHandler().unlock(Emote.AIR_GUITAR);
				player.sendMessage(Colour.RS_GREEN.wrap("Congratulations, you've unlocked the Air Guitar emote!"));
			}
		}
		if (play) {
			if (currentlyPlaying == music) {
				return;
			}
			play(slot);
		}
	}

	public int unlockedMusicCount() {
		int count = 0;
		final VarManager varManager = player.getVarManager();
		for (final int varp : VARP_IDS) {
			count += Integer.bitCount(varManager.getValue(varp));
		}
		return count;
	}

	/**
	 * Processes the music player.
	 */
	void processMusicPlayer() {
		if (stopped) {
			return;
		}
		if (forcePlayNext || ++ticks >= nextSongAtTicks) {
			forcePlayNext = false;
			resetCurrent();
			ticks = 0;
			if (!player.getBooleanSetting(Setting.LOOP_MUSIC)) {
				if (player.getBooleanSetting(Setting.AUTO_MUSIC)) {
					playRandomTrack();
				} else {
					stopped = true;
				}
			} else {
				final Music current = currentlyPlaying;
				currentlyPlaying = null;
				if (current != null) {
					final OptionalInt slot = Enums.MUSIC_SLOT_NAME_ENUM.getKey(current.getName());
					play(slot.orElseThrow(RuntimeException::new));
				}
			}
		}
	}

	public void playJingle(final int jingle) {
		this.ticks = 0;
		player.getPacketDispatcher().playJingle(jingle);
	}

	/**
	 * Sends the hint for the music track at the given slot.
	 */
	public void sendUnlockHint(final int slotId) {
		final Optional<String> musicName = Enums.MUSIC_SLOT_NAME_ENUM.getValue(slotId);
		final Music music = Music.map.get(musicName.orElseThrow(RuntimeException::new));
		if (music == null) {
			return;
		}
		player.sendMessage((!isUnlocked(slotId) ? "This track unlocks " : "This track was unlocked ") + music.getHint().replace("unlocked ", ""));
	}

	/**
	 * Resets the current music by stopping it client-sided.
	 */
	private void resetCurrent() {
		// Only send packet is music volume is not 0
		if (player.getVarManager().getValue(SettingVariables.MUSIC_VOLUME_VARP_ID) != 0)
			player.getPacketDispatcher().sendMusic(-1);
	}

	/**
	 * Whether the track at the given slot is unlocked or not.
	 */
	private boolean isUnlocked(final int slot) {
		final OptionalInt optionalRandomSong = Enums.MUSIC_SLOT_INTERFACE_ENUM.getValue(slot);
		if (!optionalRandomSong.isPresent()) {
			return false;
		}
		final int randomSong = optionalRandomSong.getAsInt();
		if (randomSong == -1) {
			return true;
		}
		final int index = (randomSong >> 14 & 16383) - 1;
		if (index >= VARP_IDS.length) {
			return false;
		}
		final int value = unlockedTracks.get(VARP_IDS[index]) | MusicLoader.DEFAULT_VARP_VALUES[index];
		final int bitIndex = randomSong & 16383;
		return (value >> bitIndex & 1) == 1;
	}

	/**
	 * Attempts to play the track at the requested slot. If it's not unlocked, or an error is thrown, returns as false and stops.
	 */
	public boolean play(final int slot) {
		if (!isUnlocked(slot)) {
			return false;
		}
		final Optional<String> musicName = Enums.MUSIC_SLOT_NAME_ENUM.getValue(slot);
		final Music music = Music.map.get(musicName.orElseThrow(RuntimeException::new));
		if (music == null) {
			return false;
		}
		if (currentlyPlaying == music) {
			resetCurrent();
		}
		nextSongAtTicks = music.getDuration();
		ticks = 0;
		currentlyPlaying = music;
		stopped = false;
		player.getPacketDispatcher().sendMusic(music.getMusicId());
		if (GameInterface.MUSIC_TAB.getPlugin().isPresent()) player.getPacketDispatcher().sendComponentText(GameInterface.MUSIC_TAB, GameInterface.MUSIC_TAB.getPlugin().get().getComponent("Song name"), music.getName());
		TreasureTrail.playSong(player, music.getName());
		return true;
	}

	public Int2IntOpenHashMap getUnlockedTracks() {
		return unlockedTracks;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public void setForcePlayNext() { forcePlayNext = true; }
}
