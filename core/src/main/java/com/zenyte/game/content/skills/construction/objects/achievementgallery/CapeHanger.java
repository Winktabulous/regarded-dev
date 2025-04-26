package com.zenyte.game.content.skills.construction.objects.achievementgallery;

import com.zenyte.game.content.skills.construction.*;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kris | 27. veebr 2018 : 0:14.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CapeHanger implements ObjectInteraction, ItemOnObjectAction {

	private static final Animation ANIM = new Animation(645);
	private static final Animation PUT_ITEM_ANIM = new Animation(834);
	private static final Furniture[] CAPES = new Furniture[1 + Furniture.MOUNTED_MYTHICAL_CAPE.ordinal() - Furniture.UNTRIMMED_ACHIEVEMENT_DIARY_CAPE.ordinal()];
	private static final Map<Integer, Furniture> MAP = new HashMap<Integer, Furniture>(CAPES.length * 2);
	private static final Map<Integer, Furniture> OBJECT_MAP = new HashMap<Integer, Furniture>(CAPES.length);
	
	static {
		int index = 0;
		for (int i = Furniture.UNTRIMMED_ACHIEVEMENT_DIARY_CAPE.ordinal(); i <= Furniture.MOUNTED_MYTHICAL_CAPE.ordinal(); i++) {
			final Furniture furn = Furniture.VALUES[i];
			CAPES[index++] = furn;
			OBJECT_MAP.put(furn.getObjectId(), furn);
			for (Item it : furn.getMaterials()) {
				if (it == null)
					continue;
				if (it.getId() == -1)
					continue;
				MAP.put(it.getId(), furn);
			}
		}
	}
	
	@Override
	public Object[] getObjects() {
		final List<Object> list = new ArrayList<Object>(CAPES.length + 1);
		for (Furniture cape : CAPES)
			list.add(cape.getObjectId());
		list.add(29166);
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if (player.getCurrentHouse() != player.getConstruction()) {
			player.sendMessage("You can only do this in your own house.");
			return;
		}
		if (!(player.getControllerManager().getController() instanceof ConstructionController))
			return;
		final Construction construction = player.getConstruction();
		final RoomReference room = construction.getReference(player.getLocation());
		if (room == null)
			return;
		final FurnitureData data = room.getFurniture(Furniture.CAPE_HANGER);
		if (data == null) {
			player.sendMessage("You need the remove the other cape from the hanger first.");
			return;
		}
		final Furniture requestedFurn = MAP.get(item.getId());
		if (requestedFurn == null)
			return;
		final List<Item> items = new ArrayList<Item>(2);
		for (Item i : requestedFurn.getMaterials()) {
			if (i == null)
				continue;
			if (i.getId() == -1)
				continue;
			items.add(i);
		}
		if (!player.getInventory().containsItems(items)) {
			final StringBuilder b = new StringBuilder();
			for (Item i : requestedFurn.getMaterials()) {
				if (i == null)
					continue;
				if (i.getId() == -1)
					continue;
				final String name = i.getName().toLowerCase();
				b.append((Utils.startWithVowel(name) ? "an " : "a ") + name + " and ");
			}
			if (b.length() >= 5)
				b.delete(b.length() - 5, b.length());
			player.sendMessage("You need " + b + " to enqueue it to the cape hanger.");
			return;
		}
		player.lock(1);
		player.setAnimation(PUT_ITEM_ANIM);
		boolean hood = true;
		for (Item i : requestedFurn.getMaterials()) {
			if (i == null)
				continue;
			if (i.getId() == -1) {
				hood = false;
				continue;
			}
			player.getInventory().deleteItem(i);
		}
		data.setFurniture(requestedFurn);
		player.sendMessage("You put the " + (hood ? "cape and hood" : "cape") + " on the cape hanger.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				World.spawnObject(new WorldObject(requestedFurn.getObjectId(), object.getType(), object.getRotation(), object));
			}
		});
	}

	@Override
	public Object[] getItems() {
		return MAP.keySet().toArray(new Object[MAP.keySet().size()]);
	}

	@Override
	public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
		if (option.equals("admire")) {
			player.lock(5);
			player.setAnimation(ANIM);
			player.sendMessage("You admire the " + object.getName().toLowerCase() + ".");
		} else if (option.equals("take")) {
			if (player.getCurrentHouse() != player.getConstruction()) {
				player.sendMessage("You can only do this in your own house.");
				return;
			}
			final Furniture furniture = OBJECT_MAP.get(object.getId());
			if (furniture == null)
				return;
			final FurnitureData data = reference.getFurniture(furniture);
			if (data == null)
				return;
			final Furniture furn = data.getFurniture();
			if (furn == null)
				return;
			final Item[] materials = furn.getMaterials();
			if (materials == null)
				return;
			final List<Item> items = new ArrayList<Item>(2);
			for (Item i : materials) {
				if (i == null)
					continue;
				if (i.getId() == -1)
					continue;
				items.add(i);
			}
			if (player.getInventory().getFreeSlots() < items.size()) {
				player.sendMessage("You need at least " + (items.size() == 1 ? "one" : "two") + "free space" + (items.size() == 1 ? "" : "s") + " to take the cape off the hanger.");
				return;
			}
			player.setAnimation(PUT_ITEM_ANIM);
			data.setFurniture(Furniture.CAPE_HANGER);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					for (Item i : items) 
						player.getInventory().addItem(i);
					player.sendMessage("You take the " + (items.size() == 2 ? "cape and hood" : "cape") + " off the cape hanger.");
					World.spawnObject(new WorldObject(Furniture.CAPE_HANGER.getObjectId(), object.getType(), object.getRotation(), object));
				}
			});
		}
	}

}
