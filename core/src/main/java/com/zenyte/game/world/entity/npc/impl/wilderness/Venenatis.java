package com.zenyte.game.world.entity.npc.impl.wilderness;

/*
import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.boss.BossRespawnTimer;
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
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.calog.CAType;



public class Venenatis extends NPC implements CombatScript, Spawnable {
	private static final Animation MELEE_ATTACK_ANIM = new Animation(5319);
	private static final Animation MAGIC_ATTACK_ANIM = new Animation(5322);
	private static final Graphics PRAYER_DRAIN_GFX = new Graphics(172, 0, 92);
	private static final Graphics STUN_GFX = new Graphics(254, 0, 92);
	private static final Projectile MAGIC_ATTACK_PROJ = new Projectile(165, 43, 25, 30, 15, 18, 64, 5);

	public Venenatis(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int getRespawnDelay() {
		return BossRespawnTimer.VENENATIS.getTimer().intValue();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof final Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_CALLISTO, 2);
		}
	}

	@Override
	protected void onFinish(Entity source) {
		super.onFinish(source);

		if (source instanceof final Player player) {
			player.getCombatAchievements().checkKcTask("venenatis", 10, CAType.VENENATIS_ADEPT);
			player.getCombatAchievements().checkKcTask("venenatis", 20, CAType.VENENATIS_VETERAN);
		}
	}

	@Override
	public int attack(Entity target) {
		if (target instanceof NPC) {
			return 0;
		}
		final int attack = Utils.random(5);
		final Player player = (Player) target;
		switch (attack) {
		case 0: 
		case 1: 
		case 2: 
		case 3: 
			if (Utils.random(1) == 0) {
				getCombatDefinitions().setAttackStyle("Melee");
				setAnimation(MELEE_ATTACK_ANIM);
				delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
			} else {
				getCombatDefinitions().setAttackStyle("Magic");
				setAnimation(MAGIC_ATTACK_ANIM);
				for (final Entity e : getPossibleTargets(EntityType.PLAYER)) {
					delayHit(this, World.sendProjectile(this, e, MAGIC_ATTACK_PROJ), e, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, e), HitType.MELEE));
				}
			}
			break;
		case 4: 
			getCombatDefinitions().setAttackStyle("Magic");
			setAnimation(MAGIC_ATTACK_ANIM);
			player.setGraphics(PRAYER_DRAIN_GFX);
			player.getPrayerManager().drainPrayerPoints((int) (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 0.35F));
			player.sendMessage("Your prayer was drained!");
			break;
		case 5: 
			getCombatDefinitions().setAttackStyle("Melee");
			setAnimation(MAGIC_ATTACK_ANIM);
			player.stun(2);
			player.setGraphics(STUN_GFX);
			for (int i = 0, len = (Utils.random(4) == 0 ? 2 : 1); i < len; i++) {
				delayHit(this, 0, target, new Hit(this, Utils.random(20, 50), HitType.REGULAR));
			}
			player.sendMessage("Venenatis hurls her web at you, sticking you to the ground.");
			break;
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("venenatis");
	}
}
*/
