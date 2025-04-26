package com.zenyte.game.content.tombsofamascut.encounter;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.TOAManager;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOAPathType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.InteractableEntity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.dialogue.OptionDialogue;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Savions.
 */
public class MainHallEncounter extends TOARaidArea {

	public static final int HELPFUL_SPIRIT_ID = 11694;
	private static final Location WARDEN_ENTRANCE_LOCATION = new Location(3548, 5134);
	private static final Location SUPPLY_NPC_LOCATION = new Location(3548, 5154);
	private final int[] bossLevelIncreases = new int[TOAPathType.VALUES.length];
	private final List<String> eligibleSupplyPlayerUsernames = new ArrayList<>();
	private TOAPathType startedPath;
	private TOASupplyNpc supplyNpc;
	private Container[] supplyContainers;

	public MainHallEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
		super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
	}

	@Override public void enter(Player player) {
		super.enter(player);
		for (int i = 0; i < bossLevelIncreases.length; i++) {
			if (bossLevelIncreases[i] != 0) {
				player.sendMessage("You hear a mysterious rumbling coming from the Path of " + TOAPathType.getForIndex(i).getPathName() + ".");
				player.getTOAManager().refreshPathLevel(i);
			}
		}
		if (supplyNpc != null && eligibleSupplyPlayerUsernames.contains(player.getUsername())) {
			player.sendMessage("<col=0000b2>A helpful spirit has arrived with some supplies.");
			eligibleSupplyPlayerUsernames.remove(player.getUsername());
			player.getTOAManager().setCanClaimSupplies(true);
		}
	}

	@Override public void constructed() {
		super.constructed();
		if (party.getBossLevels()[0] == 0 && party.getBossLevels()[1] == 0 && party.getBossLevels()[2] == 0 && party.getBossLevels()[3] == 0) {
			if (party.getPartySettings().isActive(InvocationType.PATHMASTER)) {
				Arrays.fill(bossLevelIncreases, 3);
			} else if (party.getPartySettings().isActive(InvocationType.PATHFINDER)) {
				Arrays.fill(bossLevelIncreases, 2);
			} else if (party.getPartySettings().isActive(InvocationType.PATHSEEKER)) {
				Arrays.fill(bossLevelIncreases, 1);
			}
		}
		final boolean walkThePath = party.getPartySettings().isActive(InvocationType.WALK_THE_PATH);
		if (party.getPathsCompleted().size() == 1 && walkThePath) {
			levelRandomPath(2);
		}
		if (party.getPathsCompleted().size() == 2) {
			if (walkThePath) {
				levelRandomPath(1);
			}
		}
		if (party.getPathsCompleted().size() == 2 || party.getPathsCompleted().size() == 4) {
			setSupplies();
		}
		if (party.getPathsCompleted().size() == 3 && walkThePath) {
			levelRandomPath(1);
		}
		if (party.getPathsCompleted().size() == 4) {
			replaceEntrance(WARDEN_ENTRANCE_LOCATION, 1);
		}
		for (int i = 0; i < bossLevelIncreases.length; i++) {
			party.increaseBossLevel(i, bossLevelIncreases[i]);
		}
		party.getPathsCompleted().forEach(path -> replaceEntrance(path.getEntranceLocation(), 2));
	}

	private void levelRandomPath(final int amount) {
		final List<TOAPathType> availablePathTypes = Arrays.stream(TOAPathType.VALUES).filter(pathType -> !party.getPathsCompleted().contains(pathType)).toList();
		for (int i = 0; i < amount; i++) {
			final TOAPathType pathType = Utils.random(availablePathTypes);
			if (pathType != null) {
				bossLevelIncreases[pathType.ordinal()] += 1;
			}
		}
	}

	private void setSupplies() {
		float factor = 1F;
		if (party.getPartySettings().isActive(InvocationType.NEED_SOME_HELP)) {
			factor = .67F;
		} else if (party.getPartySettings().isActive(InvocationType.NEED_LESS_HELP)) {
			factor = .34F;
		} else if (party.getPartySettings().isActive(InvocationType.NO_HELP_NEEDED)) {
			factor = .1F;
		}
		final boolean onDiet = party.getPartySettings().isActive(InvocationType.ON_A_DIET);
		supplyNpc = new TOASupplyNpc(HELPFUL_SPIRIT_ID, getLocation(SUPPLY_NPC_LOCATION));
		World.spawnNPC(supplyNpc);
		eligibleSupplyPlayerUsernames.addAll(party.getOriginalPlayers());
		supplyContainers = new Container[3];

		supplyContainers[0] = new Container(ContainerType.TOA_SUPPLY_LIFE, ContainerPolicy.ALWAYS_STACK, 9, Optional.empty());
		addSupplyItem(supplyContainers[0], new Item(ItemId.NECTAR_4, Math.max(1, (int) (5 * factor))));
		addSupplyItem(supplyContainers[0], new Item(ItemId.TEARS_OF_ELIDINIS_4, Math.max(1, (int) (5 * factor))));
		addSupplyItem(supplyContainers[0], new Item(ItemId.AMBROSIA_2, Math.max(1, (int) ((onDiet ? 3 : 2) * factor))));
		addSupplyItem(supplyContainers[0], new Item(ItemId.BLESSED_CRYSTAL_SCARAB_2, Math.max(1, (int) ((onDiet ? 5 : 3) * factor))));
		if (!onDiet) {
			addSupplyItem(supplyContainers[0], new Item(ItemId.SILK_DRESSING_2, (int) (3 * factor)));
		}

		supplyContainers[1] = new Container(ContainerType.TOA_SUPPLY_CHAOS, ContainerPolicy.ALWAYS_STACK, 9, Optional.empty());
		addSupplyItem(supplyContainers[1], new Item(ItemId.NECTAR_4, Math.max(1, (int) (8 * factor))));
		addSupplyItem(supplyContainers[1], new Item(ItemId.TEARS_OF_ELIDINIS_4, (int) (6 * factor)));
		addSupplyItem(supplyContainers[1], new Item(ItemId.SMELLING_SALTS_2, (int) (2 * factor)));
		if (supplyContainers[1].getSize() < 2) {
			addSupplyItem(supplyContainers[1], new Item(ItemId.LIQUID_ADRENALINE_2, 1));
		}

		supplyContainers[2] = new Container(ContainerType.TOA_SUPPLY_POWER, ContainerPolicy.ALWAYS_STACK, 9, Optional.empty());
		addSupplyItem(supplyContainers[2], new Item(ItemId.SMELLING_SALTS_2, Math.max(1, (int) (2 * factor))));
		addSupplyItem(supplyContainers[2], new Item(ItemId.LIQUID_ADRENALINE_2, Math.max(1, (int) (2 * factor))));
	}

	private void addSupplyItem(Container container, Item item) {
		if (item.getAmount() > 0) {
			container.add(item);
		}
	}

	@Override public void onRoomStart() {

	}

	@Override public void onRoomEnd() {

	}

	@Override public void onRoomReset() {

	}

	public void handleWardensEnter(final Player player, boolean quickEnter) {
		final boolean isLeader = party.isLeader(player);
		final Runnable runnable = () -> {
			if (player.getTOAManager().enter(true, EncounterType.WARDENS_FIRST_ROOM)) {
				player.getVarManager().sendBit(TOAManager.HUD_PATH_VARBIT, 5);
			}
		};
		final Runnable ownerRunnable = () -> {
			runnable.run();
			players.forEach(p -> {
				if (!player.getUsername().equals(p.getUsername())) {
					p.sendMessage(player.getName() + " has proceeded to the lower level. Join " +
							(player.getAppearance().isMale() ? "him" : "her") + "...");
				}
			});
		};
		final boolean needsAbandon = player.getTOAManager().needsAbandonRequest();
		final Runnable confirmRunnable = () -> {
			if (isLeader) {
				if (!quickEnter && !needsAbandon) {
					player.getDialogueManager().start(new OptionDialogue(player, "Do you wish to proceed to the lower level?",
							new String[]{"Yes.", "No."}, new Runnable[]{ownerRunnable, null}));
				} else {
					ownerRunnable.run();
				}
			} else {
				runnable.run();
			}
		};
		if (needsAbandon && isLeader) {
			player.getTOAManager().startAbandonDialogue("Do you wish to proceed to the lower level?", confirmRunnable);
		} else {
			confirmRunnable.run();
		}
	}

	public void handlePathEnter(final TOAPathType pathType, final Player player, boolean quickEnter) {
		if (party.getCurrentRaidArea().isDestroyed()) {
			setStartedPath(null);
		}
		if (startedPath != null) {
			if (startedPath.equals(pathType)) {
				if (player.getTOAManager().enter(true, pathType.getFirstEncounter())) {
					player.getVarManager().sendBit(TOAManager.HUD_PATH_VARBIT, pathType.ordinal() + 1);
				}
			} else {
				player.getDialogueManager().start(new PlainChat(player, "You can't proceed as a different path has already been selected."));
			}
		} else {
			final boolean isLeader = party.isLeader(player);
			final Runnable runnable = () -> {
				if (player.getTOAManager().enter(true, pathType.getFirstEncounter())) {
					player.getVarManager().sendBit(TOAManager.HUD_PATH_VARBIT, pathType.ordinal() + 1);
				}
			};
			final Runnable ownerRunnable = () -> {
				runnable.run();
				setStartedPath(pathType);
				players.forEach(p -> {
					if (!player.getUsername().equals(p.getUsername())) {
						p.sendMessage(player.getName() + " has chosen to walk the Path of " + pathType.getPathName() + ". Join " +
								(player.getAppearance().isMale() ? "him" : "her") + "...");
					}
				});
			};
			final boolean needsAbandon = player.getTOAManager().needsAbandonRequest();
			final Runnable confirmRunnable = () -> {
				if (isLeader) {
					if (!quickEnter && !needsAbandon) {
						player.getDialogueManager().start(new OptionDialogue(player, "Do you wish to walk the Path of " + pathType.getPathName() + "?",
								new String[]{"Yes.", "No."}, new Runnable[]{ownerRunnable, null}));
					} else {
						ownerRunnable.run();
					}
				} else {
					runnable.run();
				}
			};
			if (needsAbandon && isLeader) {
				player.getTOAManager().startAbandonDialogue("Do you wish to walk the Path of " + pathType.getPathName(), confirmRunnable);
			} else {
				confirmRunnable.run();
			}
		}
	}

	private void replaceEntrance(final Location originalTile, int idIncrease) {
		final WorldObject object = World.getObjectWithType(getLocation(originalTile), 10, true);
		if (object != null) {
			World.spawnObject(new WorldObject(object.getId() + idIncrease, object.getType(), object.getRotation(), object.getLocation()));
		}
	}

	public void setStartedPath(TOAPathType startedPath) {
		party.setPathType(this.startedPath = startedPath);
		Arrays.stream(TOAPathType.VALUES).filter(path -> !party.getPathsCompleted().contains(path) && !path.equals(startedPath)).
				forEach(path -> replaceEntrance(path.getEntranceLocation(), startedPath == null ? -1 : 1));
	}

	public Container[] getSupplyContainers() { return supplyContainers; }

	static class TOASupplyNpc extends NPC {

		public TOASupplyNpc(int id, Location tile) {
			super(id, tile, Direction.SOUTH, 0);
		}

		@Override public void setRespawnTask() {}

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public void setFaceLocation(Location tile) {}

		@Override public boolean isEntityClipped() { return false; }

		@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }
	}
}
