package com.zenyte.game.content.boss.sarachnis;

import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;

public class SarachnisMinion extends NPC {

	private final Sarachnis sarachnis;

	public SarachnisMinion(int id, Location tile, Direction facing, int radius, Sarachnis sarachnis) {
		super(id, tile, facing, radius);
		setSpawned(false);
		setForceAggressive(true);
		this.sarachnis = sarachnis;
	}

	@Override
	public NPCCombatDefinitions getCombatDefinitions() {
		NPCCombatDefinitions defs = super.getCombatDefinitions();
		defs.getSpawnDefinitions().setDeathAnimation(new Animation(8318));
		final Animation death = defs.getSpawnDefinitions().getDeathAnimation();
		if (death != null) {
			deathDelay = Math.max(Math.min((int) Math.ceil(AnimationUtil.getDuration(death) / 1200.0F), 10), 1);
		}
		return defs;
	}

	@Override
	public void setRespawnTask() {

	}

	@Override
	protected void onDeath(Entity source) {
		super.onDeath(source);

		sarachnis.getSpawns().remove(this);
	}

}
