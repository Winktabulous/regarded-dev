package com.zenyte.game.content.treasuretrails.npcs.drops.processors;

import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.npcs.drops.PredicatedClueDrop;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.utils.IntArray;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.Indice;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Kris | 22/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class ClueProcessor extends DropProcessor {

    private final int[] faladorGuardIds = IntArray.of(3269, 3270, 3271, 3272);

    @Override
    public void attach() {
        for (final int i : getAllIds()) {
            final NPCDefinitions definitions = NPCDefinitions.get(i);
            if (definitions == null) {
                continue;
            }
            final List<PredicatedClueDrop> clueNPCs = map().get(definitions.getName().toLowerCase());
            final PredicatedClueDrop clueNPC = CollectionUtils.findMatching(clueNPCs, n -> {
                final Predicate<NPCDefinitions> predicate = n.getPredicate();
                return predicate == null || predicate.test(definitions);
            });
            if (clueNPC == null) {
                continue;
            }
            final int itemId = itemId();
            appendDrop(new DisplayedDrop(itemId, 1, 1, clueNPC.getRate(), (player, npcId) -> npcId == i, i));
            if (itemId != ClueItem.BEGINNER.getScrollBox()) {
                if (definitions.getName().equalsIgnoreCase("Guard") && definitions.getCombatLevel() >= 19 && definitions.getCombatLevel() <= 22) {
                    put(i, itemId, new PredicatedDrop("The drop rate is lowered to 1/115 after completion of the medium Falador diary."));
                } else {
                    final int halvedRate = (int) Math.floor(clueNPC.getRate() / 2);
                    put(i, itemId, new PredicatedDrop("The drop rate is lowered to 1/" + halvedRate + " when killed in Wilderness while wielding an imbued ring of wealth."));
                }
            }
        }
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        final NPCDefinitions definitions = npc.getDefinitions();
        final List<PredicatedClueDrop> list = map().get(definitions.getLowercaseName());
        if (list == null || list.isEmpty()) {
            return;
        }
        final PredicatedClueDrop constant = CollectionUtils.findMatching(list, n -> {
            final Predicate<NPCDefinitions> predicate = n.getPredicate();
            return predicate == null || predicate.test(definitions);
        });
        if (constant == null) {
            return;
        }
        final int itemId = itemId();
        final Item ring = killer.getEquipment().getItem(EquipmentSlot.RING);
        final boolean isImbuedRingOfWealth = ring != null && ring.getName().startsWith("Ring of wealth (i");
        final boolean boosted = itemId != ClueItem.BEGINNER.getScrollBox() && isImbuedRingOfWealth
                && WildernessArea.isWithinWilderness(npc.getX(), npc.getY());
        final double rate = ArrayUtils.contains(faladorGuardIds, npc.getId()) ? (constant.getRate() * 0.9) : boosted ? (constant.getRate() / 2) : constant.getRate();
        if (Utils.randomDouble() < 1F / rate) {
            npc.dropItem(killer, new Item(itemId));
        }
    }

    protected abstract int itemId();
    protected abstract Map<String, List<PredicatedClueDrop>> map();

    @Override
    public int[] ids() {
        final IntOpenHashSet set = new IntOpenHashSet();
        loop : for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
            final NPCDefinitions definitions = NPCDefinitions.get(i);
            if (definitions == null || definitions.getCombatLevel() == 0) {
                continue;
            }
            final String name = definitions.getName().toLowerCase();
            final List<PredicatedClueDrop> clueNPCs = map().get(name);
            if (clueNPCs == null) {
                continue;
            }
            for (final PredicatedClueDrop npc : clueNPCs) {
                final Predicate<NPCDefinitions> predicate = npc.getPredicate();
                if (predicate == null || predicate.test(definitions)) {
                    set.add(i);
                    continue loop;
                }
            }
        }
        return set.toIntArray();
    }
}
