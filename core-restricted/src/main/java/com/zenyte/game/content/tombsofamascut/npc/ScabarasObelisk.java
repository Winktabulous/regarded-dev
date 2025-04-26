package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.ScabarasEncounter;
import com.zenyte.game.content.tombsofamascut.raid.ScabarasPuzzleType;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.InteractableEntity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class ScabarasObelisk extends NPC {

	public static final int ID = 11698;
	private static final int TILE_LOCK_OBJECT_ID = 43876;
	private static final SoundEffect PILLAR_HIT_SOUND = new SoundEffect(955);
	private final ScabarasEncounter puzzleEncounter;

	public ScabarasObelisk(Location tile, ScabarasEncounter puzzleEncounter, boolean completed) {
		super(completed ? ID + 1 : ID, tile, Direction.SOUTH, 0);
		this.puzzleEncounter = puzzleEncounter;
		World.spawnObject(new WorldObject(TILE_LOCK_OBJECT_ID, 10, 0, getLocation()));
	}

	@Override public void processHit(Hit hit) {
		if (hit.getSource() != null && hit.getSource() instanceof Player player) {
			player.sendSound(PILLAR_HIT_SOUND);
			if (puzzleEncounter.getObeliskIndex() > 4) {
				puzzleEncounter.complete(ScabarasPuzzleType.PILLAR);
			}
		}
	}

	@Override public void listenScheduleHit(Hit hit) {
		super.listenScheduleHit(hit);
		if (hit.getSource() != null && hit.getSource() instanceof Player player) {
			player.getActionManager().forceStop();
			player.getActionManager().setActionDelay(0);
			player.getActionManager().preventDelaySet();
			if (puzzleEncounter.validateObelisk(getLocation())) {
				setCompleted();
			}
		}
	}

	public void setCompleted() {
		setTransformation(ID + 1);
	}

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	public void resetObelisk() {
		setTransformation(ID);
	}

	@Override public boolean isForceAttackable() { return id == ID; }

	@Override public boolean canAttack(Player source) {
		return id == ID && puzzleEncounter.canUse(source, ScabarasPuzzleType.PILLAR, null);
	}

	@Override public boolean checkProjectileClip(Player player, boolean melee) {
		return false;
	}

	@Override public void sendDeath() {}

	@Override public void setRespawnTask() {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public void setFacedInteractableEntity(InteractableEntity facedInteractableEntity) {}

	@Override public void setFaceLocation(Location tile) {}

	@Override public void autoRetaliate(Entity source) {}

	@Override public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) { return false; }

	@Override public boolean isCycleHealable() { return false; }
}
