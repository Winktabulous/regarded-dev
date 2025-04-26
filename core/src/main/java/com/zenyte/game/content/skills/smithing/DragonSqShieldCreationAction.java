package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 22 jul. 2018 | 00:12:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class DragonSqShieldCreationAction extends Action {

	private static final Item SHIELD_LEFT_HALF = new Item(2366);
	private static final Item SHIELD_RIGHT_HALF = new Item(2368);
	private static final Item DRAGON_SQ_SHIELD = new Item(1187);
	
	private int ticks;

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(Smithing.HAMMER)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a hammer to do this."));
			return false;
		}
		if (!player.getInventory().containsItem(SHIELD_LEFT_HALF)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a shield left half to attach the other half to."));
			return false;
		}
		if (!player.getInventory().containsItem(SHIELD_RIGHT_HALF)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a shield right half to attach the other half to."));
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < 60) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Smithing level of at least 60 to do this."));
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
			player.getDialogueManager().start(new PlainChat(player, "You set to work trying to fix the ancient shield. It's seen some<br><br>heavy action and needs some serious work doing it."));
			break;
		case 5:
			player.setAnimation(Smithing.ANIMATION);
			break;
		case 9:
			player.getAchievementDiaries().update(ArdougneDiary.SMITH_DRAGON_SQ_SHIELD);
			player.getDialogueManager().start(new PlainChat(player, "Even for an experienced armourer it is not an easy task, but<br><br>eventually it is ready. You have restored the dragon square shield to<br><br>its former glory."));
			player.getInventory().deleteItem(SHIELD_LEFT_HALF);
			player.getInventory().deleteItem(SHIELD_RIGHT_HALF);
			player.getInventory().addItem(DRAGON_SQ_SHIELD);
			player.getSkills().addXp(SkillConstants.SMITHING, 75);
			return -1;
		}
		return 0;
	}

	@Override
	public boolean interruptedByDialogue() {
		return false;
	}

}
