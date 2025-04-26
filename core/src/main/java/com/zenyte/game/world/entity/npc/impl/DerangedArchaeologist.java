package com.zenyte.game.world.entity.npc.impl;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class DerangedArchaeologist extends NPC implements CombatScript, Spawnable {
	private static final ForceTalk[] MESSAGES = {
			new ForceTalk("Round and round and round and round!"), new ForceTalk("The plants! They're alive!"),
			new ForceTalk("They came from the ground! They came from the ground!!!"), new ForceTalk("The doors won't stay closed forever!"),
			new ForceTalk("They're cheering! Why are they cheering?"), new ForceTalk("Time is running out! She will rise again!!!"),
			new ForceTalk("No hiding!")
	};

	private static final Graphics SPECIAL_GFX = new Graphics(157, 0, 124);
	private static final ForceTalk DEATH_MESSAGE = new ForceTalk("Ow!");
	private static final ForceTalk SPECIAL_MESSAGE = new ForceTalk("Learn to Read!");
	private static final Animation RANGED_ANIM = new Animation(3353);
	private static final Graphics EXPLOSIVE_GFX = new Graphics(305, 0, 92);
	private static final Projectile RANGED_PROJ = new Projectile(1259, 42, 30, 40, 10, 25, 32, 5);
	private static final Projectile SPECIAL_PROJ = new Projectile(1260, 42, 0, 40, 40, 50, 32, 5);
	private static final byte[][][] OFFSETS = new byte[][][] {new byte[][] {new byte[] {1, 0}, new byte[] {0, 1}}, new byte[][] {new byte[] {0, -1}, new byte[] {1, 0}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, -1}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, 1}}};
	private int specialCooldown;
	private boolean hitPlayerWithSpecial;
	private boolean onlyMagic = true;

	public DerangedArchaeologist(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
		this.attackDistance = 10;
		this.radius = 20;
		this.maxDistance = 20;
	}

	@Override
	public NPC spawn() {
		hitPlayerWithSpecial = false;
		onlyMagic = true;
		return super.spawn();
	}

	@Override
	public int getRespawnDelay() {
		return 50;
	}

	@Override
	public int attack(final Entity target) {
		if (!(target instanceof Player)) {
			return 0;
		}
		if (isWithinMeleeDistance(this, target)) {
			melee(target);
		} else {
			if (Utils.randomBoolean(2) && WorldThread.getCurrentCycle() >= specialCooldown) {
				special(target);
			} else {
				ranged(target);
			}
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);

		if (hit.getHitType() != HitType.MAGIC) {
			onlyMagic = false;
		}
	}

	private void melee(final Entity target) {
		setForceTalk(Utils.random(MESSAGES));
		setAnimation(getCombatDefinitions().getAttackAnim());
		final Hit hit = new Hit(this, getRandomMaxHit(this, 25, MELEE, target), HitType.REGULAR);
		delayHit(this, 0, target, hit);
		getCombatDefinitions().setAttackStyle("Ranged");
	}

	private void ranged(final Entity target) {
		setForceTalk(Utils.random(MESSAGES));
		setAnimation(RANGED_ANIM);
		delayHit(this, World.sendProjectile(this, target, RANGED_PROJ), target, new Hit(this, getRandomMaxHit(this, 25, RANGED, target), HitType.RANGED).onLand(hit -> {
			if (Utils.random(2) == 0) {
				World.sendGraphics(EXPLOSIVE_GFX, target.getLocation());
			}
		}));
	}

	private void special(final Entity target) {
		specialCooldown = (int) (WorldThread.getCurrentCycle() + 25);
		setAnimation(RANGED_ANIM);
		final ArrayList<Location> locations = new ArrayList<>(3);
		final Location base = new Location(target.getLocation());
		final int r = Utils.random(OFFSETS.length - 1);
		for (int i = 0; i < 2; i++) {
			locations.add(new Location(base.getX() + OFFSETS[r][0][i], base.getY() + OFFSETS[r][1][i]));
		}
		locations.add(base);
		for (final Location l : locations) {
			World.sendProjectile(this, l, SPECIAL_PROJ);
		}
		WorldTasksManager.schedule(() -> {
			for (final Location l : locations) {
				World.sendGraphics(SPECIAL_GFX, l);
				int distance = l.getTileDistance(target.getPosition());
				if (distance < 2) {
					int damage = distance == 0 ? 56 : 18;
					delayHit(this, 0, target, new Hit(this, damage, HitType.REGULAR));
					hitPlayerWithSpecial = true;
				}
			}

			specialSecond(target, base);
		}, SPECIAL_PROJ.getTime(this, base));
		setForceTalk(SPECIAL_MESSAGE);
	}

	private void specialSecond(final Entity target, final Location base) {
		final ArrayList<Location> locations = new ArrayList<>(2);
		final int r = Utils.random(OFFSETS.length - 1);
		for (int i = 0; i < 2; i++) {
			locations.add(new Location(base.getX() + OFFSETS[r][0][i], base.getY() + OFFSETS[r][1][i]));
		}
		for (final Location l : locations) {
			World.sendProjectile(base, l, SPECIAL_PROJ);
			WorldTasksManager.schedule(() -> {
				World.sendGraphics(SPECIAL_GFX, l);
				int distance = l.getTileDistance(target.getPosition());
				if (distance < 2) {
					int damage = distance == 0 ? 56 : 18;
					delayHit(this, 0, target, new Hit(this, damage, HitType.REGULAR));
					hitPlayerWithSpecial = true;
				}
			}, SPECIAL_PROJ.getTime(base, l));
		}
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		setForceTalk(DEATH_MESSAGE);
	}

	@Override
	protected void onFinish(Entity source) {
		super.onFinish(source);

		if (source instanceof final Player player) {
			player.getCombatAchievements().checkKcTask("deranged archaeologist", 10, CAType.DERANGED_ARCHAEOLOGIST_NOVICE);
			player.getCombatAchievements().checkKcTask("deranged archaeologist", 25, CAType.DERANGED_ARCHAEOLOGIST_CHAMPION);
			if (!hitPlayerWithSpecial) {
				player.getCombatAchievements().complete(CAType.ID_RATHER_BE_ILLITERATE);
			}
			if (onlyMagic) {
				player.getCombatAchievements().complete(CAType.MAGE_OF_THE_SWAMP);
			}
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == NpcId.DERANGED_ARCHAEOLOGIST;
	}

}
