package com.zenyte.game.content.skills.runecrafting;

/**
 * @author Kris | 22. okt 2017 : 19:34.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum CombinationRunecrafting {
	MIST_RUNE_AIR(4695, Runecrafting.WATER_RUNE.getRuneId(), 6, 8, Runecrafting.FIRE_RUNE.getAltarObjectId(), 1444),
	MIST_RUNE_WATER(4695, Runecrafting.AIR_RUNE.getRuneId(), 6, 8.5, Runecrafting.WATER_RUNE.getAltarObjectId(), 1438),
	DUST_RUNE_AIR(4696, Runecrafting.EARTH_RUNE.getRuneId(), 10, 8.3, Runecrafting.AIR_RUNE.getAltarObjectId(), 1440),
    DUST_RUNE_EARTH(4696, Runecrafting.AIR_RUNE.getRuneId(), 10, 9, Runecrafting.EARTH_RUNE.getAltarObjectId(), 1438),
    MUD_RUNE_WATER(4698, Runecrafting.EARTH_RUNE.getRuneId(), 13, 9.3, Runecrafting.WATER_RUNE.getAltarObjectId(), 1440),
    MUD_RUNE_EARTH(4698, Runecrafting.WATER_RUNE.getRuneId(), 13, 9.5, Runecrafting.EARTH_RUNE.getAltarObjectId(), 1444),
    SMOKE_RUNE_AIR(4697, Runecrafting.FIRE_RUNE.getRuneId(), 15, 8.5, Runecrafting.AIR_RUNE.getAltarObjectId(), 1442),
    SMOKE_RUNE_FIRE(4697, Runecrafting.AIR_RUNE.getRuneId(), 15, 9, Runecrafting.FIRE_RUNE.getAltarObjectId(), 1438),
    STEAM_RUNE_WATER(4694, Runecrafting.FIRE_RUNE.getRuneId(), 19, 9.5, Runecrafting.WATER_RUNE.getAltarObjectId(), 1442),
    STEAM_RUNE_FIRE(4694, Runecrafting.WATER_RUNE.getRuneId(), 19, 10, Runecrafting.FIRE_RUNE.getAltarObjectId(), 1444),
    LAVA_RUNE_EARTH(4699, Runecrafting.FIRE_RUNE.getRuneId(), 23, 10, Runecrafting.EARTH_RUNE.getAltarObjectId(), 1442),
    LAVA_RUNE_FIRE(4699, Runecrafting.EARTH_RUNE.getRuneId(), 23, 10.5, Runecrafting.FIRE_RUNE.getAltarObjectId(), 1440);

    public static final CombinationRunecrafting[] VALUES = values();
    private final int runeId, requiredRuneId, levelRequired, objectId, talismanId;
    private final double experience;

    CombinationRunecrafting(final int runeId, final int requiredRuneId, final int requiredLevel, final double experience, final int objectId, final int talismanId) {
        this.runeId = runeId;
        this.requiredRuneId = requiredRuneId;
        levelRequired = requiredLevel;
        this.experience = experience;
        this.objectId = objectId;
        this.talismanId = talismanId;
    }

    public int getRuneId() {
        return runeId;
    }

    public int getRequiredRuneId() {
        return requiredRuneId;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getTalismanId() {
        return talismanId;
    }

    public double getExperience() {
        return experience;
    }

}
