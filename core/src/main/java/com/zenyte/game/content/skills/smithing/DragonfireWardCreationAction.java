package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 22 jul. 2018 | 17:18:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class DragonfireWardCreationAction extends Action {

	private static final Item ANTI_DRAGON_SHIELD = new Item(1540);
	private static final Item SKELETAL_VISAGE = new Item(22006);
	private static final Item DRAGONFIRE_WARD = new Item(22003);

	private int ticks;

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(Smithing.HAMMER)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a hammer to do this."));
			return false;
		}
		if (!player.getInventory().containsItem(ANTI_DRAGON_SHIELD)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have an anti-dragon-shield to attach the visage onto."));
			return false;
		}
		if (!player.getInventory().containsItem(SKELETAL_VISAGE)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a skeletal visage so it can be attached on a shield."));
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < 90) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Smithing level of at least 90 to do this."));
			return false;
		}
		return true;
	}

	@Override
	public boolean process() {
		return true;
	}

	@Override
	public int processWithDelay() {
		switch (ticks++) {
		case 0:
			player.setAnimation(Smithing.ANIMATION);
			player.getDialogueManager().start(new ItemChat(player, SKELETAL_VISAGE, "You set to work trying to attach the ancient skeletal visage to your anti-dragonbreath shield. It's not easy to work with the ancient artifact and it takes all of your skill as a master smith."));
			break;
		case 5:
			player.setAnimation(Smithing.ANIMATION);
			break;
		case 9:
			player.getDialogueManager().start(new ItemChat(player, DRAGONFIRE_WARD, "Even for an expert armourer it is not an easy task,<br>but eventually it is ready. You have crafted the<br>draconic visage and anti-dragonbreath shield into a<br>dragonfire shield."));
			player.getInventory().deleteItem(ANTI_DRAGON_SHIELD);
			player.getInventory().deleteItem(SKELETAL_VISAGE);
			player.getInventory().addItem(DRAGONFIRE_WARD);
			player.getSkills().addXp(SkillConstants.SMITHING, 2000);
			return -1;
		}
		return 0;
	}

	@Override
	public boolean interruptedByDialogue() {
		return false;
	}

}
