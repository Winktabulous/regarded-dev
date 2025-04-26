
package com.zenyte.plugins.drop.kalphitequeen;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 22/11/2018 15:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KalphiteQueenProcessor extends DropProcessor {
    @Override
    public void attach() {
        //Dragon chainbody
        appendDrop(new DisplayedDrop(3140, 1, 1, 128));
        //Dragon 2h sword
        appendDrop(new DisplayedDrop(7158, 1, 1, 128));
        //Kq head
        appendDrop(new DisplayedDrop(7981, 1, 1, 64));
        //Kq head (tattered)
        appendDrop(new DisplayedDrop(22671, 1, 1, 128));
        //Lava Whip
        appendDrop(new DisplayedDrop(32064, 1, 1, 850));
        //Dragon Pickaxe
        appendDrop(new DisplayedDrop(ItemId.DRAGON_PICKAXE, 1, 1, 250));
        //Jar of Sand
        appendDrop(new DisplayedDrop(ItemId.JAR_OF_SAND, 1, 1, 600));
        put(22671, new PredicatedDrop("Players will receive the tattered head only on their 128th kill."));
    }

    public void onDeath(final NPC npc, final Player killer) {
        if (Utils.random(600) == 0) {
            npc.dropItem(killer, new Item(ItemId.JAR_OF_SAND));
        }
        if (Utils.random(250) == 0) {
            npc.dropItem(killer, new Item(ItemId.DRAGON_PICKAXE));
        }
        if (killer.getKillcount(npc) == 128) {
            npc.dropItem(killer, new Item(22671));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {

            if (randomDrop(killer, 850) == 0)
                return new Item(CustomItemId.LAVA_WHIP);

            final int random = Utils.random(128);
            if (random < 2) {
                //1/128
                return new Item(ItemId.DRAGON_CHAINBODY_3140);
            } else if (random < 4) {
                //1/64
                return new Item(ItemId.KQ_HEAD);
            } else if (random < 5) {
                //1/256
                return new Item(ItemId.DRAGON_2H_SWORD);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {128, 963, 965, 4303, 4304, 6500, 6501};
    }
}
