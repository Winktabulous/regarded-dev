package com.zenyte.game.content.minigame.barrows.wights;

import com.zenyte.game.content.minigame.barrows.BarrowsWightNPC;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

/**
 * @author Kris | 29. sept 2018 : 05:45:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class AhrimTheBlighted extends BarrowsWightNPC implements Spawnable, CombatScript {
	private static final Graphics AHRIMS_GFX = new Graphics(400, 0, 96);
	private static final Graphics SPLASH_GRAPHICS = new Graphics(85, 0, 124);

	public AhrimTheBlighted(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(final Entity target) {
		final CombatSpell spell = Utils.random(6) != 0 ? CombatSpell.FIRE_WAVE : Utils.getRandomElement(CombatSpell.CONFUSE, CombatSpell.WEAKEN, CombatSpell.CURSE);
		setAnimation(spell.getAnimation());
		setGraphics(spell.getCastGfx());
		this.delayHit(World.sendProjectile(this, target, spell.getProjectile()), target, new Hit(this, this.getRandomMaxHit(this, combatDefinitions.getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> {
			if (hit.getDamage() <= 0) {
				target.setGraphics(SPLASH_GRAPHICS);
				return;
			}
			target.setGraphics(spell.getHitGfx());
			if (spell != CombatSpell.FIRE_WAVE) {
				spell.getEffect().spellEffect(this, target, hit.getDamage());
				return;
			}
			if (target instanceof Player) {
				final Player player = (Player) target;
				if (!player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					if (Utils.random(3) == 0) {
						target.setGraphics(AHRIMS_GFX);
						target.drainSkill(SkillConstants.STRENGTH, 5);
					}
				}
			}
		}));
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		switch (id) {
			case 1672:
			case 16052: {
				return true;
			}
			default:
				return false;
		}
	}

}
