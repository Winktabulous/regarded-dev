package com.zenyte.game.content.larranskey;

import com.zenyte.game.content.skills.slayer.BossTask;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.content.skills.slayer.Task;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import kotlin.Pair;
import mgi.Indice;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;

@SuppressWarnings("unused")
public class LarransKeyDropProcessor extends DropProcessor {

    public static final Map<MemberRank, Float> RANK_DROP_BOOST = new HashMap<>();

    static {
        RANK_DROP_BOOST.put(MemberRank.PREMIUM, 10F);
        RANK_DROP_BOOST.put(MemberRank.EXPANSION, 15F);
        RANK_DROP_BOOST.put(MemberRank.EXTREME, 20F);
        RANK_DROP_BOOST.put(MemberRank.RESPECTED, 25F);
        RANK_DROP_BOOST.put(MemberRank.LEGENDARY, 30F);
        RANK_DROP_BOOST.put(MemberRank.MYTHICAL, 40F);
    }

    @Override
    public void attach() {
        for (final int i : getAllIds()) {
            final NPCDefinitions definitions = NPCDefinitions.get(i);
            if (definitions == null) {
                continue;
            }
            final NPCCombatDefinitions combatDefinitions = NPCCDLoader.get(i);
            if (combatDefinitions == null) {
                continue;
            }
            final float percentage = (float) dropPercentage(definitions.getCombatLevel(), 0.0F);
            final float fraction = (int) (100.0F / percentage);

            appendDrop(new DisplayedDrop(LarransKey.LARRANS_KEY.getId(), 1, 1, fraction, (player, npcId) -> npcId == i, i) {
                @Override
                public double getRate(Player player, int id) {
                    Float getRank = RANK_DROP_BOOST.get(player.getMemberRank());
                    if (getRank == null) {
                        return super.getRate(player, id) / 2;
                    }
                    else {
                        return (getRate() - ((getRate() / 100) * getRank)) / 2;
                    }
                }
            });

            put(i, LarransKey.LARRANS_KEY.getId(), new PredicatedDrop("It is dropped by monsters in the Wilderness while on a Slayer task given by Krystalia."));
        }
    }
    private static double dropPercentage(int combatLevel, float percentageBoost) {
        double probability = 0.0f;
        if (combatLevel <= 80) {
            probability += (1 / ( Math.floor( (3F / 10F) * Math.pow(80 - (float) combatLevel, 2)) + 100));
        } else {
            combatLevel = Math.min(combatLevel, 350);
            probability += (1 / ( Math.floor( -(5F / 27F) * (float) combatLevel) + 115));
        }

        // Rounding here
        double roundedPercentage = Math.round(probability * 100_00.0D) / 100_00.0D;
        probability = roundedPercentage * 100;

        if (percentageBoost > 0) {
            var percentOfProbability = (probability / 100) * percentageBoost;
            probability += percentOfProbability;
        }

        return probability;
    }

    public static boolean shouldDrop(int combatLevel, float percentageBoost) {
        double probability = dropPercentage(combatLevel, percentageBoost);
        return Utils.roll(probability);
    }

    @Override
    public void onDeath(NPC npc, Player killer) {
        if(WildernessArea.isWithinWilderness(killer)
                && killer.getSlayer().isCurrentAssignment(npc)
                && killer.getSlayer().getMaster() == SlayerMaster.KRYSTILIA) {
            var boost = 115F;

            if (RANK_DROP_BOOST.get(killer.getMemberRank()) != null) {
                boost += RANK_DROP_BOOST.get(killer.getMemberRank());
            }

            if (killer.getVariables().getLarransKeyBoosterTick() > 0) {
                boost += 25;
            }

            var shouldDrop = shouldDrop(npc.getCombatLevel(), boost);
            if (shouldDrop) {
                npc.dropItem(killer, LarransKey.LARRANS_KEY);
            }
        }
    }

    @Override
    public int[] ids() {
        final IntOpenHashSet set = new IntOpenHashSet();
        final ObjectLinkedOpenHashSet<Pair<String, Boolean>> names = new ObjectLinkedOpenHashSet<>();
        loop:
        for (final RegularTask regularTask : RegularTask.VALUES) {
            for (final Task entry : regularTask.getTaskSet()) {
                if (entry.getSlayerMaster() == SlayerMaster.KRYSTILIA) {
                    for (final String name : regularTask.getMonsters()) {
                        names.add(new Pair<>(name.toLowerCase(), true));
                    }
                    continue loop;
                }
            }
        }
        for (final BossTask bossTaskTask : BossTask.VALUES) {
            if (!bossTaskTask.isAssignableByKrystilia()) {
                continue;
            }
            names.add(new Pair<>(bossTaskTask.getTaskName().toLowerCase(), true));
        }
        loop:
        for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
            final NPCDefinitions definitions = NPCDefinitions.get(i);
            if (definitions == null || definitions.getCombatLevel() == 0) {
                continue;
            }
            final String name = definitions.getName().toLowerCase();
            for (final Pair<String, Boolean> validName : names) {
                if (validName.getSecond() ? name.equals(validName.getFirst()) : name.contains(validName.getFirst())) {
                    set.add(i);
                    continue loop;
                }
            }
        }
        final IntOpenHashSet wildyNPCs = new IntOpenHashSet();
        for (final NPCSpawn spawn : NPCSpawnLoader.DEFINITIONS) {
            if (WildernessArea.isWithinWilderness(spawn.getX(), spawn.getY())) {
                wildyNPCs.add(spawn.getId());
            }
        }
        wildyNPCs.add(6616);
        wildyNPCs.add(6617);
        wildyNPCs.add(6612);
        set.removeIf((IntPredicate) id -> !wildyNPCs.contains(id));
        set.add(6612);
        set.add(6611);
        return set.toIntArray();
    }
}
