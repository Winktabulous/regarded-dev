package com.zenyte.game.world.object;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;

import java.util.HashMap;
import java.util.Map;

public class PickPlant extends Action {
	
	private static final Animation PICK = new Animation(827);
	
	private final WorldObject object;
	
	private Pickables entry;
	
	public PickPlant(WorldObject object) {
		this.object = object;
	}

	@Override
	public boolean start() {
        this.entry = Pickables.map.get(WorldObjectUtils.getObjectNameOfPlayer(object, player).toLowerCase());
		if(!check())
			return false;
		player.lock();
		return true;
	}

	@Override
	public boolean process() {
		return check();
	}

	@Override
	public int processWithDelay() {
		reward();
		return -1;
	}

	@Override
	public void stop() {
		player.unlock();
	}

	private void reward() {
		String message = entry.getItem().getName().toLowerCase();
		player.setAnimation(PICK);
		if (entry.equals(Pickables.FLAX)) {
            if (Utils.random(5) == 0) {
                World.removeObject(object);
            }
        } else {
            player.addWalkSteps(object.getX(), object.getY());
            World.removeObject(object);
        }
		message = message.equals("potato") ? "a "+message : "some "+message;
		message = message.equals("some onion") ? "an onion" : message;
		player.sendMessage("You pick "+message+".");
		player.sendSound(2581);
		player.getInventory().addItem(entry.getItem());
		if (entry.equals(Pickables.FLAX)) {
			player.getAchievementDiaries().update(KandarinDiary.COLLECT_FLAX);
		}
		WorldTasksManager.schedule(() -> World.spawnObject(object), 25);
	}
	
	private boolean check() {
		if(!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You don't have enough space in your inventory");
		    return false;
        }
        return !player.isDead() && !player.isFinished() && checkObject() && entry != null;
    }

	private boolean checkObject() {
		return World.getRegion(object.getRegionId()).containsObject(object.getId(), object.getType(), object);
	}
	
	public enum Pickables {
		
		WHEAT("wheat", new Item(1947, 1)),
		ONION("onion", new Item(1957, 1)),
		POTATO("potato", new Item(1942, 1)),
		CABBAGE("cabbage", new Item(1965, 1)),
		FLAX("flax", new Item(1779, 1)),
		NETTLES("nettles", new Item(4241, 1)),
		;
		
		private final String name;
		private final Item item;
				
		Pickables(String name, Item item) {
			this.name = name;
			this.item = item;
		}
		
		public static final Pickables[] VALUES = values();
		
		public static Map<String, Pickables> map = new HashMap<>();
		
		static {
			for(Pickables entry : Pickables.VALUES)
				map.put(entry.getName(), entry);
		}
		
		public String getName() {
		    return name;
		}
		
		public Item getItem() {
		    return item;
		}
	}
	
}
