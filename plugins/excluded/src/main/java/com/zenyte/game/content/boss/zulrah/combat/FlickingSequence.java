package com.zenyte.game.content.boss.zulrah.combat;

import com.zenyte.game.content.boss.zulrah.Sequence;
import com.zenyte.game.content.boss.zulrah.ZulrahInstance;
import com.zenyte.game.content.boss.zulrah.ZulrahNPC;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Kris | 19. march 2018 : 20:41.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FlickingSequence implements Sequence {
	private static final Animation ANIM = new Animation(5069);
	private static final Projectile MAGIC_PROJ = new Projectile(1046, 65, 10, 40, 15, 18, 0, 5);
	private static final Projectile RANGED_PROJ = new Projectile(1044, 65, 10, 40, 15, 18, 0, 5);
	private static final SoundEffect RANGED_SEND = new SoundEffect(213, 15);
	private static final SoundEffect RANGED_IMPACT = new SoundEffect(224, 15);
	private static final SoundEffect MAGIC_SEND = new SoundEffect(162, 15);
	private static final SoundEffect MAGIC_IMPACT = new SoundEffect(163, 15);

	public FlickingSequence(final int startingStyle) {
		this.startingStyle = startingStyle;
	}

	private final int startingStyle;

	@Override
	public void attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		zulrah.lock();
		zulrah.setFaceEntity(target);
		final boolean shortSequence = zulrah.getRotation() == 3;
		WorldTasksManager.schedule(new WorldTask() {
			private int delaySinceLastAttack = 3;
			private int amount;
			@Override
			public void run() {
				if (zulrah.isCancelled(false)) {
					zulrah.lock(3);
					stop();
					return;
				}
				if (zulrah.isStopped()) {
					return;
				}
				if (zulrah.getFaceEntity() < 0) {
					zulrah.setFaceEntity(target);
				}
				if (++delaySinceLastAttack < 3) {
					return;
				}
				delaySinceLastAttack = 0;
				zulrah.setAnimation(ANIM);
				final boolean mage = ((amount + (startingStyle == ZulrahNPC.MAGIC ? 1 : 0)) & 1) != 0;
				final Hit hit = new Hit(zulrah, CombatUtilities.getRandomMaxHit(zulrah, 41, mage ? CombatScript.MAGIC : CombatScript.RANGED, target), mage ? HitType.MAGIC : HitType.RANGED);
				if (mage) {
					zulrah.delayHit(World.sendProjectile(zulrah, target, MAGIC_PROJ), hit);
					World.sendSoundEffect(zulrah, MAGIC_SEND);
					World.sendSoundEffect(new Location(target.getLocation()), new SoundEffect(MAGIC_IMPACT.getId(), MAGIC_IMPACT.getRadius(), MAGIC_PROJ.getProjectileDuration(zulrah.getLocation(), target.getLocation())));
				} else {
					World.sendSoundEffect(zulrah, RANGED_SEND);
					World.sendSoundEffect(new Location(target.getLocation()), new SoundEffect(RANGED_IMPACT.getId(), RANGED_IMPACT.getRadius(), RANGED_PROJ.getProjectileDuration(zulrah.getLocation(), target.getLocation())));
					zulrah.delayHit(World.sendProjectile(zulrah, target, RANGED_PROJ), hit);
				}
				if (++amount >= (shortSequence ? 8 : 10)) {
					zulrah.lock(3);
					stop();
				}
			}
		}, 0, 0);
	}
}
