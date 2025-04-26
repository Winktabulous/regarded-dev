package com.zenyte.game.content.treasuretrails.rewards;

import com.zenyte.game.item.ItemId;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Kris | 10/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BroadcastedTreasure {
    THIRD_AGE_RANGE_TOP(ItemId._3RD_AGE_RANGE_TOP),
    THIRD_AGE_RANGE_LEGS(ItemId._3RD_AGE_RANGE_LEGS),
    THIRD_AGE_RANGE_COIF(ItemId._3RD_AGE_RANGE_COIF),
    THIRD_AGE_VAMBRACES(ItemId._3RD_AGE_VAMBRACES),
    THIRD_AGE_ROBE_TOP(ItemId._3RD_AGE_ROBE_TOP),
    THIRD_AGE_ROBE_BOTTOMS(ItemId._3RD_AGE_ROBE),
    THIRD_AGE_MAGE_HAT(ItemId._3RD_AGE_MAGE_HAT),
    THIRD_AGE_AMULET(ItemId._3RD_AGE_AMULET),
    THIRD_AGE_PLATELEGS(ItemId._3RD_AGE_PLATELEGS),
    THIRD_AGE_PLATEBODY(ItemId._3RD_AGE_PLATEBODY),
    THIRD_AGE_FULL_HELM(ItemId._3RD_AGE_FULL_HELMET),
    THIRD_AGE_KITESHIELD(ItemId._3RD_AGE_KITESHIELD),
    THIRD_AGE_WAND(ItemId._3RD_AGE_WAND),
    THIRD_AGE_BOW(ItemId._3RD_AGE_BOW),
    THIRD_AGE_LONGSWORD(ItemId._3RD_AGE_LONGSWORD),
    THIRD_AGE_CLOAK(ItemId._3RD_AGE_CLOAK),
    THIRD_AGE_AXE(ItemId._3RD_AGE_AXE),
    THIRD_AGE_PICKAXE(ItemId._3RD_AGE_PICKAXE),
    THIRD_AGE_RING(ItemId.RING_OF_3RD_AGE),
    THIRD_AGE_PLATESKIRT(ItemId._3RD_AGE_PLATESKIRT),
    THIRD_AGE_DRUIDIC_ROBE_TOP(ItemId._3RD_AGE_DRUIDIC_ROBE_TOP),
    THIRD_AGE_DRUIDIC_ROBE_BOTTOMS(ItemId._3RD_AGE_DRUIDIC_ROBE_BOTTOMS),
    THIRD_AGE_DRUIDIC_STAFF(ItemId._3RD_AGE_DRUIDIC_STAFF),
    THIRD_AGE_DRUIDIC_CLOAK(ItemId._3RD_AGE_DRUIDIC_CLOAK),
    CLIMBIMG_BOOTS_G(ItemId.CLIMBING_BOOTS_G),
    SPIKED_MANACLES(ItemId.SPIKED_MANACLES),
    HOLY_SANDALS(ItemId.HOLY_SANDALS),
    WIZARD_BOOTS(ItemId.WIZARD_BOOTS),
    RANGERS_TUNIC(ItemId.RANGERS_TUNIC),
    RANGERS_BOOT(ItemId.RANGER_BOOTS),
    RANGERS_TIGHTS(ItemId.RANGERS_TIGHTS),
    ROBIN_HOOD_HAT(ItemId.ROBIN_HOOD_HAT),
    RANGER_GLOVES(ItemId.RANGER_GLOVES),
    GILDED_PLATEBODY(ItemId.GILDED_PLATEBODY),
    GILDED_PLATELEGS(ItemId.GILDED_PLATELEGS),
    GILDED_PLATESKIRT(ItemId.GILDED_PLATESKIRT),
    GILDED_FULL_HELM(ItemId.GILDED_FULL_HELM),
    GILDED_KITESHIELD(ItemId.GILDED_KITESHIELD),
    GILDED_SCIMITAR(ItemId.GILDED_SCIMITAR),
    GILDED_BOOTS(ItemId.GILDED_BOOTS),
    GILDED_MED_HELM(ItemId.GILDED_MED_HELM),
    GILDED_CHAINBODY(ItemId.GILDED_CHAINBODY),
    GILDED_SQ_SHIELD(ItemId.GILDED_SQ_SHIELD),
    GILDED_2H_SWORD(ItemId.GILDED_2H_SWORD),
    GILDED_SPEAR(ItemId.GILDED_SPEAR),
    GILDED_HASTA(ItemId.GILDED_HASTA),
    GILDED_COIF(ItemId.GILDED_COIF),
    GILDED_DHIDE_VAMBS(ItemId.GILDED_DHIDE_VAMBS),
    GILDED_DHIDE_BODY(ItemId.GILDED_DHIDE_BODY),
    GILDED_DHIDE_CHAPS(ItemId.GILDED_DHIDE_CHAPS),
    GILDED_PICKAXE(ItemId.GILDED_PICKAXE),
    GILDED_AXE(ItemId.GILDED_AXE),
    GILDED_SPADE(ItemId.GILDED_SPADE);
    private final int id;
    private static final Int2ObjectMap<BroadcastedTreasure> map = Int2ObjectMaps.unmodifiable((Int2ObjectMap<BroadcastedTreasure>) CollectionUtils.populateMap(values(), new Int2ObjectOpenHashMap<>(), BroadcastedTreasure::getId));

    public static final boolean isBroadcasted(final int id) {
        return map.containsKey(id);
    }

    BroadcastedTreasure(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
