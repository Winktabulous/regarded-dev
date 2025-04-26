package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

public class NightmareParasiteNPC extends NPC implements CombatScript {

	public static final int ID = NpcId.PARASITE;
	public static final int ID_WEAKEN = NpcId.PARASITE_9453;

	public static final int ID_PHOSANIS = NpcId.PARASITE_9468;
	public static final int ID_WEAKEN_PHOSANIS = NpcId.PARASITE_9469;

	private static final Animation RANGED_ATK = new Animation(8554);
	private static final Animation MAGIC_ATK = new Animation(8555);
	private static final Animation SPAWN_WEAKEN = new Animation(8560);
	private static final Animation SPAWN = new Animation(8561);

	private static final Projectile MAGIC_PROJ = new Projectile(1771, 10, 20, 50,10, 30, 0, 0);
	private static final Projectile RANGED_PROJ = new Projectile(1775, 10, 20, 50,10, 30, 0, 0);
	private static final Projectile HEAL_PROJ = new Projectile(1774, 10, 20, 50,10, 30, 0, 0);

	private static final Graphics MAGIC_LAND = new Graphics(1772, 0, 96);

	private final int maxHit;
	private final BaseNightmareNPC boss;
	private boolean ranged = true;
	private final NightmareTotem[] totems;
	private final Player player;

	public NightmareParasiteNPC(int id, Location location, Player player, BaseNightmareNPC boss, NightmareTotem totem1, NightmareTotem totem2, NightmareTotem totem3, NightmareTotem totem4) {
		super(id, location, Direction.SOUTH, 0);
		this.boss = boss;
		this.player = player;
		this.spawned = true;
		this.maxHit = isWeaken() ? 10 : 12;
		this.combat.setCombatDelay(5);
		this.totems = new NightmareTotem[] {totem1, totem2, totem3, totem4};
		this.maxDistance = 64;
	}

	@Override
	public NPC spawn() {
		setAnimation(isWeaken() ? SPAWN_WEAKEN : SPAWN);
		return super.spawn();
	}

	private void chooseTarget() {
		if (boss != null) {
			if (boss.isShieldNpc()) {
				if (boss.getHitpoints() < boss.getMaxHitpoints()) {
					setTarget(boss);
					return;
				}
			} else {
				int closest = Integer.MAX_VALUE;
				NightmareTotem closestTotem = null;
				for (NightmareTotem totem : totems) {
					if (totem.getHitpoints() >= totem.getMaxHitpoints() || totem.getHitpoints() <= 0) continue;
					int tiles = getLocation().getTileDistance(totem.getMiddleLocation());
					if (tiles < closest) {
						closest = tiles;
						closestTotem = totem;
					}
				}

				if (closestTotem != null) {
					setTarget(closestTotem);
					return;
				}
			}
		}

		setTarget(player);
	}

	@Override
	public void processNPC() {
		chooseTarget();
		super.processNPC();
	}

	@Override
	public int attack(Entity target) {
		if (target instanceof BaseNightmareNPC || target instanceof NightmareTotem) {
			setAnimation(MAGIC_ATK);
			int ticks = World.sendProjectile(this, target, HEAL_PROJ);
			WorldTasksManager.schedule(() -> target.applyHit(new Hit(this, Utils.random(31, 50), HitType.HEALED)), ticks);
		} else if (!ranged) {
			ranged = true;
			setAnimation(MAGIC_ATK);
			int ticks = World.sendProjectile(this, target, MAGIC_PROJ);
			target.scheduleHit(this, magic(target, maxHit).onLand(hit -> target.setGraphics(MAGIC_LAND)), ticks);
		} else {
			ranged = false;
			setAnimation(RANGED_ATK);
			int ticks = World.sendProjectile(this, target, RANGED_PROJ);
			target.scheduleHit(this, ranged(target, maxHit), ticks);
		}

		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		//TODO add check for crush later on.
		if (id == ID_WEAKEN_PHOSANIS && hit.getHitType() == HitType.MELEE) {
			hit.setDamage(getHitpoints());
		} else {
			boss.setCrushHour(false);
		}

		super.handleIngoingHit(hit);
	}

	private boolean isWeaken() {
		switch (id) {
			case ID_WEAKEN, ID_WEAKEN_PHOSANIS:
				return true;
			default:
				return false;
		}
	}

}
