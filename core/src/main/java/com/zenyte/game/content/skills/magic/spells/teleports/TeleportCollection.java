package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 9. juuli 2018 : 02:03:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum TeleportCollection implements Teleport {

	TELEOTHER_LUMBRIDGE(TeleportType.TELEOTHER_TELEPORT, new Location(3222, 3218, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) { @Override public String toString() { return "Lumbridge"; } },
	TELEOTHER_FALADOR(TeleportType.TELEOTHER_TELEPORT, new Location(2965, 3379, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) { @Override public String toString() { return "Falador"; } },
	TELEOTHER_CAMELOT(TeleportType.TELEOTHER_TELEPORT, new Location(2757, 3478, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) { @Override public String toString() { return "Camelot"; } },
	TELE_GROUP_MOONCLAN(TeleportType.GROUP_TELEPORT, new Location(2114, 3914, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) { @Override public String toString() { return "Moonclan Island"; } },
	TELE_GROUP_WATERBIRTH(TeleportType.GROUP_TELEPORT, new Location(2546, 3758, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) { @Override public String toString() { return "Waterbirth Island"; } },
	TELE_GROUP_BARBARIAN(TeleportType.GROUP_TELEPORT, new Location(2518, 3570, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) { @Override public String toString() { return "Barbarian Outpost"; } },
	TELE_GROUP_KHAZARD(TeleportType.GROUP_TELEPORT, new Location(2635, 3166, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) {
		@Override
		public String toString() {
			return "Port Khazard";
		}
	},
	TELE_GROUP_FISHING_GUILD(TeleportType.GROUP_TELEPORT, new Location(2612, 3383, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) {
		@Override
		public String toString() {
			return "Fishing Guild";
		}
	},
	TELE_GROUP_CATHERBY(TeleportType.GROUP_TELEPORT, new Location(2800, 3451, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) {
		@Override
		public String toString() {
			return "Catherby";
		}
	},
	TELE_GROUP_ICE_PLATEAU(TeleportType.GROUP_TELEPORT, new Location(2974, 3940, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED) {
		@Override
		public String toString() {
			return "Ice Plateau";
		}
	},
	ZULANDRA_OBJECT_TELEPORT(TeleportType.SCROLL_TELEPORT, new Location(2200, 3055, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	ROYAL_SEED_POD_TELEPORT_STRONGHOLD(TeleportType.ROYAL_SEED_TELEPORT, new Location(2465, 3495, 0), 0, 0, 0, 30, UNRESTRICTED),
	ROYAL_SEED_POD_TELEPORT_HOME(TeleportType.ROYAL_SEED_TELEPORT, new Location(3087, 3488, 0), 0, 0, 0, 30, UNRESTRICTED),
	ARDOUGNE_CLOAK_MONASTERY_TELEPORT(TeleportType.ARDOUGNE_CLOAK_MONASTERY, new Location(2606, 3221, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	ARDOUGNE_CLOAK_FARMING_TELEPORT(TeleportType.ARDOUGNE_CLOAK_FARMING, new Location(2664, 3374, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	CA_TROLLHEIM_TELEPORT(TeleportType.COMBAT_ACHIEVEMENT_TELEPORT, new Location(2898, 3711, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	CA_MORUIREK_TELEPORT(TeleportType.COMBAT_ACHIEVEMENT_TELEPORT, new Location(2495, 5098, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	DESERT_AMULET_KALPHITE_HIVE(TeleportType.DESERT_AMULET_KALPHITE_HIVE, new Location(3322, 3123, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	DESERT_AMULET_NARDAH(TeleportType.DESERT_AMULET_NARDAH, new Location(3427, 2927, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	EXPLORERS_RING_CABBAGE_TELEPORT(TeleportType.EXPLORERS_RING_CABBAGE, new Location(3053, 3288, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	FREMENNIK_SEA_BOOTS_RELLEKKA_TELEPORT(TeleportType.FREMENNIK_SEABOOTS_RELLEKKA, new Location(2642, 3675, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	KARAMJA_GLOVES_GEM_MINE(TeleportType.REGULAR_TELEPORT, new Location(2842, 9387, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	KARAMJA_GLOVES_DURADEL(TeleportType.REGULAR_TELEPORT, new Location(2869, 2982, 1), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	WILDERNESS_SWORD_FOUNTAIN_OF_RUNE(TeleportType.FOUNTAIN_OF_RUNE_TELEPORT, new Location(3379, 3891, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	WESTERN_BANNER_FISHING_COLONY(TeleportType.WESTERN_BANNER_FISHING_COLONY, new Location(2336, 3693, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	KANDARIN_HEADGEAR_SHERLOCK(TeleportType.REGULAR_TELEPORT, new Location(2734, 3418, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	MYTHICAL_CAPE(TeleportType.REGULAR_TELEPORT, new Location(2457, 2850, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	ICY_BASALT_WEISS(TeleportType.ICY_BASALT, new Location(2846, 3940, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	STONY_BASALT_TROLL_STRONGHOLD(TeleportType.STONY_BASALT, new Location(2845, 3693, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	STONY_BASALT_TROLL_STRONGHOLD_TOP(TeleportType.STONY_BASALT, new Location(2838, 3693, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
	MORYTANIA_LEGS_BURGH_DE_ROTT(TeleportType.MORYTANIA_LEGS_SLIME_PIT, new Location(3483, 3231, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
    MORYTANIA_LEGS_SLIME_PIT(TeleportType.MORYTANIA_LEGS_SLIME_PIT, new Location(3683, 9888, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
    RADAS_BLESSING_KOUREND_WOODLAND(TeleportType.RADAS_BLESSING_KOUREND_WOODLAND, new Location(1552, 3455, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
    RADAS_BLESSING_MOUNT_KARUULM(TeleportType.RADAS_BLESSING_MOUNT_KARUULM, new Location(1311, 3799, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
    PURO_CENTER_OF_CROP_CIRCLE(TeleportType.CROP_CIRCLE, new Location(2592, 4317, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
    ZANARIS_CENTER_OF_CROP_CIRCLE(TeleportType.CROP_CIRCLE, new Location(2426, 4446, 0), 0, 0, DISTANCE, WILDERNESS_LEVEL, UNRESTRICTED),
    EVIL_BOB_ISLAND(TeleportType.REGULAR_TELEPORT, new Location(2526, 4778, 0), 0, 0, 0, WILDERNESS_LEVEL, UNRESTRICTED),
    FALADOR_PARTY_ROOM(TeleportType.REGULAR_TELEPORT, new Location(3045, 3370, 0), 0, 0, 2, WILDERNESS_LEVEL, UNRESTRICTED),
    VERZIK_CRYSTAL_SHARD(TeleportType.REGULAR_TELEPORT, new Location(3677, 3217, 0), 0, 0, 0, WILDERNESS_LEVEL, UNRESTRICTED);

    private final TeleportType type;
    private final Location destination;
    private final int level;
    private final double experience;
    private final int randomizationDistance;
    private final int wildernessLevel;
    private final boolean combatRestricted;
    private final Item[] runes;

    TeleportCollection(final TeleportType type, final Location destination, final int level, final double experience, final int randomizationDistance, final int wildernessLevel, final boolean combatRestricted, final Item... runes) {
        this.type = type;
        this.destination = destination;
        this.level = level;
        this.experience = experience;
        this.randomizationDistance = randomizationDistance;
        this.wildernessLevel = wildernessLevel;
        this.combatRestricted = combatRestricted;
        this.runes = runes;
    }

    public TeleportType getType() {
        return type;
    }

    public Location getDestination() {
        return destination;
    }

    public int getLevel() {
        return level;
    }

    public double getExperience() {
        return experience;
    }

    public int getRandomizationDistance() {
        return randomizationDistance;
    }

    public int getWildernessLevel() {
        return wildernessLevel;
    }

    public boolean isCombatRestricted() {
        return combatRestricted;
    }

    public Item[] getRunes() {
        return runes;
    }

}
