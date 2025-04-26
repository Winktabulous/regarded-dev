package com.zenyte.game.content.boss.skotizo.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.drop.catacombsofkourend.CatacombsProcessor;

/**
 * @author Tommeh | 07/03/2020 | 19:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class SkotizoProcessor extends DropProcessor {
    @Override
    public void attach() {
        //Uncut onyx
        appendDrop(new DisplayedDrop(ItemId.UNCUT_ONYX, 1, 1, 50));
        //Jar of souls
        appendDrop(new DisplayedDrop(ItemId.JAR_OF_DARKNESS, 1, 1, 600));
        //Dark claw
        appendDrop(new DisplayedDrop(ItemId.DARK_CLAW, 1, 1, 25));
        appendDrop(new DisplayedDrop(ItemId.DARK_FLIPPERS, 1, 1, 500));
        //Dark totem base
        appendDrop(new DisplayedDrop(19679, 1, 1, 50));
        put(19679, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don\'t already have the respective totem piece. Only found in Catacombs of Kourend."));
        //Dark totem middle
        appendDrop(new DisplayedDrop(19681, 1, 1, 50));
        put(19681, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don\'t already have the respective totem piece. Only found in Catacombs of Kourend."));
        //Dark totem top
        appendDrop(new DisplayedDrop(19683, 1, 1, 50));
        put(19683, new PredicatedDrop("This item is dropped in a specific order, and is only dropped if you don\'t already have the respective totem piece. Only found in Catacombs of Kourend."));
        appendDrop(new DisplayedDrop(ItemId.ANCIENT_SHARD, 1, 4, 25));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        final int shardRandom = random(100);
        final int count = shardRandom < 5 ? 4 : shardRandom < 12 ? 3 : shardRandom < 21 ? 2 : shardRandom < 34 ? 1 : 0;
        if (count > 0) {
            npc.dropItem(killer, new Item(ItemId.ANCIENT_SHARD, count));
        }
        if (randomDrop(killer, 600) == 0) {
            npc.dropItem(killer, new Item(ItemId.JAR_OF_DARKNESS));
            return;
        }
        if(randomDrop(killer,500) == 0) {
            npc.dropItem(killer, new Item(ItemId.DARK_FLIPPERS));
            return;
        }
        if (randomDrop(killer,55) == 0) {
            npc.dropItem(killer, new Item(ItemId.DARK_TOTEM));
            return;
        }
        if (randomDrop(killer,50) == 0) {
            CatacombsProcessor.dropTotemPiece(killer, npc);
            return;
        }
        if (randomDrop(killer,25) == 0) {
            npc.dropItem(killer, new Item(ItemId.DARK_CLAW));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(50) == 0) {
                return new Item(ItemId.UNCUT_ONYX);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {NpcId.SKOTIZO};
    }
}
