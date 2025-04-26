package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.skills.runecrafting.CombinationRunecrafting;
import com.zenyte.game.content.skills.runecrafting.CombinationRunecraftingAction;
import com.zenyte.game.content.skills.runecrafting.Runecrafting;
import com.zenyte.game.content.skills.runecrafting.Tiara;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Kris | 11. nov 2017 : 0:57.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 */
public final class TalismanItemOnRunecraftingAltarAction implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
		final Runecrafting rune = Runecrafting.getRuneByAltarObject(object.getId());
		if (rune == null) {
			return;
		}
		if (player.getInventory().containsItem(5525, 1)) {
			final Tiara tiara = Tiara.getTiara(player, item, object.getId());
			if (tiara != null) {
				player.getDialogueManager().start(new OptionDialogue(player, "Do you want to enchant the tiara?", new String[] {"Yes.", "No."}, new Runnable[] {() -> {
					if (tiara.equals(Tiara.MIND)) {
						player.getAchievementDiaries().update(FaladorDiary.MAKE_MIND_TIARA);
					}
					player.getInventory().deleteItem(tiara.getTalisman(), 1);
					player.getInventory().deleteItem(5525, 1);
					player.getInventory().addItem(new Item(tiara.getId()));
					player.getSkills().addXp(SkillConstants.RUNECRAFTING, tiara.getExperience());
					player.sendMessage("You bind the power of the talisman into your tiara.");
				}, null}));
			} else {
				player.sendMessage("Nothing interesting happens.");
			}
			return;
		}
		for (final CombinationRunecrafting cRune : CombinationRunecrafting.VALUES) {
			if (cRune.getObjectId() == object.getId() && (item.getId() == cRune.getTalismanId() || (item.getId() == cRune.getRequiredRuneId() && player.getVariables().getTime(TickVariable.MAGIC_IMBUE) > 0) || (item.getId() == cRune.getRequiredRuneId() && player.getEquipment().getId(EquipmentSlot.AMULET) == 5521))) {
				player.getActionManager().setAction(new CombinationRunecraftingAction(cRune));
				return;
			}
		}
		player.sendMessage("Nothing interesting happens.");
	}

	@Override
	public Object[] getItems() {
		final HashSet<Object> list = new HashSet<Object>();
		for (int i = 1438; i <= 1456; i += 2) {
			list.add(i);
		}
		list.add(5525);
		list.add(22118);
		for (final CombinationRunecrafting crune : CombinationRunecrafting.VALUES) {
			list.add(crune.getRequiredRuneId());
		}
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (final Runecrafting r : Runecrafting.VALUES) {
			if (r.getAltarObjectId() == -1) {
				continue;
			}
			list.add(r.getAltarObjectId());
		}
		return list.toArray(new Object[list.size()]);
	}
}
