package com.zenyte.game.util;

/**
 * @author Kris | 19/02/2019 15:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Attribute<T> {

    public Attribute(final String name, final Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    private final String name;
    private final Class<T> clazz;

    public String name() {
        return name;
    }

    public Class<T> clazz() {
        return clazz;
    }

}
