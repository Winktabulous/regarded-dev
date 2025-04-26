package com.zenyte.plugins.object;

import com.zenyte.game.content.wildernessVault.WildernessVaultConstants;
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler;
import com.zenyte.game.content.wildernessVault.WildernessVaultStatus;
import com.zenyte.game.content.wildernessVault.reward.WildernessVaultRewardHandler;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

import static com.zenyte.game.content.wildernessVault.WildernessVaultConstants.*;

public class WildernessVaultObjects implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		WildernessVaultHandler instance = WildernessVaultHandler.getInstance();

		switch (object.getId()) {
			case FIRE_OBJECT: {
				instance.getQueenReaver().fires.removeIf(next -> next.getLocation().matches(object.getLocation()));
				World.removeObject(World.getObjectWithType(object.getLocation(), 10));
				break;
			}

			case WILDERNESS_VAULT_LADDER_UP: {
				if (!instance.getAllPlayers().contains(player)) {
					break;
				}

				WildernessVaultRewardHandler n = (WildernessVaultRewardHandler) player.getTemporaryAttributes().get("WILDY_CHEST_LOOT");
				if (n != null && !n.getContainer().isEmpty()) {
					player.getDialogueManager().start(new Dialogue(player) {
						@Override
						public void buildDialogue() {
							plain("You're about to leave when you still have loot in your chest! If you leave your loot will be " + Colour.RED.wrap("lost!!!"));
							options(new DialogueOption("Yes, leave.", () -> {
								leaveChestRoom(player);
							}), new DialogueOption("Stay"));
						}
					});
					break;
				}

				leaveChestRoom(player);
				break;
			}

			case WILDERNESS_VAULT_LADDER: {
				if (!instance.getAllPlayers().contains(player)) {
					break;
				}

				if (instance.getBossStatus() == WildernessVaultHandler.BossStatus.KILLED) {
					try {
						if (!WildernessVaultHandler.canLoot.contains(player.getName())) {
							player.sendMessage("You didn't do enough damage to receive the loot.");
							return;
						}

						if (WildernessVaultHandler.getLooted().contains(player.getName())) {
							player.sendMessage("You have already looted the chest.");
							return;
						}

						WildernessVaultRewardHandler n = (WildernessVaultRewardHandler) player.getTemporaryAttributes().getOrDefault("WILDY_CHEST_LOOT", new WildernessVaultRewardHandler(player));
						if (n.isLooted()) {
							player.sendMessage("You have already looted the chest.");
							return;
						}

						n.onEnterRoom();
						player.getTemporaryAttributes().put("WILDY_CHEST_LOOT", n);
					} catch (OutOfSpaceException e) {
						e.printStackTrace();
						player.sendMessage("Please try again in a moment.");
					}
				} else {
					player.sendMessage("The crypt has not been opened yet.");
				}
				break;
			}

			case LEAVE_BARRIER: {
				String str = instance.getVaultStatus() == WildernessVaultStatus.STARTED ? "The vault is sealed so if I leave I won't be able to return. It's also in the wilderness." :
						"Are you sure you want to leave? You may not be able to return if the vault is sealed. It's also in the wilderness.";
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain(str);
						options("Leave vault?", "Yes", "No").onOptionOne(() -> {
							player.setLocation(instance.getCurrentSpawn().locationPlayer());
							player.getVariables().schedule(250, TickVariable.TELEBLOCK);
							player.getVariables().schedule(350, TickVariable.TELEBLOCK_IMMUNITY);
						});
					}
				});
				break;
			}

			case CHEST:
			case CHEST_OPEN:
			case RARE_CHEST:
			case RARE_CHEST_OPEN: {
				WildernessVaultRewardHandler loot = (WildernessVaultRewardHandler) player.getTemporaryAttributes().getOrDefault("WILDY_CHEST_LOOT", null);
				if (loot == null) {
					return;
				}
				loot.lootChest();
				player.getVariables().setSkull(true);
				break;
			}

			case WILDERNESS_VAULT_ENTRANCE:
			case WILDERNESS_VAULT_ENTRANCE_SEALED: {
				if (option.equalsIgnoreCase("peek")) {
					final int players = instance.getAllPlayers().size();
					StringBuilder sb = new StringBuilder();
					if (players == 0) {
						sb.append("The vault is empty. ");
					} else if (players == 1) {
						sb.append("There is 1 player in the vault. ");
					} else {
						sb.append("There are ");
						sb.append(players);
						sb.append(" players in the vault. ");
					}

					if (instance.getVaultStatus() == WildernessVaultStatus.STARTED) {
						if (players > 0) {
							sb.append("Players in vault will be expelled in ");
						} else {
							sb.append("The vault will decay in ");
						}
						sb.append(instance.timeTillNextEvent(true));
					} else {
						sb.append("The event hasn't started yet");
					}
					sb.append(".");

					player.sendMessage(sb.toString());
				} else {
					if (instance.getVaultStatus() == WildernessVaultStatus.STARTED) {
						player.sendMessage("The vault is locked.");
						return;
					}

					if (player.getCombatLevel() < 50) {
						player.getDialogueManager().start(new Dialogue(player) {
							@Override
							public void buildDialogue() {
								plain("You need a combat level of 50 or greater to enter the vault.");
							}
						});
						return;
					}

					if (player.getVariables().getTime(TickVariable.TELEBLOCK) > 0) {
						player.getDialogueManager().start(new Dialogue(player) {
							@Override
							public void buildDialogue() {
								plain("You can't enter while teleblocked.");
							}
						});
						return;
					}

					if (instance.getFirstEntranceTime() == 0) {
						instance.setFirstEntranceTime();
					}

					player.setLocation(WildernessVaultConstants.POSITION_ENTRANCE);
				}
				break;
			}
		}
	}

	private static void leaveChestRoom(Player player) {
		player.setLocation(CHEST_LADDER);
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{WILDERNESS_VAULT_ENTRANCE, WILDERNESS_VAULT_LADDER, WILDERNESS_VAULT_LADDER_UP, LEAVE_BARRIER, FIRE_OBJECT,
				WILDERNESS_VAULT_ENTRANCE_SEALED, CHEST, CHEST_OPEN, RARE_CHEST, RARE_CHEST_OPEN};
	}
}
