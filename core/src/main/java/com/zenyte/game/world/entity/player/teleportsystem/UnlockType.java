package com.zenyte.game.world.entity.player.teleportsystem;

/**
 * @author Kris | 30/03/2019 18:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum UnlockType {

    DEFAULT("Default"),
    VISIT("Visiting"),
    SCROLL("Teleport scroll");

    final String formatted;
    
    UnlockType(String formatted) {
        this.formatted = formatted;
    }

}
