package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.content.skills.magic.spells.teleports.structures.*;

/**
 * @author Kris | 9. juuli 2018 : 01:12:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum TeleportType {
	
	HOME_TELEPORT(new ArceuusStructure()),
    RANDOM_EVENT_TELEPORT(new RandomEventStructure()),
	REGULAR_TELEPORT(new RegularStructure()),
	HIGH_REVISION_TELEPORT(new HighRevisionTeleportStructure()),
	ANCIENT_TELEPORT(new ArceuusStructure()),
	LUNAR_TELEPORT(new ArceuusStructure()),
	ARCEUUS_TELEPORT(new ArceuusStructure()),
	NEAR_REALITY_PORTAL_TELEPORT(new ArceuusStructure()),
	RESPAWN_POINT_TELEPORT(new RespawnPointStructure()),
	BOUNTY_TARGET_TELEPORT(new BountyTargetStructure()),
	HOUSE_TELEPORT(new HouseStructure()),
	TELEOTHER_TELEPORT(new TeleotherStructure()),
	GROUP_TELEPORT(new GroupStructure()),
	SCROLL_TELEPORT(new ScrollStructure()),
	ROYAL_SEED_TELEPORT(new RoyalSeedStructure()),
	TABLET_TELEPORT(new TabletStructure()),
	ZENYTE_TABLET_TELEPORT(new TabletStructure()),
    VOLCANIC_MINE_TELEPORT(new VolcanicMineStructure()),
	RING_OF_RETURNING_TELEPORT(new RingOfReturningStructure()),
	SKULL_SCEPTRE_TELEPORT(new SkullSceptreStructure()),
    MINIGAMES_TELEPORT(new MinigameStructure()),
    ARDOUGNE_CLOAK_MONASTERY(new ArdougneCloakMonasteryStructure()),
    ARDOUGNE_CLOAK_FARMING(new ArdougneCloakFarmingStructure()),
	COMBAT_ACHIEVEMENT_TELEPORT(new RegularStructure()),
    EXPLORERS_RING_CABBAGE(new ExplorersRingStructure()),
	DESERT_AMULET_KALPHITE_HIVE(new DesertAmuletKalphiteHiveStructure()),
	DESERT_AMULET_NARDAH(new DesertAmuletNardahStructure()),
	FREMENNIK_SEABOOTS_RELLEKKA(new FremennikSeaBootsRellekkaStructure()),
	MORYTANIA_LEGS_SLIME_PIT(new MorytaniaLegsSlimePitStructure()),
	MORYTANIA_LEGS_BURGH_DE_ROTT(new MorytaniaLegsBurghDeRottStructure()),
    WESTERN_BANNER_FISHING_COLONY(new WesternBannerFishingColonyStructure()),
    RADAS_BLESSING_KOUREND_WOODLAND(new RadasBlessingKourendWoodlandStructure()),
    RADAS_BLESSING_MOUNT_KARUULM(new RadasBlessingMountKaruulmStructure()),
    FOUNTAIN_OF_RUNE_TELEPORT(new FountainOfRuneStructure()),
    LEVER_TELEPORT(new LeverPullStructure()),
    XERICS_TELEPORT(new XericsTalismanStructure()),
    ICY_BASALT(new IcyBasaltStructure()),
    STONY_BASALT(new StonyBasaltStructure()),
    CROP_CIRCLE(new CropCircleStructure()),
    ECTOPHIAL(new EctophialStructure()),
    WILDERNESS(new WildernessStructure()),
    PHARAOH_SCEPTRE(new PharaohSceptreTeleportStructure()),
    CRYSTAL_OF_MEMORIES(new CrystalOfMemoriesStructure()),
    INSTANT_UNSAFE(new InstantStructure());

    private final TeleportStructure structure;

    TeleportType(final TeleportStructure structure) {
        this.structure = structure;
    }

    public TeleportStructure getStructure() {
        return structure;
    }

}
