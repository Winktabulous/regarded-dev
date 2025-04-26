package com.zenyte.game.content.skills.fishing;

import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author Noele | Nov 9, 2017 : 12:22:34 AM
 *
 * @see <a href="https://noeles.life"></a>
 * @see <a href="noele@zenyte.com"></a>
 */

public class FishingLocations {
	
	public static List<Location> occupied = new ArrayList<>();

	public enum SpotLocations {

		/* Region id for minnows is actually non-valid because it shares region with fishing guild. */
		MINNOWS(11000, true, new Location(2611, 3443, 0), new Location(2610, 3444, 0), new Location(2610, 3443), new Location(2612, 3444, 0),
				new Location(2617, 3444, 0), new Location(2618, 3443, 0), new Location(2620, 3443, 0), new Location(2619, 3444, 0),
				new Location(2611, 3447, 0), new Location(2606, 3443, 0), new Location(2608, 3440, 0), new Location(2618, 3440, 0),
				new Location(2623, 3444, 0), new Location(2618, 3447, 0)),

		WESTERN_MOR_UL_REK(9807, true, new Location(2446, 5104, 0), new Location(2477, 5078, 0), new Location(2478, 5078, 0)),
		EASTERN_MOR_UL_REK(10063, true, new Location(2539, 5088, 0), new Location(2540, 5088, 0), new Location(2541, 5088, 0), new Location(2536, 5086, 0)),


		DRAYNOR_VILLAGE(12338, true, new Location(3085, 3231, 0), new Location(3085, 3230, 0),
				new Location(3086, 3228, 0), new Location(3086, 3227, 0)),
		
		BARBARIAN_VILLAGE(12341, true, new Location(3104, 3424, 0), new Location(3104, 3425, 0), new Location(3110, 3432, 0),
				new Location(3110, 3433, 0), new Location(3110, 3434, 0)),
		
		CATHERBY(11317, true, new Location(2859, 3426, 0), new Location(2860, 3426, 0), new Location(2855, 3423, 0),
				new Location(2854, 3423, 0), new Location(2853, 3423, 0), new Location(2846, 3429, 0),
				new Location(2845, 3429, 0), new Location(2844, 3429, 0),
				new Location(2840, 3431, 0), new Location(2839, 3431, 0), new Location(2838, 3431, 0),
				new Location(2837, 3431, 0), new Location(2836, 3431, 0)),
		
		FISHING_GUILD(10293, true, new Location(2599, 3419, 0), new Location(2600, 3419, 0), new Location(2602, 3419, 0),
				new Location(2603, 3419, 0), new Location(2610, 3416, 0), new Location(2605, 3424, 0),
				new Location(2605, 3420, 0), new Location(2605, 3421, 0), new Location(2609, 3416, 0),
				new Location(2603, 3423, 0), new Location(2602, 3423, 0), new Location(2601, 3423, 0),
				new Location(2608, 3416, 0), new Location(2603, 3422, 0), new Location(2602, 3422, 0),
				new Location(2601, 3422, 0), new Location(2605, 3425, 0), new Location(2598, 3424, 0),
				new Location(2603, 3426, 0), new Location(2602, 3426, 0), new Location(2598, 3423, 0),
				new Location(2603, 3417, 0), new Location(2604, 3417, 0), new Location(2602, 3414, 0),
				new Location(2602, 3413, 0), new Location(2602, 3412, 0), new Location(2612, 3415, 0),
				new Location(2612, 3414, 0), new Location(2612, 3412, 0), new Location(2612, 3411, 0),
				new Location(2607, 3416, 0), new Location(2606, 3416, 0)),

		PISCATORIS(9273, true, new Location(2345, 3702, 0), new Location(2344, 3702, 0), new Location(2343, 3702, 0),
				new Location(2342, 3702, 0), new Location(2327, 3700, 0), new Location(2326, 3700, 0)),

		APE_ATOLL(10794, true, new Location(2694, 2706, 0), new Location(2699, 2702, 0), new Location(2700, 2702, 0),
				new Location(2707, 2698, 0))
		;
		
		private final int regionId;
		private final boolean movable;
		private final Location[] locations;
		
		private static final SpotLocations[] values = values();
		private static final Int2ObjectMap<SpotLocations> map = new Int2ObjectOpenHashMap<>();

		static {
            CollectionUtils.populateMap(values, map, value -> value.regionId);
        }

        SpotLocations(int regionId, boolean movable, Location... locations) {
            this.regionId = regionId;
            this.movable = movable;
            this.locations = locations;
        }

        public static SpotLocations getArea(int id) {
            return map.get(id);
        }

        public int getRegionId() {
            return regionId;
        }

        public boolean isMovable() {
            return movable;
        }

        public Location[] getLocations() {
            return locations;
        }

    }
	
}
