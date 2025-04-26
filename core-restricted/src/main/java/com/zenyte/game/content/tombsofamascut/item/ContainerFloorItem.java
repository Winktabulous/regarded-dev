package com.zenyte.game.content.tombsofamascut.item;

import com.zenyte.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.flooritem.FloorItemPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Savions.
 */
public class ContainerFloorItem implements FloorItemPlugin {

	private static final SoundEffect ITEM_TAKE_SOUND = new SoundEffect(2582);
	private static final Animation ITEM_TAKE_ANIM = new Animation(827);

	@Override public void handle(Player player, FloorItem item, int optionId, String option) {
		if (player.getArea() instanceof CrondisPuzzleEncounter crondisPuzzleEncounter && EncounterStage.COMPLETED.equals(crondisPuzzleEncounter.getStage())) {
			player.sendMessage("You don't need a container right now.");
			return;
		}
		if (player.getInventory().containsItem(CrondisPuzzleEncounter.CONTAINER_ITEM_ID)) {
			player.sendMessage("You already have a container.");
		} else if (player.getInventory().hasSpaceFor(CrondisPuzzleEncounter.CONTAINER_ITEM_ID)) {
			player.getInventory().addItem(new Item(CrondisPuzzleEncounter.CONTAINER_ITEM_ID, 1));
			player.sendSound(ITEM_TAKE_SOUND);
			player.setAnimation(ITEM_TAKE_ANIM);
		} else {
			player.sendMessage("You do not have enough space to pick this up.");
		}
	}

	@Override public boolean overrideTake() { return true; }

	@Override public boolean canTelegrab(@NotNull Player player, @NotNull FloorItem item) { return false; }

	@Override public void telegrab(@NotNull Player player, @NotNull FloorItem item) { }

	@Override public int[] getItems() {
		return new int[] {CrondisPuzzleEncounter.CONTAINER_ITEM_ID};
	}
}
