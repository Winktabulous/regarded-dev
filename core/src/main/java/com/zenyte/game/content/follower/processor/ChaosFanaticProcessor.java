package com.zenyte.game.content.follower.processor;

import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;

/**
 * @author Tommeh | 01/10/2019 | 19:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ChaosFanaticProcessor extends DropProcessor {
    public static final int NPC_ID = 6619;

    @Override
    public void attach() {
        final BossPet pet = BossPet.PET_CHAOS_ELEMENTAL;
        appendDrop(new DisplayedDrop(pet.getItemId(), 1, 1, pet.getRarity(null, NPC_ID)));
    }

    @Override
    public int[] ids() {
        return new int[] {NPC_ID};
    }
}
