package com.zenyte.game.world.entity.npc;

public enum NpcOverhead {
        MELEE(0),
        RANGE(1),
        MAGE(2),
        RANGE_MAGE(6),
        RANGE_MELEE(7),
        MAGE_MELEE(8),
        RANGE_MAGE_MELEE(9)
    ;
    public final int archive = 440;
    public final int sprite;
    NpcOverhead(int spriteId){
        this.sprite = spriteId;
    }
}
