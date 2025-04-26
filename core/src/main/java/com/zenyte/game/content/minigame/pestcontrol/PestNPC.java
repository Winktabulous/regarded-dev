package com.zenyte.game.content.minigame.pestcontrol;

import com.zenyte.game.content.minigame.pestcontrol.npc.PestPortalNPC;
import com.zenyte.game.content.minigame.pestcontrol.npc.VoidKnightNPC;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 26. juuni 2018 : 18:39:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class PestNPC extends NPC {
	public PestNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(id, tile, Direction.SOUTH, 50);
		this.instance = instance;
		this.portal = portal;
		instance.addNPC(this);
		this.maxDistance = 64;
		aggressionDistance = 15;
		forceCheckAggression = true;
		this.supplyCache = false;
	}

	protected final PestControlInstance instance;
	protected final PestPortalNPC portal;

	@Override
	public void finish() {
		super.finish();
		instance.removeNPC(this);
	}

	@Override
	public void processHit(final Hit hit) {
		super.processHit(hit);
		if (hit.getSource() instanceof Player) {
			instance.addDamageDealt((Player) hit.getSource(), this, hit.getDamage());
		}
	}

	@Override
	public void setRespawnTask() {
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public List<Entity> getPossibleTargets(final EntityType type) {
		final VoidKnightNPC knight = instance.getVoidKnight();
		if (getLocation().withinDistance(knight, aggressionDistance) && !ProjectileUtils.isProjectileClipped(null, null, this, knight, combatDefinitions.isMelee())) {
			possibleTargets.clear();
			possibleTargets.add(knight);
			return possibleTargets;
		}
		return super.getPossibleTargets(type);
	}
}
