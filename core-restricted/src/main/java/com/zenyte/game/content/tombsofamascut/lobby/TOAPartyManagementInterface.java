package com.zenyte.game.content.tombsofamascut.lobby;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.TOAPartySettings;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Savions.
 */
public class TOAPartyManagementInterface extends Interface {

	private static final int PRESET_SELECT_VARBIT = 14541;
	private static final int NON_MEMBER_VIEW_VALUE = 0;
	private static final int MEMBER_VIEW_VALUE = 1;
	private static final int LEADER_VIEW_VALUE = 2;
	private static final int APPLICANT_VIEW_VALUE = 3;
	private static final int KICKED_VIEW_VALUE = 4;
	private static final String REWARD_POTENTIAL_INFO = "Reward Potential|" +
			"Before entering a raid, you can customise the difficulty of the challenges you'll face by using <col=ffffff>Invocations</col>. " +
			"There are a large variety of Invocations available, covering both challenge-specific mechanics as well as raid-wide systems.<br><br>" +
			"Enabling or disabling Invocations will change the <col=ffffff>Raid Level</col>. Higher Raid Levels will result in more rewards becoming available." +
			"<br><br>If a reward is outlined in <col=ffd270>gold</col>, it is reasonably possible to obtain it at this Raid Level. " +
			"If a reward is outlined in <col=ff7070>red</col>, it is not possible to obtain it at this Raid Level. " +
			"If a reward is not outlined, it is still possible, though highly unlikely, to obtain it at this Raid Level.|" +
			"Close|";

	@Override protected void attach() {
		put(1, 0, "Open");
		put(1, 1, "Refresh");
		put(1, 2, "Unblock");
		put(1, 3, "Set completions");
		put(1, 4, "Member options");
		put(1, 5, "Clear all");
		put(1, 6, "Load preset");
		put(1, 7, "Save preset");
		for (int slotId = 8; slotId < 12; slotId++) {
			put(1, slotId, "Tab select " + slotId);
		}
		for (int slotId = 12; slotId < 20; slotId++) {
			put(1, slotId, "Member list " + slotId);
		}
		for (int slotId = 20; slotId < 36; slotId++) {
			put(1, slotId, "Applicants "  + slotId);
		}
		for (int slotId = 36; slotId < 36 + InvocationType.VALUES.length; slotId++) {
			put(1, slotId, "Invocation select " + slotId);
		}
		put(96, "Reward info");
		put(98, "Preset select");
	}

