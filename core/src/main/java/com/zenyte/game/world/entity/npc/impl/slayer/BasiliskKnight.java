package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
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
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

public class BasiliskKnight extends NPC implements Spawnable, CombatScript {

	public BasiliskKnight(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	private static final Projectile PROJECTILE = new Projectile(1735, 18, 25, 0, 55);
	private static final Projectile NO_SHIELD = new Projectile(1739, 18, 25, 0, 55);
	private static final Graphics SPLASH_PROJECTILE = new Graphics(1736, 0, 124);

	@Override
	public int attack(final Entity target) {
		final BasiliskKnight npc = this;
		if (!(target instanceof Player)) {
			return 0;
		}
		final Player player = (Player) target;
		boolean melee = isWithinMeleeDistance(this, player) && Utils.random(1) == 0;
		attackSound();
		if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
			npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
			delayHit(npc, 0, player, new Hit(npc, 13, HitType.REGULAR));
			WorldTasksManager.schedule(() -> {
				if (!player.getLocation().withinDistance(target.getLocation(), 15)) {
					return;
				}
				for (int i = 0; i <= 6; i++) {
					if (i == 3) {
						continue;
					}
					if (i == 5) {
						player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4078947368421053));
					} else {
						player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.4078947368421053));
					}
				}
			}, World.sendProjectile(npc, target, NO_SHIELD));
		} else {
			npc.setAnimation(new Animation(8500));
			if(melee) {
				delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
			} else {
				int delay = World.sendProjectile(this, player, PROJECTILE);
				delayHit(this, delay, player, new Hit(this, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).
						onLand((hit) -> player.setGraphics(SPLASH_PROJECTILE)));
			}
		}
		return npc.getCombatDefinitions().getAttackSpeed();
	}

	@Override protected void onDeath(Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			((Player) source).getCombatAchievements().complete(CAType.REFLECTING_ON_THIS_ENCOUNTER);
		}
	}

	@Override
	public boolean validate(int id, String name) {
		return id == 9293;
	}
}