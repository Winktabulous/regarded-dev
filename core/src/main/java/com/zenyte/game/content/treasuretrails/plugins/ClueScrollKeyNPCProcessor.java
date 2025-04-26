package com.zenyte.game.content.treasuretrails.plugins;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.content.treasuretrails.challenges.KeyRequest;
import com.zenyte.game.content.treasuretrails.clues.CrypticClue;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 08/04/2019 14:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClueScrollKeyNPCProcessor extends DropProcessor {
    @Override
    public void attach() {
        CrypticClue.npcKeyMap.forEach((npcId, keyList) -> keyList.forEach(clue -> {
            if (!(clue.getChallenge() instanceof KeyRequest)) {
                return;
            }
            final KeyRequest challenge = ((KeyRequest) clue.getChallenge());
            appendDrop(new DisplayedDrop(challenge.getKeyId(), 1, 1, 1, (p, npc) -> npc.equals(npcId), npcId));
            put(npcId, challenge.getKeyId(), new PredicatedDrop("The key is only dropped whilst carrying the respective clue scroll."));
        }));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        TreasureTrail.getKey(killer, npc).ifPresent(key -> npc.dropItem(killer, new Item(key)));
    }

    @Override
    public int[] ids() {
        return CrypticClue.npcKeyMap.keySet().toIntArray();
    }
}
