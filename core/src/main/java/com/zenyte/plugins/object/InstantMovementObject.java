package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.InstantMovementObjects;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class InstantMovementObject implements ObjectAction {
	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final InstantMovementObjects entry = InstantMovementObjects.entries.get(object.getPositionHash());
		if (entry == null || entry.getId() != object.getId()) {
			return;
		}
		final int delay = entry.getDelay();
		player.lock();
		player.faceObject(object);
		if (entry.getAnimation() != Animation.STOP) {
			player.setAnimation(entry.getAnimation());
		}
		if (entry.equals(InstantMovementObjects.BRIMHAVEN_DUNG_LARGE_STAIRS_BOTTOM) || entry.equals(InstantMovementObjects.BRIMHAVEN_DUNG_LARGE_STAIRS_TOP) || entry.equals(InstantMovementObjects.BRIMHAVEN_DUNG_DEMON_STAIRS_TOP) || entry.equals(InstantMovementObjects.BRIMHAVEN_DUNG_DEMON_STAIRS_BOTTOM)) {
			player.getAchievementDiaries().update(KaramjaDiary.CLIMB_THE_STAIRS_IN_BRIMHAVEN_DUNGEON);
		}
		player.addTemporaryAttribute("tb_remove_delay", (int) (WorldThread.getCurrentCycle() + 1 + delay));
		WorldTasksManager.schedule(() -> {
			if(entry.isScreenFade()) {
				new FadeScreen(player, () -> movePlayer(player, entry)).fade(3);
			} else {
				movePlayer(player, entry);
			}
			player.unlock();
		}, entry.getDelay());
	}

	public void movePlayer(Player player, InstantMovementObjects entry) {
		player.setLocation(entry.getLocation());
		if (entry.getAnimation() != Animation.STOP) {
			player.setAnimation(Animation.STOP);
		}
	}

	@Override
	public Object[] getObjects() {
		final List<Object> list = new ArrayList<>();
		for (InstantMovementObjects entry : InstantMovementObjects.VALUES) if (!list.contains(entry.getId())) list.add(entry.getId());
		return list.toArray(new Object[0]);
	}
}
