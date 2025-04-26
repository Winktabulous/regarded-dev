package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.CombatScriptsHandler;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Kris | 13. veebr 2018 : 21:27.20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DarkBeast extends NPC implements Spawnable, CombatScript {
	public DarkBeast(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		combat = new NPCCombat(this) {
			@Override
			public int combatAttack() {
				if (target == null) {
					return 0;
				}
				final boolean melee = npc.getCombatDefinitions().isMelee();
				if (npc.isProjectileClipped(target, melee)) {
					return 0;
				}
				int distance = npc.getAttackDistance();
				if (CollisionUtil.collides(npc.getX(), npc.getY(), npc.getSize(), target.getX(), target.getY(), target.getSize())) {
					return 0;
				}
				if (outOfRange(target, distance, target.getSize(), false)) {
					return 0;
				}
				if (!npc.hasWalkSteps() && outOfRange(target, 0, target.getSize(), true)) {
					return sendMagicAttack(npc, target);
				}
				addAttackedByDelay(target);
				return CombatScriptsHandler.specialAttack(npc, target);
			}
			@Override
			protected boolean appendMovement() {
				final boolean melee = npc.getCombatDefinitions().isMelee();
				final int maxDistance = npc.isForceFollowClose() || melee ? 0 : npc.getAttackDistance();
				if (npc.isProjectileClipped(target, npc.isForceFollowClose() || melee) || outOfRange(target, maxDistance, target.getSize(), melee)) {
					npc.resetWalkSteps();
					npc.calcFollow(target, npc.isRun() ? 2 : 1, true, npc.isIntelligent(), npc.isEntityClipped());
				}
				return true;
			}
			@Override
			public void removeTarget() {
				super.removeTarget();
				((DarkBeast) npc).usingMagic = false;
			}
		};
	}

	private boolean usingMagic;

	@Override
	public void autoRetaliate(final Entity source) {
		super.autoRetaliate(source);
		if (!combat.underCombat()) {
			usingMagic = true;
		}
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 4005 || id == 7250;
	}

	private static final Projectile MAGICAL_PROJ = new Projectile(130, 35, 30, 40, 5, 38, 64, 5);
	private static final Graphics SPLASH = new Graphics(85, 0, 92);
	private static final Graphics GFX = new Graphics(131, 0, 92);

	@Override
	public int attack(final Entity target) {
		final DarkBeast beast = this;
		if (beast.isUsingMagic()) {
			return sendMagicAttack(beast, target);
		}
		attackSound();
		beast.setAnimation(beast.getCombatDefinitions().getAttackAnim());
		delayHit(beast, 0, target, new Hit(beast, getRandomMaxHit(beast, 17, MELEE, target), HitType.MELEE));
		return beast.getCombatDefinitions().getAttackSpeed();
	}

	private static final int sendMagicAttack(final NPC npc, final Entity target) {
		npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
		CombatUtilities.delayHit(npc, World.sendProjectile(npc, target, MAGICAL_PROJ), target, new Hit(npc, CombatUtilities.getRandomMaxHit(npc, 8, MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() == 0 ? SPLASH : GFX)));
		return npc.getCombatDefinitions().getAttackSpeed();
	}

	public boolean isUsingMagic() {
		return usingMagic;
	}

	public void setUsingMagic(boolean usingMagic) {
		this.usingMagic = usingMagic;
	}
}
