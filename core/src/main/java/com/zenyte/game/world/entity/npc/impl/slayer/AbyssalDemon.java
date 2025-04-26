package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 12 dec. 2017 : 21:11:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class AbyssalDemon extends NPC implements Spawnable, CombatScript {
	private static final Graphics TELEPORT_GRAPHICS = new Graphics(409);
	private static final byte[][] BASIC_OFFSETS = new byte[][] {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

	public AbyssalDemon(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	private static final SoundEffect teleportSound = new SoundEffect(2398);

	/* Disabled as a part of gameplay improvements */
	private static final boolean TELEPORT_ENABLED = false;

	@Override
	public int attack(Entity target) {
		if (!(target instanceof Player)) {
			return 0;
		}
		attackSound();
		final Player player = (Player) target;
		setAnimation(getCombatDefinitions().getAttackAnim());
		delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
		if(TELEPORT_ENABLED) {
			if (Utils.random(4) == 0) {
				Location tile = null;
				for (int tryCount = 0; tryCount <= 20; tryCount++) {
					if (tryCount == 20) {
						tile = null;
						break;
					}
					final byte[] offsets = BASIC_OFFSETS[Utils.random(3)];
					tile = new Location(getX() + offsets[0], getY() + offsets[1], getPlane());
					if (!target.isProjectileClipped(tile, true)) {
						break;
					}
				}
				if (tile != null) {
					player.setLocation(tile);
					player.setGraphics(TELEPORT_GRAPHICS);
					World.sendSoundEffect(tile, teleportSound);
				}
			}
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			player.getAchievementDiaries().update(MorytaniaDiary.KILL_ABYSSAL_DEMON);
		}
	}

	@Override
	public boolean validate(int id, String name) {
		return id == 415 || id == 416 || id == 7241;
	}
}
