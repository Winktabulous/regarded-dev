
package com.zenyte.plugins.drop.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 20/04/2019 19:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LootingBagProcessor extends DropProcessor {
    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (random(30) == 0) {
            if (WildernessArea.isWithinWilderness(npc.getX(), npc.getY())) {
                if (LootingBag.hasBag(killer)) {
                    return;
                }
                npc.dropItem(killer, new Item(11941));
            }
        }
    }

    @Override
    public void attach() {
        for (final int id : getAllIds()) {
            appendDrop(new DisplayedDrop(11941, 1, 1, 30, (player, integer) -> integer == id, id));
            put(id, 11941, new PredicatedDrop("Only dropped by those found in the Wilderness."));
        }
    }

    @Override
    public int[] ids() {
        final IntOpenHashSet set = new IntOpenHashSet();
        for (final NPCSpawn spawn : NPCSpawnLoader.DEFINITIONS) {
            if (WildernessArea.isWithinWilderness(spawn.getX(), spawn.getY())) {
                set.add(spawn.getId());
            }
        }
        return set.toIntArray();
    }
}
