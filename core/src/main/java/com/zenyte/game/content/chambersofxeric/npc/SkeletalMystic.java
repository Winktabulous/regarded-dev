package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.room.DarkAltarRoom;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.utilities.CollectionUtils;

/**
 * @author Kris | 16. nov 2017 : 21:18.43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class SkeletalMystic extends RaidNPC<DarkAltarRoom> implements CombatScript {
	public SkeletalMystic(final Raid raid, final DarkAltarRoom room, final int id, final Location tile) {
		super(raid, room, id, tile);
		setForceAggressive(true);
		attackDistance = 7;
		this.maxDistance = 20;
	}

	private int delay;

	@Override
	protected boolean isToxinImmune() {
		return true;
	}

	@Override
	public void onFinish(final Entity source) {
		super.onFinish(source);
		if (CollectionUtils.findMatching(room.getNpcs(), npc -> !npc.isFinished() && !npc.isDead()) != null) {
			return;
		}
		World.removeObject(room.getMark());
	}

	@Override
	public void processNPC() {
		super.processNPC();
		final Entity target = combat.getTarget();
		if (target != null) {
			if (combatDefinitions.getAttackStyle().isMelee()) {
				if (!Utils.isOnRange(getX(), getY(), getSize(), target.getX(), target.getY(), target.getSize(), 1)) {
					if (delay++ > 8) {
						combatDefinitions.setAttackStyle(AttackType.MAGIC);
					}
					return;
				}
			}
		}
		delay = 0;
	}

	@Override
	public double getMagicPrayerMultiplier() {
		return 0.5;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.5;
	}

	@Override
	public boolean isEntityClipped() {
		return true;
	}

	private static final Graphics vulnImpact = new Graphics(169, 0, 90);
	private static final Projectile vulnProj = new Projectile(168, 50, 25, 45, 15, 18, 64, 5);
	private static final Graphics splash = new Graphics(339, 0, 60);
	private static final Graphics fireImpact = new Graphics(157, 0, 90);
	private static final SoundEffect fireStartSound = new SoundEffect(155, 10, 0);
	private static final SoundEffect fireHitSound = new SoundEffect(156, 10, -1);
	private static final SoundEffect splashSound = new SoundEffect(227, 10, -1);
	private static final SoundEffect vulnStartSound = new SoundEffect(127, 10, 0);
	private static final SoundEffect vulnHitSound = new SoundEffect(3012, 10, -1);
	private static final SoundEffect meleeSound = new SoundEffect(511, 10, 0);
	private static final Graphics vulnStart = new Graphics(1321, 0, 96);
	private static final Graphics fireStart = new Graphics(1322, 0, 96);
	private static final Projectile fireProj = new Projectile(130, 50, 25, 45, 15, 18, 64, 5);
	private static final Animation meleeAnim = new Animation(5485);
	private static final Animation magicAnim = new Animation(5523);

	@Override
	public int attack(final Entity target) {
		final boolean melee = combatDefinitions.isMelee();
		final int magic = Utils.random(4);
		if (melee) {
			setAnimation(meleeAnim);
			delayHit(this, 0, target, melee(target, getMaxHit(25)));
			World.sendSoundEffect(getLocation(), meleeSound);
		} else {
			if (magic == 0) {
				setAnimation(magicAnim);
				setGraphics(vulnStart);
				final int delay = World.sendProjectile(this, target, vulnProj);
				final Hit hit = magic(target, combatDefinitions.getMaxHit());
				World.sendSoundEffect(getLocation(), vulnStartSound);
				final SoundEffect sound = hit.getDamage() == 0 ? splashSound : vulnHitSound;
				World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), vulnProj.getProjectileDuration(getLocation(), target.getLocation())));
				WorldTasksManager.schedule(() -> {
					if (!target.getLocation().withinDistance(getLocation(), 25)) {
						return;
					}
					if (hit.getDamage() == 0) {
						target.setGraphics(splash);
						return;
					}
					target.setGraphics(vulnImpact);
					target.drainSkill(SkillConstants.DEFENCE, Utils.random(4, 8));
				}, delay);
			} else {
				setAnimation(magicAnim);
				setGraphics(fireStart);
				final int delay = World.sendProjectile(this, target, fireProj);
				final Hit hit = magic(target, (int) ((25 + (raid.getOriginalPlayers().size() / 4)) * (raid.isChallengeMode() ? 1.5F : 1.0F)));
				delayHit(this, delay, target, hit.onLand(h -> target.setGraphics(hit.getDamage() == 0 ? splash : fireImpact)));
				World.sendSoundEffect(getLocation(), fireStartSound);
				final SoundEffect sound = hit.getDamage() == 0 ? splashSound : fireHitSound;
				World.sendSoundEffect(target.getLocation(), new SoundEffect(sound.getId(), sound.getRadius(), fireProj.getProjectileDuration(getLocation(), target.getLocation())));
			}
		}
		WorldTasksManager.schedule(() -> {
			final boolean canUseMelee = Utils.random(1) == 0 && !isProjectileClipped(target, true);
			combatDefinitions.setAttackStyle(canUseMelee ? AttackType.MELEE : AttackType.MAGIC);
		});
		return combatDefinitions.getAttackSpeed();
	}
}
