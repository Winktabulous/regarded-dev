package com.zenyte.game.content.skills.afk;

import com.zenyte.game.content.skills.afk.impl.*;

public enum AFKSkillingObject {
    FARMING(50084, AfkFarmingAction.class),
    FIRE_MAKING(50085, AfkFiremakingAction.class),
    CRAFTING(50093, AfkCraftingAction.class),
    MINING(50087, AfkMiningAction.class),
    SMITHING(50094, AfkSmithingAction.class),
    WOODCUTTING(50090, AfkWoodCuttingAction.class),
    AGILITY(50061, AfkAgilityAction.class),
    THIEVING(50089, AfkThievingAction.class),
    RUNECRAFTING(50095, AfkRunecraftingAction.class),
    FISHING(50096, AfkFishingAction.class),
    ;

    private int objectId;
    private Class<? extends BasicAfkAction> clazz;

    AFKSkillingObject(int objectId, Class<? extends BasicAfkAction> clazz) {
        this.objectId = objectId;
        this.clazz = clazz;
    }

    public int getObjectId() {
        return objectId;
    }

    public Class<? extends BasicAfkAction> getClazz() {
        return clazz;
    }

    public static AFKSkillingObject forId(int id) {
        for (AFKSkillingObject value : VALUES) {
            if(value.getObjectId() == id)
                return value;
        }
        return null;
    }

    public static final AFKSkillingObject[] VALUES = values();
}
