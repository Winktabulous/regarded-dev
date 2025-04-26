package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.encounter.HetEncounter;
import com.zenyte.game.content.tombsofamascut.object.MirrorObjectAction;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class MirrorItemAction extends ItemPlugin {

	private static final SoundEffect PLACE_SOUND = new SoundEffect(2739);
	private static final Animation PLACE_ANIM = new Animation(827);

	@Override public void handle() {
		final OptionHandler handler = (player, item, container, slotId) -> {
			if (player.getArea() instanceof final HetEncounter encounter) {
				final WorldObject object = World.getObjectOfSlot(player.getLocation(), 10);
				if (object != null) {
					player.sendMessage("You can't place that here.");
				} else {
					player.getInventory().set(slotId, null);
					player.getInventory().refresh(slotId);
					player.sendSound(PLACE_SOUND);
					player.setAnimation(PLACE_ANIM);
					final WorldObject mirrorObject = new WorldObject(HetEncounter.MIRROR_PICK_UP, 11, 0, new Location(player.getLocation()));
					World.spawnObject(mirrorObject);
					encounter.addObject(mirrorObject);
				}
			}
		};
		bind("Place", handler);
		bind("Drop", handler);
	}

	@Override public int[] getItems() {
		return new int[] {MirrorObjectAction.MIRROR_ITEM_ID};
	}
}
