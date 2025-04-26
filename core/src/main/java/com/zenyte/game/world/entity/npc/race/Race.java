package com.zenyte.game.world.entity.npc.race;

import mgi.types.config.AnimationDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 3. apr 2018 : 15:37.47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum Race {

	HUMANOID(1056, 1636, 1744, 1088, 304, 1898, 1664, 1848, 1743, 1916, 1290, 1678, 1922, 1877),
	DWARF(410),
	DRAGON(1210),
	DEMON(1208),
	GNOME(432),
	FAIRY(412),
	GOBLIN(421, 1576, 1578),
	HOBGOBLIN(425),
	CAVE_GOBLIN(1540),
	ZOMBIE(1453, 5656, 2064),
	MONKEY(433, 700, 1853),
	TROLL(574, 1234),
	SNAKE(453),
	DUCK(1714),
	CAT(393),
	KALPHITE(1586, 1589),
	DAGANNOTH(650),
	SKELETON(1440, 1437, 451),
	GORILLA(1849, 699),
	AVIANSIE(1755),
	DOG(1662),
	BIRD(1713, 902),
	PENGUIN(1461),
	SNAIL(750),
	MUMMY(1435),
	GHOST(1438, 86, 1396, 1439),
	SHADE(761),
	CRAB(651),
	VYREWATCH(1228),
	FLESH_CRAWLER(687),
	GIANT(1211),
	SPIDER(1390),
	OGRE(434),
	GOAT(449),
	BUTTERFLY(391),
	SHEEP(1389),
	TZHAAR(157),
	FROG(35, 493),
	GHAST(716),
	IMP(1380),
	VERMIN(444),
	CAMEL(392),
	SCAVENGER(254, 450),
	TERRORBIRD(1711),
	COW(1490),
	UNDEAD_LUMBERJACK(1529),
	ORK(1124),
	ORC(435),
	FISHING_SPOT(864),
	SHARK(448),
	TREE_SPIRIT(408),
	TITAN(415),
	GOLEM(423, 639),
	CAVE_CRAWLER(908),
	ABERRANT_SPECTRE(924),
	KURASK(1510),
	GARGOYLE(914),
	BANSHEE(1521),
	NECHRYAEL(911),
	ABYSSAL_DEMON(1534),
	BASILISK(1544),
	BLOODVELD(903),
	DUST_DEVIL(913),
	SMOKE_DEVIL(1802),
	COCKATRICE(909),
	SLIME(34),
	SLUG(921),
	FIEND(1578),
	JELLY(916),
	CRAWLING_HAND(910),
	TUROTH(1593),
	SNAKELING(184),
	BEAR(693),
	BAT(386),
	SCARAB(581),
	PICKAXE(430),
	ELEMENTAL_BALANCE(504),
	WOUNDED_SOLDIER(1604);
	
	private final int[] skeletonIds;
	
	private static final Race[] VALUES = values();
	public static final Map<Integer, Race> MAP = new HashMap<Integer, Race>(250);
	
	static {
		for (int i = VALUES.length - 1; i >= 0; i--) {
			final Race race = VALUES[i];
			for (int x = race.skeletonIds.length - 1; x >= 0; x--) {
				MAP.put(race.skeletonIds[x], race);
			}
		}
		for (int i = 197; i <= 302; i++) {
			MAP.put(i, HUMANOID);
		}
	}
	
	Race(final int... skeletonIds) {
		this.skeletonIds = skeletonIds;
	}
	
	/**
	 * Whether the requested npc is of this race.
	 */
	public final boolean is(final int npcId) {
		final NPCDefinitions definitions = NPCDefinitions.get(npcId);
		final int standAnimation = definitions.getStandAnimation();
		if (standAnimation == -1) {
			return false;
		}
		final AnimationDefinitions animation = AnimationDefinitions.get(standAnimation);
		final int[] frameIds = animation.getFrameIds();
		if (frameIds == null || frameIds.length == 0) {
			return false;
		}
		final int animationSkeleton = frameIds[0] >> 16;
		return ArrayUtils.contains(skeletonIds, animationSkeleton);
	}
	
	/**
	 * Gets the race of the requested NPC. If none can be found, returns null.
	 */
	public static final Race getRace(final int npcId) {
		final NPCDefinitions definitions = NPCDefinitions.get(npcId);
		final int standAnimation = definitions.getStandAnimation();
		if (standAnimation == -1) {
			return null;
		}
		final AnimationDefinitions animation = AnimationDefinitions.get(standAnimation);
		final int[] frameIds = animation.getFrameIds();
		if (frameIds == null || frameIds.length == 0) {
			return null;
		}
		final int animationSkeleton = frameIds[0] >> 16;
		return MAP.get(animationSkeleton);
	}
	
	public static final Race getAnimationRace(final int animationId) {
		final AnimationDefinitions animation = AnimationDefinitions.get(animationId);
		final int[] frameIds = animation.getFrameIds();
		if (frameIds == null || frameIds.length == 0) {
			return null;
		}
		final int animationSkeleton = frameIds[0] >> 16;
		return MAP.get(animationSkeleton);
	}
	
	public int[] getSkeletonIds() {
	    return skeletonIds;
	}

}
