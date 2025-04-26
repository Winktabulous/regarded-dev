package com.zenyte.plugins.item;

import com.near_reality.game.content.imbue.DisimbueItemHandler;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.plugins.itemonitem.SlayerHelmItemCreation;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.runelite.api.ItemID;

/**
 * @author Kris | 25. aug 2018 : 22:47:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SlayerHelm extends ItemPlugin {
	public static final Item BLACK_MASK = new Item(ItemID.BLACK_MASK);
	public static final Item BLACK_MASK_I = new Item(ItemID.BLACK_MASK_I);

	public enum SlayerHelmRecolour {
		BLACK(ItemId.KBD_HEADS, ItemId.BLACK_SLAYER_HELMET, ItemId.BLACK_SLAYER_HELMET_I, "King black bonnet"),
		GREEN(ItemId.KQ_HEAD, ItemId.GREEN_SLAYER_HELMET, ItemId.GREEN_SLAYER_HELMET_I, "Kalphite khat"),
		RED(ItemId.ABYSSAL_HEAD, ItemId.RED_SLAYER_HELMET, ItemId.RED_SLAYER_HELMET_I, "Unholy helmet"),
		PURPLE(ItemId.DARK_CLAW, ItemId.PURPLE_SLAYER_HELMET, ItemId.PURPLE_SLAYER_HELMET_I, "Dark Mantle"),
		TURQOISE(ItemId.VORKATHS_HEAD_21907, ItemId.TURQUOISE_SLAYER_HELMET, ItemId.TURQUOISE_SLAYER_HELMET_I, "Undead head"),
		HYDRA(ItemId.ALCHEMICAL_HYDRA_HEADS, ItemId.HYDRA_SLAYER_HELMET, ItemId.HYDRA_SLAYER_HELMET_I, "Use more head"),
		TWISTED(ItemId.TWISTED_HORNS, ItemId.TWISTED_SLAYER_HELMET, ItemId.TWISTED_SLAYER_HELMET_I, "Twisted Vision"),
		ARAXXOR(ItemId.ARAXYTE_HEAD, ItemId.ARAXYTE_SLAYER_HELMET, ItemId.ARAXYTE_SLAYER_HELMET_I, "Araxyte helm");

		public static final SlayerHelmRecolour[] values = values();
		private final int base;
		private final int helm;
		private final int helmImbued;
		private final String slayerReward;

		SlayerHelmRecolour(int base, int helm, int helmImbued, String slayerReward) {
			this.base = base;
			this.helm = helm;
			this.helmImbued = helmImbued;
			this.slayerReward = slayerReward;
		}

		public int getBase() {
			return base;
		}

		public int getHelm() {
			return helm;
		}

		public int getHelmImbued() {
			return helmImbued;
		}

		public String getSlayerReward() {
			return slayerReward;
		}
	}

	@Override
	public void handle() {
		bind("Disassemble", (player, item, slotId) -> {
			if (item.getId() == ItemId.SLAYER_HELMET || item.getId() == ItemId.SLAYER_HELMET_I) {
				if (player.getInventory().getFreeSlots() < SlayerHelmItemCreation.REQUIRED_ITEMS.length + 1) {
					player.sendMessage("You need more inventory space to disassemble your slayer helmet.");
					return;
				}
				player.getInventory().deleteItem(item);
				for (final Item component : SlayerHelmItemCreation.REQUIRED_ITEMS) {
					player.getInventory().addItem(component);
				}
				if(item.getName().endsWith("(i)")) {
					player.getInventory().addItem(BLACK_MASK_I);
				} else {
					player.getInventory().addItem(BLACK_MASK);
				}
			} else {
				if (player.getInventory().getFreeSlots() < SlayerHelmItemCreation.REQUIRED_ITEMS.length + 2) {
					player.sendMessage("You need more inventory space to disassemble your slayer helmet.");
					return;
				}
				player.getInventory().deleteItem(item);
				for (final Item component : SlayerHelmItemCreation.REQUIRED_ITEMS) {
					player.getInventory().addItem(component);
				}
				if(item.getName().endsWith("(i)")) {
					player.getInventory().addItem(BLACK_MASK_I);
				} else {
					player.getInventory().addItem(BLACK_MASK);
				}
				for (final SlayerHelm.SlayerHelmRecolour recolour : SlayerHelmRecolour.values) {
					if (item.getId() == recolour.getHelm() || item.getId() == recolour.getHelmImbued()) {
						player.getInventory().addItem(new Item(recolour.getBase()));
						break;
					}
				}
			}
		});
		bind("Check", (player, item, slotId) -> player.getSlayer().sendTaskInformation());
		bind("Log", (player, item, slotId) -> player.getNotificationSettings().sendKillLog(NotificationSettings.SLAYER_NPC_NAMES, true));
		bind("Partner", (player, item, slotId) -> {
			player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 68);
			player.getSlayer().refreshPartnerInterface();
		});
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final SlayerHelm.SlayerHelmRecolour recolour : SlayerHelmRecolour.values) {
			list.add(recolour.getHelm());
			list.add(recolour.getHelmImbued());
		}
		list.add(ItemId.SLAYER_HELMET);
		list.add(ItemId.SLAYER_HELMET_I);
		list.add(ItemId.TZTOK_SLAYER_HELMET);
		list.add(ItemId.TZTOK_SLAYER_HELMET_I);
		list.add(ItemId.VAMPYRIC_SLAYER_HELMET);
		list.add(ItemId.VAMPYRIC_SLAYER_HELMET_I);
		list.add(ItemId.TZKAL_SLAYER_HELMET);
		list.add(ItemId.TZKAL_SLAYER_HELMET_I);
		return list.toArray(new int[list.size()]);
	}
}
