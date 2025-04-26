package com.zenyte.plugins.object;

import com.zenyte.game.content.boss.cerberus.CerberusRoom;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;

/**
 * @author Tommeh | 3 mei 2018 | 18:17:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class IronWinchObject implements ObjectAction {
	public static final Animation turnAnimation = new Animation(4506);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final CerberusRoom position = object.getX() == 1291 ? CerberusRoom.WEST : object.getX() == 1328 ? CerberusRoom.EAST : CerberusRoom.NORTH;
		if (optionId == 1) {
			player.lock();
			WorldTasksManager.schedule(new TickTask() {
				@Override
				public void run() {
					if (ticks == 0) {
						player.setAnimation(turnAnimation);
					} else if (ticks == 2) {
						new FadeScreen(player, () -> player.setLocation(position.getExit())).fade(2);
						stop();
					}
					ticks++;
				}
			}, 0, 1);
		} else if (optionId == 2) {
			final int count = CharacterLoop.find(position.getWesternFireLocation(), 25, Player.class, __ -> true).size();
			player.getDialogueManager().start(new NPCChat(player, 5870, count == 0 ? "No adventurers are inside the cave." : (count + (count == 1 ? " adventurer is" : " adventurers are") + " inside the cave.")));
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Iron winch"};
	}
}
