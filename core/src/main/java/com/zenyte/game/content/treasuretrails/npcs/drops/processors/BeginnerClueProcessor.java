package com.zenyte.game.content.treasuretrails.npcs.drops.processors;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.npcs.drops.BeginnerClueNPC;
import com.zenyte.game.content.treasuretrails.npcs.drops.PredicatedClueDrop;

import java.util.List;
import java.util.Map;

/**
 * @author Kris | 22/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BeginnerClueProcessor extends ClueProcessor {

    @Override
    protected int itemId() {
        return ClueItem.BEGINNER.getScrollBox();
    }

    @Override
    protected Map<String, List<PredicatedClueDrop>> map() {
        return BeginnerClueNPC.mappedNPCs;
    }
}
