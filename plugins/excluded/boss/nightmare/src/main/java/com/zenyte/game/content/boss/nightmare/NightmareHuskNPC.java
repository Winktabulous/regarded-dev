package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

public abstract class NightmareHuskNPC extends NPC implements CombatScript {

	public static final Animation SPAWN_ANIMATION = new Animation(8567);
	private final Player spawnedFor;
	public NightmareHuskNPC oppositeHusk;
	public long deathTick = -1L;
	private BaseNightmareNPC boss;

	public NightmareHuskNPC(int id, Location tile, Player spawnedFor, BaseNightmareNPC boss) {
		super(id, tile, Direction.SOUTH, 0);
		this.spawned = true;
		this.spawnedFor = spawnedFor;
		this.boss = boss;
	}

	@Override
	public NPC spawn() {
		setAnimation(SPAWN_ANIMATION);
		return super.spawn();
	}

	@Override
	public void processNPC() {
		setTarget(spawnedFor);

		super.processNPC();
	}

	@Override
	public void sendDeath() {
		super.sendDeath();

		deathTick = WorldThread.getCurrentCycle();
		if (spawnedFor != null && spawnedFor.isFrozen() && (isDead() || isFinished()) && oppositeHusk != null && (oppositeHusk.isDead() || oppositeHusk.isFinished())) {
			spawnedFor.resetFreeze();
			spawnedFor.sendMessage("<col=229628>As the last husk dies, you feel yourself become free of the Nightmare's trance.");
			if (deathTick == oppositeHusk.deathTick) {
				spawnedFor.getCombatAchievements().complete(CAType.EXPLOSION);
			}
		}
	}

	public BaseNightmareNPC getBoss() {
		return boss;
	}

	@Override
	protected boolean isMovableEntity() {
		return false;
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

}