	@Override protected void build() {
		bind("Open", (player, slotId, itemId, option) -> {
			GameInterface.TOA_PARTY_OVERVIEW.open(player);
		});
		bind("Refresh", (player, slotId, itemId, option) -> {
			updatePartyManagementInterface(player);
		});
		bind("Unblock", (player, slotId, itemId, option) -> {
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty != null && viewingParty.isLeader(player)) {
				final PlainChat message = new PlainChat(player, "All players rejected from this party have been unblocked and may apply again.");
				message.setOnCloseRunnable(() -> {
					updatePartyManagementInterface(player);
				});
				player.getDialogueManager().start(message);
				viewingParty.getBlockedPlayers().clear();
			}
		});
		bind("Set completions", (player, slotId, itemId, option) -> {
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty != null && viewingParty.isLeader(player)) {
				player.sendInputInt("Set a preffered number of completions up to 100 (or 0 to clear it):", value -> {
					viewingParty.getPartySettings().setKcRequirement(Math.min(100, value));
					updatePartyManagementInterface(player);
				});
			}
		});
		bind("Member options", (player, slotId, itemId, option) -> {
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty == null) {
				return;
			}
			switch(player.getTOAManager().getViewingValue()) {
				case NON_MEMBER_VIEW_VALUE -> {
					TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);
					if (currentLobbyParty != null && currentLobbyParty.insideRaid()) {
						player.getDialogueManager().start(new PlainChat(player, "You should join your party in the tombs."));
						return;
					}
					TOALobbyParty appliedParty = TOALobbyParty.getAppliedParty(player);
					if (appliedParty != null) {
						final Player leader = appliedParty.getLeader();
						if (appliedParty.withdraw(player) && leader != null) {
							updatePartyApplicants(leader);
						}
					}
					final Runnable applyRunnable = () -> {
						if (viewingParty.apply(player)) {
							player.sendMessage("You have applied to join the party of " + viewingParty.getLeaderDisplayName() + ".");
							if (player.getTOAManager().getCurrentInterfaceTab() != 1) {
								player.getTOAManager().setCurrentInterfaceTab(1);
							}
							updatePartyManagementInterface(player);
							final Player leader = viewingParty.getLeader();
							if (leader != null) {
								updatePartyApplicants(leader);
							}
						} else {
							player.getDialogueManager().start(new PlainChat(player, "That party is no longer recruiting.").setOnCloseRunnable(() -> GameInterface.TOA_PARTY_OVERVIEW.open(player)));
						}
					};
					TOALobbyParty currentLobbyParty2 = TOALobbyParty.getCurrentParty(player);
					if (currentLobbyParty2 != null) {
						final OptionDialogue dialogue = new OptionDialogue(player, "You are already in a party", new String[] {"Stay in my existing party.", "Quit that one and apply to this one."},
								new Runnable[] {() -> updatePartyManagementInterface(player), () -> {
									currentLobbyParty2.leave(player, true);
									applyRunnable.run();
								}});
						player.getDialogueManager().start(dialogue);
					} else {
						applyRunnable.run();
					}
				}
				case MEMBER_VIEW_VALUE -> {
					TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);
					if (currentLobbyParty != null) {
						if (currentLobbyParty.insideRaid()) {
							player.getDialogueManager().start(new PlainChat(player, "You should join your party in the tombs."));
							return;
						}
						if (currentLobbyParty.leave(player, true)) {
							player.sendMessage("You have left the part of " + viewingParty.getLeaderDisplayName() + ".");
							final Player leader = viewingParty.getLeader();
							if (leader != null) {
								updatePartyManagementInterface(leader);
							}
							GameInterface.TOA_PARTY_OVERVIEW.open(player);
						}
					}
				}
				case LEADER_VIEW_VALUE -> {
					if (viewingParty.leave(player, true)) {
						player.sendMessage("Your party has disbanded.");
						viewingParty.disband();
					}
					GameInterface.TOA_PARTY_OVERVIEW.open(player);
				}
				case APPLICANT_VIEW_VALUE -> {
					if (viewingParty.withdraw(player)) {
						player.sendMessage("You have withdrawn your party application.");
						updatePartyManagementInterface(player);
						final Player leader = viewingParty.getLeader();
						if (leader != null) {
							updatePartyApplicants(leader);
						}
					}
				}
				case KICKED_VIEW_VALUE -> player.getDialogueManager().start(new PlainChat(player, "You have been declined by this party.").
						setOnCloseRunnable(() -> updatePartyManagementInterface(player)));
			}
		});
		bind("Clear all", (player, slotId, itemId, option) -> {
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty == null || !viewingParty.isLeader(player)) {
				return;
			}
			final OptionDialogue dialogue = new OptionDialogue(player, "Are you sure you want to clear all active Invocations?", new String[] {"Yes.", "No."},
					new Runnable[] {() -> {
						viewingParty.getPartySettings().clearInvocations();
						updatePartyManagementInterface(player);
						player.sendSound(2381);
					}, () -> updatePartyManagementInterface(player)});
			player.getDialogueManager().start(dialogue);
		});
		for (int slot = 8; slot < 12; slot++) {
			bind("Tab select " + slot, (player, slotId, itemId, option) -> {
				player.getTOAManager().setCurrentInterfaceTab(slotId - 8);
				updatePartyManagementInterface(player);
			});
		}
		for (int slot = 12; slot < 20; slot++) {
			bind("Member list " + slot, (player, slotId, itemId, option) -> {
				final int index = slotId - 12;
				final TOALobbyParty viewingParty = checkViewingParty(player);
				if (viewingParty == null || !viewingParty.isLeader(player) || index >= viewingParty.getPlayers().size()) {
					return;
				}
				final Player p = viewingParty.getPlayers().get(index);
				if (p.getUsername().equals(player.getUsername())) {
					if (viewingParty.leave(player, true)) {
						player.sendMessage("You have left your party");
						GameInterface.TOA_PARTY_OVERVIEW.open(player);
					}
				} else if (viewingParty.getPlayers().contains(p)) {
					viewingParty.removePlayer(p);
					updatePartyManagementInterface(player);
					if (p.getTOAManager().viewingManagementInterface(viewingParty.getLeaderDisplayName())) {
						updatePartyManagementInterface(p);
					}
					p.getTOAManager().sendEmptyPartyList();
					p.sendMessage("You have been kicked from the party of " + player.getName() + ".");
					p.sendSound(2277);
					player.sendMessage("You have kicked " + p.getName() + " from your party.");
				}
			});
		}
		for (int slot = 20; slot < 36; slot++) {
			bind("Applicants " + slot, (player, slotId, itemId, option) -> {
				boolean accept = slotId < 28;
				final TOALobbyParty viewingParty = checkViewingParty(player);
				if (viewingParty == null) {
					return;
				}
				final Player p = viewingParty.getApplicant(slotId - (accept ? 20 : 28));
				if (accept && viewingParty.getPlayers().size() >= TOAManager.MAX_PARTY_MEMBERS) {
					player.sendMessage("Your party is full.");
					return;
				}
				if (p != null && viewingParty.handleAcceptant(p, accept)) {
					if (p.getTOAManager().viewingManagementInterface(viewingParty.getLeaderDisplayName())) {
						updatePartyManagementInterface(p);
					}
					if (accept) {
						player.sendMessage("You have accepted " + p.getName() + " into your party.");
						p.sendMessage("Your application to the party of " + viewingParty.getLeaderDisplayName() + " has been accepted.");
						updatePartyManagementInterface(player);
						p.sendSound(2655);
					} else {
						player.sendMessage("You have declined the party application from " + p.getName() + ".");
						p.sendMessage("Your application to the party of " + viewingParty.getLeaderDisplayName() + " has been declined.");
						updatePartyApplicants(player);
						p.sendSound(2277);
					}
				}
				updatePartyManagementInterface(player);
			});
		}
		for (int slot = 36; slot < 36 + InvocationType.VALUES.length; slot++) {
			bind("Invocation select " + slot, (player, slotId, itemId, option) -> {
				final TOALobbyParty viewingParty = checkViewingParty(player);
				if (viewingParty != null && viewingParty.isLeader(player)) {
					player.getTOAManager().toggleInvocation(InvocationType.VALUES[slotId - 36], player);
					updatePartyManagementInterface(player);
				}
			});
		}
		bind("Reward info", (player, slotId, itemId, option) -> {
			player.getInterfaceHandler().sendInterface(InterfacePosition.TOA_MANAGEMENT, 289, true);
			player.getPacketDispatcher().sendClientScript(4212, REWARD_POTENTIAL_INFO, 50724925);
		});
		bind("Preset select", (player, slotId, itemId, option) -> {
			if (slotId < 0 || slotId > 4) {
				return;
			}
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty != null && viewingParty.isLeader(player)) {
				if (option == 1) {
					final int currentPresetSlot = player.getVarManager().getBitValue(PRESET_SELECT_VARBIT);
					player.getVarManager().sendBitInstant(PRESET_SELECT_VARBIT, currentPresetSlot == slotId + 1 ? 0 : slotId + 1);
					updatePartyManagementInterface(player);
				} else {
					final OptionDialogue dialogue = new OptionDialogue(player, "Are you sure you wish to clear this preset?", new String[] {"Clear this preset.", "Cancel"},
							new Runnable[] {() -> {
								player.getTOAManager().clearInvocationPreset(slotId);
								updatePartyManagementInterface(player);
								player.sendMessage("Your preset has been cleared.");
								player.sendSound(2381);
								player.getTOAManager().clearInvocationPreset(slotId);
							}, () -> updatePartyManagementInterface(player)});
					player.getDialogueManager().start(dialogue);
				}
			}
		});
		bind("Save preset", (player, slotId, itemId, option) -> {
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty != null && viewingParty.isLeader(player)) {
				final int currentPresetSlot = player.getVarManager().getBitValue(PRESET_SELECT_VARBIT) - 1;
				if (currentPresetSlot == -1) {
					player.sendMessage("You do not have a valid preset selected to save to.");
					updatePartyManagementInterface(player);
				} else {
					final Runnable runnable = () -> {
						player.getVarManager().sendBitInstant(PRESET_SELECT_VARBIT, 0);
						player.getTOAManager().saveInvocationPreset(currentPresetSlot);
						updatePartyManagementInterface(player);
						player.sendMessage("Your preset has been saved.");
						player.sendSound(2655);
					};
					if (!player.getTOAManager().isPresetEmpty(currentPresetSlot)) {
						final OptionDialogue dialogue = new OptionDialogue(player, "You already have a preset saved in this slot.", new String[] {"Save and overwrite this preset.", "Cancel"},
								new Runnable[] { () -> {
									runnable.run();
									updatePartyManagementInterface(player);
								}, () -> updatePartyManagementInterface(player)});
						player.getDialogueManager().start(dialogue);
					} else {
						runnable.run();
					}
				}
			}
		});
		bind("Load preset", (player, slotId, itemId, option) -> {
			final TOALobbyParty viewingParty = checkViewingParty(player);
			if (viewingParty != null && viewingParty.isLeader(player)) {
				final int currentPresetSlot = player.getVarManager().getBitValue(PRESET_SELECT_VARBIT) - 1;
				player.getVarManager().sendBitInstant(PRESET_SELECT_VARBIT, 0);
				if (currentPresetSlot == -1) {
					player.sendMessage("You do not have a valid preset selected to load from.");
					updatePartyManagementInterface(player);
				} else if (player.getTOAManager().isPresetEmpty(currentPresetSlot)) {
					player.sendMessage("You do not have any invocations stored in this preset.");
					updatePartyManagementInterface(player);
				} else {
					viewingParty.getPartySettings().loadInvocationPreset(player.getTOAManager().getInvocationPreset(currentPresetSlot));
					player.sendMessage("Your preset has been loaded.");
					player.sendSound(2655);
					updatePartyManagementInterface(player);
				}
			}
		});
	}

	@Override public void open(Player player) {
		player.getInterfaceHandler().closeInterfaceSpecific(getInterface().getPosition().getComponent(PaneType.TOA_MANAGEMENT), PaneType.TOA_MANAGEMENT);
		updatePartyManagementInterface(player);
	}

	public static void updatePartyManagementInterface(final Player player) {
		player.getInterfaceHandler().sendInterface(GameInterface.TOA_PARTY_MANAGEMENT);
		final TOALobbyParty viewingParty = checkViewingParty(player);
		if (viewingParty == null) {
			return;
		}
		setViewingValue(player, viewingParty);
		for (int indx = 0; indx < TOAManager.MAX_PARTY_MEMBERS; indx++) {
			if (indx >= viewingParty.getPlayers().size()) {
				player.getPacketDispatcher().sendClientScript(6722, 2, "");
			} else {
				final Player p = viewingParty.getPlayers().get(indx);
				player.getPacketDispatcher().sendClientScript(6722, 2, getPlayerStatString(p, player.getUsername().equalsIgnoreCase(p.getUsername())));
			}
		}
		updatePartyApplicants(player);
		final TOAPartySettings partySettings = viewingParty.getPartySettings();
		final int[] bitMaps = viewingParty.getPartySettings().getInvocationBitmaps();
		player.getPacketDispatcher().sendClientScript(6729, player.getTOAManager().getViewingValue(), partySettings.getKcRequirement(),
				partySettings.getActiveInvocations(), partySettings.getRaidLevel(), player.getTOAManager().getCurrentInterfaceTab(), bitMaps[0], bitMaps[1], bitMaps[2]);
		if (viewingParty.isLeader(player)) {
			player.getPacketDispatcher().sendComponentSettings(GameInterface.TOA_PARTY_MANAGEMENT.getId(), 98, 0, 5, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2);
			player.getPacketDispatcher().sendComponentSettings(GameInterface.TOA_PARTY_MANAGEMENT.getId(), 1, 0, 79, AccessMask.CONTINUE);
		}
	}

	public static void updatePartyApplicants(final Player player) {
		TOALobbyParty party = TOALobbyParty.getViewingParty(player);
		if(party != null) {
			for (Player applicant : TOALobbyParty.getViewingParty(player).getApplicants()) {
				player.getPacketDispatcher().sendClientScript(6727, getPlayerStatString(applicant, player.getUsername().equals(applicant.getUsername())));
			}
		}
	}

	private static TOALobbyParty checkViewingParty(final Player player) {
		final TOALobbyParty viewingParty = TOALobbyParty.getViewingParty(player);
		if (viewingParty == null || viewingParty.getLeader() == null) {
			GameInterface.TOA_PARTY_OVERVIEW.open(player);
			return null;
		}
		return viewingParty;
	}

	private static void setViewingValue(final Player player, final TOALobbyParty viewingParty) {
		int viewingValue = NON_MEMBER_VIEW_VALUE;
		if (viewingParty.isLeader(player)) {
			viewingValue = LEADER_VIEW_VALUE;
		} else if (viewingParty.getPlayers().contains(player)) {
			viewingValue = MEMBER_VIEW_VALUE;
		} else if (viewingParty.getApplicants().contains(player)) {
			viewingValue = APPLICANT_VIEW_VALUE;
		} else if (viewingParty.getBlockedPlayers().contains(player)) {
			viewingValue = KICKED_VIEW_VALUE;
		}
		player.getTOAManager().setViewingValue(viewingValue);
	}


	private static String getPlayerStatString(final Player player, boolean isViewingPlayer) {
		final StringBuilder builder = new StringBuilder();
		if (isViewingPlayer) {
			builder.append("<col=FFFFFF>");
		}
		builder.append(player.getName()).append("|");
		builder.append(player.getCombatLevel()).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.ATTACK)).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.STRENGTH)).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.RANGED)).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.MAGIC)).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.DEFENCE)).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.HITPOINTS)).append("|");
		builder.append(player.getSkills().getLevelForXp(SkillConstants.PRAYER)).append("|");
		builder.append(player.getNotificationSettings().getKillcount("tombs of amascut: entry mode")).append(" / ");
		builder.append(player.getNotificationSettings().getKillcount("tombs of amascut: normal mode")).append(" / ");
		builder.append(player.getNotificationSettings().getKillcount("tombs of amascut: expert mode")).append("|");
		return builder.toString();
	}

	@Override public GameInterface getInterface() {
		return GameInterface.TOA_PARTY_MANAGEMENT;
	}
}
