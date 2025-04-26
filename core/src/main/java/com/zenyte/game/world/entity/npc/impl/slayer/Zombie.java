package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 2 okt. 2018 | 14:03:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Zombie extends NPC implements Spawnable {
	public Zombie(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			player.getAchievementDiaries().update(LumbridgeDiary.KILL_A_ZOMBIE);
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return name.equals("zombie");
	}
}
