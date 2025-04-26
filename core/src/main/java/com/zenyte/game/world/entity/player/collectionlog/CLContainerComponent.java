package com.zenyte.game.world.entity.player.collectionlog;

/**
 * @author Kris | 13/03/2019 14:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */

enum CLContainerComponent {

    CONTAINER, CONTAINER_OPTIONS, CONTAINER_TEXT, CONTAINER_SCROLLBAR;
    private final String toString = name().toLowerCase().replaceAll("_", " ");
    
    public String toString() {
        return toString;
    }

}
