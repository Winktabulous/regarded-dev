package com.zenyte.game.content.boss.nightmare;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;

public class NightmareHuskMagicNPC extends NightmareHuskNPC {

	public static final int ID = NpcId.HUSK_9455;
	public static final int ID_PHOSANIS = NpcId.HUSK_9467;
	private static final Animation ATTACK_ANIMATION = new Animation(8565);
	private static final Projectile PROJECTILE = new Projectile(1776, 30, 30, 20, 1, 30, 0, 0);
	private static final Graphics LAND_GRAPHIC = new Graphics(1777, 0, 96);

	public NightmareHuskMagicNPC(int id, Location tile, Player spawnedFor, BaseNightmareNPC boss) {
		super(id, tile, spawnedFor, boss);
	}

	@Override
	public int attack(Entity target) {
		setAnimation(ATTACK_ANIMATION);
		delayHit(this, World.sendProjectile(this, target, PROJECTILE), target, magic(target, getCombatDefinitions().getMaxHit()).onLand(hit -> target.setGraphics(LAND_GRAPHIC)));
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		//TODO add check for crush later on.
		if (id == ID_PHOSANIS && hit.getHitType() == HitType.MELEE) {
			hit.setDamage(getHitpoints());
		} else {
			getBoss().setCrushHour(false);
		}

		super.handleIngoingHit(hit);
	}

}
