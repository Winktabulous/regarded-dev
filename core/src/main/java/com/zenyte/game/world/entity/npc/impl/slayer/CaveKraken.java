package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.boons.impl.IceForTheEyeless;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.WalkStep;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.region.RSPolygon;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * @author Tommeh | 13 dec. 2017 : 22:08:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CaveKraken extends NPC implements CombatScript, Spawnable {
	private static final Animation AWAKE_ANIM = new Animation(7135);
	private static final Projectile PROJECTILE = new Projectile(162, 40, 28, 57, 20, 18, 64, 5);
	private static final Graphics SPLASH_GRAPHICS = new Graphics(85, 0, 124);
	private int ticks;

	public CaveKraken(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		this.attackDistance = 10;
		this.maxDistance = 20;
	}

	@Override
	public float getXpModifier(final Hit hit) {
		if (hit.getHitType() == HitType.MELEE) {
			hit.setDamage(0);
			return 0;
		}
		return 1;
	}

	@Override
	public void processNPC() {
		if (id == 492) {
			if (getCombat().underCombat()) {
				ticks = 0;
			}
			if (++ticks >= 50) {
				setTransformation(493);
				cancelCombat();
			}
		}
		if (!combat.process() && getRadius() > 0 && getForceWalk() == null && getWalkSteps().isEmpty() && Utils.random(5) == 0) {
			final int moveX = Utils.random(getRadius());
			final int moveY = Utils.random(getRadius());
			addWalkSteps(getRespawnTile().getX() + moveX, getRespawnTile().getY() + moveY, getRadius());
		} else if (getForceWalk() != null) {
			if (getLocation().getPositionHash() == getForceWalk().getPositionHash()) {
				setForceWalk(null);
			} else if (getWalkSteps().size() == 0) {
				this.addWalkSteps(getForceWalk().getX(), getForceWalk().getY(), getSize(), true);
			}
		}
		super.processNPC();
	}

	public static final RSPolygon[] polygons = new RSPolygon[] {new RSPolygon(new int[][] {{2268, 9999}, {2267, 9998}, {2264, 9995}, {2264, 9993}, {2265, 9992}, {2266, 9992}, {2268, 9991}, {2269, 9990}, {2270, 9988}, {2273, 9988}, {2273, 9989}, {2274, 9990}, {2274, 9993}, {2273, 9994}, {2273, 9995}, {2272, 9996}, {2270, 9997}}), new RSPolygon(new int[][] {{2262, 9999}, {2259, 9999}, {2257, 9997}, {2257, 9995}, {2258, 9994}, {2261, 9994}, {2262, 9996}}), new RSPolygon(new int[][] {{2264, 10006}, {2263, 10006}, {2263, 10003}, {2264, 10002}, {2266, 10002}, {2268, 10004}, {2265, 10006}}), new RSPolygon(new int[][] {{2281, 10015}, {2279, 10015}, {2279, 10013}, {2277, 10013}, {2274, 10011}, {2273, 10010}, {2273, 10008}, {2274, 10007}, {2276, 10007}, {2277, 10008}, {2284, 10008}, {2284, 10004}, {2285, 10003}, {2287, 10003}, {2288, 10004}, {2288, 10005}, {2285, 10010}, {2282, 10012}}), new RSPolygon(new int[][] {{2294, 10021}, {2293, 10021}, {2291, 10019}, {2290, 10017}, {2291, 10016}, {2291, 10015}, {2292, 10014}, {2293, 10014}, {2294, 10015}, {2294, 10018}, {2293, 10018}, {2293, 10020}, {2295, 10020}, {2299, 10014}, {2299, 10013}, {2297, 10011}, {2298, 10009}, {2300, 10009}, {2300, 10011}, {2301, 10011}, {2301, 10017}, {2299, 10019}, {2297, 10020}, {2296, 10020}, {2295, 10021}}), new RSPolygon(new int[][] {{2298, 10005}, {2296, 10005}, {2295, 10003}, {2295, 10002}, {2296, 10001}, {2296, 9999}, {2297, 9998}, {2297, 9995}, {2295, 9993}, {2293, 9993}, {2291, 9994}, {2290, 9994}, {2290, 9993}, {2291, 9992}, {2292, 9991}, {2293, 9991}, {2295, 9991}, {2296, 9990}, {2298, 9989}, {2300, 9990}, {2299, 9993}, {2299, 9996}, {2300, 9997}, {2299, 9999}, {2298, 10001}}), new RSPolygon(new int[][] {{2259, 10041}, {2258, 10042}, {2257, 10042}, {2256, 10040}, {2255, 10038}, {2253, 10038}, {2252, 10036}, {2254, 10033}, {2254, 10030}, {2253, 10030}, {2253, 10028}, {2252, 10026}, {2257, 10021}, {2257, 10017}, {2261, 10014}, {2264, 10013}, {2265, 10013}, {2266, 10010}, {2269, 10010}, {2269, 10013}, {2268, 10015}, {2264, 10018}, {2262, 10022}, {2258, 10026}, {2258, 10028}, {2258, 10033}, {2257, 10035}, {2257, 10037}, {2259, 10039}}), new RSPolygon(new int[][] {{2246, 10029}, {2243, 10024}, {2243, 10023}, {2245, 10020}, {2245, 10016}, {2244, 10016}, {2244, 10013}, {2245, 10012}, {2250, 10012}, {2250, 10014}, {2248, 10015}, {2247, 10017}, {2247, 10019}, {2248, 10019}, {2248, 10020}, {2247, 10021}, {2247, 10025}, {2248, 10026}, {2248, 10028}})};
	private static final IntSet tiles = new IntOpenHashSet();

	static {
		for (final RSPolygon polygon : polygons) {
			final Rectangle2D box = polygon.getPolygon().getBounds2D();
			for (double x = box.getMinX(); x <= box.getMaxX(); x++) {
				for (double y = box.getMinY(); y <= box.getMaxY(); y++) {
					if (polygon.contains((int) x, (int) y)) {
						tiles.add(Location.hash((int) x, (int) y, 0));
					}
				}
			}
		}
	}

	@Override
	public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY, final boolean check) {
		final int dir = DirectionUtil.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1) {
			return false;
		}
		if (check) {
			if (!canMove(nextX, nextY)) {
				return false;
			}
		}
		getWalkSteps().enqueue(WalkStep.getHash(dir, nextX, nextY, check));
		return true;
	}

	private final boolean canMove(final int x, final int y) {
		for (int x1 = x; x1 < (x + 2); x1++) {
			for (int y1 = y; y1 < (y + 2); y1++) {
				if (!tiles.contains(Location.hash(x1, y1, 0))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void processMovement() {
		if (getFaceEntity() >= 0) {
			final Entity target = getFaceEntity() >= 32768 ? World.getPlayers().get(getFaceEntity() - 32768) : World.getNPCs().get(getFaceEntity());
			if (target != null) {
				setDirection(DirectionUtil.getFaceDirection(target.getLocation().getCoordFaceX(target.getSize()) - getX(), target.getLocation().getCoordFaceY(target.getSize()) - getY()));
			}
		}
		if (getNextLocation() != null) {
			if (lastLocation == null) {
				lastLocation = new Location(location);
			} else {
				lastLocation.setLocation(location);
			}
			forceLocation(getNextLocation());
			setLocation(null);
			setTeleported(true);
			World.updateEntityChunk(this, false);
			resetWalkSteps();
			return;
		}
		setRunDirection(-1);
		setWalkDirection(-1);
		if (getWalkSteps().isEmpty() || isLocked() && getEntityType() == EntityType.NPC) {
			return;
		}
		if (lastLocation == null) {
			lastLocation = new Location(location);
		} else {
			lastLocation.setLocation(location);
		}
		setTeleported(false);
		final int steps = isRun() ? 2 : 1;
		for (int stepCount = 0; stepCount < steps; stepCount++) {
			final int nextStep = getNextWalkStep();
			if (nextStep == 0) {
				break;
			}
			final int dir = WalkStep.getDirection(nextStep);
			final int offsetX = Utils.DIRECTION_DELTA_X[dir];
			final int offsetY = Utils.DIRECTION_DELTA_Y[dir];
			if ((WalkStep.check(nextStep) && !canMove(getX() + offsetX, getY() + offsetY))) {
				resetWalkSteps();
				break;
			}
			if (stepCount == 0) {
				setWalkDirection(dir);
			} else {
				setRunDirection(dir);
			}
			getUpdateFlags().flag(UpdateFlag.MOVEMENT_TYPE);
			getLocation().moveLocation(Utils.DIRECTION_DELTA_X[dir], Utils.DIRECTION_DELTA_Y[dir], 0);
		}
		World.updateEntityChunk(this, false);
	}

	@Override
	public void dropItem(final Player killer, final Item item, final Location tile, boolean guaranteedDrop) {
		//Kraken always drops loot underneath the player.
		tile.setLocation(killer.getLocation());
		super.dropItem(killer, item, tile, guaranteedDrop);
	}

	@Override
	public void drop(final Location location) {
		if (getMostDamagePlayer() != null) {
			super.drop(getMostDamagePlayer().getLocation());
		}
	}

	@Override
	public int attack(final Entity target) {
		setUnprioritizedAnimation(getCombatDefinitions().getAttackAnim());
		delayHit(this, World.sendProjectile(this, target, PROJECTILE), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> {
			if (hit.getDamage() <= 0) {
				target.setGraphics(SPLASH_GRAPHICS);
			}
		}));
		return getCombatDefinitions().getAttackSpeed();
	}

	public static final CombatSpell[] iceSpells = new CombatSpell[] {CombatSpell.ICE_RUSH, CombatSpell.ICE_BURST, CombatSpell.ICE_BLITZ, CombatSpell.ICE_BARRAGE};


	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
		if (!(hit.getSource() instanceof Player)) {
			return;
		}
		final Player target = (Player) hit.getSource();
		if (id == 493) {
			WorldTasksManager.schedule(new WorldTask() {
				int ticks;
				@Override
				public void run() {
					switch (ticks++) {
					case 0: 
						setTransformation(492);
						setAnimation(AWAKE_ANIM);
						faceEntity(target);
						PlayerCombat.attackEntity(target, CaveKraken.this, null);
						break;
					case 3: 
						getCombat().setTarget(target);
						stop();
						break;
					}
				}
			}, 0, 1);
		} else {
			if(target.getBoonManager().hasBoon(IceForTheEyeless.class) && hit.getHitType() == HitType.MAGIC && hit.getAttribute("spell") instanceof CombatSpell spell && Arrays.stream(iceSpells).anyMatch(it -> it == spell)) {
				hit.setDamage((int) (hit.getDamage() * 1.1F));
			}
			super.handleIngoingHit(hit);
		}
	}

	@Override
	public boolean canAttack(final Player source) {
		return true;
	}

	@Override
	public void onFinish(final Entity source) {
		drop(getMiddleLocation());
		if (source != null) {
			if (source instanceof Player) {
				final Player player = (Player) source;
				sendNotifications(player);
			}
		}
		reset();
		finish();
		setId(493);
		if (!spawned) {
			setRespawnTask();
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 492 || id == 493;
	}
}
