package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.model.MinimapState;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

/**
 * @author Kris | 04/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
//public class HalloweenMazeArea extends HalloweenSurfaceArea {
//
//    @Override
//    public RSPolygon[] polygons() {
//        return new RSPolygon[] {
//                new RSPolygon(new int[][]{
//                        { 1728, 4706},
//                        { 1772, 4706 },
//                        { 1772, 4736 },
//                        { 1728, 4736 }
//                })
//        };
//    }
//
//    @Override
//    public void enter(Player player) {
//        super.enter(player);
//        player.getPacketDispatcher().sendMinimapState(MinimapState.DISABLED);
//    }
//
//    @Override
//    public void leave(Player player, boolean logout) {
//        super.leave(player, logout);
//        player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
//    }
//
//    @Override
//    public String name() {
//        return "Halloween Mansion (Maze)";
//    }
//
//}
