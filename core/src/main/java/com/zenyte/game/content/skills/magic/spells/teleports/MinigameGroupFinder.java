package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.StaticInitializer;

/**
 * @author Kris | 25/10/2018 16:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

@StaticInitializer
public enum MinigameGroupFinder implements Teleport {

    BARBARIAN_ASSAULT(new Location(2519, 3570, 0)),
    BLAST_FURNACE(new Location(2930, 10192, 0)),
    BURTHORPE_GAMES_ROOM(new Location(2899, 3563, 0)),
    CASTLE_WARS(new Location(2441, 3090, 0)),
    CLAN_WARS(new Location(3370, 3162, 0)),
    DAGANNOTH_KINGS(null),
    FISHING_TRAWLER(new Location(2666, 3161, 0)),
    GOD_WARS(null),
    LAST_MAN_STANDING(new Location(3403, 3177, 0)),
    NIGHTMARE_ZONE(new Location(2610, 3115, 0)),
    PEST_CONTROL(new Location(2658, 2660, 0)),
    PLAYER_OWNED_HOUSES(null),
    RAT_PITS(new Location(3269, 3403, 0)),
    SHADES_OF_MORTTON(new Location(3506, 3314, 0)),
    SHIELD_OF_ARRAV(null),
    THEATRE_OF_BLOOD(null),
    TITHE_FARM(new Location(1791, 3591, 0)),
    TROUBLE_BREWING(new Location(3813, 3025, 0)),
    TZHAAR_FIGHT_PIT(new Location(2400, 5179, 0)),
    VOLCANIC_MINE(null);

    private static final MinigameGroupFinder[] values = values();

    static {
        /*if (values.length != Enums.MINIGAMES_LIST.getSize()) {
            throw new RuntimeException("Minigame group finder is out of date!");
        }*/
    }

    public static MinigameGroupFinder getMinigame(final int slot) {
        if (slot < 1 || slot > values.length) {
            throw new RuntimeException("Invalid minigame for slot: " + slot);
        }
        return values[slot - 1];
    }

    private final Location tile;

    @Override
    public TeleportType getType() {
        return TeleportType.MINIGAMES_TELEPORT;
    }

    @Override
    public Location getDestination() {
        return tile;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public int getRandomizationDistance() {
        return 2;
    }

    @Override
    public Item[] getRunes() {
        return null;
    }

    @Override
    public int getWildernessLevel() {
        return -1;
    }

    @Override
    public boolean isCombatRestricted() {
        return true;
    }

    MinigameGroupFinder(Location tile) {
        this.tile = tile;
    }

    @Override
    public void onArrival(Player player) {

    }
}
