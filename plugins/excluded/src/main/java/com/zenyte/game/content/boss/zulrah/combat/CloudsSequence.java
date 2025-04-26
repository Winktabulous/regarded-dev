package com.zenyte.game.content.boss.zulrah.combat;

import com.zenyte.game.content.boss.zulrah.Sequence;
import com.zenyte.game.content.boss.zulrah.ZulrahInstance;
import com.zenyte.game.content.boss.zulrah.ZulrahNPC;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. march 2018 : 20:09.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CloudsSequence implements Sequence {

	private static final Animation ANIM = new Animation(5069);
	private static final Projectile PRIMARY_PROJ = new Projectile(1045, 65, 10, 40, 15, 28, 0, 5);
	private static final Projectile SECONDARY_PROJ = new Projectile(1045, 65, 10, 40, 15, 58, 0, 5);

	private static final SoundEffect SOUND_EFFECT = new SoundEffect(796, 15);

    public CloudsSequence(final Location primaryTile, final Location secondaryTile) {
		this.primaryTile = primaryTile;
		this.secondaryTile = secondaryTile;
	}

	private final Location primaryTile, secondaryTile;

	@Override
	public void attack(final ZulrahNPC zulrah, final ZulrahInstance instance, final Player target) {
		if (!zulrah.isLocked()) {
			zulrah.lock(3);
		}
		zulrah.setFaceEntity(null);
		final Location primaryTile = instance.getLocation(this.primaryTile);
		final Location secondaryTile = instance.getLocation(this.secondaryTile);
		final Location faceTile = new Location((primaryTile.getX() + secondaryTile.getX()) / 2,
				(primaryTile.getY() + secondaryTile.getY()) / 2, zulrah.getPlane());
		zulrah.setFaceLocation(faceTile);
		zulrah.setAnimation(ANIM);
		World.sendSoundEffect(zulrah, SOUND_EFFECT);
		World.sendProjectile(zulrah, primaryTile, PRIMARY_PROJ);
		World.sendProjectile(zulrah, secondaryTile, SECONDARY_PROJ);
		zulrah.addCloud(primaryTile, PRIMARY_PROJ.getTime(zulrah, primaryTile));
		zulrah.addCloud(secondaryTile, SECONDARY_PROJ.getTime(zulrah, secondaryTile));
	}

}
