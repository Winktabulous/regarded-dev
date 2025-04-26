package com.zenyte.game.world.entity.npc.combat;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.HitEntry;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.AttackDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect;
import com.zenyte.utils.DefaultLogger;


/**
 * @author Kris | 29. juuni 2018 : 02:57:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface CombatScript {
	AttackType MELEE = AttackType.MELEE;
	AttackType RANGED = AttackType.RANGED;
	AttackType MAGIC = AttackType.MAGIC;
	AttackType CRUSH = AttackType.CRUSH;
	AttackType STAB = AttackType.STAB;
	AttackType SLASH = AttackType.SLASH;

	default void delayHit(final NPC npc, final int delay, final Entity target, final Hit... hits) {
		CombatUtilities.delayHit(npc, delay, target, hits);
	}

	default boolean isWithinMeleeDistance(final NPC npc, final Entity target) {
		return CombatUtilities.isWithinMeleeDistance(npc, target);
	}

	default int getRandomMaxHit(final NPC npc, final int maxHit, final AttackType attackStyle, final Entity target) {
		return getRandomMaxHit(npc, maxHit, attackStyle, attackStyle, target);
	}

	default int getRandomMaxHit(final NPC npc, final int maxHit, final AttackType attackStyle, final AttackType targetStyle, final Entity target) {
		return CombatUtilities.getRandomMaxHit(npc, maxHit, attackStyle, targetStyle, target);
	}

	default void processPoison(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (defs.getToxinDefinitions() == null || defs.getToxinDefinitions().getType() != Toxins.ToxinType.POISON || Utils.random(3) != 0) {
			return;
		}
		target.getToxins().applyToxin(Toxins.ToxinType.POISON, defs.getToxinDefinitions().getDamage(), npc);
	}

	int attack(final Entity target);

	default void animate() {
		((NPC) this).setAnimation(((NPC) this).getCombatDefinitions().getAttackDefinitions().getAnimation());
	}

	default void attackSound() {
		try {
			final SoundEffect sound = ((NPC) this).getCombatDefinitions().getAttackDefinitions().getStartSound();
			if (sound == null || sound.getId() <= 0) {
				return;
			}
			World.sendSoundEffect(((NPC) this).getMiddleLocation(), sound);
		} catch (Exception e) {
			DefaultLogger.logger.error("", e);
		}
	}

	default Hit executeMeleeHit(final Entity target, final int max) {
		final Hit hit = melee(target, max);
		delayHit(0, target, hit);
		return hit;
	}

	default void executeMeleeHit(final Entity target, final HitType type, final int max) {
		final NPC npc = (NPC) this;
		final Hit hit = new Hit(npc, getRandomMaxHit(npc, max, MELEE, target), type);
		delayHit(0, target, hit);
	}

	default void useSpell(final CombatSpell spell, final Entity target) {
		useSpell(spell, target, spell.getMaxHit());
	}

	default void useSpell(final CombatSpell spell, final Entity target, final int maxHit) {
		final NPC npc = (NPC) this;
		final Projectile projectile = spell.getProjectile();
		int delay = 1;
		int clientDelay = 30;
		final Graphics castGfx = spell.getCastGfx();
		if (castGfx != null) {
			npc.setGraphics(castGfx);
		}
		if (projectile != null) {
			clientDelay = projectile.getProjectileDuration(npc.getLocation(), target.getLocation());
			if (projectile.getGraphicsId() != -1) {
				delay = World.sendProjectile(npc, target, projectile);
			} else {
				delay = projectile.getTime(npc, target);
			}
		}
		final SoundEffect sound = spell.getHitSound();
		final Graphics gfx = spell.getHitGfx();
		final SpellEffect effect = spell.getEffect();
		npc.setAnimation(spell.getAnimation());
		final HitEntry hitEntry = new HitEntry(npc, delay, magic(target, maxHit));
		target.appendHitEntry(hitEntry);//Processes prayer modifications, we need to know the max hit post-prayer to know whether it's a splash or not.
		if (hitEntry.getHit().getDamage() > 0) {
			if (gfx != null) {
				target.setGraphics(new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
			}
			if (sound != null) {
				World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));
			}
			delayHit(delay, target, hitEntry.getHit().onLand(hit -> {
				if (effect != null) {
					effect.spellEffect(npc, target, hit.getDamage());
				}
			}));
		} else {
			if (sound != null) {
				World.sendSoundEffect(target.getLocation(), new SoundEffect(227, 10, clientDelay));
			}
			if (gfx != null) {
				target.setGraphics(new Graphics(Default.SPLASH_GRAPHICS.getId(), clientDelay, Default.SPLASH_GRAPHICS.getHeight()));
			}
		}
	}

	default void delayHit(final int delay, final Entity target, final Hit... hits) {
		CombatUtilities.delayHit((NPC) this, delay, target, hits);
	}

	default void playAttackSound(final Entity target) {
		if (target instanceof Player) {
			final NPC npc = (NPC) this;
			final AttackDefinitions attDefs = npc.getCombatDefinitions().getAttackDefinitions();
			final SoundEffect sound = attDefs.getStartSound();
			if (sound != null) {
				((Player) target).sendSound(sound);
			}
		}
	}

	default void applyHit(final Entity target, final Hit... hits) {
		CombatUtilities.delayHit((NPC) this, -1, target, hits);
	}

	/**
	 * Creates a pure melee hit.
	 * 
	 * @param target
	 *            the target who is being hit by it.
	 * @param max
	 *            the maximum hit possible.
	 * @return a melee hit.
	 */
	default Hit melee(final Entity target, final int max) {
		final NPC npc = (NPC) this;
		return new Hit(npc, getRandomMaxHit(npc, max, MELEE, target), HitType.MELEE);
	}

	default Hit magic(final Entity target, final int max) {
		final NPC npc = (NPC) this;
		return new Hit(npc, getRandomMaxHit(npc, max, MAGIC, target), HitType.MAGIC);
	}

	default Hit ranged(final Entity target, final int max) {
		final NPC npc = (NPC) this;
		return new Hit(npc, getRandomMaxHit(npc, max, RANGED, target), HitType.RANGED);
	}

	default Hit magicalMelee(final Entity target, final int max) {
		final NPC npc = (NPC) this;
		return new Hit(npc, getRandomMaxHit(npc, max, MELEE, MAGIC, target), HitType.MELEE);
	}
}
