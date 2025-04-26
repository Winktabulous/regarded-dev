package com.zenyte.plugins.drop.corporealbeast;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CorporealBeastProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Spirit shield
        appendDrop(new DisplayedDrop(12829, 1, 1, 32));
        //Holy elixir
        appendDrop(new DisplayedDrop(12833, 1, 1, 100));
        //Spectral sigil
        appendDrop(new DisplayedDrop(12823, 1, 1, 200));
        //Arcane sigil
        appendDrop(new DisplayedDrop(12827, 1, 1, 400));
        appendDrop(new DisplayedDrop(ItemId.JAR_OF_SPIRITS, 1, 1, 600));
        //Elysian sigil
        appendDrop(new DisplayedDrop(12819, 1, 1, 800));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 200) == 0) {
                return new Item(12823);
            }
            if (randomDrop(killer, 400) == 0) {
                return new Item(12827);
            }
            if (randomDrop(killer, 600) == 0) {
                return new Item(ItemId.JAR_OF_SPIRITS);
            }
            if (randomDrop(killer, 800) == 0) {
                return new Item(12819);
            }
            if (randomDrop(killer,32) == 0) {
                return new Item(12829);
            }
            if (randomDrop(killer,100) == 0) {
                return new Item(12833);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 319 };
    }
}
