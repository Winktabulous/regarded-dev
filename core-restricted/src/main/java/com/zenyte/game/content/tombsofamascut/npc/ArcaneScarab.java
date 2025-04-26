package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.encounter.KephriEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class ArcaneScarab extends TOANPC implements CombatScript {

	private static final int ID = 11726;
	private static final Location[] ARCANE_SCARAB_LOCATIONS = {new Location(3556, 5401), new Location(3544, 5413), new Location(3550, 5401), new Location(3556, 5413)};
	private static final int[][] CHARGE_OFFSETS = {{0, 0}, {0, 1}, {0, 2}, {1, 2}, {2, 2}, {2, 1}, {2, 0}, {1, 0}};
	private static final int[] BLAST_OFFSETS = {1, 1};
	private static final Animation FLY_UP_ANIM = new Animation(9597);
	private static final Animation FLY_DOWN_ANIM = new Animation(9596);
	private static final Graphics CHARGE_GFX = new Graphics(2149);
	private static final Projectile BLAST_PROJECTILE = new Projectile(2149, 0, 0, 0, 20, 90, 0, 0);
	private static final SoundEffect BLAST_SOUND = new SoundEffect(6473, 0, 90);
	private final KephriEncounter encounter;
	private int chargeDelay = 5;
	private int charges;
	private int locationIndex = 1;
	private int flyingTicks;
	private int moderateHits;

	public ArcaneScarab(KephriEncounter encounter) {
		super(ID, encounter.getLocation(ARCANE_SCARAB_LOCATIONS[0]), Direction.NORTH_WEST, 0, encounter);
		this.encounter = encounter;
		this.combat = new NPCCombat(this) {
			@Override
			public void setTarget(final Entity target, TargetSwitchCause cause) { }
			@Override
			public void forceTarget(final Entity target) { }
		};
	}

	@Override public NPC spawn() {
		setAnimation(FLY_DOWN_ANIM);
		return super.spawn();
	}

	@Override public void processNPC() {
		if (EncounterStage.STARTED.equals(encounter.getStage()) && encounter.getKephri() != null && !encounter.getKephri().isDying()
				&& !encounter.getKephri().isFinished() && !isDying() && !isFinished()) {
			if (flyingTicks > 0 && --flyingTicks <= 0) {
				setAnimation(FLY_DOWN_ANIM);
				final Location newLocation = encounter.getLocation(ARCANE_SCARAB_LOCATIONS[locationIndex++]);
				setLocation(newLocation);
				locationIndex %= ARCANE_SCARAB_LOCATIONS.length;
				faceDirection(Direction.getDirection(newLocation, encounter.getKephri().getLocation().transform(2, 2)));
				setCantInteract(false);
				chargeDelay = 5;
			} else if (chargeDelay > 0 && --chargeDelay <= 0) {
				chargeDelay = 3;
				addCharge();
			}
		}
	}

	@Override public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if ((hit.getDamage() >= 15 && ++moderateHits >= 3) || hit.getDamage() >= 40) {
			flyAway();
		}
	}

	private void addCharge() {
		charges++;
		if (charges >= 9) {
			final Player[] players = encounter.getChallengePlayers();
			for (Player p : players) {
				if (p != null) {
					p.sendSound(BLAST_SOUND);
					final int base = encounter.getKephri().getMaxHit(65);
					World.sendProjectile(getLocation().transform(BLAST_OFFSETS[0], BLAST_OFFSETS[1]), p, BLAST_PROJECTILE);
					delayHit(this, 2, p, new Hit(this, Utils.random(base / 2, base), HitType.MAGIC));
				}
			}
			flyAway();
		} else {
			for (int i = 0; i < charges; i++) {
				World.sendGraphics(CHARGE_GFX, getLocation().transform(CHARGE_OFFSETS[i][0], CHARGE_OFFSETS[i][1]));
			}
		}
	}

	private void flyAway() {
		setCantInteract(true);
		setAnimation(FLY_UP_ANIM);
		charges = 0;
		flyingTicks = 2;
		moderateHits = 0;
	}

	@Override public double getMagicPrayerMultiplier() { return .33D; }

	@Override public void autoRetaliate(Entity source) {}

	@Override public boolean isEntityClipped() { return false; }

	@Override public void setTarget(Entity target, TargetSwitchCause cause) {}

	@Override public void setFaceEntity(Entity entity) {}

	@Override public float getPointMultiplier() { return .5F; }

	@Override public int attack(Entity target) {
		return 0;
	}
}
