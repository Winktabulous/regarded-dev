package com.zenyte.game.content.area.taverley;

import com.zenyte.game.content.boss.cerberus.area.CerberusLairInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

import java.util.Objects;

import static com.zenyte.game.content.skills.slayer.BossTask.CERBERUS;
import static com.zenyte.game.content.skills.slayer.RegularTask.HELLHOUNDS;

/**
 * @author Tommeh | 3 mei 2018 | 17:52:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class KeyMasterD extends Dialogue {

	private static final int COST = 150_000;

	public KeyMasterD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		var task = player.getSlayer().getAssignment();
		if (Objects.isNull(task) || Objects.isNull(task.getTask())) {
			plain("You can only create an instance when you have a hellhound or Cerberus task.");
			return;
		}
		if (task.getTaskName().equals("HELLHOUNDS")) {
			offerInstance(player);
			return;
		}
		else if (task.getTaskName().equals("CERBERUS")) {
			offerInstance(player);
			return;
		}
		player.sendMessage("You can only create an instance when you have a hellhound or Cerberus task.");
	}

	private void offerInstance(Player player) {
		options("Would you like to create a personal instance for 150,000 GP?",
			new DialogueOption("Yes", () -> {
				final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
				final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
				if ((long) amountInBank + amountInInventory >= COST) {
					player.lock(1);
					player.getInventory().deleteItem(new Item(ItemId.COINS_995, COST))
						.onFailure(remainder -> player.getBank().remove(remainder));
					player.sendMessage("Please wait a few moments as your instance is being constructed.");
					try {
						final AllocatedArea area = MapBuilder.findEmptyChunk(6, 8);
						final CerberusLairInstance instance = new CerberusLairInstance(player, area);
						instance.constructRegion();
					} catch (OutOfSpaceException e) {
						e.printStackTrace(System.err);
					}
					return;
				}
				setKey(50);
			}), new DialogueOption("No."));
		plain(50, "You don't have enough coins with you or in your bank.");
	}

}
