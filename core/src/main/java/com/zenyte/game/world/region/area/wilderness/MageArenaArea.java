package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MageArenaArea extends WildernessArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3102, 3953 },
                        { 3096, 3953 },
                        { 3084, 3941 },
                        { 3084, 3923 },
                        { 3093, 3914 },
                        { 3118, 3914 },
                        { 3127, 3923 },
                        { 3127, 3942 },
                        { 3116, 3953 },
                        { 3110, 3953 },
                        { 3109, 3952 },
                        { 3108, 3952 },
                        { 3107, 3953 },
                        { 3105, 3953 },
                        { 3104, 3952 },
                        { 3103, 3952 }
                })
        };
    }

    @Override
    public boolean processCombat(Player player, Entity entity, String style) {
        if (!style.equalsIgnoreCase("Magic")) {
            player.sendMessage("You can only use Magic in the Mage Arena.");
            return false;
        }
        return true;
    }

    @Override
    public String name() {
        return "Mage Arena";
    }

}
