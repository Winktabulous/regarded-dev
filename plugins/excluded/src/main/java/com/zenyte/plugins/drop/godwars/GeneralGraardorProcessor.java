package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 24/11/2018 21:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GeneralGraardorProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Armour
        appendDrop(new DisplayedDrop(11832, 1, 1, 100));
        appendDrop(new DisplayedDrop(11834, 1, 1, 100));
        appendDrop(new DisplayedDrop(11836, 1, 1, 100));
        //Bandos hilt
        appendDrop(new DisplayedDrop(11812, 1, 1, 135));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 50));
        appendDrop(new DisplayedDrop(11820, 1, 1, 50));
        appendDrop(new DisplayedDrop(11822, 1, 1, 50));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int itemId;
            if ((itemId = rollPool(killer, 50, 11818, 11820, 11822)) != -1) {
                return new Item(itemId);
            }
            if ((itemId = rollPool(killer, 100, 11832, 11834, 11836)) != -1) {
                return new Item(itemId);
            }
            if (randomDrop(killer, 135) == 0) {
                return new Item(11812);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2215, 6494 };
    }
}
