package com.zenyte.game.content.skills.farming;

import com.zenyte.game.util.Attribute;

import java.util.EnumSet;

/**
 * @author Kris | 19/02/2019 11:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum FarmingAttribute {

    FLAGS(new Attribute<>("flags", EnumSet.class)),
    VALUE(new Attribute<>("value", Integer.class)),
    LIVES(new Attribute<>("lives", Integer.class)),
    TIME(new Attribute<>("time", Long.class)),
    PATCH(new Attribute<>("patch", FarmingPatch.class)),
    PRODUCT(new Attribute<>("product", FarmingProduct.class)),
    REDWOOD(new Attribute<>("redwood", RedwoodTree.class)),
    COMPOST(new Attribute<>("compost", CompostBin.class));

    private final Attribute<?> key;

    FarmingAttribute(Attribute<?> key) {
        this.key = key;
    }

    public Attribute<?> getKey() {
        return key;
    }

}
