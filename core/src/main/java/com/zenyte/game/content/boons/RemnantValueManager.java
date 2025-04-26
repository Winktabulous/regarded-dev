package com.zenyte.game.content.boons;

import com.google.common.eventbus.Subscribe;
import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.RuneDate;
import com.zenyte.game.util.Colour;
import com.zenyte.plugins.events.ServerLaunchEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class RemnantValueManager {

    public static ArrayList<RemnantValue> values = new ArrayList<>();

    static {
        // ENHANCED_CRYSTAL_WEAPON_SEED
        values.add(gen(1500, ItemId.ENHANCED_CRYSTAL_WEAPON_SEED));
        /* Bonds */
        values.add(gen(1200, CustomItemId.DONATOR_PIN_10));
        values.add(gen(3000, CustomItemId.DONATOR_PIN_25));
        values.add(gen(6000, CustomItemId.DONATOR_PIN_50));
        values.add(gen(12000, CustomItemId.DONATOR_PIN_100));
        values.add(gen(50, 30215));
        values.add(gen(2, CustomItemId.BROKEN_EGG_SHELLS));
        values.add(gen(150, ItemId.GODSWORD_BLADE));
        values.add(gen(50, ItemId.DRAGON_AXE));
        values.add(gen(500, ItemId.DRAGON_PICKAXE));
        values.add(gen(200, ItemId.TRIDENT_OF_THE_SEAS));
        values.add(gen(100, ItemId.WARRIOR_RING, ItemId.SEERS_RING));
        values.add(gen(150, ItemId.BERSERKER_RING, ItemId.ARCHERS_RING));
        values.add(gen(500, ItemId.RING_OF_THE_GODS, ItemId.TYRANNICAL_RING, ItemId.TREASONOUS_RING));
        values.add(gen(300, ItemId.BLESSED_SPIRIT_SHIELD));
        /* id1 */
        values.add(gen(500, ItemId.ZAMORAK_HILT, ItemId.SARADOMIN_HILT, ItemId.BANDOS_HILT, ItemId.ARMADYL_HILT));
        values.add(gen(750, ItemId.ZAMORAK_GODSWORD, ItemId.SARADOMIN_GODSWORD, ItemId.BANDOS_GODSWORD, ItemId.ARMADYL_GODSWORD));
        values.add(gen(500, ItemId.ZAMORAKIAN_SPEAR, ItemId.ZAMORAKIAN_HASTA));
        values.add(gen(150, ItemId.STEAM_BATTLESTAFF, ItemId.SARADOMIN_SWORD));
        values.add(gen(200, ItemId.SARADOMINS_LIGHT));
        /* id2 */
        values.add(gen(800, ItemId.BANDOS_TASSETS, ItemId.BANDOS_CHESTPLATE, ItemId.ARMADYL_HELMET, ItemId.ARMADYL_CHESTPLATE, ItemId.ARMADYL_CHAINSKIRT));
        values.add(gen(750, ItemId.ARMADYL_CROSSBOW));
        values.add(gen(300, 27614));
        values.add(gen(100, ItemId.ABYSSAL_WHIP));
        values.add(gen(1250, 13263));
        values.add(gen(500, 12002, 11791, ItemId.BANDOS_BOOTS));
        values.add(gen(800, 13265));
        /* id3 */
        values.add(gen(300, ItemId.KRAKEN_TENTACLE));
        values.add(gen(1250, 22547, 22542, 22552));
        values.add(gen(900, ItemId.TOXIC_BLOWPIPE, ItemId.MAGIC_FANG, ItemId.SERPENTINE_VISAGE));
        /* uncomment after eco stabilization */
        //values.add(gen(900, ItemId.SERPENTINE_HELM));
        values.add(gen(400, ItemId.DRAGON_CROSSBOW));
        values.add(gen(1250, 24422));
        values.add(gen(1500, 24419, 24420, 24421));
        /* id4 */
        values.add(gen(1900, 24514, 24511, 24517));
        values.add(gen(2000, 24417));
        values.add(gen(1500, 12007, 12885, 12936, 13245, 13277, 19701, 21745, 22106, 23064, 23525, 24495, 25521, 25524));
        values.add(gen(1000, 13239, 13235));
        values.add(gen(2000, 13237));
        values.add(gen(400, ItemId.SMOULDERING_STONE, ItemId.CRYSTAL_ARMOUR_SEED));
        values.add(gen(1250, ItemId.DEXTEROUS_PRAYER_SCROLL, ItemId.ARCANE_PRAYER_SCROLL));
        values.add(gen(1000, 27673, 27670, 27667));
        values.add(gen(1250, 20724));
        values.add(gen(1000, 21270));
        /* id5 */
        values.add(gen(1500, 13576, 27610));
        values.add(gen(800, ItemId.BASILISK_JAW));
        values.add(gen(750, ItemId.UNCUT_ZENYTE));
        values.add(gen(1000, ItemId.VOIDWAKER_BLADE, ItemId.VOIDWAKER_GEM, ItemId.VOIDWAKER_HILT));
        values.add(gen(2500, 27690));
        values.add(gen(1500, 22983));
        values.add(gen(1750, 22966));
        values.add(gen(2250, 22978));
        values.add(gen(500, 22988, 22296));
        values.add(gen(1000, ItemId.BRIMSTONE_RING));
        values.add(gen(800, ItemId.AMULET_OF_TORTURE, ItemId.NECKLACE_OF_ANGUISH, ItemId.RING_OF_SUFFERING, ItemId.TORMENTED_BRACELET));
        /* id6 */
        values.add(gen(1250, ItemId.AVERNIC_DEFENDER_HILT));
        values.add(gen(1500, ItemId.ELIDINIS_WARD));
        values.add(gen(2500, 27251));
        values.add(gen(1250, 25975));
        values.add(gen(750, ItemId.DRACONIC_VISAGE, ItemId.WYVERN_VISAGE, ItemId.DRAGONFIRE_SHIELD, ItemId.DRAGONFIRE_SHIELD_11284, ItemId.ANCIENT_WYVERN_SHIELD));
        values.add(gen(900, ItemId.SKELETAL_VISAGE, ItemId.DRAGONFIRE_WARD, ItemId.DRAGONFIRE_WARD_22003));
        values.add(gen(1500, 23995, 25862));
        values.add(gen(7500, ItemId.TORVA_FULLHELM, ItemId.TORVA_PLATEBODY, ItemId.TORVA_PLATELEGS));
        values.add(gen(1250, 26235));
        values.add(gen(1000, 21000));
        values.add(gen(1500, 21012, ItemId.ANCESTRAL_HAT, ItemId.ANCESTRAL_ROBE_TOP, ItemId.ANCESTRAL_ROBE_BOTTOM));
        values.add(gen(1000, 21015));
        values.add(gen(2000, 21043, 21006, 21003));
        /* id7 */
        values.add(gen(1500, ItemId.DRAGON_CLAWS));
        values.add(gen(2250, 22323));
        values.add(gen(2500, ItemId.OSMUMTENS_FANG));
        values.add(gen(1250, 12823));
        values.add(gen(1750, 12827));
        values.add(gen(2750, 12819));
        values.add(gen(2000, 12821));
        values.add(gen(2500, 12825));
        values.add(gen(3500, 12817));
        values.add(gen(750, ItemId.RANGER_BOOTS));
        values.add(gen(1500, 22326, 22327, 22328, 26372, 26374));
        values.add(gen(1750, ItemId.ANCIENT_GODSWORD));
        values.add(gen(1250, 26370));
        values.add(gen(4750, 27226, 27229, 27232));
        values.add(gen(5500, ItemId.MASORI_BODY_F, ItemId.MASORI_CHAPS_F, ItemId.MASORI_MASK_F));
        values.add(gen(30000, 20997, 27275, 22325));
        values.add(gen(5000, ItemId.POLYPORE_STAFF));
        values.add(gen(2500, ItemId.KORASI));
        values.add(gen(1500, 22613, 22622, 22610, 22647));
        values.add(gen(1500, 32002));
        values.add(gen(500, 22994));
        values.add(gen(1000, 32198, 32195));
        values.add(gen(750, 24777));
        values.add(gen(500, ItemId.SARACHNIS_CUDGEL, 22111));
        values.add(gen(1500, ItemId.TANZANITE_MUTAGEN, ItemId.MAGMA_MUTAGEN));
        values.add(gen(2000, 25746));
        values.add(gen(3000, 25742, 25744));
        values.add(gen(1000, 10334, 10330, 10332, 10336, 10350, 10348, 10346, 10352, 10342, 10338, 10340));
        values.add(gen(1250, 12422, 12424, 12426, 20011, 20014));
        values.add(gen(1500, 23336, 23339, 23342, 23345));
        values.add(gen(600, 19478));
        values.add(gen(800, 19481));
        values.add(gen(500, 11926, 11924));
        values.add(gen(3000, 1038, 1040, 1042, 1044, 1046, 1048));
        values.add(gen(4000, 11862, 11863, 12399));
        values.add(gen(4500, 27828, 32066, 32068, 32224));
        values.add(gen(5000, 32078));
        values.add(gen(2500, 1053, 1055, 1057));
        values.add(gen(3000, 11847));
        values.add(gen(3250, 32237, 32226));
        values.add(gen(2500, 1050));
        values.add(gen(3000, 13343, 13344));
        values.add(gen(3500, 32225, 32234, 21859));

        values.add(gen(1000, ItemId._3RD_AGE_AMULET));
        values.add(gen(2250, ItemId.GHRAZI_RAPIER));
        values.add(gen(150, ItemId.BOOK_OF_THE_DEAD));
        values.add(gen(300, ItemId.ANGELIC_ARTIFACT));
        values.add(gen(200, ItemId.UNCHARGED_TRIDENT));
        values.add(gen(900, ItemId.SERPENTINE_HELM_UNCHARGED));
        values.add(gen(1000, ItemId._3RD_AGE_PLATESKIRT));

        values.add(gen(1500, ItemId.JAR_OF_VENOM));

        values.add(gen(2000, CustomItemId.DRAGON_KITE, CustomItemId.LIME_WHIP, CustomItemId.DEATH_CAPE));

        values.add(gen(1, CustomItemId.REMNANT_POINT_VOUCHER_1));
        values.add(gen(5, CustomItemId.REMNANT_POINT_VOUCHER_5));
        values.add(gen(10, CustomItemId.REMNANT_POINT_VOUCHER_10));

        values.add(gen(25, ItemId.DRAGON_BOOTS, ItemId.DRAGON_CHAINBODY_3140, ItemId.MASTER_WAND, ItemId.DARK_BOW, ItemId.INFINITY_BOOTS, ItemId.MAGES_BOOK, ItemId.ELDER_CHAOS_HOOD, ItemId.ELDER_CHAOS_TOP, ItemId.ELDER_CHAOS_ROBE));
        values.add(gen(100, ItemId.DAGONHAI_HAT, ItemId.DAGONHAI_ROBE_BOTTOM, ItemId.DAGONHAI_ROBE_TOP));
        values.add(gen(50, ItemId.SMOKE_BATTLESTAFF, ItemId.LAVA_BATTLESTAFF));
        values.add(gen(100, ItemId.AMULET_OF_ETERNAL_GLORY));
        values.add(gen(750, ItemId.ETERNAL_CRYSTAL, ItemId.PEGASIAN_CRYSTAL, ItemId.PRIMORDIAL_CRYSTAL));

        values.add(gen(100, ItemId.ODIUM_SHARD_1, ItemId.ODIUM_SHARD_2, ItemId.ODIUM_SHARD_3, ItemId.MALEDICTION_SHARD_1, ItemId.MALEDICTION_SHARD_2, ItemId.MALEDICTION_SHARD_3));

        values.add(gen(4000, ItemId.ZURIELS_HOOD, ItemId.ZURIELS_ROBE_BOTTOM, ItemId.ZURIELS_ROBE_TOP, ItemId.STATIUSS_FULL_HELM, ItemId.STATIUSS_PLATEBODY, ItemId.STATIUSS_PLATELEGS,
                ItemId.VESTAS_CHAINBODY, ItemId.VESTAS_HELM, ItemId.VESTAS_PLATESKIRT, ItemId.MORRIGANS_COIF, ItemId.MORRIGANS_LEATHER_BODY, ItemId.MORRIGANS_LEATHER_CHAPS));
    }

    @Subscribe
    public static void onServerLaunchEvent(final ServerLaunchEvent event) {
        categorize();
    }

    private static final LinkedHashMap<Integer, Integer> VALUE_BY_ID = new LinkedHashMap<>();
    static void categorize() {
        for(RemnantValue value: values) {
            for(Item item: value.getItems()) {
                if(!VALUE_BY_ID.containsKey(item.getId())) {
                    VALUE_BY_ID.put(item.getId(), value.getValue());
                } else {
                    throw new RuntimeException("Duplicate exchange value defined for ID: " + item.getId());
                }
            }
        }
    }

    public static boolean isPresent(int itemId) {
        for(RemnantValue value: values) {
            for(Item item: value.getItems()) {
                if (item.getId() == itemId || item.toNote().getId() == itemId)
                    return true;
            }
        }
        return false;
    }

    public static int getValue(Item item) {
        return VALUE_BY_ID.getOrDefault(item.getId(), VALUE_BY_ID.getOrDefault(item.getDefinitions().getUnnotedOrDefault(), 0));
    }

    public static RemnantValue gen(int value, int... ids) {
        return new RemnantValue(value, ids);
    }

    private static ArrayList<RemnantValue> values() {
        return values;
    }
}
