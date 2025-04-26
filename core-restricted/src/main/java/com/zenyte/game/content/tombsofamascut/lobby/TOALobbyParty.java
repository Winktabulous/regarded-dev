package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.TOAPartySettings;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.PlainChat;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

/**
 * @author Savions.
 */
public class TOALobbyParty {

	public static final int PARTY_STATUS_VAR = 14345;
	static final int MAX_LOBBY_PARTIES = 45;
	private static final List<TOALobbyParty> LOBBY_PARTIES = new ArrayList<TOALobbyParty>();
	private static final HashMap<Player, List<TOALobbyParty>> PLAYER_PARTIES = new LinkedHashMap<>();
	public static final LinkedList<TOARaidParty> RAID_PARTIES = new LinkedList<>();
	private final List<Player> players = new ArrayList<Player>();
	private final List<Player> applicants = new ArrayList<Player>();
	private final List<Player> blockedPlayers = new ArrayList<Player>();
	private TOAPartySettings partySettings;
	private String leaderDisplayName;
	private String partyString;
	private final long creationTime = WorldThread.getCurrentCycle();
	private TOARaidParty raidParty;

	public TOALobbyParty(final Player owner) {
		addPlayer(owner);
		leaderDisplayName = owner.getName();
		partySettings = (TOAPartySettings) owner.getTOAManager().getPartySettings();
	}

	public static void setPartyList(Player p, List<TOALobbyParty> parties) {
		PLAYER_PARTIES.put(p, parties);
	}

	public static void setViewingParty(Player player, TOALobbyParty party) {
		player.getTOAManager().setViewingParty(party);
	}

	public static void setCurrentParty(Player player, TOALobbyParty party) {
		player.getTOAManager().setCurrentParty(party);
	}

	public static TOALobbyParty getCurrentParty(Player player) {
		return (TOALobbyParty) player.getTOAManager().getCurrentParty();
	}

	public static void setAppliedParty(Player player, TOALobbyParty party) {
		player.getTOAManager().setAppliedParty(party);
	}

	public static TOALobbyParty getAppliedParty(Player player) {
		return (TOALobbyParty) player.getTOAManager().getAppliedParty();
	}


	private void addPlayer(final Player player) {
		players.add(player);
		setCurrentParty(player, this);
		buildPartyString();
		players.forEach(p -> p.getPacketDispatcher().sendComponentText(GameInterface.TOA_PARTY.getId(), 5, partyString));
		player.getVarManager().sendBit(PARTY_STATUS_VAR, 1);
	}

	void removePlayer(final Player player) {
		players.remove(player);
		setCurrentParty(player, null);
		buildPartyString();
		players.forEach(p -> p.getPacketDispatcher().sendComponentText(GameInterface.TOA_PARTY.getId(), 5, partyString));
	}

	void disband() {
		players.removeIf(p -> {
			p.getTOAManager().sendEmptyPartyList();
			setCurrentParty(p, null);
			if (p.getTOAManager().viewingManagementInterface(leaderDisplayName)) {
				p.getDialogueManager().start(new PlainChat(p, "Your party has disbanded.").setOnCloseRunnable(() -> GameInterface.TOA_PARTY_OVERVIEW.open(p)));
			} else {
				p.sendMessage("Your party has disbanded.");
			}
			return true;
		});
		applicants.forEach(p -> {
			setAppliedParty(p, null);
			if (p.getTOAManager().viewingManagementInterface(leaderDisplayName)) {
				p.getDialogueManager().start(new PlainChat(p, "That party is no longer recruiting.").setOnCloseRunnable(() -> GameInterface.TOA_PARTY_OVERVIEW.open(p)));
			} else {
				p.sendMessage("The party to which you were applying has disbanded.");
			}
		});
		blockedPlayers.forEach(p -> {
			setAppliedParty(p, null);
			if (p.getTOAManager().viewingManagementInterface(leaderDisplayName)) {
				p.getDialogueManager().start(new PlainChat(p, "That party has disbanded.").setOnCloseRunnable(() -> GameInterface.TOA_PARTY_OVERVIEW.open(p)));
			}
		});
		removeFromList();
	}

