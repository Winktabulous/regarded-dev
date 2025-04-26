package com.zenyte.game.world.entity.npc.drop.matrix;

/**
 * @author Tommeh | 05/10/2019 | 19:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ItemDrop {

    private final int npcId;
    private final NPCDrops.DisplayedNPCDrop drop;

    public ItemDrop(int npcId, NPCDrops.DisplayedNPCDrop drop) {
        this.npcId = npcId;
        this.drop = drop;
    }

    public int getNpcId() {
        return npcId;
    }

    public NPCDrops.DisplayedNPCDrop getDrop() {
        return drop;
    }
}
