package com.zenyte.game.content.boss.smokedevil;

import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.minigame.barrows.BarrowsWight;
import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.GlobalAreaManager;

import java.util.Set;

/**
 * @author Tommeh | 21 aug. 2018 | 16:29:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
@SuppressWarnings("unused")
public class SmokeDevil extends NPC implements CombatScript, Spawnable {

	private static final Projectile ATTACK_PROJ = new Projectile(644, 30, 30, 30, 0, 28, 0, 5);
	private static final Projectile PROJECTILE = new Projectile(73, 30, 35, 108, 10);
	private boolean hitSomeone = false;
	private boolean specHits = true;

	public SmokeDevil(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		this.maxDistance = 25;
	}

	@Override
	public NPC spawn() {
		hitSomeone = false;
		specHits = true;
		return super.spawn();
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public int getRespawnDelay() {
		return id == 499 ? 14 : 60;
	}

	@Override
	public int attack(final Entity target) {
		if (!(target instanceof final Player player)) {
			return 0;
		}
		hitSomeone = true;
		setAnimation(getCombatDefinitions().getAttackAnim());
		if (!SlayerEquipment.FACE_MASK.isWielding(player)) {
			getCombatDefinitions().setAttackStyle("Magic");
			delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 15, HitType.REGULAR).onLand(hit -> {
				player.getSkills().setLevel(SkillConstants.ATTACK, 0);
				player.getSkills().setLevel(SkillConstants.STRENGTH, 0);
				player.getSkills().setLevel(SkillConstants.RANGED, 0);
				player.getSkills().setLevel(SkillConstants.MAGIC, 0);
				player.getSkills().setLevel(SkillConstants.PRAYER, (int) (player.getSkills().getLevel(SkillConstants.PRAYER) * 0.5064935064935066));
				player.getSkills().setLevel(SkillConstants.DEFENCE, (int) (player.getSkills().getLevel(SkillConstants.DEFENCE) * 0.5064935064935066));
				player.getSkills().setLevel(SkillConstants.AGILITY, (int) (player.getSkills().getLevel(SkillConstants.AGILITY) * 0.5064935064935066));
			}));
		} else {
			delayHit(this, World.sendProjectile(this, target, ATTACK_PROJ), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), isBoss() ? HitType.REGULAR : HitType.RANGED));
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void processNPC() {
		super.processNPC();

		if (WorldThread.getCurrentCycle() % 100 == 0) {
			final Set<Player> players = GlobalAreaManager.get("Thermonuclear Boss Room").getPlayers();
			final int playerCount = players.size();
			if (playerCount == 0) {
				hitSomeone = false;
				specHits = true;
			}
		}
	}

	@Override
	public boolean canAttack(final Player player) {
		return player.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 93;
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (isBoss() && source instanceof final Player player) {
			player.getAchievementDiaries().update(WesternProvincesDiary.KILL_THERMONUCLEAR_SMOKE_DEVIL);
			player.getCombatAchievements().checkKcTask("thermonuclear smoke devil", 20, CAType.THERMONUCLEAR_VETERAN);
			if (specHits) {
				player.getCombatAchievements().complete(CAType.SPECD_OUT);
			}
			if (!hitSomeone) {
				player.getCombatAchievements().complete(CAType.HAZARD_PREVENTION);
			}
		}
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		super.handleIngoingHit(hit);
		if (isBoss() && !hit.isSpecial()) {
			specHits = false;
		}
	}

	@Override
	public void handleOutgoingHit(Entity target, Hit hit) {
		super.handleOutgoingHit(target, hit);
	}

	private boolean isBoss() {
		return id == 499;
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 498 || id == 499;
	}
}
