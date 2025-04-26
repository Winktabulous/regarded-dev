package com.zenyte.game.content.minigame.warriorsguild.magicalanimator;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 16. dets 2017 : 2:44.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MagicalAnimator {

	private static final Animation PLACE_ANIM = new Animation(827);
	public static final int[][] ARMOUR_SETS = { 
			{ 1155, 1117, 1075 }, { 1153, 1115, 1067 }, { 1157, 1119, 1069 },
			{ 1165, 1125, 1077 }, { 1159, 1121, 1071 }, { 1161, 1123, 1073 }, 
			{ 1163, 1127, 1079 } };
	
	/**
	 * Handles the magical animator.
	 * @param player
	 * @param item
	 * @param object
	 */
	public static void handleAnimator(final Player player, final Item item, final WorldObject object) {
		if (player.getTemporaryAttributes().get("animatedArmour") != null) {
			player.sendMessage("You've already summoned an animated armour.");
			return;
		}
		final int index = getSet(item.getId());
		if (index == -1) {
			return;
		}
		final int[] set = ARMOUR_SETS[index];
		for (final int i : set) {
			if (!player.getInventory().containsItem(i, 1)) {
				player.sendMessage("You need a set of full helm, platebody and platelegs to animate the magical animator.");
				return;
			}
		}
		for (final int i : set) {
			player.getInventory().deleteItem(i, 1);
		}
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			private AnimatedArmour armour;
			@Override
			public void run() {
				switch(ticks++) {
				case 0:
					player.setAnimation(PLACE_ANIM);
					player.getDialogueManager().start(new PlainChat(player, "You place your armour on the platform where it disappears....", false));
					break;
				case 3:
					player.getDialogueManager().start(new PlainChat(player, "The animator hums, something appears to be working. You stand back...", false));
					player.setRunSilent(true);
					player.addWalkSteps(player.getX(), player.getY() + 2, 2);
					break;
				case 6:
					armour = new AnimatedArmour(2450 + index, new Location(object), player);
					armour.spawn();
					armour.setAttackedBy(player);
					final long currentTick = WorldThread.getCurrentCycle();
					armour.setAttackedTick(currentTick);
					armour.setAttackedByDelay(currentTick + 50);
					player.getPacketDispatcher().sendHintArrow(new HintArrow(armour));
					player.getTemporaryAttributes().put("animatedArmour", armour);
					player.faceObject(object);
					player.setRunSilent(false);
					player.unlock();
					player.getDialogueManager().finish();
					break;
				case 7:
					armour.addWalkSteps(armour.getX(), armour.getY() + 1, 1, false);
					break;
				case 8:
					armour.getCombat().setTarget(player);
					stop();
					return;
				}
			}
			
		}, 0, 0);
	}
	
	/**
	 * Gets the index of the armour set used based off of the piece used.
	 * @param piece piece of the armour you wish to get the index of.
	 * @return index of the armour set from ARMOUR_SETS array.
	 */
	private static int getSet(final int piece) {
		int index = -1;
		for (final int[] sets : ARMOUR_SETS) {
			index++;
			for (final int pieces : sets) {
				if (pieces == piece) {
					return index;
				}
			}
		}
		return index;
	}
	
	
}
