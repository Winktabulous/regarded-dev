package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 11 dec. 2017 : 22:17:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CaveHorror extends NPC implements Spawnable, CombatScript {
	private static final Animation ATTACK_1_ANIMATION = new Animation(4234);
	private static final Animation ATTACK_2_ANIMATION = new Animation(4235);
	private static final Animation ATTACK_3_ANIMATION = new Animation(4237);
	private static final Animation PLAYER_ANIMATION = new Animation(4434);

	public CaveHorror(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
		this.deathDelay = 1;
	}

	@Override
	public int getRespawnDelay() {
		return 20;
	}

	@Override
	public int attack(Entity target) {
		if (!(target instanceof Player)) {
			return 0;
		}
		attackSound();
		final Player player = (Player) target;
		int random = Utils.random(4) == 0 ? 4 : Utils.random(1);
		setAnimation(random == 0 ? ATTACK_1_ANIMATION : random == 1 ? ATTACK_2_ANIMATION : ATTACK_3_ANIMATION);
		final boolean hasWitchwoodIcon = SlayerEquipment.WITCHWOOD_ICON.isWielding(player);
		delayHit(this, 0, target, new Hit(this, hasWitchwoodIcon ? getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target) : 9, HitType.MELEE).setExecuteIfLocked());
		if (random == 4 && !hasWitchwoodIcon) {
			player.setAnimation(PLAYER_ANIMATION);
			player.lock(3);
		}
		return getCombatDefinitions().getAttackSpeed();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			player.getAchievementDiaries().update(MorytaniaDiary.KILL_A_CAVE_HORROR);
		}
	}

	@Override
	public boolean validate(int id, String name) {
		return name.equals("cave horror");
	}
}
