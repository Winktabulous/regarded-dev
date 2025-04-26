package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.clues.CrypticClue;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 08/04/2019 00:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueScrollNPCProcessor extends DropProcessor {

    @Override
    public void attach() {

    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        TreasureTrail.kill(killer, npc);
    }

    @Override
    public int[] ids() {
        return new IntOpenHashSet(CrypticClue.npcKillMap.keySet()).toIntArray();
    }
}
