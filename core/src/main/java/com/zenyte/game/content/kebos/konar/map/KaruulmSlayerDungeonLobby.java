package com.zenyte.game.content.kebos.konar.map;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 14/10/2019 | 20:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KaruulmSlayerDungeonLobby extends KaruulmSlayerDungeon {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] {
                { 1303, 10211 },
                { 1303, 10201 },
                { 1307, 10201 },
                { 1307, 10197 },
                { 1311, 10197 },
                { 1311, 10187 },
                { 1313, 10187 },
                { 1313, 10197 },
                { 1317, 10197 },
                { 1317, 10201 },
                { 1321, 10201 },
                { 1321, 10211 },
                { 1317, 10211 },
                { 1317, 10215 },
                { 1307, 10215 },
                { 1307, 10211 }
        })};
    }

    @Override
    public String name() {
        return "Karuulm Slayer Dungeon Lobby";
    }
}
