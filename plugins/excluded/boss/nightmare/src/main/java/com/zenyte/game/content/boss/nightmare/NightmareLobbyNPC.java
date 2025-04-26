package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;

public class NightmareLobbyNPC extends NPC {

	public static final int SLEEPING = NpcId.THE_NIGHTMARE_9460;
	public static final int AWAKENING = NpcId.THE_NIGHTMARE_9461;
	public static final int FIRST_PHASE = NpcId.THE_NIGHTMARE_9462;
	public static final int SECOND_PHASE = NpcId.THE_NIGHTMARE_9463;
	public static final int THIRD_PHASE = NpcId.THE_NIGHTMARE_9464;

	public static final Animation RISE_UP = new Animation(8573);
	public static final Animation AWAKE = new Animation(8575);
	public static final Animation STALL = new Animation(8576);
	public static final Animation GO_SLEEP = new Animation(8581);

	public NightmareLobbyNPC() {
		super(SLEEPING, new Location(3806, 9757, 1), Direction.SOUTH, 0);
	}

	public void startAwaken() {
		setAnimation(RISE_UP);
		WorldTasksManager.schedule(() -> setTransformation(AWAKENING), 4);
	}

	public void awaken() {
		setAnimation(AWAKE);
		WorldTasksManager.schedule(() -> setTransformation(FIRST_PHASE), 1);
	}

	public void stall() {
		setAnimation(STALL);
		WorldTasksManager.schedule(() -> setTransformation(AWAKENING), 1);
	}

	public void sleep() {
		setAnimation(GO_SLEEP);
		WorldTasksManager.schedule(() -> setTransformation(SLEEPING), 4);
	}

	@Override
	protected boolean canMove(int fromX, int fromY, int direction) {
		return false;
	}

}
