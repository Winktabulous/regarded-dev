package com.zenyte.game.content.boss.nightmare.object;

import com.zenyte.game.content.boss.nightmare.area.NightmareBossArea;
import com.zenyte.game.content.boss.nightmare.area.PhosaniInstance;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public class NightmareEnergyBarrier implements ObjectAction {

	private static Location LEAVE_LOCATION = new Location(3808, 9755, 1);
	private static Location LEAVE_LOCATION_PHOSANIS = new Location(3808, 9779, 1);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Are you sure you want to leave?", new DialogueOption("Yes.", () -> {
					player.lock();
					player.getPacketDispatcher().sendClientScript(2893, 41549825, 41549826, 39504, 13109328, -1, -1);
					player.blockIncomingHits(6);
					WorldTasksManager.schedule(() -> {
						player.unlock();
						if (player.getArea() instanceof PhosaniInstance) {
							player.teleport(LEAVE_LOCATION_PHOSANIS);
						} else {
							player.teleport(LEAVE_LOCATION);
						}
						player.setAnimation(NightmareBossArea.ENTER_ANIMATION_END);
						player.getPacketDispatcher().sendClientScript(2894, 41549825, 41549826, -1, -1);
					}, 2);
				}), new DialogueOption("No."));
			}
		});
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {ObjectId.ENERGY_BARRIER_37730};
	}

}
