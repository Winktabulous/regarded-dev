package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.room.ScavengerRoom;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 18. nov 2017 : 23:56.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ScavengerBeast extends RaidNPC<ScavengerRoom> implements CombatScript {
	public ScavengerBeast(final ScavengerRoom room, final Raid raid, final Location tile) {
		super(raid, room, 7548 + Utils.random(1), tile);
		this.maxDistance = 4;
		this.setForceAggressive(true);
		final int combatHitpointsOffset = Math.max(59, Math.min(raid.getCombatLevel(), 126));
		final float combatHitpointsMultiplier = combatHitpointsOffset / 126.0F;
		final int combatOffset = Math.max(70, Math.min(raid.getCombatLevel(), 126));
		final float combatMultiplier = combatOffset / 126.0F;
		aggressiveLevelMultiplier = combatMultiplier;
		defenceMultiplier = combatMultiplier;
		hitpointsMultiplier = combatHitpointsMultiplier;
	}

	@Override
	protected boolean isToxinImmune() {
		return true;
	}

	@Override
	public float getXpModifier(final Hit hit) {
		return 0.1F;
	}

	@Override
	public int getRespawnDelay() {
		return 2;
	}

	@Override
	public boolean isTolerable() {
		return false;
	}



	@Override
	protected Player getDropRecipient() {
		return getMostDamagePlayer();
	}

	@Override
	protected void drop(final Location tile) {
		final Player killer = getDropRecipient();
		if (killer == null) {
			return;
		}
		onDrop(killer);
		final List<DropProcessor> processors = DropProcessorLoader.get(id);
		if (processors != null) {
			for (final DropProcessor processor : processors) {
				processor.onDeath(this, killer);
			}
		}
		final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
		if (drops == null) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			final int finalI = i;
			NPCDrops.rollTable(killer, drops, drop -> {
				if (drop.isAlways() && finalI != 0) {
					return;
				}
				dropItem(killer, drop, tile);
			});
		}
	}

	private static final Animation attack = new Animation(255);

	@Override
	public int attack(final Entity target) {
		setAnimation(attack);
		delayHit(0, target, melee(target, (int) (13 * (raid.isChallengeMode() ? 1.5F : 1.0F))));
		playAttackSound(target);
		return combatDefinitions.getAttackSpeed();
	}

	@Override
	public void setRespawnTask() {
		if (!isFinished()) {
			reset();
			finish();
		}
		WorldTasksManager.schedule(this::spawn, getRespawnDelay());
	}

	@Override
	public NPC spawn() {
		room.findSpawnTile().ifPresent(value -> setRespawnTile(new ImmutableLocation(value)));
		return super.spawn();
	}


	public static final class ScavengerDropProcessor extends DropProcessor {
		@Override
		public void attach() {
			put(526, new PredicatedDrop("Scavenger beasts will always drop three items at a time."));
		}

		@Override
		public int[] ids() {
			return new int[] {7548, 7549};
		}
	}
}
