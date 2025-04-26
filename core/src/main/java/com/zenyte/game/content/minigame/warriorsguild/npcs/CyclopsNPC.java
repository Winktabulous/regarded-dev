package com.zenyte.game.content.minigame.warriorsguild.npcs;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemChain;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. dets 2017 : 1:21.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CyclopsNPC extends NPC implements Spawnable {
	public CyclopsNPC(final int id, final Location tile, final Direction facing, final int radius) {
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
					checkDefender(source);
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

	private final void checkDefender(final Entity source) {
		if (!(source instanceof Player)) {
			return;
		}
		if (Utils.random(17) != 0) {
			return;
		}
		final Player player = (Player) source;
		final int currentDefender = ItemChain.DEFENDERS.getHighestCarrying(player);
		final int nextDefender = getNextDefender(currentDefender);
		this.dropItem(player, new Item(nextDefender));
	}

	private final int getNextDefender(final int currentDefender) {
		// Defenders that dont follow the chain or can not be dropped here.
		if (currentDefender == ItemId.RUNE_DEFENDER_T || currentDefender == ItemId.RUNE_DEFENDER || currentDefender == ItemId.DRAGON_DEFENDER || currentDefender == ItemId.DRAGON_DEFENDER_T) {
			return ItemId.RUNE_DEFENDER;
		}
		return ItemChain.DEFENDERS.after(currentDefender, ItemId.BRONZE_DEFENDER);
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
		return id >= 2463 && id <= 2468;
	}
}
