package com.zenyte.plugins.drop.thermonuclearsmokedevil;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class ThermonuclearSmokeDevilProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Occult necklace
        appendDrop(new DisplayedDrop(12002, 1, 1, 150));
        //Smoke battlestaff
        appendDrop(new DisplayedDrop(11998, 1, 1, 250));
        //Dragon chainbody
        appendDrop(new DisplayedDrop(3140, 1, 1, 200));

        appendDrop(new DisplayedDrop(ItemId.JAR_OF_SMOKE, 1, 1, 600));

        appendDrop(new DisplayedDrop(32230, 1, 1, 500));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (random(500) == 0) {
            npc.dropItem(killer, new Item(32230));
        }
        if(randomDrop(killer, 600) == 0) {
            npc.dropItem(killer, new Item(ItemId.JAR_OF_SMOKE));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 200) == 0) {
                return new Item(3140);
            }
            if (randomDrop(killer,250) == 0) {
                return new Item(11998);
            }
            if (randomDrop(killer,150) == 0) {
                return new Item(12002);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 499 };
    }
}
