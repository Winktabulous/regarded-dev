
package com.zenyte.plugins.drop.wilderness;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.game.world.region.area.wilderness.WildernessGodwarsDungeon;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 28/04/2019 17:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EcumenicalKeyProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(11942, 1, 1, 0) {
            @Override
            public double getRate(final Player player, final int id) {
                return rate(player);
            }
        });
        for (final int id : getAllIds()) {
            put(id, 11942, new PredicatedDrop("Only dropped by those in Wilderness Godwars Dungeon. Only up to 3(4 & 5 after medium & hard Wilderness Diaries respectively) keys at a time."));
        }
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (WildernessArea.isWithinWilderness(npc.getX(), npc.getY())) {
            if (random(rate(killer)) == 0) {
                npc.dropItem(killer, new Item(11942));
            }
        }
    }

    private int rate(final Player player) {
        final int maxCount = DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player) ? 5 : DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD2, player) ? 4 : 3;
        final int heldCount = player.getAmountOf(11942);
        if (maxCount == 5) {
            if (heldCount >= 4) {
                return 80;
            } else if (heldCount >= 3) {
                return 70;
            }
            return 60;
        } else if (maxCount == 4) {
            if (heldCount >= 3) {
                return 80;
            } else if (heldCount >= 2) {
                return 70;
            }
            return 60;
        }
        return heldCount == 0 ? 60 : heldCount == 1 ? 70 : 80;
    }

    @Override
    public int[] ids() {
        final IntOpenHashSet set = new IntOpenHashSet();
        final RSPolygon polygon = WildernessGodwarsDungeon.polygon;
        for (final NPCSpawn spawn : NPCSpawnLoader.DEFINITIONS) {
            if (polygon.contains(spawn.getX(), spawn.getY())) {
                set.add(spawn.getId());
            }
        }
        return set.toIntArray();
    }
}
