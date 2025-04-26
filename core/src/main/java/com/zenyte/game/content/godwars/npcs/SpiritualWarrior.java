package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.CombatScriptsHandler;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 3/9/2020
 */
public class SpiritualWarrior extends SpawnableKillcountNPC implements CombatScript {

	private static final Animation specialAnimation = new Animation(1132);
	private static final Graphics specialGraphic = new Graphics(1103);

	protected SpiritualWarrior(int id, Location tile, Direction facing, int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public int attack(Entity target) {
		if (id == NpcId.SPIRITUAL_WARRIOR_11290 && Utils.randomBoolean(8)) {
			setAnimation(specialAnimation);
			special(target, target.getLocation().copy(), 2);
			return 10;
		}

		return CombatScriptsHandler.DEFAULT_SCRIPT.attack(this, target);
	}

	private void special(Entity entity, Location location, int dist) {
		if (dist <= 0) {
			World.sendGraphics(specialGraphic, location);
			if (entity.getLocation().matches(location)) {
				entity.scheduleHit(this, new Hit(Utils.random(40), HitType.REGULAR), -1);
			}
			return;
		}

		Location southWest = location.transform(Direction.SOUTH_WEST, dist);
		Location northWest = location.transform(Direction.NORTH_WEST, dist);
		Location northEast = location.transform(Direction.NORTH_EAST, dist);
		Location southEast = location.transform(Direction.SOUTH_EAST, dist);
		World.sendGraphics(specialGraphic, southWest);
		World.sendGraphics(specialGraphic, northWest);
		World.sendGraphics(specialGraphic, northEast);
		World.sendGraphics(specialGraphic, southEast);
		WorldTasksManager.schedule(() -> special(entity, location, dist - 1), 1);
	}

	@Override
	public void onDeath(Entity source) {
		super.onDeath(source);
		if (source instanceof final Player player) {
			player.getAchievementDiaries().update(WildernessDiary.KILL_A_SPIRITUAL_WARRIOR);
		}
	}

	@Override
	public boolean canBeMulticannoned(@NotNull Player player) {
		return false;
	}

	@Override
	public boolean validate(int id, String name) {
		return id == NpcId.SPIRITUAL_WARRIOR ||
				id == NpcId.SPIRITUAL_WARRIOR_2243 ||
				id == NpcId.SPIRITUAL_WARRIOR_3159 ||
				id == NpcId.SPIRITUAL_WARRIOR_3166 ||
				id == NpcId.SPIRITUAL_WARRIOR_11290;
	}

}
