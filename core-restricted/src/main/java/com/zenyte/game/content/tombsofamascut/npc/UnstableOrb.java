package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.AkkhaEncounter;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions.
 */
public class UnstableOrb extends NPC {

	private static final int ID = 11804;
	private static final Graphics EXPLODE_GFX = new Graphics(2260, 10, 158);
	private static final SoundEffect EXPLODE_SOUND = new SoundEffect(5735, 4, 10);
	private final Direction direction;
	private final AkkhaEncounter encounter;
	private boolean finish;

	public UnstableOrb(Location tile, Direction direction, AkkhaEncounter encounter) {
		super(ID, tile, true);
		this.direction = direction;
		this.encounter = encounter;
		setForceAggressive(false);
	}

	@Override public void processNPC() {
		if (finish && !isFinished()) {
			encounter.removeUnstableOrb(this);
			finish();
			return;
		}
		final Player[] players = encounter.getChallengePlayers();
		final List<Player> playersHit = new ArrayList<>();
		for (final Player p : players) {
			if (p != null && getLocation().equals(p.getLocation())) {
				playersHit.add(p);
				finish = true;
			}
		}
		if (playersHit.size() > 0) {
			final int baseDamage = (int) (Math.floor(encounter.getParty().getDamageMultiplier() * 13) / Math.min(2, playersHit.size())) + Utils.random(2);
			playersHit.forEach(p -> {
				p.applyHit(new Hit(encounter.getAkkha(), baseDamage, HitType.DEFAULT));
			});
		}
		if (finish || isLocked()) {
			return;
		}
		final Location nextLoc = getLocation().transform(direction.getOffsetX(), direction.getOffsetY());
		if (!addWalkSteps(nextLoc.getX(), nextLoc.getY(), -1, false) || !World.isFloorFree(nextLoc, 1)) {
			setGraphics(EXPLODE_GFX);
			World.sendSoundEffect(nextLoc, EXPLODE_SOUND);
			finish = true;
		}
	}

	@Override public void setRespawnTask() {}

	@Override public boolean canAttack(Player source) {
		return false;
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}
}
