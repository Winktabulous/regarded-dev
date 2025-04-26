package com.zenyte.game.content.minigame.duelarena.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 30-11-2018 | 20:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ObstaclesArenaArea extends ArenaArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][]{
                { 3339, 3240 },
                { 3339, 3239 },
                { 3337, 3239 },
                { 3337, 3238 },
                { 3335, 3238 },
                { 3335, 3237 },
                { 3334, 3237 },
                { 3334, 3235 },
                { 3333, 3235 },
                { 3333, 3230 },
                { 3334, 3230 },
                { 3334, 3228 },
                { 3335, 3228 },
                { 3335, 3227 },
                { 3337, 3227 },
                { 3337, 3226 },
                { 3339, 3226 },
                { 3339, 3225 },
                { 3352, 3225 },
                { 3352, 3226 },
                { 3354, 3226 },
                { 3354, 3227 },
                { 3356, 3227 },
                { 3356, 3228 },
                { 3357, 3228 },
                { 3357, 3230 },
                { 3358, 3230 },
                { 3359, 3235 },
                { 3357, 3235 },
                { 3357, 3237 },
                { 3356, 3237 },
                { 3356, 3238 },
                { 3354, 3238 },
                { 3354, 3239 },
                { 3352, 3239 },
                { 3352, 3240 }
        }) };
    }

    @Override
    public String name() {
        return "Obstacle Arena";
    }
}