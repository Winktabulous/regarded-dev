package com.near_reality.game.content.boss.nex;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

public class NexMinion extends NPC implements CombatScript {

	public static final Animation attackAnimation = new Animation(1979);
	private boolean immune = true;
	private final NexNPC nex;

	protected NexMinion(int id, Location tile, Direction facing, NexNPC nex) {
		super(id, tile, facing, 0);
		setSpawned(true);
		this.nex = nex;
	}

	public boolean isImmune() {
		return immune;
	}

	public void removeImmunity() {
		this.immune = false;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);

		if (immune) {
			hit.setDamage(0);
			Entity source = hit.getSource();
			if (source instanceof Player) {
				((Player) source).sendMessage(getName() + " is currently immune to your attacks.");
			}
		}
	}

	public void enableAggression() {
		setForceAggressive(true);
		setAggressionDistance(64);
		setMaxDistance(64);
	}

	@Override
	public void autoRetaliate(Entity source) {
		if (immune) return;
		super.autoRetaliate(source);
	}

	@Override
	protected boolean isMovableEntity() {
		return false;
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	public NexNPC getNex() {
		return nex;
	}

	@Override
	public int attack(Entity target) {
		useSpell(getSpell(), target, getCombatDefinitions().getAttackDefinitions().getMaxHit());
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (!(source instanceof final Player player)) {
			return;
		}
		GodType.ANCIENT.addKillcount(player, 3);
	}

	public CombatSpell getSpell() {
		return null;
	}

	@Override
	public boolean checkProjectileClip(final Player player, boolean melee) {
		if (melee) return false;
		return super.checkProjectileClip(player, false);
	}

}
