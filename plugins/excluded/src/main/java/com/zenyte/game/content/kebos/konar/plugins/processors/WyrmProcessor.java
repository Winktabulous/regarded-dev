package com.zenyte.game.content.kebos.konar.plugins.processors;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 26/10/2019 | 17:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WyrmProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.DRAGON_HARPOON, 1, 1, 300));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_SWORD, 1, 1, 300));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_THROWNAXE, 75, 150, 300));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_KNIFE, 75, 150, 300));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 300) == 0) {
                if (Utils.random(1) == 0) {
                    return new Item(Utils.random(1) == 0 ? ItemId.DRAGON_THROWNAXE : ItemId.DRAGON_KNIFE, Utils.random(75, 150));
                }
                return new Item(Utils.random(1) == 0 ? ItemId.DRAGON_HARPOON : ItemId.DRAGON_SWORD);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {8610};
    }
}
