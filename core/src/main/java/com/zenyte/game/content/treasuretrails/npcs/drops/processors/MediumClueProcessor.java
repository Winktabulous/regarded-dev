package com.zenyte.game.content.treasuretrails.npcs.drops.processors;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.npcs.drops.MediumClueNPC;
import com.zenyte.game.content.treasuretrails.npcs.drops.PredicatedClueDrop;

import java.util.List;
import java.util.Map;

/**
 * @author Kris | 22/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MediumClueProcessor extends ClueProcessor {

    @Override
    protected int itemId() {
        return ClueItem.MEDIUM.getScrollBox();
    }

    @Override
    protected Map<String, List<PredicatedClueDrop>> map() {
        return MediumClueNPC.mappedNPCs;
    }
}
