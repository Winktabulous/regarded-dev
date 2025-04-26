package com.zenyte.game.world.entity.npc.drop.matrix;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.ItemContainer;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.HashMap;
import java.util.List;

public class DropPrediction implements Runnable {

	private Item[] loots = null;
	private final HashMap<Integer, Integer> rareDrops = new HashMap<>();

	private void addItems(final Drop drop) {

		final Item item = new Item(drop.getItemId(), drop.getMinAmount() + getRandom(drop.getExtraAmount()));
		if (drop.getBaseRate() <= 50) {
			rareDrops.put(item.getId(), 0);
		}
		for (int i = 0; i < loots.length; i++) {
			if (loots[i] != null && loots[i].getId() == item.getId()) {
				loots[i].setAmount(loots[i].getAmount() + item.getAmount());
				break;
			}
			if (loots[i] == null) {
				loots[i] = item;
				break;
			}
		}
    }
	
	public static final int getRandom(final int maxValue) {
		return (int) (Utils.randomDouble() * (maxValue + 1));
	}
	
	private final int npcId;
	private int amount;
	private final Player player;
	
	public DropPrediction(final Player player, final int npcId, final int amount) {
		this.npcId = npcId;
		this.amount = amount;
		this.player = player;
	}

	private void generateDrops(final Player player, final int npcId, int amount) {
		final NPCDrops.DropTable drops = NPCDrops.getTable(npcId);
		if (drops == null) {
			return;
		}
		int index = 0;

		while(index < amount) {
			NPCDrops.rollTable(player, drops, this::addItems);
			index++;
		}




    }


	@Override
	public void run() {
		if(amount > 10000)
			amount = 10000;
		loots = new Item[100];
		for (int i = 0; i < amount; i++) {
			generateDrops(player, npcId, amount);
		}
		long value = 0;
		for (final Item item : loots) {
			if (item == null) {
				continue;
			}
			value += (long) item.getSellPrice() * item.getAmount();
		}
		
		String val;
		if (value < 100000) {
			val = "<col=ffff00>" + value + "</col>";
		} else if (value < 10000000) {
			val = "<col=ffffff>" + (value / 1000) + "K" + "</col>";
		} else if (value < 1_000_000_000L) {
			val = "<col=00ff80>" + (value / 1000000) + "M" + "</col>";
		} else {
			val = "<col=00ff80>" + (value / 1000000000) + "B" + "</col>";
		}
		rareDrops.forEach((k, v) -> {
			double rate = 0.0000;
			for (final Item item : loots) {
				if (item != null && item.getDefinitions().getPrice() > 5000) {
					if (item.getId() == k) {
						rate = Utils.round(((double) item.getAmount() / amount) * 100, 4);
						player.sendMessage("<col=00ff00>The drop rate for "
								+ ItemDefinitions.get(k).getName() + ", based off of " + amount
								+ " kills is " + rate + "% (1:" + (amount / item.getAmount()) + ").");
					}
				}
			}

		});
		player.sendMessage("Total value: " + val);
    }

}
