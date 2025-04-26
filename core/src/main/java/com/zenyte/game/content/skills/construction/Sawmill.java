package com.zenyte.game.content.skills.construction;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;

/**
 * @author Tommeh | 15 mei 2018 | 19:51:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class Sawmill extends Action {

	private final Plank plank;
	private final int amount;

	private boolean check(final boolean message) {
		if (!player.getInventory().containsItem(plank.getBase())) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			if (message) {
				player.getDialogueManager().start(new NPCChat(player, 3101, "You'll need to bring me some more logs."));
			}
			return false;
		}
		if (!player.getInventory().containsItem(new Item(995, plank.getCost()))) {
			player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
			if (message) {
				player.getDialogueManager().start(new NPCChat(player, 3101, "Those planks cost " + plank.getCost() + ". You don't have enough<br>money for all of them."));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean start() {
		return check(true);
	}

	@Override
	public boolean process() {
		return true;
	}

    public Sawmill(Plank plank, int amount) {
        this.plank = plank;
        this.amount = amount;
    }

	@Override
	public int processWithDelay() {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		player.getAchievementDiaries().update(VarrockDiary.MAKE_A_PLANK);
		int completed = 0;
		for (int i = 0; i < amount; i++) {
			if (!check(false)) {
				break;
			}
            player.getInventory().deleteItem(new Item(995, plank.getCost()));
            player.getInventory().deleteItem(plank.getBase());
            player.getInventory().addItem(plank.getProduct());
            completed++;
        }
        if (completed >= 20 && plank.equals(Plank.MAHOGANY)) {
            player.getAchievementDiaries().update(VarrockDiary.MAKE_MAHOGANY_PLANKS);
        }
        return -1;
    }

}
