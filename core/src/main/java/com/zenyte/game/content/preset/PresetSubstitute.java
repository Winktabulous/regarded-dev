package com.zenyte.game.content.preset;

import com.zenyte.game.item.ItemId;
import it.unimi.dsi.fastutil.ints.*;

/**
 * @author Kris | 20/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum PresetSubstitute {
    BLACK_MASK_10(ItemId.BLACK_MASK_10, ItemId.BLACK_MASK_9, 10), BLACK_MASK_9(ItemId.BLACK_MASK_9, ItemId.BLACK_MASK_8, 9), BLACK_MASK_8(ItemId.BLACK_MASK_8, ItemId.BLACK_MASK_7, 8), BLACK_MASK_7(ItemId.BLACK_MASK_7, ItemId.BLACK_MASK_6, 7), BLACK_MASK_6(ItemId.BLACK_MASK_6, ItemId.BLACK_MASK_5, 6), BLACK_MASK_5(ItemId.BLACK_MASK_5, ItemId.BLACK_MASK_4, 5), BLACK_MASK_4(ItemId.BLACK_MASK_4, ItemId.BLACK_MASK_3, 4), BLACK_MASK_3(ItemId.BLACK_MASK_3, ItemId.BLACK_MASK_2, 3), BLACK_MASK_2(ItemId.BLACK_MASK_2, ItemId.BLACK_MASK_1, 2), BLACK_MASK_1(ItemId.BLACK_MASK_1, ItemId.BLACK_MASK, 1), BLACK_MASK(ItemId.BLACK_MASK, -1, 0), BLACK_MASK_I10(ItemId.BLACK_MASK_10_I, ItemId.BLACK_MASK_9_I, 10), BLACK_MASK_I9(ItemId.BLACK_MASK_9_I, ItemId.BLACK_MASK_8_I, 9), BLACK_MASK_I8(ItemId.BLACK_MASK_8_I, ItemId.BLACK_MASK_7_I, 8), BLACK_MASK_I7(ItemId.BLACK_MASK_7_I, ItemId.BLACK_MASK_6_I, 7), BLACK_MASK_I6(ItemId.BLACK_MASK_6_I, ItemId.BLACK_MASK_5_I, 6), BLACK_MASK_I5(ItemId.BLACK_MASK_5_I, ItemId.BLACK_MASK_4_I, 5), BLACK_MASK_I4(ItemId.BLACK_MASK_4_I, ItemId.BLACK_MASK_3_I, 4), BLACK_MASK_I3(ItemId.BLACK_MASK_3_I, ItemId.BLACK_MASK_2_I, 3), BLACK_MASK_I2(ItemId.BLACK_MASK_2_I, ItemId.BLACK_MASK_1_I, 2), BLACK_MASK_I1(ItemId.BLACK_MASK_1_I, ItemId.BLACK_MASK_I, 1), BLACK_MASK_I(ItemId.BLACK_MASK_I, -1, 0), PHARAOHS_SCEPTRE_8(ItemId.PHARAOHS_SCEPTRE_8, ItemId.PHARAOHS_SCEPTRE_7, 8), PHARAOHS_SCEPTRE_7(ItemId.PHARAOHS_SCEPTRE_7, ItemId.PHARAOHS_SCEPTRE_6, 7), PHARAOHS_SCEPTRE_6(ItemId.PHARAOHS_SCEPTRE_6, ItemId.PHARAOHS_SCEPTRE_5, 6), PHARAOHS_SCEPTRE_5(ItemId.PHARAOHS_SCEPTRE_5, ItemId.PHARAOHS_SCEPTRE_4, 5), PHARAOHS_SCEPTRE_4(ItemId.PHARAOHS_SCEPTRE_4, ItemId.PHARAOHS_SCEPTRE_3, 4), PHARAOHS_SCEPTRE_3(ItemId.PHARAOHS_SCEPTRE_3, ItemId.PHARAOHS_SCEPTRE_2, 3), PHARAOHS_SCEPTRE_2(ItemId.PHARAOHS_SCEPTRE_2, ItemId.PHARAOHS_SCEPTRE_1, 2), PHARAOHS_SCEPTRE_1(ItemId.PHARAOHS_SCEPTRE_1, ItemId.PHARAOHS_SCEPTRE, 1), PHARAOHS_SCEPTRE(ItemId.PHARAOHS_SCEPTRE, -1, 0), AMULET_OF_THE_DAMNED(ItemId.AMULET_OF_THE_DAMNED_FULL, ItemId.AMULET_OF_THE_DAMNED, 1), AMULET_OF_THE_DAMNED_EMPTY(ItemId.AMULET_OF_THE_DAMNED, -1, 0), WATERSKIN_4(ItemId.WATERSKIN4, ItemId.WATERSKIN3, 4), WATERSKIN_3(ItemId.WATERSKIN3, ItemId.WATERSKIN2, 3), WATERSKIN_2(ItemId.WATERSKIN2, ItemId.WATERSKIN1, 2), WATERSKIN_1(ItemId.WATERSKIN1, ItemId.WATERSKIN0, 1), WATERSKIN_0(ItemId.WATERSKIN0, -1, 0), OGRE_BELLOWS_3(ItemId.OGRE_BELLOWS_3, ItemId.OGRE_BELLOWS_2, 3), OGRE_BELLOWS_2(ItemId.OGRE_BELLOWS_2, ItemId.OGRE_BELLOWS_1, 2), OGRE_BELLOWS_1(ItemId.OGRE_BELLOWS_1, ItemId.OGRE_BELLOWS, 1), OGRE_BELLOWS_0(ItemId.OGRE_BELLOWS, -1, 0), SPECIAL_WATERING_CAN(ItemId.GRICOLLERS_CAN, ItemId.WATERING_CAN8, 9), WATERING_CAN_8(ItemId.WATERING_CAN8, ItemId.WATERING_CAN7, 8), WATERING_CAN_7(ItemId.WATERING_CAN7, ItemId.WATERING_CAN6, 7), WATERING_CAN_6(ItemId.WATERING_CAN6, ItemId.WATERING_CAN5, 6), WATERING_CAN_5(ItemId.WATERING_CAN5, ItemId.WATERING_CAN4, 5), WATERING_CAN_4(ItemId.WATERING_CAN4, ItemId.WATERING_CAN3, 4), WATERING_CAN_3(ItemId.WATERING_CAN3, ItemId.WATERING_CAN2, 3), WATERING_CAN_2(ItemId.WATERING_CAN2, ItemId.WATERING_CAN1, 2), WATERING_CAN_1(ItemId.WATERING_CAN1, ItemId.WATERING_CAN, 1), WATERING_CAN_0(ItemId.WATERING_CAN, -1, 0), AMULET_OF_GLORY6(ItemId.AMULET_OF_GLORY6, ItemId.AMULET_OF_GLORY5, 6), AMULET_OF_GLORY5(ItemId.AMULET_OF_GLORY5, ItemId.AMULET_OF_GLORY4, 5), AMULET_OF_GLORY4(ItemId.AMULET_OF_GLORY4, ItemId.AMULET_OF_GLORY3, 4), AMULET_OF_GLORY3(ItemId.AMULET_OF_GLORY3, ItemId.AMULET_OF_GLORY2, 3), AMULET_OF_GLORY2(ItemId.AMULET_OF_GLORY2, ItemId.AMULET_OF_GLORY1, 2), AMULET_OF_GLORY1(ItemId.AMULET_OF_GLORY1, ItemId.AMULET_OF_GLORY, 1), AMULET_OF_GLORY0(ItemId.AMULET_OF_GLORY, -1, 0), AMULET_OF_GLORY_T6(ItemId.AMULET_OF_GLORY_T6, ItemId.AMULET_OF_GLORY_T5, 6), AMULET_OF_GLORY_T5(ItemId.AMULET_OF_GLORY_T5, ItemId.AMULET_OF_GLORY_T4, 5), AMULET_OF_GLORY_T4(ItemId.AMULET_OF_GLORY_T4, ItemId.AMULET_OF_GLORY_T3, 4), AMULET_OF_GLORY_T3(ItemId.AMULET_OF_GLORY_T3, ItemId.AMULET_OF_GLORY_T2, 3), AMULET_OF_GLORY_T2(ItemId.AMULET_OF_GLORY_T2, ItemId.AMULET_OF_GLORY_T1, 2), AMULET_OF_GLORY_T1(ItemId.AMULET_OF_GLORY_T1, ItemId.AMULET_OF_GLORY_T, 1), AMULET_OF_GLORY_T(ItemId.AMULET_OF_GLORY_T, -1, 0), RING_OF_DUELING_8(ItemId.RING_OF_DUELING8, ItemId.RING_OF_DUELING7, 8), RING_OF_DUELING_7(ItemId.RING_OF_DUELING7, ItemId.RING_OF_DUELING6, 7), RING_OF_DUELING_6(ItemId.RING_OF_DUELING6, ItemId.RING_OF_DUELING5, 6), RING_OF_DUELING_5(ItemId.RING_OF_DUELING5, ItemId.RING_OF_DUELING4, 5), RING_OF_DUELING_4(ItemId.RING_OF_DUELING4, ItemId.RING_OF_DUELING3, 4), RING_OF_DUELING_3(ItemId.RING_OF_DUELING3, ItemId.RING_OF_DUELING2, 3), RING_OF_DUELING_2(ItemId.RING_OF_DUELING2, ItemId.RING_OF_DUELING1, 2), RING_OF_DUELING_1(ItemId.RING_OF_DUELING1, -1, 1), GAMES_NECKLACE_8(ItemId.GAMES_NECKLACE8, ItemId.GAMES_NECKLACE7, 8), GAMES_NECKLACE_7(ItemId.GAMES_NECKLACE7, ItemId.GAMES_NECKLACE6, 7), GAMES_NECKLACE_6(ItemId.GAMES_NECKLACE6, ItemId.GAMES_NECKLACE5, 6), GAMES_NECKLACE_5(ItemId.GAMES_NECKLACE5, ItemId.GAMES_NECKLACE4, 5), GAMES_NECKLACE_4(ItemId.GAMES_NECKLACE4, ItemId.GAMES_NECKLACE3, 4), GAMES_NECKLACE_3(ItemId.GAMES_NECKLACE3, ItemId.GAMES_NECKLACE2, 3), GAMES_NECKLACE_2(ItemId.GAMES_NECKLACE2, ItemId.GAMES_NECKLACE1, 2), GAMES_NECKLACE_1(ItemId.GAMES_NECKLACE1, -1, 1), SKILLS_NECKLACE_6(ItemId.SKILLS_NECKLACE6, ItemId.SKILLS_NECKLACE5, 6), SKILLS_NECKLACE_5(ItemId.SKILLS_NECKLACE5, ItemId.SKILLS_NECKLACE4, 5), SKILLS_NECKLACE_4(ItemId.SKILLS_NECKLACE4, ItemId.SKILLS_NECKLACE3, 4), SKILLS_NECKLACE_3(ItemId.SKILLS_NECKLACE3, ItemId.SKILLS_NECKLACE2, 3), SKILLS_NECKLACE_2(ItemId.SKILLS_NECKLACE2, ItemId.SKILLS_NECKLACE1, 2), SKILLS_NECKLACE_1(ItemId.SKILLS_NECKLACE1, -1, 1), ETERNAL_SLAYER_RING(ItemId.SLAYER_RING_ETERNAL, ItemId.SLAYER_RING_8, 9), SLAYER_RING_8(ItemId.SLAYER_RING_8, ItemId.SLAYER_RING_7, 8), SLAYER_RING_7(ItemId.SLAYER_RING_7, ItemId.SLAYER_RING_6, 7), SLAYER_RING_6(ItemId.SLAYER_RING_6, ItemId.SLAYER_RING_5, 6), SLAYER_RING_5(ItemId.SLAYER_RING_5, ItemId.SLAYER_RING_4, 5), SLAYER_RING_4(ItemId.SLAYER_RING_4, ItemId.SLAYER_RING_3, 4), SLAYER_RING_3(ItemId.SLAYER_RING_3, ItemId.SLAYER_RING_2, 3), SLAYER_RING_2(ItemId.SLAYER_RING_2, ItemId.SLAYER_RING_1, 2), SLAYER_RING_1(ItemId.SLAYER_RING_1, -1, 1), COMBAT_BRACELET_6(ItemId.COMBAT_BRACELET6, ItemId.COMBAT_BRACELET5, 6), COMBAT_BRACELET_5(ItemId.COMBAT_BRACELET5, ItemId.COMBAT_BRACELET4, 5), COMBAT_BRACELET_4(ItemId.COMBAT_BRACELET4, ItemId.COMBAT_BRACELET3, 4), COMBAT_BRACELET_3(ItemId.COMBAT_BRACELET3, ItemId.COMBAT_BRACELET2, 3), COMBAT_BRACELET_2(ItemId.COMBAT_BRACELET2, ItemId.COMBAT_BRACELET1, 2), COMBAT_BRACELET_1(ItemId.COMBAT_BRACELET1, -1, 1), ABYSSAL_BRACELET_5(ItemId.ABYSSAL_BRACELET5, ItemId.ABYSSAL_BRACELET4, 5), ABYSSAL_BRACELET_4(ItemId.ABYSSAL_BRACELET4, ItemId.ABYSSAL_BRACELET3, 4), ABYSSAL_BRACELET_3(ItemId.ABYSSAL_BRACELET3, ItemId.ABYSSAL_BRACELET2, 3), ABYSSAL_BRACELET_2(ItemId.ABYSSAL_BRACELET2, ItemId.ABYSSAL_BRACELET1, 2), ABYSSAL_BRACELET_1(ItemId.ABYSSAL_BRACELET1, -1, 1), CASTLE_WARS_BRACELET_3(ItemId.CASTLE_WARS_BRACELET3, ItemId.CASTLE_WARS_BRACELET2, 3), CASTLE_WARS_BRACELET_2(ItemId.CASTLE_WARS_BRACELET2, ItemId.CASTLE_WARS_BRACELET1, 2), CASTLE_WARS_BRACELET_1(ItemId.CASTLE_WARS_BRACELET1, -1, 1), DIGSITE_PENDANT_5(ItemId.DIGSITE_PENDANT_5, ItemId.DIGSITE_PENDANT_4, 5), DIGSITE_PENDANT_4(ItemId.DIGSITE_PENDANT_4, ItemId.DIGSITE_PENDANT_3, 4), DIGSITE_PENDANT_3(ItemId.DIGSITE_PENDANT_3, ItemId.DIGSITE_PENDANT_2, 3), DIGSITE_PENDANT_2(ItemId.DIGSITE_PENDANT_2, ItemId.DIGSITE_PENDANT_1, 2), DIGSITE_PENDANT_1(ItemId.DIGSITE_PENDANT_1, -1, 1), RING_OF_RETURNING_5(ItemId.RING_OF_RETURNING5, ItemId.RING_OF_RETURNING4, 5), RING_OF_RETURNING_4(ItemId.RING_OF_RETURNING4, ItemId.RING_OF_RETURNING3, 4), RING_OF_RETURNING_3(ItemId.RING_OF_RETURNING3, ItemId.RING_OF_RETURNING2, 3), RING_OF_RETURNING_2(ItemId.RING_OF_RETURNING2, ItemId.RING_OF_RETURNING1, 2), RING_OF_RETURNING_1(ItemId.RING_OF_RETURNING1, -1, 1), NECKLACE_OF_PASSAGE_5(ItemId.NECKLACE_OF_PASSAGE5, ItemId.NECKLACE_OF_PASSAGE4, 5), NECKLACE_OF_PASSAGE_4(ItemId.NECKLACE_OF_PASSAGE4, ItemId.NECKLACE_OF_PASSAGE3, 4), NECKLACE_OF_PASSAGE_3(ItemId.NECKLACE_OF_PASSAGE3, ItemId.NECKLACE_OF_PASSAGE2, 3), NECKLACE_OF_PASSAGE_2(ItemId.NECKLACE_OF_PASSAGE2, ItemId.NECKLACE_OF_PASSAGE1, 2), NECKLACE_OF_PASSAGE_1(ItemId.NECKLACE_OF_PASSAGE1, -1, 1), BURNING_AMULET_5(ItemId.BURNING_AMULET5, ItemId.BURNING_AMULET4, 5), BURNING_AMULET_4(ItemId.BURNING_AMULET4, ItemId.BURNING_AMULET3, 4), BURNING_AMULET_3(ItemId.BURNING_AMULET3, ItemId.BURNING_AMULET2, 3), BURNING_AMULET_2(ItemId.BURNING_AMULET2, ItemId.BURNING_AMULET1, 2), BURNING_AMULET_1(ItemId.BURNING_AMULET1, -1, 1);
    private final int itemId;
    private final int nextId;
    private final int charges;
    private static final Int2ObjectMap<PresetSubstitute> ITEMS = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<PresetSubstitute> BROKEN_ITEMS = new Int2ObjectOpenHashMap<>();
    private static final Int2IntMap degradedToFullIdMap = new Int2IntOpenHashMap();
    private static final Int2IntMap fullMap = new Int2IntOpenHashMap();

    static {
        for (final PresetSubstitute value : values()) {
            ITEMS.put(value.itemId, value);
            BROKEN_ITEMS.put(value.nextId, value);
            degradedToFullIdMap.put(value.getItemId(), iterateToFullId(value.getItemId()));
            final int completelyDegradedItem = getCompletelyDegradedId(value.itemId);
            final int full = iterateToFullCharges(completelyDegradedItem);
            fullMap.put(value.itemId, full);
        }
    }

    public static final int iterateToFullId(final int itemId) {
        int id = itemId;
        PresetSubstitute last = null;
        final PresetSubstitute current = ITEMS.get(itemId);
        int charges = current == null ? 0 : current.charges;
        while (true) {
            final PresetSubstitute item = BROKEN_ITEMS.get(id);
            if (item == last || item == null) {
                return last == null ? id : last.itemId;
            }
            last = item;
            id = item.getItemId();
            if (item.charges > charges) {
                charges = item.charges;
            }
        }
    }

    public static final int getCompletelyDegradedId(final int itemId) {
        int id = itemId;
        PresetSubstitute last = null;
        while (true) {
            final PresetSubstitute item = ITEMS.get(id);
            if (item == last || item == null) {
                return id;
            }
            last = item;
            id = item.getNextId();
        }
    }

    private static final int iterateToFullCharges(final int itemId) {
        int id = itemId;
        PresetSubstitute last = null;
        final PresetSubstitute current = ITEMS.get(itemId);
        int charges = current == null ? 0 : current.charges;
        while (true) {
            final PresetSubstitute item = BROKEN_ITEMS.get(id);
            if (item == last || item == null) {
                return charges;
            }
            last = item;
            id = item.getItemId();
            if (item.charges > charges) {
                charges = item.charges;
            }
        }
    }

    public static final int getFullCharges(final int itemId) {
        return fullMap.get(itemId);
    }

    public static final int getCharges(final int itemId) {
        final PresetSubstitute constant = ITEMS.get(itemId);
        return constant == null ? 0 : constant.charges;
    }

    PresetSubstitute(int itemId, int nextId, int charges) {
        this.itemId = itemId;
        this.nextId = nextId;
        this.charges = charges;
    }

    public static final int[] findSubstitutes(final int id) {
        final int fullId = degradedToFullIdMap.get(id);
        if (fullId == 0) {
            return null;
        }
        PresetSubstitute deg = ITEMS.get(fullId);
        if (deg == null) {
            return null;
        }
        final IntArrayList list = new IntArrayList();
        list.add(deg.getItemId());
        while (true) {
            final PresetSubstitute nextDeg = ITEMS.get(deg.getNextId());
            if (nextDeg == null || nextDeg == deg) {
                break;
            }
            deg = nextDeg;
            list.add(deg.getItemId());
        }
        return list.toIntArray();
    }

    public int getItemId() {
        return itemId;
    }

    public int getNextId() {
        return nextId;
    }

    public int getCharges() {
        return charges;
    }
}
