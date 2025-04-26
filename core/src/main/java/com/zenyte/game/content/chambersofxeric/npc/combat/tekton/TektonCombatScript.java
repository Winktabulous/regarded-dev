package com.zenyte.game.content.chambersofxeric.npc.combat.tekton;

import com.zenyte.game.content.chambersofxeric.npc.Tekton;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 9. mai 2018 : 23:48:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TektonCombatScript implements TektonScript {
	@Override
	public void execute(final Tekton tekton) {
		WorldTasksManager.schedule(new WorldTask() {
			private int stage;
			private int attackType;
			private boolean strike;
			private Direction direction;
			private Direction lastDirection;
			private final IntList list = new IntArrayList(6);
			private final List<Entity> reachableTargets = new ArrayList<>(tekton.getRaid().getPlayers().size());
			private final boolean enraged = tekton.isEnraged();
			@Override
			public void run() {
				if (tekton.getRoom().getRaid().isDestroyed() || tekton.isDead() || tekton.isFinished()) {
					stop();
					return;
				}
				/* After 30 consecutive successful attacks, tekton retreats back to the anvil. */
				if (stage >= (enraged ? 45 : 30)) {
					tekton.setOnceReturnedToAnvil();
					tekton.execute("return to anvil");
					stop();
					return;
				}
				if (stage % 3 == 0) {
					fillReachableTargets(tekton, reachableTargets);
					if (reachableTargets.isEmpty()) {
						tekton.execute("return to anvil");
						stop();
						return;
					}
					if (!CombatUtilities.isWithinMeleeDistance(tekton, tekton.getTarget())) {
						tekton.setTarget(reachableTargets.get(Utils.random(reachableTargets.size() - 1)));
					}
					attackType = Utils.random(2);
					if (attackType == 0) {
						tekton.animate("stab");
					} else if (attackType == 1) {
						tekton.animate("slash");
					} else {
						tekton.animate("crush");
					}
					direction = tekton.faceLocation(tekton.getTarget().getLocation());
					if (lastDirection == null) {
						lastDirection = direction;
					}
					strike = true;
				} else if (strike) {
					strike = false;
					if (attackType == 1) {
						fillSlashTiles(list, lastDirection, tekton);
					} else {
						fillHitTiles(list, lastDirection, tekton);
					}
					fillReachableTargets(tekton, reachableTargets);
					for (final Entity target : reachableTargets) {
						if (CollisionUtil.collides(tekton.getX(), tekton.getY(), tekton.getSize(), target.getX(), target.getY(), target.getSize()) || list.contains(target.getLocation().getPositionHash())) {
							final boolean enraged = tekton.isEnraged();
							final boolean challenge = tekton.getRoom().getRaid().isChallengeMode();
							final int max = enraged ? challenge ? 87 : 59 : (challenge ? 78 : 52);
							tekton.getCombatDefinitions().setAttackStyle(attackType == 0 ? "Stab" : attackType == 1 ? "Slash" : "Crush");
							final int hit = CombatUtilities.getRandomMaxHit(tekton, max, CombatScript.MELEE, CombatScript.MELEE, target);
							CombatUtilities.delayHit(tekton, 0, target, new Hit(tekton, hit, HitType.MELEE));
						}
					}
					lastDirection = direction;
				}
				stage++;
			}
		}, 0, 0);
	}

	private void fillSlashTiles(final IntList tiles, final Direction direction, final Tekton tekton) {
		if (!tiles.isEmpty()) {
			tiles.clear();
		}
		final int x = tekton.getX();
		final int y = tekton.getY();
		final int size = tekton.getSize();
		final int plane = tekton.getPlane();
		switch (direction) {
		case SOUTH: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + i, y - 1, plane));
			}
			//Also damages the tiles on the west
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x - 1, y + i, plane));
			}
			break;
		case WEST: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x - 1, y + i, plane));
			}
			//Also damages the tiles on the north
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + i, y + size, plane));
			}
			break;
		case NORTH: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + i, y + size, plane));
			}
			//Also damages the tiles on the east
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + i, y + size, plane));
			}
			break;
		default: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + size, y + i, plane));
			}
			//Also damages the tiles on the south
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + size, y + i, plane));
			}
			break;
		}
	}

	private void fillHitTiles(final IntList tiles, final Direction direction, final Tekton tekton) {
		if (!tiles.isEmpty()) {
			tiles.clear();
		}
		final int x = tekton.getX();
		final int y = tekton.getY();
		final int size = tekton.getSize();
		final int plane = tekton.getPlane();
		switch (direction) {
		case SOUTH: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + i, y - 1, plane));
			}
			break;
		case WEST: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x - 1, y + i, plane));
			}
			break;
		case NORTH: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + i, y + size, plane));
			}
			break;
		default: 
			for (int i = 0; i < size; i++) {
				tiles.add(Location.getHash(x + size, y + i, plane));
			}
			break;
		}
	}

	/**
	 * Constructs a list of possible targets that are next to tekton, within melee distance, and not underneath it.
	 * 
	 * @param tekton
	 *            the Tekton NPC
	 */
	private void fillReachableTargets(final Tekton tekton, final List<Entity> list) {
		if (!list.isEmpty()) {
			list.clear();
		}
		final int x = tekton.getX();
		final int y = tekton.getY();
		for (final Player player : tekton.getRaid().getPlayers()) {
			final int px = player.getX();
			final int py = player.getY();
			if (CombatUtilities.isWithinMeleeDistance(tekton, player)) {
				if (CollisionUtil.collides(tekton.getX(), tekton.getY(), tekton.getSize(), player.getX(), player.getY(), player.getSize())) {
					continue;
				}
				if (px == x - 1 && py == y - 1 || px == x + 4 && py == y - 1 || px == x + 4 && py == y + 4 || px == x - 1 && py == y + 4) {
					if (!player.hasWalkSteps()) {
						continue;
					}
				}
				list.add(player);
			}
		}
	}
}
