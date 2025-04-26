package com.zenyte.game.world.entity.masks;

/**
 * @author Kris | 27/11/2018 10:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface RenderType {

    int getStand();
    int getStandTurn();
    int getWalk();
    int getRotate180();
    int getRotate90();
    int getRotate270();
    int getRun();

}
