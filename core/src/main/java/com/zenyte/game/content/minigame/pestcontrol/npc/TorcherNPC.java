package com.zenyte.game.content.minigame.pestcontrol.npc;

import com.zenyte.game.content.minigame.pestcontrol.PestControlInstance;
import com.zenyte.game.content.minigame.pestcontrol.PestNPC;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 29. juuni 2018 : 02:35:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TorcherNPC extends PestNPC implements CombatScript {

	private static final Graphics ATTACK_GFX = new Graphics(646);
	private static final SoundEffect[] ATTACK_SOUND_EFFECTS = new SoundEffect[] { new SoundEffect(848, 5), new SoundEffect(846, 5) };
	private static final Graphics IMPACT_GFX = new Graphics(648, 0, 90);
	private static final SoundEffect IMPACT_SOUND_EFFECT = new SoundEffect(847, 5);
	private static final Projectile PROJECTILE = new Projectile(647, 50, 30, 20, 25, 10, 0, 5);

	public TorcherNPC(final PestControlInstance instance, final PestPortalNPC portal, final int id, final Location tile) {
		super(instance, portal, id, tile);
		forceAggressive = true;
		attackDistance = 10;
	}

	@Override
	public int attack(final Entity target) {
		World.sendSoundEffect(this, ATTACK_SOUND_EFFECTS[Utils.random(ATTACK_SOUND_EFFECTS.length - 1)]);
		setGraphics(ATTACK_GFX);
		setAnimation(combatDefinitions.getAttackAnim());
		delayHit(World.sendProjectile(this, target, PROJECTILE), target,
                new Hit(this, this.getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.MAGIC).onLand(hit -> {
                    if (hit.getDamage() > 0) {
                        World.sendSoundEffect(target, IMPACT_SOUND_EFFECT);
                        target.setGraphics(IMPACT_GFX);
                    }
                }));
		return combatDefinitions.getAttackSpeed();
	}

}
