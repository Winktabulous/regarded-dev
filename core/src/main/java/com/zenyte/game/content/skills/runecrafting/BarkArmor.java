package com.zenyte.game.content.skills.runecrafting;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

public enum BarkArmor {

    SWAMP_GAUNTLETS(ItemId.SWAMPBARK_GAUNTLETS, ItemId.SPLITBARK_GAUNTLETS, 100, false, 0, 42, EquipmentSlot.HANDS),
    SWAMP_BOOTS(ItemId.SWAMPBARK_BOOTS, ItemId.SPLITBARK_BOOTS, 100, false, 0, 42, EquipmentSlot.BOOTS),
    SWAMP_HELM(ItemId.SWAMPBARK_HELM, ItemId.SPLITBARK_HELM, 200, false, 0, 46, EquipmentSlot.HELMET),
    SWAMP_BODY(ItemId.SWAMPBARK_BODY, ItemId.SPLITBARK_BODY, 500, false, 0, 48, EquipmentSlot.PLATE),
    SWAMP_LEGS(ItemId.SWAMPBARK_LEGS, ItemId.SPLITBARK_LEGS, 500, false, 0, 48, EquipmentSlot.LEGS),

    BLOOD_GAUNTLETS(ItemId.BLOODBARK_GAUNTLETS, ItemId.SPLITBARK_GAUNTLETS, 100, true, 0, 77, EquipmentSlot.HANDS),
    BLOOD_BOOTS(ItemId.BLOODBARK_BOOTS, ItemId.SPLITBARK_BOOTS, 100, true, 0, 77, EquipmentSlot.BOOTS),
    BLOOD_HELM(ItemId.BLOODBARK_HELM, ItemId.SPLITBARK_HELM, 250, true, 0, 79, EquipmentSlot.HELMET),
    BLOOD_BODY(ItemId.BLOODBARK_BODY, ItemId.SPLITBARK_BODY, 500, true, 0, 81, EquipmentSlot.PLATE),
    BLOOD_LEGS(ItemId.BLOODBARK_LEGS, ItemId.SPLITBARK_LEGS, 500, true, 0, 81, EquipmentSlot.LEGS);

    public static final List<BarkArmor> BLOOD_ARMOR = new ArrayList<>();
    public static final List<BarkArmor> SWAMP_ARMOR = new ArrayList<>();
    public static final BarkArmor[] VALUES = values();
    private final int itemId;
    private final int splitbarkId;
    private final int runeCost;
    private final boolean blood;
    private final double boost;
    private final int runecraftingLevel;
    private final EquipmentSlot equipmentSlot;

    static {
        for (BarkArmor barkArmor : VALUES) {
            if (barkArmor.blood) {
                BLOOD_ARMOR.add(barkArmor);
            } else {
                SWAMP_ARMOR.add(barkArmor);
            }
        }
    }

    BarkArmor(int itemId, int splitbarkId, int runeCost, boolean blood, double boost, int runecraftingLevel, EquipmentSlot equipmentSlot) {
        this.itemId = itemId;
        this.splitbarkId = splitbarkId;
        this.runeCost = runeCost;
        this.blood = blood;
        this.boost = boost;
        this.runecraftingLevel = runecraftingLevel;
        this.equipmentSlot = equipmentSlot;
    }

    public int getRunecraftingLevel() {
        return runecraftingLevel;
    }

    public int getItemId() {
        return itemId;
    }

    public int getSplitbarkId() {
        return splitbarkId;
    }

    public int getRuneCost() {
        return runeCost;
    }

    public boolean isBlood() {
        return blood;
    }

    public double getBoost() {
        return boost;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

}
