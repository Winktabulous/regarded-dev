package com.zenyte.plugins.interfaces;

import com.zenyte.game.content.skills.construction.costume.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.UserInterface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ItemContainer;

/**
 * @author Kris | 1. march 2018 : 16:19.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CostumeRoomUI implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		final Object obj = player.getTemporaryAttributes().get("costumeRoomObject");
		if (obj == null) {
			return;
		}
		if (obj.equals("ARMOUR_CASE")) {
			final ArmourCase armourCase = player.getConstruction().getArmourCase();
			final int idx = armourCase.getPage();
			final ItemContainer container = ArmourData.CONTAINERS[idx];
			final Item item = container.get(slotId / 4);
			final String name = item.getName();
			if (name.equals("More...")) {
				armourCase.open(armourCase.getPage() + 1);
				return;
			} else if (name.equals("Back...")) {
				armourCase.open(armourCase.getPage() - 1);
				return;
			}
			if (optionId == 10) {
				ItemUtil.sendItemExamine(player, item);
				return;
			} else {
				armourCase.takeSet(slotId);
			}
		} else if (obj.equals("FANCY_DRESS_BOX")) {
			final FancyDressBox box = player.getConstruction().getFancyDressBox();
			final ItemContainer container = FancyDressData.CONTAINER;
			final Item item = container.get(slotId / 4);
			if (optionId == 10) {
				ItemUtil.sendItemExamine(player, item);
				return;
			} else {
				box.takeSet(slotId);
			}
		} else if (obj.equals("TOY_BOX")) {
			final ToyBox toyBox = player.getConstruction().getToyBox();
			final int idx = toyBox.getPage();
			final ItemContainer container = ToyBoxData.CONTAINERS[idx];
			final Item item = container.get(slotId / 4);
			final String name = item.getName();
			if (name.equals("More...")) {
				toyBox.open(toyBox.getPage() + 1);
				return;
			} else if (name.equals("Back...")) {
				toyBox.open(toyBox.getPage() - 1);
				return;
			}
			if (optionId == 10) {
				ItemUtil.sendItemExamine(player, item);
				return;
			} else {
				toyBox.takeSet(slotId);
			}
		} else if (obj.equals("MAGIC_WARDROBE")) {
			final MagicWardrobe box = player.getConstruction().getMagicWardrobe();
			final ItemContainer container = MagicWardrobeData.CONTAINER;
			final Item item = container.get(slotId / 4);
			if (optionId == 10) {
				ItemUtil.sendItemExamine(player, item);
				return;
			} else {
				box.takeSet(slotId);
			}
		} else if (obj.equals("CAPE_RACK")) {
			final CapeRack toyBox = player.getConstruction().getCapeRack();
			final int idx = toyBox.getPage();
			final ItemContainer container = CapeRackData.CONTAINERS[idx];
			final Item item = container.get(slotId / 4);
			final String name = item.getName();
			if (name.equals("More...")) {
				toyBox.open(toyBox.getPage() + 1);
				return;
			} else if (name.equals("Back...")) {
				toyBox.open(toyBox.getPage() - 1);
				return;
			}
			if (optionId == 10) {
				ItemUtil.sendItemExamine(player, item);
				return;
			} else {
				toyBox.takeSet(slotId);
			}
		} else if (obj.equals("TREASURE_CHEST")) {
			final TreasureChest treasureChest = player.getConstruction().getTreasureChest();
			final int idx = treasureChest.getPage();
			final ItemContainer container = TreasureChestData.CONTAINERS[idx];
			final Item item = container.get(slotId / 4);
			final String name = item.getName();
			if (name.equals("More...")) {
				treasureChest.open(treasureChest.getPage() + 1);
				return;
			} else if (name.equals("Back...")) {
				treasureChest.open(treasureChest.getPage() - 1);
				return;
			}
			if (optionId == 10) {
				ItemUtil.sendItemExamine(player, item);
				return;
			} else {
				treasureChest.takeSet(slotId);
			}
		}
	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 592 };
	}

}
