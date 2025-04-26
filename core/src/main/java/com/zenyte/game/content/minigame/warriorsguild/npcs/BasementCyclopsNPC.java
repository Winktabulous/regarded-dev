package com.zenyte.game.content.minigame.warriorsguild.npcs;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 18. dets 2017 : 0:49.04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BasementCyclopsNPC extends NPC implements Spawnable {
	public BasementCyclopsNPC(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int getRespawnDelay() {
		return 20;
	}

	@Override
	public void sendDeath() {
		final Player source = getMostDamagePlayerCheckIronman();
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			private int loop;
			@Override
			public void run() {
				if (loop == 0) {
					setAnimation(defs.getDeathAnim());
				} else if (loop == 1) {
					drop(getMiddleLocation());
					if (Utils.random(40) == 0) {
						if (source != null) {
							dropItem(source, new Item(ItemId.DRAGON_DEFENDER));
						}
					}
					reset();
					finish();
					if (!isSpawned()) {
						setRespawnTask();
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public void applyHit(final Hit hit) {
		super.applyHit(hit);
		final HitType type = hit.getHitType();
		if (type == HitType.MAGIC || type == HitType.RANGED) {
			hit.setDamage(0);
		}
	}

	@Override
	public float getXpModifier(final Hit hit) {
		final HitType type = hit.getHitType();
		if (type == HitType.MAGIC || type == HitType.RANGED) {
			return 0;
		}
		return 1;
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id >= 2137 && id <= 2142;
	}
}
