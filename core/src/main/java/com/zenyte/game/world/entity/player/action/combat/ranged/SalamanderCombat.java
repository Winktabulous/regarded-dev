package com.zenyte.game.world.entity.player.action.combat.ranged;

import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.AttackStyle.AttackExperienceType;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialType;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import mgi.utilities.CollectionUtils;

import java.util.EnumMap;

/**
 * @author Kris | 1. juuni 2018 : 17:02:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SalamanderCombat extends RangedCombat {

	private static final class StyleDefinition {
		private final Animation animation;
		private final Graphics graphics;
		private final SoundEffect sound;

		public StyleDefinition(Animation animation, Graphics graphics, SoundEffect sound) {
			this.animation = animation;
			this.graphics = graphics;
			this.sound = sound;
		}
	}


	private enum Tar {
		GUAM(AmmunitionDefinitions.GUAM_TAR, new StyleDefinition(new Animation(5247), new Graphics(953, 0, 104), new SoundEffect(734)), new StyleDefinition(new Animation(-1), new Graphics(953, 0, 104), new SoundEffect(735)), new StyleDefinition(new Animation(-1), new Graphics(953, 0, 104), new SoundEffect(734))), MARRENTILL(AmmunitionDefinitions.MARRENTILL_TAR, new StyleDefinition(new Animation(5247), new Graphics(952, 0, 104), new SoundEffect(738)), new StyleDefinition(new Animation(-1), new Graphics(952, 0, 104), new SoundEffect(736)), new StyleDefinition(new Animation(-1), new Graphics(952, 0, 104), new SoundEffect(740))), TARROMIN(AmmunitionDefinitions.TARROMIN_TAR, new StyleDefinition(new Animation(5247), new Graphics(952, 0, 104), new SoundEffect(738)), new StyleDefinition(new Animation(-1), new Graphics(952, 0, 104), new SoundEffect(736)), new StyleDefinition(new Animation(-1), new Graphics(952, 0, 104), new SoundEffect(740))), HARRALANDER(AmmunitionDefinitions.HARRALANDER_TAR, new StyleDefinition(new Animation(5247), new Graphics(952, 0, 104), new SoundEffect(738)), new StyleDefinition(new Animation(-1), new Graphics(952, 0, 104), new SoundEffect(736)), new StyleDefinition(new Animation(-1), new Graphics(952, 0, 104), new SoundEffect(740)));
		private final AmmunitionDefinitions definitions;
		private final StyleDefinition[] styles;

		Tar(final AmmunitionDefinitions definitions, final StyleDefinition... styles) {
			this.definitions = definitions;
			this.styles = styles;
		}

		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		private static final EnumMap<AmmunitionDefinitions, Tar> map;

		static {
			CollectionUtils.populateMap(values(), map = new EnumMap<>(AmmunitionDefinitions.class), v -> v.definitions);
		}
	}

	public SalamanderCombat(final Entity target, final AmmunitionDefinition defs) {
		super(target, defs);
	}

	@Override
	protected boolean withinRange(final Position targetPosition, int maximumDistance, final int targetSize) {
		final Location target = targetPosition.getPosition();
		final int distanceX = player.getX() - target.getX();
		final int distanceY = player.getY() - target.getY();
		final int npcSize = player.getSize();
		return !(distanceX > targetSize + maximumDistance || distanceY > targetSize + maximumDistance || distanceX < -npcSize - maximumDistance || distanceY < -npcSize - maximumDistance);
	}

	@Override
	public int processAfterMovement() {
		if (!isWithinAttackDistance()) {
			return 0;
		}
		if (!canAttack()) {
			return -1;
		}
		final RegionArea area = player.getArea();
		if (area instanceof PlayerCombatPlugin) {
			((PlayerCombatPlugin) area).onAttack(player, target, "Ranged", null, false);
		}
		addAttackedByDelay(player, target);
		if (player.getCombatDefinitions().isUsingSpecial()) {
			final int delay = useSpecial(player, SpecialType.RANGED);
			if (delay != -1) {
				return delay - 1;
			}
		}
		final int ticks = this.fireProjectile();
		//animate();
		dropAmmunition(ticks, true);
		final SalamanderCombat.Tar tar = Tar.map.get(ammunition);
		assert tar != null : "No tar found for ammunition " + ammunition;
		resetFlag();
		final int style = player.getCombatDefinitions().getStyle();
		final SalamanderCombat.StyleDefinition tarStyle = tar.styles[Math.min(style, tar.styles.length - 1)];
		player.setGraphics(tarStyle.graphics);
		player.setAnimation(tarStyle.animation);
		player.getPacketDispatcher().sendSoundEffect(tarStyle.sound);
		Hit hit;
		if (style == 0) {
			hit = getMeleeHit(player, target, 1, 1);
		} else if (style == 1) {
			hit = getHit(player, target, 1, 1, 1, false);
		} else {
			hit = getMagicHit(player, target, 1, 1);
		}
		if (hit.getDamage() > 0 && hit.getHitType() == HitType.RANGED) {
			addPoisonTask(ticks);
		}
		hit.putAttribute("salamander_weapon", Boolean.TRUE);
		delayHit(ticks, hit);
		//drawback();
		checkIfShouldTerminate(hit.getHitType());
		return getWeaponSpeed();
	}

	public Hit getMeleeHit(final Player player, final Entity target, final double accuracyModifier, final double strengthModifier) {
		return new Hit(player, getRandomMeleeHit(player, target, getMeleeMaxHit(player, strengthModifier), accuracyModifier), HitType.MELEE);
	}

	public int getRandomMeleeHit(final Player player, final Entity target, final int maxhit, final double modifier) {
		final AttackType type = player.getCombatDefinitions().getAttackType();
		return getRandomHit(player, target, maxhit, modifier, type);
	}

	/*public final int getRandomMeleeHit(final Player player, final Entity target, final int maxhit,
                                        final double modifier,
			final int oppositeIndex) {
		final int hit = target.getRandomMeleeHit(player, maxhit, modifier * accuracyModifier, oppositeIndex);
		if (hit > -1) {
			return hit;
		}
		float effectiveLevel = player.getSkills().getLevel(Skills.ATTACK) + 8;
		effectiveLevel *= player.getPrayerManager().getSkillBoost(Skills.ATTACK);
		final AttackType type = player.getCombatDefinitions().getAttackType();
		final AttackExperienceType attackType = player.getCombatDefinitions().getAttackExperienceType();
		effectiveLevel += attackType == AttackExperienceType.ATTACK_XP ? 3 : attackType == AttackExperienceType.SHARED_XP ? 1 : 0;
		final int targetRoll = getTargetDefenceRoll(target, oppositeIndex);
		final int accuracyBoost = player.getBonuses().getBonus(type.ordinal());
		float roll = (int) (effectiveLevel * (accuracyBoost + 64f));
		roll *= modifier;
		roll *= accuracyModifier;
		float accuracy = 0;
		if (roll > targetRoll) {
			accuracy = 1 - (targetRoll + 2f) / (2 * (roll + 1));
		} else {
			accuracy = roll / (2f * (targetRoll + 1));
		}
		if (player.getPrivilege() == Privilege.ADMINISTRATOR) {
			player.sendFilteredMessage("Accuracy: " + accuracy + ", maximum hit: " + maxhit);
		}
		if (accuracy < Utils.randomDouble()) {
			return 0;
		}
		return Utils.random(maxhit);
	}*/
	public int getMeleeMaxHit(final Player player, final double modifier) {
		float effectiveLevel = player.getSkills().getLevel(SkillConstants.STRENGTH) + 8;
		effectiveLevel *= player.getPrayerManager().getSkillBoost(SkillConstants.STRENGTH);
		final AttackExperienceType attackType = player.getCombatDefinitions().getAttackExperienceType();
		effectiveLevel += attackType == AttackExperienceType.STRENGTH_XP ? 3 : attackType == AttackExperienceType.SHARED_XP ? 1 : 0;
		effectiveLevel *= maxhitModifier;
		final int equipmentStrength = player.getBonuses().getBonus(10);
		final double max = 0.5F + effectiveLevel * (equipmentStrength + 64) / 640.0F * modifier;
		return (int) max;
	}

	public Hit getMagicHit(final Player player, final Entity target, final double accuracyModifier, final double strengthModifier) {
		return new Hit(player, getRandomHit(player, target, getMaxHit(player, strengthModifier, 1, false), accuracyModifier), HitType.MAGIC);
	}

	/*public final int getRandomMagicHit(final Player player, final Entity target, final int maxhit,
                                        final double modifier) {
		return getRandomHit(player, target, maxhit, modifier, 8);
	}*/
	public int getMagicMaxHit(final Player player, final double modifier) {
		float damage = (int) Math.floor((0.5F + player.getSkills().getLevel(SkillConstants.MAGIC) * (64 + getSalamanderStrength(player)) / 640.0F));
		float equipmentMultiplier = 1 + (player.getBonuses().getBonus(12) / 100.0F);
		equipmentMultiplier *= maxhitModifier;
		damage *= equipmentMultiplier;
		return (int) damage;
	}

	private static int getSalamanderStrength(final Player player) {
		final Item weapon = player.getWeapon();
		if (weapon == null) {
			return 0;
		}
		switch (weapon.getId()) {
		case 10149: 
			return 56;
		case 10146: 
			return 59;
		case 10147: 
			return 77;
		case 10148: 
			return 92;
		default: 
			return 0;
		}
	}
	/*public int getRandomMagicHit(final Player player, final Entity target, final int maxhit, final double modifier,
                                  final int oppositeIndex) {
		final int hit = target.getRandomMagicHit(player, null, maxhit, modifier * accuracyModifier, oppositeIndex);
		if (hit > -1) {
			return hit;
		}
		float effectiveLevel = player.getSkills().getLevel(Skills.MAGIC) + 8;
		effectiveLevel *= player.getPrayerManager().getMagicBoost(Skills.ATTACK);
		final int targetRoll = getTargetDefenceRoll(target, oppositeIndex);
		final int accuracyBoost = player.getBonuses().getBonus(3);
		float roll = effectiveLevel * (accuracyBoost + 64);
		roll *= accuracyModifier;
		roll *= modifier;
		float accuracy = 0;
		if (roll > targetRoll) {
			accuracy = 1 - (targetRoll + 2f) / (2 * (roll + 1));
		} else {
			accuracy = roll / (2f * (targetRoll + 1));
		}
		if (player.getPrivilege() == Privilege.ADMINISTRATOR) {
			player.sendFilteredMessage("Accuracy: " + accuracy + ", maximum hit: " + maxhit);
		}
		if (accuracy < Utils.randomDouble()) {
			return 0;
		}
		return Utils.random(maxhit);
	}*/
}
