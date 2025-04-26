package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 29. juuni 2018 : 21:47:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DefilerNPC extends PestNPC implements CombatScript {

	private static final SoundEffect ATTACK_SOUND_EFFECT = new SoundEffect(392, 5);
	private static final Projectile PROJECTILE = new Projectile(657, 50, 30, 20, 25, 10, 0, 5);

	public DefilerNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
		forceAggressive = true;
		attackDistance = 10;
	}

	@Override
	public int attack(final Entity target) {
		World.sendSoundEffect(this, ATTACK_SOUND_EFFECT);
		setAnimation(combatDefinitions.getAttackAnim());
		delayHit(World.sendProjectile(this, target, PROJECTILE), target, new Hit(this, this.getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.MAGIC));
		return combatDefinitions.getAttackSpeed();
	}

}
