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
 * @author Kris | 19. march 2018 : 20:18.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class RangedSequence implements Sequence {

	private static final Animation ANIM = new Animation(5069);
	private static final Projectile PROJ = new Projectile(1044, 65, 10, 40, 15, 18, 0, 5);

    private static final SoundEffect RANGED_SEND = new SoundEffect(213, 15);
    private static final SoundEffect RANGED_IMPACT = new SoundEffect(224, 15);

	public RangedSequence(final int amount) {
		this.amount = amount;
	}

	private final int amount;

	@Override
	public void attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		zulrah.lock();
		zulrah.setFaceEntity(target);
		WorldTasksManager.schedule(new WorldTask() {
			private int num = amount;
            private int delaySinceLastAttack = 3;
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
                World.sendSoundEffect(zulrah, RANGED_SEND);
                World.sendSoundEffect(new Location(target.getLocation()), new SoundEffect(RANGED_IMPACT.getId(), RANGED_IMPACT.getRadius(),
                        PROJ.getProjectileDuration(zulrah.getLocation(), target.getLocation())));
                zulrah.delayHit(World.sendProjectile(zulrah, target, PROJ), new Hit(zulrah, CombatUtilities.getRandomMaxHit(zulrah, 41, CombatScript.RANGED, target),
                        HitType.RANGED));
				if (--num <= 0) {
					zulrah.lock(3);
					stop();
				}
			}
		}, 0, 0);
	}

}
