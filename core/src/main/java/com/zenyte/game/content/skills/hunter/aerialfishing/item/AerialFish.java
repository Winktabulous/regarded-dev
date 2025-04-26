package com.zenyte.game.content.skills.hunter.aerialfishing.item;

/**
 * @author Cresinkel
 */

public enum AerialFish {

    BLUEGILL(11.5, 16.5, 3.5, 35, 43),
    COMMON_TENCH(40, 45, 10, 51, 56),
    MOTTLED_EEL(65, 90, 20, 68, 73),
    GREATER_SIREN(100, 130, 25, 87, 91);

    
    final double fishingXP;
    final double hunterXP;
    final double cookingXP;
    final int hunterLevel;
    final int fishingLevel;
    AerialFish(double fishingXP, double hunterXP, double cookingXP, int hunterLevel, int fishingLevel) {
        this.fishingXP = fishingXP;
        this.hunterXP = hunterXP;
        this.cookingXP = cookingXP;
        this.hunterLevel = hunterLevel;
        this.fishingLevel = fishingLevel;
    }


    public int getHunterLevel() {
        return hunterLevel;
    }

    public int getFishingLevel() {
        return fishingLevel;
    }

    public double getFishingXP() {
        return fishingXP;
    }

    public double getHunterXP() {
        return hunterXP;
    }
}
