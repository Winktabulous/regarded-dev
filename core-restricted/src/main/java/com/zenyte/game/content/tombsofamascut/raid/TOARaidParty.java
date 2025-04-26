package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.content.tombsofamascut.AbstractTOAClazz;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.TOAPartySettings;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.*;

import static com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty.RAID_PARTIES;

/**
 * @author Savions.
 */
public class TOARaidParty extends AbstractTOAClazz {

	private final List<String> originalPlayers = new ArrayList<String>();
	private final List<Player> players = new ArrayList<Player>();
	private final TOALobbyParty lobbyParty;
	protected final TOAPartySettings partySettings;
	private TOARaidArea currentRaidArea;
	private EncounterType currentEncounterType;
	private final Object[] hudPlayerList = new Object[TOAManager.MAX_PARTY_MEMBERS];
	private final int[] bossLevels = new int[TOAPathType.VALUES.length];
	private final List<TOAPathType> pathsCompleted = new ObjectArrayList<>();
	private long startTime;
	private long totalTime = -1;
	private long totalChallengeTime = -1;
	private int completedRaidLevel = -1;
	private long timeLimitMinutes = -1;
	private TOAPathType pathType;
	private final List<ChallengeResult> challengeResults = new ArrayList<>(9);
	private int permittedTeamDeaths = -1;
	private int teamDeaths = 0;
	private int totalDeaths = 0;
	private boolean failedTimeChallenge;
	private Player leader;


	public TOARaidParty(final TOALobbyParty lobbyParty, final Player leader) {
		this.leader = leader;
		this.lobbyParty = lobbyParty;
		this.partySettings = new TOAPartySettings();
		for(Player p: lobbyParty.getPlayers()) {
			add(p);
		}
		updateOriginalPlayers();
		generateHudPlayerList();
		if (partySettings.isActive(InvocationType.WALK_FOR_IT)) {
			timeLimitMinutes = 40;
		} else if (partySettings.isActive(InvocationType.JOG_FOR_IT)) {
			timeLimitMinutes = 35;
		} else if (partySettings.isActive(InvocationType.RUN_FOR_IT)) {
			timeLimitMinutes = 30;
		} else if (partySettings.isActive(InvocationType.SPRINT_FOR_IT)) {
			timeLimitMinutes = 25;
		}
		if (partySettings.isActive(InvocationType.TRY_AGAIN)) {
			permittedTeamDeaths = 10;
		} else if (partySettings.isActive(InvocationType.PERSISTENCE)) {
			permittedTeamDeaths = 5;
		} else if (partySettings.isActive(InvocationType.SOFTCORE_RUN)) {
			permittedTeamDeaths = 3;
		} else if (partySettings.isActive(InvocationType.HARDCORE_RUN)) {
			permittedTeamDeaths = 1;
		}
	}

	public TOARaidParty(final TOALobbyParty lobbyParty, final TOAPartySettings partySettings, final Player leader) {
		this.leader = leader;
		this.lobbyParty = lobbyParty;
		this.partySettings = new TOAPartySettings();
		this.partySettings.copyPartySettings(partySettings);
		for(Player p: lobbyParty.getPlayers()) {
			add(p);
		}
		updateOriginalPlayers();
		generateHudPlayerList();
		if (partySettings.isActive(InvocationType.WALK_FOR_IT)) {
			timeLimitMinutes = 40;
		} else if (partySettings.isActive(InvocationType.JOG_FOR_IT)) {
			timeLimitMinutes = 35;
		} else if (partySettings.isActive(InvocationType.RUN_FOR_IT)) {
			timeLimitMinutes = 30;
		} else if (partySettings.isActive(InvocationType.SPRINT_FOR_IT)) {
			timeLimitMinutes = 25;
		}
		if (partySettings.isActive(InvocationType.TRY_AGAIN)) {
			permittedTeamDeaths = 10;
		} else if (partySettings.isActive(InvocationType.PERSISTENCE)) {
			permittedTeamDeaths = 5;
		} else if (partySettings.isActive(InvocationType.SOFTCORE_RUN)) {
			permittedTeamDeaths = 3;
		} else if (partySettings.isActive(InvocationType.HARDCORE_RUN)) {
			permittedTeamDeaths = 1;
		}
	}

