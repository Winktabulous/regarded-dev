package com.zenyte.plugins;

/**
 * @author Kris | 18/10/2018 18:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum ListenerType {

    INTERFACE_SWITCH,
    LOBBY_CLOSE,
    LOGIN,
    LOGOUT;

    public static final ListenerType[] values = values();

}
