package com.zenyte.game.content.minigame.duelarena.area;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Tommeh | 29-11-2018 | 21:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class NormalArenaArea extends ArenaArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][]{
                { 3338, 3259 },
                { 3338, 3258 },
                { 3337, 3258 },
                { 3337, 3257 },
                { 3335, 3257 },
                { 3335, 3256 },
                { 3334, 3256 },
                { 3334, 3254 },
                { 3333, 3254 },
                { 3333, 3249 },
                { 3334, 3249 },
                { 3334, 3247 },
                { 3335, 3247 },
                { 3335, 3246 },
                { 3337, 3246 },
                { 3337, 3245 },
                { 3339, 3245 },
                { 3339, 3244 },
                { 3352, 3244 },
                { 3352, 3245 },
                { 3354, 3245 },
                { 3354, 3246 },
                { 3356, 3246 },
                { 3356, 3247 },
                { 3357, 3247 },
                { 3357, 3249 },
                { 3358, 3249 },
                { 3358, 3250 },
                { 3359, 3250 },
                { 3359, 3253 },
                { 3358, 3253 },
                { 3358, 3254 },
                { 3357, 3254 },
                { 3357, 3256 },
                { 3356, 3256 },
                { 3356, 3257 },
                { 3354, 3257 },
                { 3354, 3258 },
                { 3352, 3258 },
                { 3352, 3259 }
        }) };
    }

    @Override
    public String name() {
        return "Normal Arena";
    }
}
