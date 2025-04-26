package com.zenyte.game.content.minigame.pestcontrol.area;

import com.zenyte.game.content.minigame.pestcontrol.PestControlGameType;
import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestControlStatistic;
import com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.area.VoidKnightsOutpost;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities.TIME_UNTIL_GAME_START;

/**
 * @author Kris | 26. juuni 2018 : 20:25:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public abstract class AbstractLanderArea extends VoidKnightsOutpost implements CycleProcessPlugin {
	private static final Logger log = LoggerFactory.getLogger(AbstractLanderArea.class);
	private final Object2IntOpenHashMap<Player> prioritizedPlayers = new Object2IntOpenHashMap<>();
	protected int ticks = TIME_UNTIL_GAME_START;

	@Override
	public void enter(final Player player) {
		player.getTeleportManager().unlock(PortalTeleport.PEST_CONTROL);
		prioritizedPlayers.put(player, 1);
		PestControlUtilities.sendLanderInterface(player);
		for (final Player p : players) {
			PestControlUtilities.updateLanderInformation(p, this);
		}
	}

	@Override
	public void leave(final Player player, boolean logout) {
		prioritizedPlayers.removeInt(player);
		player.getInterfaceHandler().closeInterface(InterfacePosition.MINIGAME_OVERLAY);
		for (final Player p : players) {
			PestControlUtilities.updateLanderInformation(p, this);
		}
	}

	@Override
	public void process() {
		ticks--;
		if (ticks == 0) {
			ticks = TIME_UNTIL_GAME_START;
			if (players.isEmpty()) {
				return;
			}
			if (players.size() < PestControlUtilities.MINIMUM_PLAYERS_LIMIT) {
				return;
			}
			final ArrayList<Object2IntMap.Entry<Player>> list = new ArrayList<Object2IntMap.Entry<Player>>(prioritizedPlayers.object2IntEntrySet());
			list.sort(Entry.comparingByValue());
			final HashMap<Player, PestControlStatistic> result = new HashMap<Player, PestControlStatistic>(Math.min(list.size(), PestControlUtilities.MAXIMUM_PLAYERS_LIMIT));
			final ArrayList<Object2IntMap.Entry<Player>> removed = new ArrayList<Object2IntMap.Entry<Player>>();
			for (final Object2IntMap.Entry<Player> entry : list) {
				final Player player = entry.getKey();
				removed.add(entry);
				result.put(player, new PestControlStatistic(PestControlUtilities.HALF_FULL_ACTIVITY_PERCENTAGE_VALUE));
				if (result.size() >= PestControlUtilities.MAXIMUM_PLAYERS_LIMIT) {
					break;
				}
			}
			for (final Object2IntMap.Entry<Player> p : removed) {
				prioritizedPlayers.removeInt(p);
			}
			dispatch(result);
			for (final Object2IntMap.Entry<Player> entry : prioritizedPlayers.object2IntEntrySet()) {
				final int value = entry.getIntValue();
				entry.getKey().sendMessage(String.format("You have been given priority level %d over other players in joining the next game.", value));
				entry.setValue(value + 1);
			}
		}
		if (players.isEmpty()) {
			return;
		}
		if (ticks % 50 == 0) {
			for (final Player player : players) {
				PestControlUtilities.updateLanderInformation(player, this);
			}
		}
	}

	private void dispatch(final Map<Player, PestControlStatistic> players) {
		try {
			final AllocatedArea area = MapBuilder.findEmptyChunk(PestControlUtilities.INSTANCE_WIDTH, PestControlUtilities.INSTANCE_HEIGHT);
			for (final Map.Entry<Player, PestControlStatistic> entry : players.entrySet()) {
				final Player player = entry.getKey();
				if (player.isNulled()) {
					continue;
				}
				player.lock();
			}
			new PestControlInstance(getType(), players, area).constructRegion();
		} catch (OutOfSpaceException e) {
			log.error("", e);
		}
	}

	public abstract PestControlGameType getType();

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
}
