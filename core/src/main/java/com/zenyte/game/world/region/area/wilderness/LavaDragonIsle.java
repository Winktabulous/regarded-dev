package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 01/09/2019 02:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LavaDragonIsle extends WildernessArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3209, 3856 },
                        { 3193, 3856 },
                        { 3188, 3862 },
                        { 3174, 3853 },
                        { 3178, 3830 },
                        { 3169, 3808 },
                        { 3190, 3801 },
                        { 3192, 3799 },
                        { 3201, 3809 },
                        { 3211, 3802 },
                        { 3228, 3816 },
                        { 3233, 3817 },
                        { 3237, 3826 },
                        { 3232, 3837 },
                        { 3235, 3844 },
                        { 3223, 3851 }
                })
        };
    }

    @Override
    public String name() {
        return "Wilderness: Lava Dragon Isle";
    }
}
