package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.tombsofamascut.encounter.HetEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class MirrorObjectAction implements ObjectAction {

	public static final int MIRROR_ITEM_ID = 27296;
	private static final SoundEffect PICK_SOUND = new SoundEffect(2581);
	private static final SoundEffect MIRROR_ROTATE_SOUND = new SoundEffect(6543);
	private static final SoundEffect PUSH_SOUND = new SoundEffect(86);
	private static final Animation PUSH_ANIM = new Animation(810);
	private static final Animation PICK_ANIM = new Animation(827);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final HetEncounter encounter) {
			if (object.getId() == HetEncounter.MIRROR_STATIC_DIRTY) {
				player.sendMessage("You clean the mirror.");
				object.setId(HetEncounter.MIRROR_STATIC);
				World.spawnObject(object);
				player.sendSound(PICK_SOUND);
				player.setAnimation(PICK_ANIM);
			} else {
				switch (option) {
					case "Pick-up":
						if (player.getInventory().getFreeSlots() < 1) {
							player.sendMessage("You don't have enough space in your inventory.");
						} else {
							encounter.removeObject(object);
							World.removeObject(object);
							player.sendSound(PICK_SOUND);
							player.setAnimation(PICK_ANIM);
							player.getInventory().addItem(new Item(MIRROR_ITEM_ID));
						}
						break;
					case "Rotate-clockwise":
						rotateMirror(player, object, true);
						break;
					case "Rotate-anticlockwise":
						rotateMirror(player, object, false);
						break;
					case "Push":
						final Direction direction = Direction.getDirection(player.getLocation(), object.getLocation());
						player.sendSound(PUSH_SOUND);
						player.setAnimation(PUSH_ANIM);
						player.lock(1);
						final Location currentLocation = object.getLocation().transform(0, 0);
						WorldTasksManager.schedule(encounter.addRunningTask(() -> {
							if (EncounterStage.STARTED.equals(encounter.getStage())) {
								final Location newLocation = currentLocation.transform(direction.getOffsetX(), direction.getOffsetY());
								if (currentLocation.equals(object.getLocation()) && World.getObjectOfSlot(newLocation, 10) == null) {
									World.removeObject(new WorldObject(object));
									object.setLocation(newLocation);
									World.spawnObject(object);
								}
							}
						}), 0);
						break;
				}
			}
		}
	}

	private static void rotateMirror(final Player player, final WorldObject object, boolean clockWise) {
		object.setRotation((object.getRotation() + (clockWise ? 1 : -1)) % 4);
		World.spawnObject(object);
		player.sendSound(MIRROR_ROTATE_SOUND);
	}

	@Override public Object[] getObjects() {
		return new Object[] {HetEncounter.MIRROR_PICK_UP, HetEncounter.MIRROR_STATIC_DIRTY};
	}
}
