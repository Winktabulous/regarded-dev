package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Savions.
 */
public class PickaxeTakeAction implements ObjectAction, ItemOnObjectAction {

	private static final int PICKAXE_VARBIT = 14440;
	private static final Animation ANIM = new Animation(832);
	private static final SoundEffect SOUND = new SoundEffect(2582);

	static {
		VarManager.appendPersistentVarbit(PICKAXE_VARBIT);
	}

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		handleObject(player, "Take-pickaxe".equals(option));
	}

	@Override public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		handleObject(player, false);
	}

	private static void handleObject(Player player, boolean take) {
		final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> pickaxe = MiningDefinitions.PickaxeDefinitions.get(player, true);
		final int pickaxeValue = player.getVarManager().getBitValue(PICKAXE_VARBIT);
		if (take) {
			if (pickaxe.isPresent()) {
				player.sendMessage("You already have a pickaxe.");
			} else if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("You don't have any space in your inventory.");
			} else {
				player.setAnimation(ANIM);
				player.sendSound(SOUND);
				player.lock(2);
				final Item pickaxeItem = new Item(getPickaxeId(pickaxeValue));
				player.getInventory().addItem(pickaxeItem);
				player.sendMessage("You take the " + pickaxeItem.getName() + ".");
				player.getVarManager().sendBit(PICKAXE_VARBIT, 0);
			}
		} else if (pickaxe.isEmpty()) {
			player.sendMessage("You don't have anything to deposit.");
		} else if (pickaxeValue > 0) {
			player.sendMessage("There's already a pickaxe stored here.");
		} else {
			final MiningDefinitions.PickaxeDefinitions objectDef = MiningDefinitions.PickaxeDefinitions.getToolDef(pickaxe.get().getItem().getId());
			if (objectDef != null) {
				player.setAnimation(ANIM);
				player.sendSound(SOUND);
				player.lock(2);
				final Item pickaxeItem = pickaxe.get().getItem();
				if(player.removeItem(pickaxeItem, true)) {
					player.getVarManager().sendBit(PICKAXE_VARBIT, getPickaxeValue(objectDef));
					player.sendMessage("You place down the " + pickaxeItem.getName() + ".");
				}
			}
		}
	}

	private static int getPickaxeId(int value) {
		return switch (value) {
			case 13 -> MiningDefinitions.PickaxeDefinitions.THIRD_AGE.getId();
			case 14 -> MiningDefinitions.PickaxeDefinitions.INACTIVE_CRYSTAL.getId();
			default -> value > 13 ? 0 : MiningDefinitions.PickaxeDefinitions.VALUES[value].getId();
		};
	}

	private static int getPickaxeValue(MiningDefinitions.PickaxeDefinitions pickaxe) {
		return switch(pickaxe) {
			case THIRD_AGE -> 13;
			case INACTIVE_CRYSTAL -> 14;
			default -> pickaxe.ordinal();
		};
	}

	@Override public Object[] getItems() {
		final List<Integer> ids = Arrays.stream(MiningDefinitions.PickaxeDefinitions.VALUES).map(MiningDefinitions.PickaxeDefinitions::getId).toList();
		return ids.toArray();
	}

	@Override public Object[] getObjects() {
		return new Object[] {45468};
	}
}
