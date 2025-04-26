package com.zenyte.game.content.skills.construction;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 7. march 2018 : 2:43.08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ConstructionConstants {

	public static final Location CENTER = new Location(26, 27, 1);
	public static final int EXIT_PORTAL = 4525;
	
	public static final Item WATERING_CAN = new Item(5331);
	
	public static final Location SMALL_CENTER = new Location(18, 20, 1);
	public static final Location MEDIUM_CENTER = new Location(26, 28, 1);
	public static final Location LARGE_CENTER = new Location(34, 36, 1);
	public static final SoundEffect ENTER_SOUND = new SoundEffect(984);
	
	public static final Animation REG_ANIM = new Animation(3676);
	public static final Animation FLOOR_ANIM = new Animation(3683);
	public static final Animation SHELF_ANIM = new Animation(3684);
	public static final Animation WATERING_ANIM = new Animation(2293);
	
	public static final int[][] DOOR_POSITIONS = new int[][] {
		new int[] { 3, 0, 3, 3, -1 }, new int[] { 4, 0, 3, 4, -1 },
		new int[] { 0, 4, 0, -1, 4 }, new int[] { 0, 3, 0, -1, 3 }, 
		new int[] { 7, 3, 2, 8, 3 }, new int[] { 7, 4, 2, 8, 4 },
		new int[] { 4, 7, 1, 4, 8 }, new int[] { 3, 7, 1, 3, 8 }
	};
	
	public static final int[] NAILS = new int[] { 
			4824, 4823, 4822, 4821, 1539, 4820, 4819
	};
	
	public static final int[] DOOR_IDS = new int[] {
		13100, 13118, 13118, 13107, 13118, 14751, 27084
	};
	
	public static final int[] WALL_IDS = new int[] {
			13098, 1902, 1415, 13111, 13011, 13116, 27082
	};

	
	public static boolean isDoor(WorldObject object) {
		return object.getDefinitions().getName().equalsIgnoreCase("Door hotspot");
	}
	
}
