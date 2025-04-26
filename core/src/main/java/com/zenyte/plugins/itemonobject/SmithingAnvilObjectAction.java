package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.skills.BluriteSmithingD;
import com.zenyte.plugins.interfaces.SmithingInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11. nov 2017 : 0:43.52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public final class SmithingAnvilObjectAction implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		final int tier = SmithingInterface.getTierForBar(item);
		if (item.getId() == Smithing.HAMMER.getId()) {
			player.sendMessage("To smith a metal bar, you can click on the anvil while you have the bar in your inventory.");
		} else if (Smithing.isBar(item) && !player.getInventory().containsItems(Smithing.HAMMER)) {
			player.getDialogueManager().start(new PlainChat(player, "You need a hammer to work the metal with."));
		} else if (player.getSkills().getLevel(SkillConstants.SMITHING) < Smithing.BAR_LEVELS[tier - 1]) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Smithing level of at least " + Smithing.BAR_LEVELS[tier - 1] + " to work " + item.getDefinitions().getName().toLowerCase() + "s."));
		} else {
			if (item.getId() == 9467) {
				player.getDialogueManager().start(new BluriteSmithingD(player, object.getId()));
			} else {
				SmithingInterface.openInterface(player, tier, object.getId());
			}
		}
	}

	@Override
	public Object[] getItems() {
		final List<Object> list = new ArrayList<Object>();
		for (Item i : Smithing.BARS) {
			list.add(i.getId());
		}
		list.add(Smithing.HAMMER.getId());
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Anvil"};
	}
}
