package com.zenyte.game.content.minigame.duelarena.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 30-11-2018 | 20:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class NoMovementArena extends ArenaArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][]{
                { 3339, 3221 },
                { 3339, 3220 },
                { 3337, 3220 },
                { 3337, 3219 },
                { 3335, 3219 },
                { 3335, 3218 },
                { 3334, 3218 },
                { 3334, 3216 },
                { 3333, 3216 },
                { 3333, 3211 },
                { 3334, 3211 },
                { 3334, 3209 },
                { 3335, 3209 },
                { 3335, 3208 },
                { 3337, 3208 },
                { 3337, 3207 },
                { 3339, 3207 },
                { 3339, 3206 },
                { 3345, 3206 },
                { 3345, 3207 },
                { 3346, 3207 },
                { 3346, 3206 },
                { 3352, 3206 },
                { 3352, 3207 },
                { 3354, 3207 },
                { 3354, 3208 },
                { 3356, 3208 },
                { 3356, 3209 },
                { 3357, 3209 },
                { 3357, 3211 },
                { 3358, 3211 },
                { 3358, 3216 },
                { 3357, 3216 },
                { 3357, 3218 },
                { 3356, 3218 },
                { 3356, 3219 },
                { 3354, 3219 },
                { 3354, 3220 },
                { 3352, 3220 },
                { 3352, 3221 },
                { 3346, 3221 },
                { 3346, 3220 },
                { 3345, 3220 },
                { 3345, 3221 }
        }) };
    }

    @Override
    public String name() {
        return "No-movement Arena";
    }
}
