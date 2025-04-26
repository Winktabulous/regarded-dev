package com.zenyte.game.world.entity.npc.impl.wilderness;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

/**
 * @author Tommeh | 12 mrt. 2018 : 21:19:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class Scorpia extends NPC implements CombatScript, Spawnable {
	private static final ForceTalk SPAWN_MSG = new ForceTalk("Scssss!");
	private final ScorpiaGuardian[] guardians = new ScorpiaGuardian[2];
	private int ticks;
	private boolean killedGuardian = false;
	private boolean hitAnyone = false;

	public Scorpia(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
		this.aggressionDistance = 50;
		this.maxDistance = 50;
		this.attackDistance = 10;
	}

	@Override
	public int getRespawnDelay() {
		return 17;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!isDead()) {
			for (int i = 0; i < guardians.length; i++) {
				if (getHitpoints() <= getMaxHitpoints() / 2 && guardians[i] == null) {
					setForceTalk(SPAWN_MSG);
					for (int index = 0; index < guardians.length; index++) {
						guardians[index] = new ScorpiaGuardian(6617, new Location(getLocation().getX() + Utils.random(2), getLocation().getY() + Utils.random(2), getLocation().getPlane()), Direction.SOUTH, 5);
						guardians[index].spawn();
						guardians[index].getTemporaryAttributes().put("ScorpiaNPC", this);
					}
				}
			}
		}
		if (getCombat().underCombat()) {
			ticks = 0;
		} else if (++ticks >= 50) {
			for (int i = 0; i < guardians.length; i++) {
				if (guardians[i] == null) {
					continue;
				}
				guardians[i].finish();
				guardians[i] = null;
			}
		}
	}

	@Override
	public boolean isTolerable() {
		return false;
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override public void handleOutgoingHit(Entity target, Hit hit) {
		super.handleOutgoingHit(target, hit);
		if (hit.getDamage() > 0) {
			hitAnyone = true;
		}
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		for (int i = 0; i < guardians.length; i++) {
			if (guardians[i] != null) {
				guardians[i].sendDeath();
			}
		}
		if (source instanceof final Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_CRAZY_ARCHEAOLOGIST, 4);
		}
	}

	@Override
	protected void onFinish(Entity source) {
		super.onFinish(source);

		if (source instanceof final Player player) {
			player.getCombatAchievements().checkKcTask("scorpia", 10, CAType.SCORPIA_ADEPT);
			player.getCombatAchievements().checkKcTask("scorpia", 25, CAType.SCORPIA_VETERAN);
			if (!killedGuardian) {
				player.getCombatAchievements().complete(CAType.GUARDIANS_NO_MORE);
			}
			if (!hitAnyone) {
				player.getCombatAchievements().complete(CAType.I_CANT_REACH_THAT);
			}
		}
	}

	@Override
	public int attack(Entity target) {
		setAnimation(getCombatDefinitions().getAttackAnim());
		delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 16, MELEE, target), HitType.MELEE).onLand(hit -> {
			if (Utils.random(3) == 0 && !target.getToxins().isPoisoned()) {
				target.getToxins().applyToxin(ToxinType.POISON, 20, this);
			}
		}));
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public boolean validate(final int id, final String name) {
		return id == 6615;
	}

	public void setKilledGuardian() {
		killedGuardian = true;
	}
}
