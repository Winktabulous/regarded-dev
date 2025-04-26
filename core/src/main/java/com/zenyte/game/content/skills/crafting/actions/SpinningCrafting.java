package com.zenyte.game.content.skills.crafting.actions;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.skills.crafting.CraftingDefinitions.SpinningData;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 27 aug. 2018 | 19:02:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class SpinningCrafting extends Action {
	public static final Animation ANIMATION = new Animation(896);
	private static final Animation OBJECT_ANIMATION = new Animation(466);
	private final SpinningData data;
	private final WorldObject object;
	private final int amount;
	private int cycle;
	private Item material;

	@Override
	public boolean start() {
		if (player.getSkills().getLevel(SkillConstants.CRAFTING) < data.getLevel()) {
			player.getDialogueManager().start(new PlainChat(player, "You need a Crafting level to at least " + data.getLevel() + " to spin that."));
			return false;
		}
		for (final Item materials : data.getMaterials()) {
			if (!player.getInventory().containsItem(materials)) {
				if (!data.equals(SpinningData.CROSSBOW_STRING)) {
					player.sendMessage("You don't have any " + data.getMaterials()[0].getDefinitions().getName().toLowerCase() + ".");
				} else {
					for (final Item item : data.getMaterials()) {
						if (player.getInventory().containsItem(item)) {
							material = new Item(item.getId());
							return true;
						}
					}
					if (material == null) {
						player.sendMessage("You don't have the right items to spin a crossbow string.");
					}
				}
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean process() {
		if (!player.getInventory().containsItem(data.equals(SpinningData.CROSSBOW_STRING) ? material : data.getMaterials()[0])) {
			return false;
		}
		return cycle < amount;
	}

	@Override
	public int processWithDelay() {
		if (data.equals(SpinningData.CROSSBOW_STRING)) {
			player.getAchievementDiaries().update(ArdougneDiary.MAKE_RUNE_CROSSBOW, 8);
		} else if (data.equals(SpinningData.BOW_STRING)) {
			player.getAchievementDiaries().update(KandarinDiary.CREATE_YEW_LONGBOW, 1);
		}
		World.sendObjectAnimation(object, OBJECT_ANIMATION);
		player.setAnimation(ANIMATION);
		player.getSkills().addXp(SkillConstants.CRAFTING, data.getXp());
		player.getInventory().deleteItem(data.equals(SpinningData.CROSSBOW_STRING) ? material : data.getMaterials()[0]);
		player.getInventory().addItem(data.getProduct());
		cycle++;
		return 4;
	}

	public SpinningCrafting(SpinningData data, WorldObject object, int amount) {
		this.data = data;
		this.object = object;
		this.amount = amount;
	}
}
