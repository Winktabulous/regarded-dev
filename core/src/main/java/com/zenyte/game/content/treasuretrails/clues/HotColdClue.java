package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.HotColdChallenge;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 10/04/2019 16:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum HotColdClue implements Clue {

    ASGARNIA_WARRIORS(new Location(2860, 3562, 0)),
    ASGARNIA_JATIX(new Location(2914, 3429, 0)),
    ASGARNIA_BARB(new Location(3036, 3439, 0)),
    ASGARNIA_MIAZRQA(new Location(2973, 3489, 0)),
    ASGARNIA_COW(new Location(3033, 3308, 0)),
    ASGARNIA_PARTY_ROOM(new Location(3026, 3363, 0)),
    ASGARNIA_CRAFT_GUILD(new Location(2917, 3295, 0)),
    ASGARNIA_RIMMINGTON(new Location(2978, 3241, 0)),
    ASGARNIA_MUDSKIPPER(new Location(2984, 3109, 0)),
    ASGARNIA_TROLL(new Location(2910, 3616, 0)),
    DESERT_GENIE(new Location(3364, 2910, 0)),
    DESERT_ALKHARID_MINE(new Location(3282, 3270, 0)),
    DESERT_MENAPHOS_GATE(new Location(3224, 2816, 0)),
    DESERT_BEDABIN_CAMP(new Location(3164, 3050, 0)),
    DESERT_UZER(new Location(3431, 3106, 0)),
    DESERT_POLLNIVNEACH(new Location(3287, 2975, 0)),
    DESERT_MTA(new Location(3350, 3293, 0)),
    DESERT_SHANTY(new Location(3294, 3106, 0)),
    FELDIP_HILLS_JIGGIG(new Location(2413, 3055, 0)),
    FELDIP_HILLS_SW(new Location(2582, 2895, 0)),
    FELDIP_HILLS_GNOME_GLITER(new Location(2553, 2972, 0)),
    FELDIP_HILLS_RANTZ(new Location(2611, 2946, 0)),
    FELDIP_HILLS_SOUTH(new Location(2487, 3005, 0)),
    FELDIP_HILLS_RED_CHIN(new Location(2532, 2900, 0)),
    FELDIP_HILLS_SE(new Location(2567, 2916, 0)),
    FELDIP_HILLS_CW_BALLOON(new Location(2452, 3108, 0)),
    FREMENNIK_PROVINCE_MTN_CAMP(new Location(2804, 3672, 0)),
    FREMENNIK_PROVINCE_RELLEKKA_HUNTER(new Location(2724, 3783, 0)),
    FREMENNIK_PROVINCE_KELGADRIM_ENTRANCE(new Location(2715, 3689, 0)),
    FREMENNIK_PROVINCE_SW(new Location(2605, 3648, 0)),
    FREMENNIK_PROVINCE_LIGHTHOUSE(new Location(2589, 3598, 0)),
    //FREMENNIK_PROVINCE_ETCETERIA_CASTLE(new Location(2614, 3867, 0)),
    //FREMENNIK_PROVINCE_MISC_COURTYARD(new Location(2529, 3867, 0)),
    FREMENNIK_PROVINCE_FREMMY_ISLES_MINE(new Location(2378, 3849, 0)),
    FREMENNIK_PROVINCE_WEST_ISLES_MINE(new Location(2313, 3854, 0)),
    FREMENNIK_PROVINCE_WEST_JATIZSO_ENTRANCE(new Location(2391, 3813, 0)),
    FREMENNIK_PROVINCE_PIRATES_COVE(new Location(2210, 3814, 0)),
    FREMENNIK_PROVINCE_ASTRAL_ALTER(new Location(2147, 3862, 0)),
    FREMENNIK_PROVINCE_LUNAR_VILLAGE(new Location(2087, 3915, 0)),
    FREMENNIK_PROVINCE_LUNAR_NORTH(new Location(2106, 3949, 0)),
    KANDARIN_SINCLAR_MANSION(new Location(2726, 3588, 0)),
    KANDARIN_CATHERBY(new Location(2774, 3433, 0)),
    KANDARIN_GRAND_TREE(new Location(2444, 3503, 0)),
    KANDARIN_SEERS(new Location(2735, 3486, 0)),
    KANDARIN_MCGRUBORS_WOOD(new Location(2653, 3485, 0)),
    KANDARIN_FISHING_BUILD(new Location(2586, 3372, 0)),
    KANDARIN_WITCHHAVEN(new Location(2708, 3304, 0)),
    KANDARIN_NECRO_TOWER(new Location(2669, 3242, 0)),
    KANDARIN_FIGHT_ARENA(new Location(2587, 3134, 0)),
    KANDARIN_TREE_GNOME_VILLAGE(new Location(2526, 3160, 0)),
    KANDARIN_GRAVE_OF_SCORPIUS(new Location(2464, 3228, 0)),
    KANDARIN_KHAZARD_BATTLEFIELD(new Location(2518, 3249, 0)),
    KANDARIN_WEST_ARDY(new Location(2533, 3320, 0)),
    KANDARIN_SW_TREE_GNOME_STRONGHOLD(new Location(2411, 3431, 0)),
    KANDARIN_OUTPOST(new Location(2458, 3364, 0)),
    KANDARIN_BAXTORIAN_FALLS(new Location(2534, 3479, 0)),
    KANDARIN_BA_AGILITY_COURSE(new Location(2536, 3546, 0)),
    KARAMJA_MUSA_POINT(new Location(2914, 3168, 0)),
    KARAMJA_BRIMHAVEN_FRUIT_TREE(new Location(2783, 3214, 0)),
    KARAMJA_WEST_BRIMHAVEN(new Location(2721, 3169, 0)),
    KARAMJA_GLIDER(new Location(2966, 2975, 0)),
    //KARAMJA_KHARAZI_NE(new Location(2908, 2922, 0)),
    //KARAMJA_KHARAZI_SW(new Location(2783, 2898, 0)),
    //KARAMJA_CRASH_ISLAND(new Location(2910, 2737, 0)),
    MISTHALIN_VARROCK_STONE_CIRCLE(new Location(3225, 3355, 0)),
    MISTHALIN_LUMBRIDGE(new Location(3238, 3169, 0)),
    MISTHALIN_LUMBRIDGE_2(new Location(3170, 3278, 0)),
    MISTHALIN_GERTUDES(new Location(3158, 3421, 0)),
    MISTHALIN_DRAYNOR_BANK(new Location(3096, 3235, 0)),
    MISTHALIN_LUMBER_YARD(new Location(3303, 3483, 0)),
    MORYTANIA_BURGH_DE_ROTT(new Location(3545, 3253, 0)),
    MORYTANIA_PORT_PHASMATYS(new Location(3613, 3485, 0)),
    MORYTANIA_HOLLOWS(new Location(3500, 3423, 0)),
    MORYTANIA_SWAMP(new Location(3422, 3374, 0)),
    MORYTANIA_HAUNTED_MINE(new Location(3441, 3259, 0)),
    MORYTANIA_MAUSOLEUM(new Location(3499, 3539, 0)),
    MORYTANIA_MOS_LES_HARMLESS(new Location(3744, 3041, 0)),
    MORYTANIA_MOS_LES_HARMLESS_BAR(new Location(3670, 2974, 0)),
    //MORYTANIA_DRAGONTOOTH_NORTH(new Location(3813, 3567, 0)),
    //MORYTANIA_DRAGONTOOTH_SOUTH(new Location(3803, 3532, 0)),
    WESTERN_PROVINCE_EAGLES_PEAK(new Location(2297, 3530, 0)),
    WESTERN_PROVINCE_PISCATORIS(new Location(2337, 3689, 0)),
    WESTERN_PROVINCE_PISCATORIS_HUNTER_AREA(new Location(2361, 3566, 0)),
    //WESTERN_PROVINCE_ARANDAR(new Location(2366, 3318, 0)),
    //WESTERN_PROVINCE_ELF_CAMP_EAST(new Location(2270, 3244, 0)),
    //WESTERN_PROVINCE_ELF_CAMP_NW(new Location(2174, 3280, 0)),
    WESTERN_PROVINCE_LLETYA(new Location(2335, 3166, 0)),
    //WESTERN_PROVINCE_TYRAS(new Location(2204, 3157, 0)),
    WESTERN_PROVINCE_ZULANDRA(new Location(2196, 3057, 0)),
    WILDERNESS_5(new Location(3169, 3558, 0)),
    WILDERNESS_12(new Location(3038, 3612, 0)),
    WILDERNESS_20(new Location(3225, 3676, 0)),
    WILDERNESS_28(new Location(3374, 3734, 0)),
    WILDERNESS_35(new Location(3153, 3795, 0)),
    WILDERNESS_37(new Location(2975, 3811, 0)),
    WILDERNESS_38(new Location(3294, 3817, 0)),
    WILDERNESS_49(new Location(3140, 3910, 0)),
    WILDERNESS_54(new Location(2983, 3946, 0)),
    ZEAH_BLASTMINE_BANK(new Location(1507, 3856, 0)),
    ZEAH_BLASTMINE_NORTH(new Location(1490, 3883, 0)),
    ZEAH_LOVAKITE_FURNACE(new Location(1505, 3814, 0)),
    ZEAH_LOVAKENGJ_MINE(new Location(1477, 3779, 0)),
    ZEAH_SULPHR_MINE(new Location(1428, 3866, 0)),
    ZEAH_SHAYZIEN_BANK(new Location(1517, 3603, 0)),
    ZEAH_OVERPASS(new Location(1467, 3714, 0)),
    ZEAH_LIZARDMAN(new Location(1493, 3694, 0)),
    ZEAH_COMBAT_RING(new Location(1557, 3580, 0)),
    ZEAH_SHAYZIEN_BANK_2(new Location(1498, 3628, 0)),
    ZEAH_LIBRARY(new Location(1601, 3842, 0)),
    ZEAH_HOUSECHURCH(new Location(1682, 3792, 0)),
    ZEAH_DARK_ALTAR(new Location(1699, 3879, 0)),
    ZEAH_ARCEUUS_HOUSE(new Location(1708, 3701, 0)),
    ZEAH_ESSENCE_MINE(new Location(1762, 3852, 0)),
    ZEAH_ESSENCE_MINE_NE(new Location(1772, 3866, 0)),
    ZEAH_PISCARILUS_MINE(new Location(1768, 3705, 0)),
    ZEAH_GOLDEN_FIELD_TAVERN(new Location(1718, 3647, 0)),
    ZEAH_MESS_HALL(new Location(1658, 3621, 0)),
    ZEAH_WATSONS_HOUSE(new Location(1653, 3573, 0)),
    ZEAH_VANNAHS_FARM_STORE(new Location(1806, 3521, 0)),
    //ZEAH_FARMING_GUILD_SW(new Location(1227, 3712, 0)),
    ZEAH_FARMING_GUILD_W(new Location(1209, 3737, 0)),
    ZEAH_DAIRY_COW(new Location(1320, 3718, 0)),
    ZEAH_CRIMSON_SWIFTS(new Location(1186, 3583, 0)),

    //Beginner ones
    DRAYNOR_WHEAT_FIELD(new Location(3120, 3282, 0), ClueLevel.BEGINNER),
    ICE_MOUNTAIN(new Location(3007, 3475, 0), ClueLevel.BEGINNER),
    LUMBRIDGE_COW_FIELD(new Location(3174, 3336, 0), ClueLevel.BEGINNER),
    DRAYNOR_MANOR_MUSHROOMS(new Location(3096, 3379, 0), ClueLevel.BEGINNER),
    NORTHEAST_OF_ALKHARID_MINE(new Location(3332, 3313, 0), ClueLevel.BEGINNER)
    ;

    HotColdClue(final Location tile) {
        this(tile, ClueLevel.MASTER);
    }

    HotColdClue(final Location tile, final ClueLevel level) {
        this.tile = tile;
        this.level = level;
    }

    private final Location tile;
    private final ClueLevel level;

    @Override
    public void view(@NotNull final Player player, @NotNull final Item item) {
        player.getTemporaryAttributes().put("Clue scroll item", item);
        GameInterface.CLUE_SCROLL.open(player);
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.HOT_COLD;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public String getText() {
        return "Buried beneath the ground, who knows where it's found.<br><br>Lucky for you, A man called "
        + (level == ClueLevel.MASTER ? "Jorral" : "Reldo") +
        " may have a clue.";
    }

    @Override
    public ClueChallenge getChallenge() {
        return new HotColdChallenge(tile);
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return level;
    }
}
