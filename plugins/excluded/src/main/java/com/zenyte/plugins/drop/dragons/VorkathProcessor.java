package com.zenyte.plugins.drop.dragons;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 18:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class VorkathProcessor extends DropProcessor {

    @Override
    public void onDeath(NPC npc, Player killer) {
        if (killer.getKillcount(npc) == 34) {
            npc.dropItem(killer, new Item(21907));
        }
    }

    @Override
    public void attach() {
        //Vorkath's head
        appendDrop(new DisplayedDrop(21907, 1, 1, 35));
        //Dragonbone necklace
        appendDrop(new DisplayedDrop(22111, 1, 1, 100));
        //Skeletal visage
        appendDrop(new DisplayedDrop(22006, 1, 1, 250));
        //Jar of decay
        appendDrop(new DisplayedDrop(22106, 1, 1, 600));

        put(21907, new PredicatedDrop("Vorkath's head is always dropped on the 35th Vorkath kill."));
        put(1751, new PredicatedDrop("Vorkath always rolls twice on the main drop table.<br>The below drop rates are for a single roll."));
    }

    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            //All drop rates are doubled in here because vorkath rolls on the table twice every kill.
            if (randomDrop(killer, 600 * 2) == 0) {
                return new Item(22106);
            }
            if (randomDrop(killer, 400 * 2) == 0) {
                return new Item(22111);
            }
            if (randomDrop(killer, 250 * 2) == 0) {
                return new Item(22006);
            }
            if (randomDrop(killer, 35 * 2) == 0) {
                return new Item(21907);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 8058, 8059, 8060, 8061 };
    }
}
