package com.zenyte.game.content.skills.smithing;

import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 21 jul. 2018 | 23:19:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodswordShardCombinationAction extends Action {

	private final GodswordShardCombination combination;
	private int ticks;
	
	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(Smithing.HAMMER)) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a hammer to do this."));
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.SMITHING) < 80) {
			player.getDialogueManager().start(new PlainChat(player, "You need to have a Smithing level of at least 80 to do this."));
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
			player.getDialogueManager().start(new PlainChat(player, "You set to work, trying to fix the ancient sword..."));
			break;
		case 5:
			player.setAnimation(Smithing.ANIMATION);
			break;
		case 9:
			player.getInventory().deleteItemsIfContains(combination.getMaterials(), () -> {
				player.getDialogueManager().start(new ItemChat(player, combination.getItem(),
						"Even for an experienced smith it is not an easy task,<br>but eventually it is done."));
				player.getInventory().addItem(combination.getItem());
				player.getSkills().addXp(SkillConstants.SMITHING, 100);
			});
			return -1;
		}
		return 0;
	}

    public GodswordShardCombinationAction(GodswordShardCombination combination) {
        this.combination = combination;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    @Override
    public boolean interruptedByDialogue() {
        return false;
    }

}
