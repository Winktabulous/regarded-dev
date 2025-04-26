package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.CombatScriptsHandler;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;

/**
 * @author Kris | 28. juuni 2018 : 19:20:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ShifterNPC extends PestNPC {
	private static final Graphics TELEPORT_GFX = new Graphics(654);

	public ShifterNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
		forceAggressive = true;
		aggressionDistance = 10;
		combat = new ShifterCombat(this);
		getCombatDefinitions().setAttackStyle("Ranged");
		attackDistance = 0;
	}

	private long teleportDelay;

	private void teleport() {
		final boolean platform = Utils.random(2) == 0;
		int attempts = 50;
		while (--attempts > 0) {
			final Location tile = instance.getLocation(platform ? PestControlUtilities.getRandomPlatformLocation() : PestControlUtilities.getRandomShifterLocation());
			if (tile.withinDistance(getLocation(), 3)) {
				continue;
			}
			if (World.isTileFree(tile, getSize())) {
				final Location currentTile = new Location(getX(), getY() - 1, getPlane());
				World.sendGraphics(TELEPORT_GFX, currentTile);
				World.sendGraphics(TELEPORT_GFX, new Location(tile.getX(), tile.getY() + 1, tile.getPlane()));
				setLocation(tile);
				resetWalkSteps();
				teleportDelay = Utils.currentTimeMillis() + 10000;
				return;
			}
		}
	}

	@Override
	public void processNPC() {
		if (!isUnderCombat() && getCombat().getTarget() == null) {
			if (Utils.random(3) == 0) {
				if (teleportDelay < Utils.currentTimeMillis()) {
					teleport();
					return;
				}
			}
		}
		super.processNPC();
	}


	private final class ShifterCombat extends NPCCombat {
		public ShifterCombat(final NPC npc) {
			super(npc);
		}

		@Override
		public int combatAttack() {
			final Entity target = this.target;
			if (target == null) {
				return 0;
			}
			int maxDistance = npc.getAttackDistance();
			final boolean melee = npc.getCombatDefinitions().isMelee();
			if (npc.isForceFollowClose() || melee) {
				maxDistance = 0;
			}
			if (npc.isProjectileClipped(target, false)) {
				return 0;
			}
			final int size = npc.getSize();
			if (!Utils.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize(), maxDistance)) {
				return 0;
			}
			if (CollisionUtil.collides(npc.getX(), npc.getY(), size, target.getX(), target.getY(), target.getSize())) {
				return 0;
			}
			addAttackedByDelay(target);
			return CombatScriptsHandler.specialAttack(npc, target);
		}

		@Override
		public boolean checkAll() {
			final Entity target = this.target;
			if (target == null) {
				return false;
			}
			if (npc.isDead() || npc.isFinished() || npc.isLocked() || target.isDead() || target.isFinished() || npc.getPlane() != target.getPlane()) {
				return false;
			}
			if (isFrozen()) {
				return true;
			}
			int distanceX;
			int distanceY;
			final int size = npc.getSize();
			int maxDistance = npc.getMaxDistance();
			distanceX = target.getX() - npc.getX();
			distanceY = target.getY() - npc.getY();
			if (distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
				npc.forceWalkRespawnTile();
				return false;
			}
			if (!target.isMultiArea()) {
				if (npc.getAttackedBy() != target && npc.getAttackedByDelay() > WorldThread.getCurrentCycle()) {
					return false;
				}
				final Entity attackedBy = target.getAttackedBy();
				if (attackedBy != null && attackedBy != npc && !attackedBy.isDead() && !attackedBy.isFinished() && target.getAttackedByDelay() > WorldThread.getCurrentCycle()) {
					return false;
				}
			}
			final int targetSize = target.getSize();
			if (!target.hasWalkSteps() && CollisionUtil.collides(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize)) {
				npc.setRouteEvent(new NPCEntityEvent(npc, new EntityStrategy(target)));
				return true;
			}
			final boolean melee = npc.getCombatDefinitions().isMelee();
			maxDistance = npc.isForceFollowClose() || melee ? 0 : npc.getAttackDistance();
			npc.resetWalkSteps();
			if (npc.isProjectileClipped(target, maxDistance == 0) || (!Utils.isOnRange(npc.getX(), npc.getY(), size, target.getX(), target.getY(), targetSize, maxDistance))) {
				if (!npc.calcFollow(target, npc.isRun() ? 2 : 1, true, npc.isIntelligent(), npc.isEntityClipped()) && getCombatDelay() < 3 && melee) {
					setCombatDelay(3);
				}
				return true;
			}
			return true;
		}
	}
}
