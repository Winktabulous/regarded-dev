package com.zenyte.game.model.item.enums;

import com.zenyte.game.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 6 jun. 2018 | 18:57:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum ItemPack {
	
	BAIT_PACK(new Item(11883), new Item(313, 100)),
	COMPOST_PACK(new Item(19704), new Item(6033, 100)),
	FEATHER_PACK(new Item(11881), new Item(314, 100)),
	EMPTY_JUG_PACK(new Item(20742), new Item(1936, 100)),
	WATER_FILLED_VIAL_PACK(new Item(11879), new Item(228, 100)),
	EMPTY_VIAL_PACK(new Item(11877), new Item(230, 100)),
	OLIVE_OIL_PACK(new Item(12857), new Item(3423, 100)),
	UNFINISHED_BROAD_BOLT_PACK(new Item(11887), new Item(11876, 100)),
	BROAD_ARROWHEAD_PACK(new Item(11885), new Item(11874, 100)),
	SOFT_CLAY_PACK(new Item(12009), new Item(1762, 100)),
	AMYLASE_PACK(new Item(12641), new Item(12640, 100)),
	AIR_RUNE_PACK(new Item(12728), new Item(556, 100)),
	WATER_RUNE_PACK(new Item(12730), new Item(555, 100)),
	EARTH_RUNE_PACK(new Item(12732), new Item(557, 100)),
	FIRE_RUNE_PACK(new Item(12734), new Item(554, 100)),
	MIND_RUNE_PACK(new Item(12736), new Item(558, 100)),
	CHAOS_RUNE_PACK(new Item(12738), new Item(562, 100)),
	EYE_OF_NEWT_PACK(new Item(12859), new Item(222, 100)),
	BOX_TRAP_PACK(new Item(12742), new Item(10009, 100)),
	BIRD_SNARE_PACK(new Item(12740), new Item(10007, 100)),
	MAGIC_IMP_BOX_PACK(new Item(12744), new Item(10026, 100)),
	BONE_BOLT_PACK(new Item(13193), new Item(8882, 100)),
    PLANT_POT_PACK(new Item(13250), new Item(5355, 100)),
    SACK_PACK(new Item(13252), new Item(5419, 100)),
    BASKET_PACK(new Item(13254), new Item(5377, 100)),
    SANDWORMS_PACK(new Item(13432), new Item(13431, 100)),
    EMPTY_BUCKET_PACK(new Item(22660), new Item(1926, 100)),
    TZHAAR_AIR_RUNE_PACK(new Item(21698), new Item(556, 100)),
    TZHAAR_WATER_RUNE_PACK(new Item(21701), new Item(555, 100)),
    TZHAAR_EARTH_RUNE_PACK(new Item(21704), new Item(557, 100)),
    TZHAAR_FIRE_RUNE_PACK(new Item(21707), new Item(554, 100));

    public static final ItemPack[] VALUES = values();
    public static final Map<Integer, ItemPack> DATA = new HashMap<>();

    static {
        for (final ItemPack pack : VALUES) {
            DATA.put(pack.getPack().getId(), pack);
        }
    }

    private final Item pack;
    private final Item item;

    ItemPack(Item pack, Item item) {
        this.pack = pack;
        this.item = item;
    }

    public Item getPack() {
        return pack;
    }

    public Item getItem() {
        return item;
    }

}
