package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;

public class NightmareSleepwalker extends NPC {

	public static final int[] IDS = {NpcId.SLEEPWALKER_9446, NpcId.SLEEPWALKER_9447, NpcId.SLEEPWALKER_9448, NpcId.SLEEPWALKER_9449, NpcId.SLEEPWALKER_9450, NpcId.SLEEPWALKER_9451};
	private static final Animation FLY = new Animation(8571);
	private static final Animation AWAKE = new Animation(8572);

	private final BaseNightmareNPC boss;
	private final NightmarePhase phase;
	private int ticks = 0;

	public NightmareSleepwalker(int id, Location tile, BaseNightmareNPC boss, NightmarePhase phase) {
		super(id, tile, Direction.SOUTH, 0);
		this.spawned = true;
		this.boss = boss;
		this.phase = phase;
	}

	@Override
	public NPC spawn() {
		lock(3);
		setAnimation(AWAKE);
		return super.spawn();
	}

	@Override
	public void processHit(Hit hit) {
		hit.setDamage(getHitpoints());

		super.processHit(hit);
	}

	@Override
	public void processNPC() {
		super.processNPC();

		if (isLocked() || boss == null || isDying() || isDead() || isFinished()) {
			return;
		}
		resetWalkSteps();
		calcFollow(boss, -1, true, true, false);
		int distance = getLocation().getTileDistance(boss.getMiddleLocation());
		if (distance <= 3) {
			boss.increaseAbsorbed();
			lock();
			setAnimation(FLY);
			WorldTasksManager.schedule(this::finish, 1);
		}
	}
	@Override
	public boolean isEntityClipped() {
		return false;
	}

}
