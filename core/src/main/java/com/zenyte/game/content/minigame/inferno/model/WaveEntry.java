package com.zenyte.game.content.minigame.inferno.model;

/**
 * @author Tommeh | 29/11/2019 | 20:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WaveEntry {

    private final WaveNPC npc;
    private final int count;

    public WaveEntry(final WaveNPC npc) {
        this(npc, 1);
    }
    
    public WaveEntry(WaveNPC npc, int count) {
        this.npc = npc;
        this.count = count;
    }
    
    public WaveNPC getNpc() {
        return npc;
    }
    
    public int getCount() {
        return count;
    }
}
