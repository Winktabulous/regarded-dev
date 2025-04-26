package com.zenyte.plugins.drop.catacombsofkourend;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 30/05/2019 02:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class SuperiorCatacombsProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dark totem base
        appendDrop(new DisplayedDrop(ItemId.DARK_TOTEM_BASE, 1, 1, 1));
        put(19679, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don't already have the respective totem piece. Only found in Catacombs of Kourend."));
        //Dark totem middle
        appendDrop(new DisplayedDrop(ItemId.DARK_TOTEM_MIDDLE, 1, 1, 1));
        put(19681, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don't already have the respective totem piece. Only found in Catacombs of Kourend."));
        //Dark totem top
        appendDrop(new DisplayedDrop(ItemId.DARK_TOTEM_TOP, 1, 1, 1));
        put(19683, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don't already have the respective totem piece. Only found in Catacombs of Kourend."));
    
        appendDrop(new DisplayedDrop(ItemId.ANCIENT_SHARD, 1, 1, 1));
    }

    public void onDeath(final NPC npc, final Player killer) {
        if (!killer.inArea("Catacombs of Kourend")) {
            return;
        }
        CatacombsProcessor.dropTotemPiece(killer, npc);
        CatacombsProcessor.dropAncientShard(killer, npc);
    }

    @Override
    public int[] ids() {
        return new int[] {
                7391, 7398, 7400, 7403, 7404, 7411, 7410, 7409
        };
    }
}
