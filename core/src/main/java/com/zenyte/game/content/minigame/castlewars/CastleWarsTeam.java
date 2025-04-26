package com.zenyte.game.content.minigame.castlewars;

import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;
import java.util.List;

import static com.zenyte.game.content.minigame.castlewars.CastleWarsArea.SARADOMIN_RESPAWN;
import static com.zenyte.game.content.minigame.castlewars.CastleWarsArea.ZAMORAK_RESPAWN;
import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.SARADOMIN_LOBBY_SPAWN;
import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.ZAMORAK_LOBBY_SPAWN;
import static com.zenyte.plugins.object.CastleWarsLargeDoor.SARADOMIN_DOORS;
import static com.zenyte.plugins.object.CastleWarsLargeDoor.ZAMORAK_DOORS;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public enum CastleWarsTeam {


    SARADOMIN(SARADOMIN_RESPAWN, SARADOMIN_LOBBY_SPAWN, new IntArrayList(SARADOMIN_DOORS), Arrays.asList(CastlewarsRockPatch.SOUTH, CastlewarsRockPatch.EAST)),
    ZAMORAK(ZAMORAK_RESPAWN, ZAMORAK_LOBBY_SPAWN, new IntArrayList(ZAMORAK_DOORS), Arrays.asList(CastlewarsRockPatch.NORTH, CastlewarsRockPatch.WEST)),
    ;

    private static int saraBarricades = 0;
    private static int zamBarricades = 0;
    private final Location respawn;
    private final Location lobbySpawn;
    private final IntList largeCastleDoors;
    private final List<CastlewarsRockPatch> rockPatches;

    public static final CastleWarsTeam[] VALUES = values();

    CastleWarsTeam(Location respawn, Location lobbySpawn, IntList largeCastleDoors, List<CastlewarsRockPatch> rockPatches) {
        this.respawn = respawn;
        this.lobbySpawn = lobbySpawn;
        this.largeCastleDoors = largeCastleDoors;
        this.rockPatches = rockPatches;
    }

    public static int getSaraBarricades() {
        return saraBarricades;
    }

    public static void setSaraBarricades(int saraBarricades) {
        CastleWarsTeam.saraBarricades = saraBarricades;
    }

    public static int getZamBarricades() {
        return zamBarricades;
    }

    public static void setZamBarricades(int zamBarricades) {
        CastleWarsTeam.zamBarricades = zamBarricades;
    }

    public Location getRespawn() {
        return respawn;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public IntList getLargeCastleDoors() {
        return largeCastleDoors;
    }

    public List<CastlewarsRockPatch> getRockPatches() {
        return rockPatches;
    }
}
