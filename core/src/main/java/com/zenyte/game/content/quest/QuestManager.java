package com.zenyte.game.content.quest;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import mgi.types.config.DBRowDefinition;

/**
 * Currently static class, just to unlock all the quests. Will be turned
 * into player-based quest manager once we start adding quests.
 * @author Kris | 23. veebr 2018 : 2:38.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public final class QuestManager {

	@Listener(type = ListenerType.LOBBY_CLOSE)
	public void unlock(final Player player) {
		final VarManager vars = player.getVarManager();
		vars.sendVarInstant(145, 7);
		vars.sendVarInstant(299, 1048576);
		vars.sendVarInstant(302, 61);
		vars.sendBitInstant(821, 1);
		vars.sendBitInstant(1391, 2047);
		vars.sendBitInstant(395, 26);
		vars.sendBitInstant(1908, 1);
		vars.sendBitInstant(598, 3);
		vars.sendBitInstant(340, 2);
		// frozen door quest
		vars.sendBitInstant(13175, 10);
		for (final Quest quest : Quest.values) {
			int varId = quest.getVariable();
			DBRowDefinition dbRowDefinition = DBRowDefinition.get(quest.getDbTableIndex());
			final int questFinishStage = (int) dbRowDefinition.getValueFromRow(17, 0);
			if (quest.isVarbit()) {
				int currentValue = vars.getBitValue(varId);
				if (currentValue < questFinishStage) {
					vars.sendBitInstant(varId, questFinishStage);
				}
			} else {
				int currentValue = vars.getValue(varId);
				if (currentValue < questFinishStage) {
					vars.sendVarInstant(varId, questFinishStage);
				}
			}
		}
	}

}
