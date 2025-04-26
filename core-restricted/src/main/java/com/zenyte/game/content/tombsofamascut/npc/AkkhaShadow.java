package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.AkkhaEncounter;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class AkkhaShadow extends TOANPC {

	private static final Location[] STOPWATCH_LOCATIONS = {new Location(3689, 5416, 1), new Location(3689, 5398, 1), new Location(3671, 5398, 1), new Location(3671, 5416, 1)};
	private static final int STOPWATCH_NPC_ID = 11805;
	private static final Animation SHADOW_SPAWN_ANIM = new Animation(9790);
	private static final Animation SEND_QUADRANT_ANIM = new Animation(9777);
	public static final Tinting INVULNERABLE_TINTING = new Tinting(0, 0, 106, 40, 0, 600);
	private static final Tinting RESET_TINTING = new Tinting(-1, -1, -1, 0, 0, 0);
	private static final int ID = 11797;
	private final AkkhaEncounter encounter;
	private final int index;
	private final Akkha akkha;
	private int counter = 0;
	private boolean counting = true;
	private final StopWatchNpc stopWatchNpc;
	private StopWatchBar stopWatchBar = new StopWatchBar(this);
	private int vulnerableType = 0;
	private int quadrantDelay;

	public AkkhaShadow(Location tile, Direction facing, AkkhaEncounter encounter, int level, int index, Akkha akkha) {
		super(ID, tile, facing, 0, encounter, level);
		super.hitBar = new EntityHitBar(this) {
			@Override public int getType() {
				return 19;
			}
		};
		setAnimation(SHADOW_SPAWN_ANIM);
		stopWatchNpc = new StopWatchNpc(encounter.getLocation(STOPWATCH_LOCATIONS[index]), stopWatchBar);
		World.spawnNPC(stopWatchNpc);
		stopWatchNpc.getHitBars().clear();
		stopWatchNpc.getHitBars().add(stopWatchBar);
		stopWatchNpc.getUpdateFlags().flag(UpdateFlag.HIT);
		this.encounter = encounter;
		this.index = index;
		this.akkha = akkha;
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }
		};
	}

	@Override public void processNPC() {
		super.processNPC();
		setTinting(vulnerableType == 0 ? RESET_TINTING : INVULNERABLE_TINTING);
		if (!isDying() && !isFinished()) {
			stopWatchNpc.addHitbar();
			stopWatchNpc.getUpdateFlags().flag(UpdateFlag.HIT);
			if (counting && !akkha.performingMemoryBlast()) {
				if (quadrantDelay <= 0) {
					if (++counter >= 50) {
						counter = 0;
						quadrantDelay = 4;
					}
				} else {
					if (--quadrantDelay == 3) {
						setAnimation(SEND_QUADRANT_ANIM);
					} else if (quadrantDelay == 2) {
						encounter.sendQuadrantBlasts(true, new int[]{index});
					}
				}
			}
		}
	}

	@Override public void listenScheduleHit(Hit hit) {
		if (vulnerableType != 0) {
			if (hit.getSource() instanceof final Player player) {
				player.sendMessage("<col=ff289d>This shadow is currently immune to your attacks.</col>");
			}
			hit.setDamage(0);
		}
		super.listenScheduleHit(hit);
	}

	public void resetCounter() {
		counter = 0;
		quadrantDelay = 0;
	}

	public void setCounting(boolean counting) { this.counting = counting; }

	public int getPercentage() {
		return counter * 2;
	}

	public int getVulnerableType() { return vulnerableType; }

	public void setVulnerableType(int type) { this.vulnerableType = type; }

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public void setRespawnTask() {}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	@Override public float getPointMultiplier() {
		return 1;
	}

	@Override public boolean isEntityClipped() { return false; }

	@Override protected void onFinish(Entity source) {
		super.onFinish(source);
		if (!stopWatchNpc.isFinished()) {
			stopWatchNpc.finish();
		}
		encounter.onShadowFinish(index);
	}

	static class StopWatchNpc extends NPC {

		public StopWatchNpc(Location tile, StopWatchBar stopWatchBar) {
			super(STOPWATCH_NPC_ID, tile, Direction.SOUTH, 0);
			super.hitBar = stopWatchBar;
		}

		@Override public void setRespawnTask() {}

		@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

		@Override public void setFaceEntity(Entity entity) {}

		@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

		@Override public void setFaceLocation(Location tile) {}

		@Override public boolean isEntityClipped() { return false; }

		@Override
		public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }
	}

	static class StopWatchBar extends HitBar {

		private final AkkhaShadow shadow;

		private StopWatchBar(AkkhaShadow shadow) {
			this.shadow = shadow;
		}

		@Override public int getType() {
			return 36;
		}

		@Override public int getPercentage() {
			return shadow.getPercentage();
		}
	}
}
