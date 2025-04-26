package com.zenyte.game.content.chambersofxeric.greatolm.scripts;

import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmCombatScript;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.List;

/**
 * @author Kris | 17. jaan 2018 : 21:10.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DeepBurn implements OlmCombatScript {
	private static final Projectile projectile = new Projectile(1349, 65, 0, 30, 15, 18, 0, 5);
	private static final ForceTalk chat = new ForceTalk("Burn with me!");
	private static final ForceTalk infectedChat = new ForceTalk("I will burn with you!");
	private static final SoundEffect startSound = new SoundEffect(585, 15, 0);
	private static final SoundEffect burnSound = new SoundEffect(740, 5, 0);
	private static final int[] combatSkills = new int[] {SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.DEFENCE, SkillConstants.RANGED, SkillConstants.MAGIC};
	private final Object2IntMap<Player> infectionsMap = new Object2IntOpenHashMap<>();

	@Override
	public void handle(final GreatOlm olm) {
		olm.setBurnAttack(this);
		olm.performAttack();
		World.sendSoundEffect(olm.getMiddleLocation(), startSound);
		final Player t = olm.random(olm.getDirection());
		if (t == null) {
			return;
		}
		olm.getScripts().add(this.getClass());
		final Location head = olm.getFaceCoordinates();
		infectionsMap.put(t, 5);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (olm.getRoom().getRaid().isDestroyed() || olm.getRaid().isCompleted()) {
					for (final Object2IntMap.Entry<Player> entry : infectionsMap.object2IntEntrySet()) {
						entry.getKey().sendMessage("You feel the deep burning inside dissipate.");
					}
					stop();
					return;
				}
				final List<Player> everyone = olm.everyone(GreatOlm.ENTIRE_CHAMBER);
				final Object2IntOpenHashMap<Player> remainingInfections = new Object2IntOpenHashMap<Player>(infectionsMap.size());
				for (final Object2IntMap.Entry<Player> entry : infectionsMap.object2IntEntrySet()) {
					final Player player = entry.getKey();
					if (player.isNulled() || player.isDead() || player.isFinished() || !everyone.contains(player)) {
						continue;
					}
					burn(olm, player, chat);
					final int infectionLevel = entry.getIntValue();
					if (infectionLevel > 1) {
						remainingInfections.put(player, infectionLevel - 1);
					} else {
						player.sendMessage("You feel the deep burning inside dissipate.");
					}
					for (final Player o : everyone) {
						if (o.getLocation().withinDistance(player.getLocation(), 1)) {
							if (infectionsMap.containsKey(o)) {
								continue;
							}
							remainingInfections.put(o, 5);
							burn(olm, o, infectedChat);
						}
					}
				}
				infectionsMap.clear();
				infectionsMap.putAll(remainingInfections);
				if (infectionsMap.isEmpty()) {
					stop();
					olm.setBurnAttack(null);
					olm.getScripts().remove(DeepBurn.this.getClass());
				}
			}
		}, World.sendProjectile(head, t, projectile), 7);
	}

	private final void burn(final GreatOlm olm, final Player player, final ForceTalk chat) {
		player.setForceTalk(chat);
		player.applyHit(new Hit(olm, 5, HitType.REGULAR));
		World.sendSoundEffect(player.getLocation(), burnSound);
		for (int i = 0; i < 2; i++) {
			player.getSkills().drainSkill(combatSkills[Utils.random(combatSkills.length - 1)], 2);
		}
	}

	public Object2IntMap<Player> getInfectionsMap() {
		return infectionsMap;
	}
}
