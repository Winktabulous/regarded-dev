package com.zenyte.game.content.boss.zulrah.combat;

import com.zenyte.game.content.boss.zulrah.Sequence;
import com.zenyte.game.content.boss.zulrah.ZulrahInstance;
import com.zenyte.game.content.boss.zulrah.ZulrahNPC;
import com.zenyte.game.content.boss.zulrah.ZulrahPosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. march 2018 : 20:48.14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class DiveSequence implements Sequence {

	private static final Animation DIVE_ANIM = new Animation(5072);
	private static final Animation RISE_ANIM = new Animation(5073);
	
	public DiveSequence(final ZulrahPosition pos, final int npcId) {
		this.pos = pos;
		this.npcId = npcId;
	}
	
	private final ZulrahPosition pos;
	private final int npcId;
	
	@Override
	public void attack(ZulrahNPC zulrah, ZulrahInstance instance, Player target) {
		zulrah.setAnimation(DIVE_ANIM);
		zulrah.setCantInteract(true);
		zulrah.lock();
		zulrah.setFaceEntity(null);
		zulrah.getReceivedHits().clear();
		WorldTasksManager.schedule(new WorldTask() {
			private boolean risen;
			private int ticks;
			@Override
			public void run() {
				if (zulrah.isDead() || zulrah.isFinished()) {
					stop();
					return;
				}
				if (!risen) {
					zulrah.setLocation(instance.getLocation(pos.getSpawn()));
					zulrah.setFaceLocation(instance.getLocation(pos.getFace()));
					zulrah.setTransformation(npcId);
					zulrah.setAnimation(RISE_ANIM);
					risen = true;
				} else {
				    if (ticks == 2) {
                        zulrah.unlock();
                    } else if (ticks == 3) {
                        zulrah.setCantInteract(false);
                        stop();
                    }
                    ticks++;
				}
			}
		}, 3, 0);
	}

}
