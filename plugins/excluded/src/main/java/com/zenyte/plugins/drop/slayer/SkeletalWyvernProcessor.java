package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SkeletalWyvernProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dragon plateskirt
        appendDrop(new DisplayedDrop(4585, 1, 1, 512));
        //Dragon platelegs
        appendDrop(new DisplayedDrop(4087, 1, 1, 512));
        //Granite legs
        appendDrop(new DisplayedDrop(6809, 1, 1, 512));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(512)) < 3) {
                return new Item(random == 0 ? 4585 : random == 1 ? 4087 : 6809);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 465, 466, 467, 468 };
    }
}
