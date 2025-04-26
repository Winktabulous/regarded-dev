package com.zenyte.game.content.chambersofxeric.greatolm;

import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 14. jaan 2018 : 2:22.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RightClaw extends GreatOlmClaw {
	RightClaw(final OlmRoom room, final Location tile) {
		super(room, 7553, tile);
	}

	@Override
	protected OlmAnimation fallAnimation() {
		return OlmAnimation.RIGHT_CLAW_FALL;
	}

	@Override
	protected WorldObject clawObject() {
		return room.getRightClawObject();
	}

	@Override
	public void onDeath() {
		final LeftClaw leftClaw = room.getLeftClaw();
		if (leftClaw != null) {
			if (leftClaw.getClenchTicks() > 2) {
				leftClaw.setClenchTicks(2);
			}
		}
	}

	@Override
	protected void spawnClawlessObject() {
		final int side = room.getSide();
		final WorldObject defeatedClaw = new WorldObject(29888, 10, side == OlmRoom.LEFT ? OlmRoom.leftSideRightClawObject.getRotation() : OlmRoom.rightSideRightClawObject.getRotation(), room.getLocation(side == OlmRoom.LEFT ? OlmRoom.leftSideRightClawObject : OlmRoom.rightSideRightClawObject));
		World.spawnObject(defeatedClaw);
	}

	@Override
	protected void onFinish() {
		if (!isCantInteract()) {
			if (room.getLeftClaw() == null || room.getLeftClaw().isFinished()) {
				room.switchSide();
			}
		}
	}

	@Override
	public float getXpModifier(final Hit hit) {
		calculatableDamage.add(hit);
		final HitType type = hit.getHitType();
		if (type == HitType.RANGED) {
			return 1.0F / 3.5F;
		}
		return 1;
	}

	@Override
	public void applyHit(final Hit hit) {
		super.applyHit(hit);
		final HitType type = hit.getHitType();
		if (type == HitType.RANGED) {
			hit.setDamage((int) (hit.getDamage() / 3.5F));
		}
	}
}
