package com.zenyte.game.content.tombsofamascut.npc;

import com.zenyte.game.content.tombsofamascut.encounter.HetEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
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

/**
 * @author Savions.
 */
public class SealOrb extends NPC {

	private static final int ID = 11708;
	private static final Graphics START_GFX = new Graphics(379);
	private static final SoundEffect IMPACT_SOUND = new SoundEffect(179, 4);
	private final Direction direction;
	private final HetEncounter encounter;
	private boolean finish;
	private boolean exploded = false;

	public SealOrb(Location tile, Direction direction, HetEncounter encounter) {
		super(ID, tile, true);
		this.direction = direction;
		this.encounter = encounter;
		setForceAggressive(false);
		World.sendGraphics(START_GFX, tile);
	}

	@Override public void processNPC() {
		if (finish || (!EncounterStage.STARTED.equals(encounter.getStage()) && !isFinished())) {
			if (!exploded) {
				explode();
			}
			finish();
			return;
		}
		final Player[] players = encounter.getChallengePlayers();
		for (final Player p : players) {
			if (p != null && getLocation().equals(p.getLocation())) {
				final int baseDamage = (int) Math.floor(encounter.getParty().getDamageMultiplier() * 5F);
				p.applyHit(new Hit(encounter.getSeal(), baseDamage + Utils.random(3), HitType.DEFAULT));
				explode();
			}
		}
		if (finish) {
			return;
		}
		if (isLocked()) {
			return;
		}
		final Location nextLoc = getLocation().transform(direction.getOffsetX(), direction.getOffsetY());
		if (!addWalkSteps(nextLoc.getX(), nextLoc.getY())) {
			finish = true;
		}
	}

	private void explode() {
		World.sendGraphics(START_GFX, getLocation());
		World.sendSoundEffect(getLocation(), IMPACT_SOUND);
		exploded = true;
	}

	@Override public void setRespawnTask() {

	}

	@Override public boolean canAttack(Player source) {
		return false;
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}
}