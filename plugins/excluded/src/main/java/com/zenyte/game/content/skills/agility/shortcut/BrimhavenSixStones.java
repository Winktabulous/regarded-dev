package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 25-3-2019 | 00:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BrimhavenSixStones implements Shortcut {
	private static final Location[] STONES = {new Location(2649, 9561, 0), new Location(2649, 9560, 0), new Location(2648, 9560, 0), new Location(2647, 9560, 0), new Location(2647, 9559, 0), new Location(2647, 9558, 0)};
	private static final Location START = new Location(2649, 9562, 0);
	private static final Location END = new Location(2647, 9557, 0);
	private static final Animation JUMP = new Animation(741);

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.lock();
		final boolean start = object.getId() == 21738;
		final Location destination = start ? END : START;
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			Location location;
			@Override
			public void run() {
				if (ticks >= 0 && ticks <= 10 && ticks % 2 == 0) {
					final int index = start ? ticks / 2 : 5 - ticks / 2;
					location = STONES[index];
					player.autoForceMovement(location, 0, 30);
					player.setAnimation(JUMP);
				} else if (ticks == 12) {
					player.autoForceMovement(destination, 0, 30);
					player.setAnimation(JUMP);
				} else if (ticks == 13) {
					player.getAchievementDiaries().update(KaramjaDiary.CROSS_THE_LAVA);
					player.unlock();
				}
				ticks++;
			}
		}, 1, 0);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 12;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {21738, 21739};
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 14;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 7;
	}
}
