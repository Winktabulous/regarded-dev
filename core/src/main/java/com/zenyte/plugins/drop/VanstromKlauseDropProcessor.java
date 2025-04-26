package com.zenyte.plugins.drop;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VanstromKlauseDropProcessor extends DropProcessor {
    @Override
    public void attach() {
        NPCDrops.initDropTable(new NPCDrops.DropTable(NpcId.VANSTROM_KLAUSE_9569, 0, new Drop[]{}));
        for (Drops drop : Drops.VALUES) {
            appendDrop(new DisplayedDrop(drop.id, drop.getMin(), drop.getMax(), drop.getRate()));
        }
        appendDrop(new DisplayedDrop(ItemId.VIAL_OF_BLOOD_22446, 1, 1, 1));
        appendDrop(new DisplayedDrop(ItemId.VAMPYRE_DUST, 1, 1, 1));
        appendDrop(new DisplayedDrop(ItemId.BLOOD_SHARD, 1, 1, 300));
    }


    @Override
    public void onDeath(NPC npc, Player killer) {
        if(randomDrop(killer, 300) == 0) {
            npc.dropItem(killer, new Item(ItemId.BLOOD_SHARD));
        }
        for (Drops d : Drops.DROPS) {
            if(d.roll()) {
                npc.dropItem(killer, new Item(d.getId(), Utils.random(d.getMin(), d.getMax())), npc.getMiddleLocation().copy(), false);
                break;
            }
        }

        npc.dropItem(killer, new Item(ItemId.VIAL_OF_BLOOD_22446, 1), npc.getMiddleLocation().copy(), true);
        npc.dropItem(killer, new Item(ItemId.VAMPYRE_DUST, 1), npc.getMiddleLocation().copy(), true);
    }

    @Override
    public int[] ids() {
        return new int[] {NpcId.VANSTROM_KLAUSE_9569};
    }

    private enum Drops {
        RUNESCROLL_OF_BLOODBARK(ItemId.RUNESCROLL_OF_BLOODBARK, 1, 1, 75),
        RUNESCROLL_OF_SWAMPBARK(ItemId.RUNESCROLL_OF_SWAMPBARK, 1, 1, 75),
        COINS_995(ItemId.COINS_995,100_000, 400_000, 10),
        ONYX_BOLT_TIPS(ItemId.ONYX_BOLT_TIPS,10, 20, 20),
        RUNE_KITESHIELD(ItemId.RUNE_KITESHIELD + 1, 3, 5, 20),
        RUNE_FULL_HELM(ItemId.RUNE_FULL_HELM + 1, 3, 5, 20),
        DRAGON_LONGSWORD(ItemId.DRAGON_LONGSWORD + 1, 2, 3, 20),
        DRAGON_DAGGER(ItemId.DRAGON_DAGGER + 1, 3, 4, 20),
        RUNE_DAGGER(ItemId.RUNE_DAGGER + 1, 2, 3, 10),
        ADAMANT_PLATELEGS(ItemId.ADAMANT_PLATELEGS + 1, 3, 4, 10),
        ADAMANT_PLATEBODY(ItemId.ADAMANT_PLATEBODY + 1, 2, 3, 10),
        RUNITE_BAR(ItemId.RUNITE_BAR + 1, 2, 3, 10),
        BLOOD_RUNE(ItemId.BLOOD_RUNE, 100, 200, 5),
        ;
        private final int id;
        private final int min;
        private final int max;
        private final int rate;
        private final double chance;

        public static final Drops[] VALUES = values();
        Drops(int id, int min, int max, int rate) {
            this.id = id;
            this.min = min;
            this.max = max;
            this.rate = rate;
            this.chance = 1D / (double) rate;
        }

        private static List<Drops> DROPS;

        static {
            DROPS = Arrays.asList(values());
            DROPS.sort(Comparator.comparingDouble(d -> d.rate));
            Collections.reverse(DROPS);
        }

        public boolean roll() {
            return chance >= ThreadLocalRandom.current().nextDouble();
        }


        public int getId() {
            return id;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getRate() {
            return rate;
        }
    }
}
