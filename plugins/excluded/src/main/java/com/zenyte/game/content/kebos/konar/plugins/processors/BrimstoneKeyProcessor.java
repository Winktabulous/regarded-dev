package com.zenyte.game.content.kebos.konar.plugins.processors;

import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.content.skills.slayer.Slayer;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.content.skills.slayer.Task;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import mgi.types.config.npcs.NPCDefinitions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tommeh | 15/10/2019 | 10:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class BrimstoneKeyProcessor extends DropProcessor {

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.BRIMSTONE_KEY, 1, 1) {
            public double getRate(final Player player, int id) {
                if (id == 8058) {
                    //vorkath
                    id = 8060;
                }
                final NPCDefinitions definitions = NPCDefinitions.get(id);
                final int combatLevel = definitions != null ? definitions.getCombatLevel() : 3;
                double rate;
                if (combatLevel >= 100) {
                    rate = (-0.2 * Math.min(combatLevel, 350) + 120) / 2;
                } else {
                    rate = (Math.pow(0.2 * (combatLevel - 100), 2) + 100) / 2;
                }
                return rate;
            }
        });
        put(ItemId.BRIMSTONE_KEY, new PredicatedDrop("This drop can only be obtained when killing the monster during a slayer assignment, assigned by Konar quo Maten."));
    }

    @Override
    public void onDeath(NPC npc, Player killer) {
        super.onDeath(npc, killer);

        if (killer == null) return;
        final Slayer slayer = killer.getSlayer();
        if (!slayer.isCurrentAssignment(npc) || slayer.getMaster() != SlayerMaster.KONAR_QUO_MATEN) return;

        int rate = (int) getBasicDrops().get(0).getRate(killer, npc.getId());
        if (random(rate) == 0) {
            npc.dropItem(killer, new Item(ItemId.BRIMSTONE_KEY, World.hasBoost(XamphurBoost.BRIMSTONE_KEY_DROPS_X2) ? 2 : 1));
        }
    }

    @Override
    public int[] ids() {
        final IntSet list = IntSets.synchronize(new IntOpenHashSet(100));
        final List<NPCDefinitions> definitionsList = Arrays.asList(NPCDefinitions.getDefinitions());
        definitionsList.parallelStream().forEach(definition -> {
            if (definition == null) {
                return;
            }
            final String name = definition.getName().toLowerCase();
            for (final RegularTask task : RegularTask.VALUES) {
                for (final Task t : task.getTaskSet()) {
                    if (t.getSlayerMaster().equals(SlayerMaster.KONAR_QUO_MATEN)) {
                        for (final String monster : task.getMonsters()) {
                            if (monster.equals(name)) {
                                list.add(definition.getId());
                            }
                        }
                    }
                }
            }
        });
        return list.toIntArray();
    }

}