
package com.zenyte.plugins.drop.dragons;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.npcs.NPCDefinitions;

/**
 * @author Kris | 30/01/2019 20:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DraconicVisageProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(11286, 1, 1) {
            public double getRate(final Player player, final int id) {
                return DraconicVisageProcessor.getRate(id);
            }
        });
    }

    public void onDeath(final NPC npc, final Player killer) {
        final int rate = getRate(npc.getId());
        if (random(rate) == 0) {
            npc.dropItem(killer, new Item(11286));
        }
    }

    private static final int getRate(final int id) {
        final String name = NPCDefinitions.getOrThrow(id).getName().toLowerCase();
        switch (name) {
        case "king black dragon": 
            return 400;
        case "vorkath": 
            return 250;

        case "rune dragon": 
            return 4000;

        case "adamant dragon": 
            return 4500;

        default: 
            return 5000;
        }
    }

    @Override
    public int[] ids() {
        return new int[] {239, 256, 7255, 8031, 8088, 8089, 8030, 7273, 139, 8087, 273, 274, 2919, 272, 8080, 468, 8086, 8091, 8090, 7254, 7863, 7275, 7274, 465, 6593, 467, 2642, 8084, 466, 8027, 8092, 8093, 8026, 7862, 7861, 275, 8085, 258, 253, 259, 252, 257, 8058, 8059, 8061, 255, 254, 8060};
    }
}
