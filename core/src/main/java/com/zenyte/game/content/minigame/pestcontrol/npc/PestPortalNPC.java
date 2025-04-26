package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlGameType;
import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.content.minigame.pestcontrol.PestPortal;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 26. juuni 2018 : 19:06:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PestPortalNPC extends PestNPC {
	public PestPortalNPC(final PestControlInstance instance, final PestPortal portal) {
		super(instance, null, portal.getProtectedId(), instance.getLocation(portal.getTile()));
		this.portal = portal;
		combatDefinitions.setHitpoints(instance.getType() == PestControlGameType.NOVICE ? 200 : 250);
		setHitpoints(instance.getType() == PestControlGameType.NOVICE ? 200 : 250);
	}

	private final PestPortal portal;

	/**
	 * Gets whether the shield has been dropped or not.
	 * 
	 * @return whether the portal's shield is dropped or not.
	 */
	public boolean isShieldDropped() {
		return id == portal.getId();
	}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		return false;
	}

	/**
	 * Disables the shield on the portal, thus making it attackable.
	 */
	public void disableShield() {
		setTransformation(portal.getId());
	}

	@Override
	public void setTransformation(final int id) {
		nextTransformation = id;
		setId(id);
		size = definitions.getSize();
		updateFlags.flag(UpdateFlag.TRANSFORMATION);
	}

	@Override
	public boolean checkProjectileClip(final Player player, boolean melee) {
		return false;
	}

	@Override
	public void processNPC() {
	}

	@Override
	public void sendDeath() {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		setAnimation(null);
		final List<PestPortal> available = instance.getAvailablePortals();
		available.remove(portal);
		WorldTasksManager.schedule(new WorldTask() {
			private int loop;
			@Override
			public void run() {
				if (loop == 0) {
					setAnimation(defs.getDeathAnim());
				} else if (loop == deathDelay) {
					if (available.isEmpty()) {
						instance.finish(true);
					}
					instance.getVoidKnight().heal(50);
					finish();
					stop();
					return;
				}
				loop++;
			}
		}, 0, 1);
	}
}
