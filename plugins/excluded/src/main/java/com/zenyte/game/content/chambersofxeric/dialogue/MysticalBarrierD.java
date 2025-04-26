package com.zenyte.game.content.chambersofxeric.dialogue;

import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.content.chambersofxeric.greatolm.scripts.TransitionalFallingCrystals;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14. jaan 2018 : 3:35.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class MysticalBarrierD extends Dialogue {

	public MysticalBarrierD(final Player player, final OlmRoom room, final WorldObject barrier) {
		super(player);
		this.room = room;
		this.barrier = barrier;
	}
	
	private final OlmRoom room;
	private final WorldObject barrier;

	@Override
	public void buildDialogue() {
		plain("You sense that strange magic will prevent you from coming back through. "
				+ "Make sure you are prepared for what is ahead.");
		options("Go through?", new DialogueOption("Yes", () -> passBarrier(player, room, barrier)), new DialogueOption("No"));
	}
	
	public static final void passBarrier(final Player player, final OlmRoom room, final WorldObject barrier) {
		player.lock(2);
		if (!room.getRaid().isCompleted()) {
            player.sendMessage("As you pass through the barrier, a sense of dread washes over you.");
        }
		player.setRunSilent(2);
		player.addWalkSteps(player.getX(), player.getY() + (player.getY() < barrier.getY() ? 2 : -2), 2, false);
		if (room.getOlm() != null && room.getOlm().getScripts().contains(TransitionalFallingCrystals.class)) {
			player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, (byte) 5, (byte) 0, (byte) 0);
		}
		if (room.getHeadObject() == null) {
		    room.calculateRemainingCombatPoints();
			room.rise();
		}
        WorldTasksManager.schedule(() -> player.getMusic().unlock(Music.get("Fire in the Deep")), 2);
	}

}
