package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.runecrafting.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.HashSet;

import static com.zenyte.game.content.skills.runecrafting.BasicRunecraftingAction.RUNECRAFTING_ANIM;
import static com.zenyte.game.content.skills.runecrafting.BasicRunecraftingAction.RUNECRAFTING_GFX;

/**
 * @author Kris | 11. nov 2017 : 0:57.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public final class SplitBarkOnAltar implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
		boolean bloodAltar = object.getId() == Runecrafting.BLOOD_RUNE_REAL.getAltarObjectId();

		if(bloodAltar && !player.getSettings().learnedBloodbark() || !bloodAltar && !player.getSettings().learnedSwampbark()) {
			player.getDialogueManager().start(new PlainChat(player, "You haven't learned how to do this yet."));
			return;
		}
		BarkArmor barkArmor = null;
		for (BarkArmor value : BarkArmor.VALUES) {
			if(value.isBlood() == bloodAltar && value.getSplitbarkId() == item.getId()) {
				barkArmor = value;
				break;
			}
		}
		if(barkArmor == null)
			return;

		if(!player.getInventory().containsItem(bloodAltar ? ItemId.BLOOD_RUNE : ItemId.NATURE_RUNE, barkArmor.getRuneCost())) {
			player.getDialogueManager().start(new PlainChat(player, "You need "+barkArmor.getRuneCost()+" "+(bloodAltar ? "Blood runes" : "Nature runes")+
					" to make this."));
			return;
		}
		player.setAnimation(RUNECRAFTING_ANIM);
		player.setGraphics(RUNECRAFTING_GFX);
		player.getInventory().deleteItem(bloodAltar ? ItemId.BLOOD_RUNE : ItemId.NATURE_RUNE, barkArmor.getRuneCost());
		player.getInventory().deleteItem(barkArmor.getSplitbarkId(), 1);
		player.getInventory().addItem(new Item(barkArmor.getItemId(), 1));
		player.sendMessage("You bind the temple's power into your "+item.getName()+".");
		player.getSkills().addXp(SkillConstants.RUNECRAFTING, 25);
	}

	@Override
	public Object[] getItems() {
		final HashSet<Object> list = new HashSet<Object>();
		for (BarkArmor value : BarkArmor.VALUES) {
			if(value.isBlood())
				list.add(value.getSplitbarkId());
		}
		return list.toArray(new Object[0]);
	}

	@Override
	public Object[] getObjects() {

		return new Object[] {Runecrafting.BLOOD_RUNE_REAL.getAltarObjectId(), Runecrafting.NATURE_RUNE.getAltarObjectId()};
	}
}
