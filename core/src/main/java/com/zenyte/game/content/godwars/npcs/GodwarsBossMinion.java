package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Tommeh | 25 mrt. 2018 : 23:48:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsBossMinion extends SpawnableKillcountNPC implements Spawnable {

	public GodwarsBossMinion(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		setTargetType(EntityType.PLAYER);
		this.setAggressionDistance(30);
		this.radius = 30;
		this.maxDistance = 50;
	}

	private GodwarsBossNPC bossNPC;

	@Override
	public boolean isAcceptableTarget(final Entity target) {
		if (bossNPC != null) {
			if (bossNPC.minionTarget != null) {
				final Entity t = bossNPC.minionTarget.get();
				if (target == t) {
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public void setRespawnTask() {
		if (!isFinished()) {
			reset();
			finish();
		}
		WorldTasksManager.schedule(() -> {
			if (!isFinished()) {
				return;
			}
			//If the boss is still alive, respawn the creature.
			if (!bossNPC.isFinished() && !bossNPC.isDead()) {
				this.spawn();
			}
		}, getRespawnDelay());
	}

	@Override public void handleOutgoingHit(Entity target, Hit hit) {
		super.handleOutgoingHit(target, hit);
		if (hit.getDamage() > 0 && bossNPC != null) {
			bossNPC.setMinionsHitSomeone();
			if (HitType.MELEE.equals(hit.getHitType()) && bossNPC instanceof final KreeArra kree) {
				kree.setDoneMeleeDamage();
			}
		}
	}

	@Override
	public void autoRetaliate(final Entity source) {
		if (combat.getTarget() == source) return;
		if (bossNPC != null) {
			if (bossNPC.minionTarget != null) {
				final Entity t = bossNPC.minionTarget.get();
				if (t != null) {
					return;
				}
			}
		}
		final Entity target = combat.getTarget();
		if (target != null) {
			if (!isProjectileClipped(target, false) && getAttackingDelay() + 1200 > System.currentTimeMillis()) {
				return;
			}
		}
		if (!combat.isForceRetaliate()) {
			if (target != null) {
				if (target instanceof Player) {
					final Player player = (Player) target;
					if (player.getActionManager().getAction() instanceof PlayerCombat) {
						final PlayerCombat combat = (PlayerCombat) player.getActionManager().getAction();
						if (combat.getTarget() == this) {
							return;
						}
					}
				} else {
					final NPC npc = (NPC) target;
					if (npc.getCombat().getTarget() == this) return;
				}
			}
		}
		this.randomWalkDelay = 1;
		resetWalkSteps();
		final Entity previousTarget = combat.getTarget();
		combat.setTarget(source);
		if (previousTarget == null && combat.getCombatDelay() == 0) {
			combat.setCombatDelay(2);
		}
	}

	public int getRespawnDelay() {
		return Utils.random(20, 25);
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public GodType type() {
		return getId() >= 2206 && getId() <= 2208 ? GodType.SARADOMIN : getId() >= 2216 && getId() <= 2218 ? GodType.BANDOS : getId() >= 3163 && getId() <= 3165 ? GodType.ARMADYL : GodType.ZAMORAK;
	}

	@Override
	public boolean validate(final int id, final String name) {
		return (id >= 2216 && id <= 2218) || (id >= 2206 && id <= 2208) || (id >= 3130 && id <= 3132) || (id >= 3163 && id <= 3165);
	}

	public void setBossNPC(GodwarsBossNPC bossNPC) {
		this.bossNPC = bossNPC;
	}
}
