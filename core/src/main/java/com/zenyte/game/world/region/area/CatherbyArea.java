package com.zenyte.game.world.region.area;

import com.zenyte.game.world.region.RSPolygon;

public class CatherbyArea extends KingdomOfKandarin {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2790, 3449 }, { 2790, 3408 }, { 2811, 3408 }, { 2810, 3429 }, { 2820, 3434 }, { 2830, 3434 }, { 2834, 3427 }, { 2826, 3427 }, { 2826, 3422 }, { 2831, 3422 }, { 2842, 3414 }, { 2842, 3410 }, { 2846, 3410 }, { 2852, 3414 }, { 2861, 3414 }, { 2867, 3426 }, { 2867, 3430 }, { 2865, 3433 }, { 2860, 3437 }, { 2855, 3441 }, { 2838, 3449 }, { 2838, 3452 }, { 2817, 3471 }, { 2805, 3471 }}, 0)};
    }

    @Override
    public String name() {
        return "Catherby";
    }
}
