package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.content.skills.agility.Shortcut;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.RenderAnimation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

public class ManeuverTaverlyGap implements Shortcut {

	private static final RenderAnimation RENDER = new RenderAnimation(755, 755, 754, 754, 754, 754, -1);
	private static final RenderAnimation RENDER_BACKWARDS = new RenderAnimation(757, 757, 756, 756, 756, 756, -1);

	private static final Animation SQUEEZE = new Animation(752);
	private static final Animation TURN = new Animation(758);

	private static final Animation SQUEEZE_BACKWARDS = new Animation(753);
	private static final Animation TURN_BACKWARDS = new Animation(759);
	
	private static final int FALADOR_GAP = 47975873;

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final boolean direction = object.getPositionHash() == FALADOR_GAP;
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;

			@Override
			public void run() {
				if (ticks == 0) {
					if (direction) {
						player.getAppearance().setRenderAnimation(RENDER);
						player.setAnimation(SQUEEZE);
						player.addWalkSteps(2928, 3521, -1, false);
					} else {
						player.getAppearance().setRenderAnimation(RENDER_BACKWARDS);
						player.setAnimation(SQUEEZE_BACKWARDS);
						player.addWalkSteps(2927, 3522, -1, false);
					}
				} else if (ticks == 1) {
					if (direction)
						player.addWalkSteps(2927, 3522, -1, false);
					else
						player.addWalkSteps(2928, 3521, -1, false);
				} else if (ticks == 2) {
					if (direction)
						player.addWalkSteps(2926, 3522, -1, false);
					else
						player.addWalkSteps(2928, 3520, -1, false);
				} else if (ticks == 3) {
					player.getAppearance().resetRenderAnimation();
					if (direction)
						player.setAnimation(TURN);
					else
						player.setAnimation(TURN_BACKWARDS);
					stop();
				}
				ticks++;
			}

		}, 0, 0);

	}

	@Override
	public int getLevel(final WorldObject object) {
		return 0;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 16468 };
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 0;
	}

}