	boolean apply(final Player player) {
		if (!insideRaid() && !applicants.contains(player) && applicants.size() <= TOAManager.MAX_PARTY_MEMBERS) {
			applicants.add(player);
			setAppliedParty(player, this);
			return true;
		}
		return false;
	}

	public boolean withdraw(final Player player) {
		if (applicants.contains(player)) {
			applicants.remove(player);
			setAppliedParty(player, null);
			return true;
		}
		return false;
	}

	boolean handleAcceptant(final Player player, boolean accept) {
		if (!players.contains(player) && players.size() <TOAManager. MAX_PARTY_MEMBERS && applicants.contains(player) && TOALobbyParty.this.equals(getAppliedParty(player))) {
			applicants.remove(player);
			setAppliedParty(player, null);
			if (accept) {
				addPlayer(player);
			} else if (!blockedPlayers.contains(player)) {
				blockedPlayers.add(player);
			}
			return true;
		}
		return false;
	}

	public boolean leave(final Player player, boolean updatePartyList) {
		if (players.contains(player)) {
			removePlayer(player);
			if (player.getUsername().equals(leaderDisplayName)) {
				if (players.size() > 0) {
					final Player newLeader = getLeader();
					leaderDisplayName = newLeader.getName();
					if (!insideRaid()) {
						((TOAPartySettings) newLeader.getTOAManager().getPartySettings()).copyPartySettings(partySettings);
						partySettings = (TOAPartySettings) newLeader.getTOAManager().getPartySettings();
					}
				}
			}
			if (players.size() < 1) {
				removeFromList();
			}
			if (updatePartyList) {
				player.getTOAManager().sendEmptyPartyList();
			}
			if (raidParty != null) {
				raidParty.leave(player, false);
			}
			return true;
		}
		return false;
	}

	private void buildPartyString() {
		final String[] names = new String[TOAManager.MAX_PARTY_MEMBERS];
		Arrays.fill(names, "-");
		for (int i = 0; i < Math.min(players.size(), TOAManager.MAX_PARTY_MEMBERS); i++) {
			names[i] = players.get(i).getName();
		}
		partyString = String.join("<br>", names);
	}

	public void removeFromList() {
		LOBBY_PARTIES.remove(this);
	}

	static TOALobbyParty getParty(int index) { return index >= LOBBY_PARTIES.size() ? null : LOBBY_PARTIES.get(index); }
	static TOALobbyParty getViewingParty(Player player) {
		return (TOALobbyParty) player.getTOAManager().getViewingParty();
	}

	static boolean partyExists(TOALobbyParty party) { return LOBBY_PARTIES.contains(party); }

	public static void addLobbyParty(final TOALobbyParty party) { LOBBY_PARTIES.add(party); }

	static boolean isLobbyFull() { return LOBBY_PARTIES.size() > MAX_LOBBY_PARTIES; }

	static List<TOALobbyParty> getLobbiesForPlayer(Player player) {
		return PLAYER_PARTIES.getOrDefault(player, new ArrayList<>());
	}

	public final Player getLeader() { return players.size() > 0 ? players.get(0) : null; }

	public final boolean isLeader(final Player player) {
		return players.indexOf(player) == 0;
	}

	public final boolean insideRaid() { return raidParty != null; }

	public final List<Player> getPlayers() { return players; }

	final List<Player> getApplicants() { return applicants; }

	final Player getApplicant(int index) { return index < applicants.size() ? applicants.get(index) : null; }

	final List<Player> getBlockedPlayers() { return blockedPlayers; }

	final TOAPartySettings getPartySettings() {
		if(partySettings == null)
			partySettings = new TOAPartySettings();
		return partySettings;
	}

	public final String getLeaderDisplayName() { return leaderDisplayName; }

	final long getCreationTime() { return creationTime; }

	public TOARaidParty getRaidParty() { return raidParty; }

	public void setRaidParty(TOARaidParty raidParty) { this.raidParty = raidParty; }
}