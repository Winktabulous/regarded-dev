package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;

/**
 * @author Kris | 12. veebr 2018 : 12:18.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
public final class MetallicDragon extends NPC implements CombatScript, Spawnable {
	private static final Projectile DRAGONFIRE_PROJ = new Projectile(54, 30, 30, 38, 10, 28, 0, 5);
	private static final Animation ATTACK_ANIM = new Animation(80);
	private static final Animation SECONDARY_ATTACK_ANIM = new Animation(91);
	private static final Animation DRAGONFIRE_ANIM = new Animation(81);

	public MetallicDragon(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			final String name = getName(player);
			if (name.contains("Mithril")) {
				player.getAchievementDiaries().update(KandarinDiary.KILL_A_MITHRIL_DRAGON);
			} else {
				player.getAchievementDiaries().update(KaramjaDiary.KILL_A_METAL_DRAGON);
			}
		}
	}

	@Override
	public int attack(final Entity target) {
		final int style = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 0);
		if (style == 0 && target instanceof Player) {
			setAnimation(DRAGONFIRE_ANIM);
			final Player player = (Player) target;
			final boolean perk = player.getPerkManager().isValid(PerkWrapper.BACKFIRE);
			final double modifier = !perk ? 1 : Math.max(0, Utils.randomDouble() - 0.25F);
			final Dragonfire dragonfire = new Dragonfire(DragonfireType.CHROMATIC_DRAGONFIRE, 50, DragonfireProtection.getProtection(this, player));
			final int deflected = !perk ? 0 : ((int) Math.floor(dragonfire.getMaximumDamage() * modifier));
			delayHit(World.sendProjectile(this, target, DRAGONFIRE_PROJ), target, new Hit(this, Utils.random(Math.max(0, dragonfire.getDamage() - deflected)), HitType.REGULAR).onLand(hit -> {
				PlayerCombat.appendDragonfireShieldCharges(player);
				player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's fiery breath"));
				if (perk) {
					dragonfire.backfire(this, player, 0, deflected);
				}
			}));
		} else {
			setAnimation(Utils.random(1) == 0 ? ATTACK_ANIM : SECONDARY_ATTACK_ANIM);
			delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("steel dragon") || name.equals("bronze dragon") || name.equals("iron dragon") || name.equals("mithril dragon");
	}
}
