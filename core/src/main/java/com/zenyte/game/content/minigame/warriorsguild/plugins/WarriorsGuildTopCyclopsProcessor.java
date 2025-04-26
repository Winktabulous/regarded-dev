package com.zenyte.game.content.minigame.warriorsguild.plugins;

import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 18/04/2019 17:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WarriorsGuildTopCyclopsProcessor extends DropProcessor {

    private static final int[] topFloorCyclops = new int[] {
            2463, 2468, 2464, 2465, 2467, 2466
    };

    @Override
    public void attach() {
        for (int i = 8844; i <= 8850; i++) {
            appendDrop(new DisplayedDrop(i, 1, 1, 25));
            if (i == 8850) {
                put(i, new PredicatedDrop("Only dropped if the player is carrying an adamantite tier defender or higher."));
            } else if (i > 8844) {
                put(i, new PredicatedDrop("Only dropped if the highest tier defender the player is carrying is " + ItemDefinitions.getOrThrow(i - 1).getName().toLowerCase() + "."));
            } else {
                put(i, new PredicatedDrop("Only dropped if the player is not carrying any defenders."));
            }
        }
    }

    @Override
    public int[] ids() {
        return topFloorCyclops;
    }
}