	public final void constructEncounter(EncounterType encounterType) {
		try {
			final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(8, 8);
			final TOARaidArea area = encounterType.getChildClass().getConstructor(AllocatedArea.class, int.class, int.class, TOARaidParty.class, EncounterType.class).
					newInstance(allocatedArea, encounterType.getChunkX(), encounterType.getChunkY(), this, encounterType);
			area.constructRegion();
			currentRaidArea = area;
			currentEncounterType = encounterType;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void generateHudPlayerList() {
		Arrays.fill(hudPlayerList, "");
		for (int i = 0; i < players.size(); i++) {
			hudPlayerList[i] = players.get(i).getName();
		}
	}

	public void add(Player player) {
		if (!players.contains(player)) {
			player.getTOAManager().setRaidParty(this);
			players.add(player);
			generateHudPlayerList();
			players.forEach(p -> {
				p.getTOAManager().refreshHudPlayers();
				p.getTOAManager().refreshHudStates();
			});
		}
	}

	public void leave(Player player, boolean logout) {
		if (players.contains(player)) {
			player.getTOAManager().setRaidParty(null);
			if (!logout && !player.isFinished()) {
				player.getTOAManager().resetSessionAttributes();
			}
			players.remove(player);
			if(isLeader(player) && players.size() != 0) {
				leader = players.stream().findFirst().orElseThrow();
				leader.sendMessage("You have been promoted to the raid party leader.");
			}
			lobbyParty.leave(player, false);
			generateHudPlayerList();
			players.forEach(p -> {
				p.getTOAManager().refreshHudPlayers();
				p.getTOAManager().refreshHudStates();
			});
			if(this.getPlayers().size() == 0) {
				RAID_PARTIES.remove(this);
			}
		}
		if (!logout && !player.isFinished()) {
			originalPlayers.remove(player.getUsername());
		}
		if (currentRaidArea != null) {
			currentRaidArea.checkRoomReset();
		}
	}

	public void setCompletion() {
		totalTime = WorldThread.getCurrentCycle() - startTime;
		final int raidLevel = completedRaidLevel = partySettings.getRaidLevel();
		if (timeLimitMinutes != -1) {
			if (TimeUnit.TICKS.toMillis(totalTime) > TimeUnit.MINUTES.toMillis(timeLimitMinutes)) {
				if (partySettings.isActive(InvocationType.WALK_FOR_IT)) {
					completedRaidLevel = raidLevel - 10;
				} else if (partySettings.isActive(InvocationType.JOG_FOR_IT)) {
					completedRaidLevel = raidLevel - 15;
				} else if (partySettings.isActive(InvocationType.RUN_FOR_IT)) {
					completedRaidLevel = raidLevel - 20;
				} else if (partySettings.isActive(InvocationType.SPRINT_FOR_IT)) {
					completedRaidLevel = raidLevel - 25;
				}
				getPartySettings().setRaidLevel(completedRaidLevel);
				failedTimeChallenge = raidLevel != completedRaidLevel;
			}
		}
		computeRewards();
	}

	private void computeRewards() {

		//TODO figure out what rewards we want
		//14373 varbit purple chest
		//TODO set rewards
	}

	public long computeTotalChallengeTime() {
		totalChallengeTime = 0;
		challengeResults.forEach(result -> totalChallengeTime += result.getTime());
		return totalChallengeTime;
	}

	public float getDamageMultiplier() {
		return Math.min(1F + partySettings.getRaidLevel() * .004F, 2.5F);
	}

	public void increaseBossLevel(int index, int levels) {
		bossLevels[index] += levels;
	}

	public final boolean isLeader(final Player player) {
		return leader.getUsername().equals(player.getUsername());
	}

	public final void updateOriginalPlayers() {
		originalPlayers.clear();
		players.forEach(p -> originalPlayers.add(p.getUsername()));
	}

	public final Player getLeader() {
		return leader;
	}

	public List<String> getOriginalPlayers() { return originalPlayers; }

	public TOAPartySettings getPartySettings() { return partySettings; }

	public TOARaidArea getCurrentRaidArea() { return currentRaidArea; }

	public EncounterType getCurrentEncounterType() { return currentEncounterType; }

	public List<Player> getPlayers() { return players; }

	public TOALobbyParty getLobbyParty() { return lobbyParty; }

	public final Object[] getHudPlayerList() { return hudPlayerList; }

	public int[] getBossLevels() { return bossLevels; }
	public List<TOAPathType> getPathsCompleted() { return pathsCompleted; }

	public long getStartTime() { return startTime; }

	public void setStartTime(long startTime) { this.startTime = startTime; }

	public long getTimeLimitMinutes() { return timeLimitMinutes; }

	public void setTimeLimitMinutes(long timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

	public TOAPathType getPathType() { return pathType; }

	public void setPathType(TOAPathType pathType) { this.pathType = pathType; }

	public List<ChallengeResult> getChallengeResults() { return challengeResults; }

	public int getPermittedTeamDeaths() { return permittedTeamDeaths; }

	public int getTeamDeaths() { return teamDeaths; }

	public void increaseTeamDeaths() { teamDeaths++; }
	public int getTotalDeaths() { return totalDeaths; }

	public void increaseTotalDeaths() { totalDeaths++; }

	public long getTotalTime() { return totalTime; }

	public int getCompletedRaidLevel() { return completedRaidLevel; }

	public long getTotalChallengeTime() { return totalChallengeTime; }

	public boolean isFailedTimeChallenge() { return failedTimeChallenge; }
}