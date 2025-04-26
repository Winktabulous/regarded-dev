package com.zenyte.game.world.entity.player.controller;

import com.google.gson.annotations.Expose;
import com.zenyte.game.content.consumables.edibles.Food;
import com.zenyte.game.content.skills.magic.Magic.TeleportType;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 22. okt 2017 : 17:49.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class ControllerManager {
	private static final Logger log = LoggerFactory.getLogger(ControllerManager.class);

	public ControllerManager(final Player player) {
		this.player = player;
	}

	private final transient Player player;
	private transient Controller controller;
	@Expose
	private String lastController;
	@Expose
	private Object[] arguments;

	public void initalize(final ControllerManager manager) {
		lastController = manager.lastController;
		arguments = manager.arguments;
	}

	public void startController(final Controller controller, final Object... parameters) {
		if (this.controller != null) {
			if (this.controller.getPlayer() != null) {
				this.controller.forceStop();
			}
		}
		this.controller = controller;
		lastController = controller.getClass().getName();
		arguments = parameters;
		controller.setPlayer(player);
		controller.start();
	}

	public void login() {
		if (lastController == null) {
			return;
		}
		try {
			final Object controllerClass = Class.forName(lastController).newInstance();
			if (controllerClass == null) {
				return;
			}
			if (!(controllerClass instanceof Controller)) {
				return;
			}
			controller = (Controller) controllerClass;
			controller.setPlayer(player);
			if (controller.removeOnLogin()) {
				forceStop();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error("", e);
			lastController = null;
		}
	}

	/**
	 * Hiding errors on login to avoid spamming.
	 * Certain controllers will throw an error if the game is 'terminated' 
	 * while they're in the controller.
	 */
	public void logout() {
		if (controller == null) {
			return;
		}
		if (controller.removeOnLogout()) {
			forceStop();
		}
	}

	public void forceStop() {
		if (controller != null) {
			controller.forceStop();
			controller = null;
		}
		lastController = null;
		arguments = null;
	}

	public void process() {
		if (controller == null) {
			return;
		}
		controller.process();
	}

	public boolean processTeleport(final Location location, final TeleportType type) {
		if (controller == null) {
			return true;
		}
		return controller.processTeleport(location, type);
	}

	public void finishTeleport() {
		if (controller == null) {
			return;
		}
		controller.finishTeleport();
	}

	public boolean processObjectClick1(final WorldObject object) {
		if (controller == null) {
			return true;
		}
		return controller.processObjectClick1(object);
	}

	public boolean processObjectClick2(final WorldObject object) {
		if (controller == null) {
			return true;
		}
		return controller.processObjectClick2(object);
	}

	public boolean processObjectClick3(final WorldObject object) {
		if (controller == null) {
			return true;
		}
		return controller.processObjectClick3(object);
	}

	public boolean processObjectClick4(final WorldObject object) {
		if (controller == null) {
			return true;
		}
		return controller.processObjectClick4(object);
	}

	public boolean processObjectClick5(final WorldObject object) {
		if (controller == null) {
			return true;
		}
		return controller.processObjectClick5(object);
	}

	public boolean processNPCClick1(final NPC npc) {
		if (controller == null) {
			return true;
		}
		return controller.processNPCClick1(npc);
	}

	public boolean processNPCClick2(final NPC npc) {
		if (controller == null) {
			return true;
		}
		return controller.processNPCClick2(npc);
	}

	public boolean processNPCClick3(final NPC npc) {
		if (controller == null) {
			return true;
		}
		return controller.processNPCClick3(npc);
	}

	public boolean processNPCClick4(final NPC npc) {
		if (controller == null) {
			return true;
		}
		return controller.processNPCClick4(npc);
	}

	public boolean processNPCClick5(final NPC npc) {
		if (controller == null) {
			return true;
		}
		return controller.processNPCClick5(npc);
	}

	public boolean sendDeath(final Entity source) {
		if (controller == null) {
			return true;
		}
		return controller.sendDeath(source);
	}

	public boolean removeOnLogin() {
		if (controller == null) {
			return true;
		}
		return controller.removeOnLogin();
	}

	public boolean removeOnLogout() {
		if (controller == null) {
			return true;
		}
		return controller.removeOnLogout();
	}

	/**
	 * This method is ran through addWalkSteps method so it is ran instantly for
	 * the full path upon execution.
	 * @param direction
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canMove(final int direction, final int x, final int y) {
		if (controller == null) {
			return true;
		}
		return controller.canMove(direction, x, y);
	}

	/**
	 * This method is ran piece by piece as the player moved forward,
	 * the variables in arguments are the coordinates to the next moved location.
	 * @param primaryTile - whether the movement was primary or not.
	 * Primary means that in the case of walking, the movement is always primary,
	 * however in the case of running, only the second movement tile is primary.
	 * The one that's done in the middle and practically skipped over instantly
	 * isn't considered as primary.
	 * @param x
	 * @param y
	 */
	public void move(final boolean primaryTile, final int x, final int y) {
		if (controller == null) {
			return;
		}
		controller.move(primaryTile, x, y);
	}

	/**
	 * This method is executed whenever setNextWorldTile method is called upon the player.
	 */
	public void teleport(final Location location) {
		if (controller == null) {
			return;
		}
		controller.teleport(location);
	}

	public boolean canDropItem(final Item item) {
		if (controller == null) {
			return true;
		}
		return controller.canDropItem(item);
	}

	public boolean canEatFood(final Food food) {
		if (controller == null) {
			return true;
		}
		return controller.canEatFood(food);
	}

	public boolean canUsePrayer(final Prayer prayer) {
		if (controller == null) {
			return true;
		}
		return controller.canUsePrayer(prayer);
	}

	public boolean canButtonClick(final int interfaceId, final int componentId, final int slotId) {
		if (controller == null) {
			return true;
		}
		return controller.canButtonClick(interfaceId, componentId, slotId);
	}

	public boolean canAttack(final Entity entity) {
		if (controller == null) {
			return true;
		}
		return controller.canAttack(entity);
	}

	public boolean canHit(final Entity entity) {
		if (controller == null) {
			return true;
		}
		return controller.canHit(entity);
	}

	public boolean canEquipItem(final Item item, final int slot) {
		if (controller == null) {
			return true;
		}
		return controller.canEquipItem(item, slot);
	}

	public boolean canLeaveClanChannel() {
		if (controller == null) {
			return true;
		}
		return controller.canLeaveClanChannel();
	}

	/*public void processOutgoingHit(final Entity target, final Hit hit) {
		if (controller == null) {
			return;
		}
		controller.processOutgoingHit(target, hit);
	}*/
	public boolean processPlayerCombat(final Entity entity, final String style) {
		if (controller == null) {
			return true;
		}
		return controller.processPlayerCombat(entity, style);
	}

	public boolean processItemOption(final Player player, final int itemId, final int slotId, final String option) {
		if (controller == null) {
			return true;
		}
		return controller.processItemOption(player, itemId, slotId, option);
	}

	public Controller getController() {
		return this.controller;
	}

	public String getLastController() {
		return this.lastController;
	}

	public void setLastController(final String lastController) {
		this.lastController = lastController;
	}

	public Object[] getArguments() {
		return this.arguments;
	}

	public void setArguments(final Object[] arguments) {
		this.arguments = arguments;
	}
}
