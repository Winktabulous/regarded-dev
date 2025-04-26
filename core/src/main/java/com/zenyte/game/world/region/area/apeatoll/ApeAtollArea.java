package com.zenyte.game.world.region.area.apeatoll;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.ContainerPlugin;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 28. aug 2018 : 13:50:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class ApeAtollArea extends PolygonRegionArea implements ContainerPlugin {
	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{2688, 2816}, {2688, 2688}, {2816, 2688}, {2816, 2816}}), new RSPolygon(new int[][] {{3012, 5496}, {3012, 5448}, {3035, 5448}, {3035, 5472}, {3070, 5472}, {3070, 5496}}, 0)};
	}

	@Override
	public void enter(final Player player) {
		player.getTeleportManager().unlock(PortalTeleport.MONKEY_GUARDS);
		final Greegree greegree = Greegree.MAPPED_VALUES.get(player.getEquipment().getId(EquipmentSlot.WEAPON));
		if (greegree == null) {
			return;
		}
		greegree.transform(player);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		if (GlobalAreaManager.get("Ape Atoll").inside(player.getLocation()) || GlobalAreaManager.get("Ape Atoll Dungeon").inside(player.getLocation())) {
			return;
		}
		if (player.getTemporaryAttributes().remove("greegree") != null) {
			final Equipment equipment = player.getEquipment();
			final Item item = equipment.getItem(EquipmentSlot.WEAPON);
			Greegree.reset(player);
			if (item == null || Greegree.MAPPED_VALUES.get(item.getId()) == null) {
				return;
			}
			player.getDialogueManager().start(new PlainChat(player, "The Monkey Greegree wrenches itself from your hand as its power begins to fade..."));
			equipment.set(EquipmentSlot.WEAPON, null);
			player.getInventory().addItem(item).onFailure(it -> {
				World.spawnFloorItem(it, player);
				player.sendMessage("The Monkey Greegree was placed on the ground due to lack of space.");
			});
		}
	}

	@Override
	public String name() {
		return "Ape Atoll";
	}

	@Override
	public void onContainerModification(final Player player, final Container container, final Item previousItem, final Item currentItem) {
		if (container.getType() != ContainerType.EQUIPMENT) {
			return;
		}
		if (previousItem != null && Greegree.MAPPED_VALUES.get(previousItem.getId()) != null) {
			Greegree.reset(player);
		}
		if (currentItem != null) {
			final Greegree greegree = Greegree.MAPPED_VALUES.get(currentItem.getId());
			if (greegree != null) {
				greegree.transform(player);
			}
		}
	}
}
