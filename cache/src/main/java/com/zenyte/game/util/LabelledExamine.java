package com.zenyte.game.util;

/**
 * @author Kris | 22/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LabelledExamine extends Examine {
    public LabelledExamine(int id, final String name, String examine) {
        super(id, examine);
        this.name = name;
    }

    private final String name;
    
    public String getName() {
        return name;
    }
}
