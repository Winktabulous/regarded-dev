package com.zenyte.plugins.interfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.plugins.item.mysteryboxes.MysteryBox;
import com.zenyte.plugins.item.mysteryboxes.MysteryItem;
import com.zenyte.plugins.item.mysteryboxes.MysterySupplyItem;

import java.util.Optional;

public class MysteryBoxInterface extends Interface {

	@Override
	protected void attach() {
		put(7, "Spin");
		put(18, "Reward text");
		put(19, "Close");
		put(30, "Purchase boxes");
	}

	@Override
	protected void build() {
		bind("Purchase boxes", player -> player.getPacketDispatcher().sendURL("https://near-reality.com/store?category=9"));
		bind("Spin", (player, slotId, itemId, option) -> {
			Integer boxId = (Integer) player.getTemporaryAttributes().get("mbox_item");
			Container container = (Container) player.getTemporaryAttributes().get("mbox_container");
			if (boxId == null || container == null) {
				player.getInterfaceHandler().closeInterfaces();
				return;
			}

			MysteryItem[] rewards = (MysteryItem[]) player.getTemporaryAttributes().get("mbox_rewards");
			Integer totalWeight = (Integer) player.getTemporaryAttributes().get("mbox_totalWeight");
			MysteryItem jackpot = MysteryItem.generateItem(player, rewards, totalWeight);
			final Item reward = new Item(jackpot.getId(), Utils.random(jackpot.getMinAmount(), jackpot.getMaxAmount()), jackpot.getCharges());
			container.set(31, reward);
			player.getPacketDispatcher().sendUpdateItemsPartial(container);

			int ticks = 8;
			player.lock(ticks);
			player.putBooleanTemporaryAttribute("mbox_rolling", true);
			WorldTasksManager.schedule(() -> {
				player.putBooleanTemporaryAttribute("mbox_rolling", false);
				Item box = new Item(boxId);
				if (!player.getInventory().deleteItem(box).isFailure()) {
					player.getInventory().addOrDrop(reward);
					if (jackpot.isAnnounce()) {
						WorldBroadcasts.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, reward.getId(), box.getName());
					}

					StringBuilder stringBuilder = new StringBuilder("You open the Mystery box and find ");
					stringBuilder.append(Utils.getAOrAn(reward.getName()));
					stringBuilder.append(" ");
					stringBuilder.append(Colour.WHITE.wrap(reward.getName()));
					MysterySupplyItem[] supplies = (MysterySupplyItem[]) player.getTemporaryAttributes().get("mbox_supplies");
					if (supplies != null) {
						for (int i = 0; i < 2; i++) {
							MysterySupplyItem supplyItem = Utils.random(supplies);
							final Item supplyReward = new Item(supplyItem.getId(), Utils.random(supplyItem.getMinAmount(), supplyItem.getMaxAmount()));
							player.getInventory().addOrDrop(supplyReward);
						}
						stringBuilder.append(" and some supplies.");
					} else {
						stringBuilder.append(".");
					}

					player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Reward text"), stringBuilder.toString());
				}
			}, ticks - 1);
		});
		bind("Close", player -> player.getInterfaceHandler().closeInterfaces());
	}

	public static void openBoxQuick(Player player, int boxId, MysteryItem[] rewards, int totalWeight, MysterySupplyItem[] supplies) {
		Item box = new Item(boxId);
		if (!player.getInventory().deleteItem(box).isFailure()) {
			MysteryItem jackpot = MysteryItem.generateItem(player, rewards, totalWeight);
			final Item reward = new Item(jackpot.getId(), Utils.random(jackpot.getMinAmount(), jackpot.getMaxAmount()), jackpot.getCharges());

			player.getInventory().addOrDrop(reward);
			if (jackpot.isAnnounce()) {
				WorldBroadcasts.broadcast(player, BroadcastType.MYSTERY_BOX_RARE_ITEM, reward.getId(), box.getName());
			}

			if (supplies != null) {
				for (int i = 0; i < 2; i++) {
					MysterySupplyItem supplyItem = Utils.random(supplies);
					final Item supplyReward = new Item(supplyItem.getId(), Utils.random(supplyItem.getMinAmount(), supplyItem.getMaxAmount()));
					player.getInventory().addOrDrop(supplyReward);
				}
			}
		}
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.MYSTERY_BOX;
	}

	@Override
	public void close(Player player, Optional<GameInterface> replacement) {
		if (player.getBooleanTemporaryAttribute("mbox_rolling")) {
			return;
		}
		
		player.getTemporaryAttributes().remove("mbox_container");
		player.getTemporaryAttributes().remove("mbox_item");
		player.getTemporaryAttributes().remove("mbox_rewards");
		player.getTemporaryAttributes().remove("mbox_totalWeight");
		player.getTemporaryAttributes().remove("mbox_supplies");
	}

	public static void openBox(Player player, int item, MysteryItem[] rewards, int totalWeight, MysterySupplyItem[] supplies) {
		Container container = new Container(ContainerPolicy.NORMAL, ContainerType.MYSTERY_BOX, Optional.empty());
		for (int i = 0; i < container.getContainerSize(); i++) {
			MysteryItem mysteryItem = Utils.random(rewards);
			container.set(i, new Item(mysteryItem.getId(), Utils.random(mysteryItem.getMinAmount(), mysteryItem.getMaxAmount())));
		}

		player.getPacketDispatcher().sendUpdateItemContainer(container);
		GameInterface.MYSTERY_BOX.open(player);
		player.getTemporaryAttributes().put("mbox_container", container);
		player.getTemporaryAttributes().put("mbox_item", item);
		player.getTemporaryAttributes().put("mbox_rewards", rewards);
		player.getTemporaryAttributes().put("mbox_totalWeight", totalWeight);
		player.getTemporaryAttributes().put("mbox_supplies", supplies);
	}

}
