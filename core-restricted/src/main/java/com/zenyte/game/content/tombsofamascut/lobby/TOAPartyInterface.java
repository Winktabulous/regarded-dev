package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions.
 */
public class TOAPartyInterface extends Interface {

	private static final int CURRENT_PARTY_VAR = 3603;
	private static final int FRIENDS_ONLY_VARBIT = 14318;

	@Override protected void attach() {
		put(1, 0, "Refresh");
		put(1, 1, "Make Party");
		put(1, 2, "Filter");
		put(16, "Select party");
	}

	@Override public void open(Player player) {
		player.getTOAManager().setViewingParty(null);
		TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);

		player.getVarManager().sendVar(CURRENT_PARTY_VAR, currentLobbyParty != null ? 0 : -1);
		updateMembersTab(player);
	}

	@Override protected void build() {
		bind("Refresh", (player, slotId, itemId, option) -> {
			updateMembersTab(player);
		});
		bind("Make Party", (player, slotId, itemId, option) -> {
			TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);
			if (currentLobbyParty == null) {
				if (TOALobbyParty.isLobbyFull()) {
					player.sendMessage("The list of lobby parties is currently full. Please come back later or apply to an existing party.");
					return;
				}
				final TOALobbyParty party = new TOALobbyParty(player);
				TOALobbyParty.addLobbyParty(party);
				TOALobbyParty.setViewingParty(player, party);
				player.getTOAManager().setCurrentInterfaceTab(1);
			} else {
				TOALobbyParty.setViewingParty(player, TOALobbyParty.getCurrentParty(player));
				player.getTOAManager().setCurrentInterfaceTab(0);
			}
			GameInterface.TOA_PARTY_MANAGEMENT.open(player);
		});
		bind("Filter", (player, slotId, itemId, option) -> {
			int currentValue = player.getVarManager().getBitValue(FRIENDS_ONLY_VARBIT);
			player.getVarManager().sendBitInstant(FRIENDS_ONLY_VARBIT, currentValue == 0 ? 1 : 0);
			updateMembersTab(player);
		});
		bind("Select party", (player, slotId, itemId, option) -> {
			if (slotId <= TOALobbyParty.getLobbiesForPlayer(player).size()) {
				TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);
				if (currentLobbyParty != null && currentLobbyParty.insideRaid()) {
					player.getDialogueManager().start(new PlainChat(player, "You should join your party in the tombs."));
					return;
				}
				final TOALobbyParty party = TOALobbyParty.getLobbiesForPlayer(player).get(slotId);
				if (!TOALobbyParty.partyExists(party) || party.insideRaid()) {
					final PlainChat dialogue = new PlainChat(player, "That party is no longer recruiting.");
					dialogue.setOnCloseRunnable(() -> {
						open(player);
					});
					player.getDialogueManager().start(dialogue);
					return;
				}
				TOALobbyParty.setViewingParty(player, party);
				GameInterface.TOA_PARTY_MANAGEMENT.open(player);
			}
		});
	}

	private void updateMembersTab(final Player player) {
		super.open(player);
		final List<TOALobbyParty> parties = new ArrayList<>();
		for (int indx = 0; indx < TOALobbyParty.MAX_LOBBY_PARTIES; indx++) {
			final TOALobbyParty party = TOALobbyParty.getParty(indx);
			if (party == null) {
				player.getPacketDispatcher().sendClientScript(6601, indx, "");
			} else {
				String ownerDisplayName = party.getLeaderDisplayName();
				if (party.getPlayers().contains(player)) {
					ownerDisplayName = "<col=FFFFFF>" + ownerDisplayName;
				}
				final StringBuilder builder = new StringBuilder();
				builder.append(ownerDisplayName).append("|");
				for (int i = 1; i < TOAManager.MAX_PARTY_MEMBERS; i++) {
					if (party.getPlayers().size() > i) {
						builder.append(party.getPlayers().get(i).getName());
					}
					builder.append("|");
				}
				builder.append(party.getPlayers().size()).append("|");
				builder.append(party.getPartySettings().getKcRequirement()).append("|");
				builder.append(party.getPartySettings().getActiveInvocations()).append("|");
				builder.append(party.getPartySettings().getRaidLevel()).append("|");
				builder.append(party.getPartySettings().getMode()).append("|");
				builder.append(WorldThread.getCurrentCycle() - party.getCreationTime()).append("|");
				player.getPacketDispatcher().sendClientScript(6601, indx, builder.toString());
				parties.add(party);
			}
		}
		TOALobbyParty.setPartyList(player, parties);
	}


	@Override public GameInterface getInterface() {
		return GameInterface.TOA_PARTY_OVERVIEW;
	}
}
