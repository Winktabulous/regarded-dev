package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.actions.FadeScreenAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.interfaces.ZeahStatueScroll;

public class KourendStatueObject implements ObjectAction {

	private static final Animation READ = new Animation(2171);
	private static final Animation CROUCH = new Animation(827);
	private static final Location ROPE = new Location(1666, 10050, 0);
	private static final Item FEE = new Item(995, 200_000);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.faceObject(object);
		
		if(option.equalsIgnoreCase("read")) {
			player.setAnimation(READ);
			WorldTasksManager.schedule(() -> { ZeahStatueScroll.open(player); }, 0);
			return;
		}
		
		if(option.equalsIgnoreCase("enter")) {
			enterCatacombs(player, player.isIronman() ? CatacombsType.IRON : CatacombsType.PUBLIC);
		}

		if(option.equalsIgnoreCase("enter-paid")) {
			if(player.getBooleanAttribute("catacombs-pay-always")) {
				if(player.getInventory().containsItem(FEE)) {
					player.getInventory().deleteItem(FEE);
					enterCatacombs(player, player.isIronman() ? CatacombsType.IRON_PRIVATE : CatacombsType.PRIVATE);
				} else if (player.getBank().containsItem(FEE)) {
					player.getBank().getContainer().remove(FEE);
					enterCatacombs(player, player.isIronman() ? CatacombsType.IRON_PRIVATE : CatacombsType.PRIVATE);
				}
			}
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					options("Pay 200,000 GP to enter a shared private instance?",
							"Yes",
							"Yes (never show again)",
							"No")
							.onOptionOne(() -> enterPrivate(player, false))
							.onOptionTwo(() -> enterPrivate(player, true))
							.onOptionThree(() -> player.getDialogueManager().finish());
				}
			});

		}
	}

	private void enterPrivate(Player player, boolean setNeverAgain) {
		if(setNeverAgain)
			player.getAttributes().put("catacombs-pay-always", true);

		if(player.getInventory().containsItem(FEE)) {
			player.getInventory().deleteItem(FEE);
			enterCatacombs(player, player.isIronman() ? CatacombsType.IRON_PRIVATE : CatacombsType.PRIVATE);
		} else {
			player.sendMessage("You lack the required funds to enter the paid instance");
		}

	}

	private void enterCatacombs(Player player, CatacombsType type) {
		player.lock();
		player.setAnimation(CROUCH);
		player.sendMessage("You investigate what looks like hinges on the plaque and find it opens.");

		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;

			@Override
			public void run() {
				if(ticks == 0) {
					new FadeScreenAction(player, 2).run();
					player.sendMessage((type == CatacombsType.PRIVATE || type == CatacombsType.IRON_PRIVATE) ? "You climb down and find yourself in a more private area." : "You climb down the hole.");
				} if(ticks == 2)
					player.setLocation(new Location(ROPE.getX(), ROPE.getY() + type.offset, 0));
				else if(ticks == 3) {
					player.unlock();
					stop();
				}

				ticks++;
			}

		}, 0, 0);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { 27785 };
	}

	private enum CatacombsType {
		PUBLIC(0),
		PRIVATE(320),
		IRON(576),
		IRON_PRIVATE(832);

		public int offset;
		CatacombsType(int offset) {
			this.offset = offset;
		}
	}
}
