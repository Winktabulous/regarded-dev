package com.zenyte.plugins.item.mysteryboxes;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author Jacmob
 */
@SuppressWarnings("unused")
public class PvmArenaMysteryBox extends ItemPlugin {

    public static int totalWeight;
    public static MysteryItem[] rewards;
    public static MysterySupplyItem[] supplies;

    public static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();
    public static ObjectArrayList<DropViewerEntry> toEntries() {
        if(entries.size() == 0) {
            calculateEntries();
        }
        return entries;
    }

    private static void calculateEntries() {
        for (final MysteryItem reward : rewards) {
            OtherDropViewerEntry entry = new OtherDropViewerEntry(reward.getId(), reward.getMinAmount(), reward.getMaxAmount(), reward.getWeight(), totalWeight, "");
            entries.add(entry);
        }
    }

    public static final int COMMON = 1000;
    public static final int UNCOMMON = 500;
    public static final int RARE = 250;
    public static final int MEGA_RARE = 30;

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, supplies));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, supplies));

        rewards = new MysteryItem[] {
            // Uncommon
            new MysteryItem(ItemId.SEERS_RING, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.BERSERKER_RING, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.ARCHERS_RING, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.TYRANNICAL_RING, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.TREASONOUS_RING, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.DRAGON_PICKAXE, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.BLESSED_SPIRIT_SHIELD, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.ABYSSAL_TENTACLE, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.ABYSSAL_WHIP, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.DRAGON_CROSSBOW, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.DRAGON_BOOTS, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.AMULET_OF_FURY, 1, 1, UNCOMMON),
            new MysteryItem(ItemId.SLAYER_TASK_RESET_SCROLL, 1, 5, UNCOMMON),
            new MysteryItem(ItemId.SLAYER_TASK_PICKER_SCROLL, 1, 5, UNCOMMON),
            // Rare
            new MysteryItem(ItemId.TRIDENT_OF_THE_SEAS, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.HOLY_ELIXIR, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.OCCULT_NECKLACE, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.DRAGONFIRE_SHIELD, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.LAVA_WHIP, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.ZAMORAKIAN_SPEAR, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.STAFF_OF_THE_DEAD, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.SPECTRAL_SIGIL, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.ZENYTE_SHARD, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.ARMADYL_HELMET, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.ARMADYL_CHESTPLATE, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.ARMADYL_CHAINSKIRT, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.ARMADYL_CROSSBOW, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.BANDOS_CHESTPLATE, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.BANDOS_TASSETS, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.BANDOS_GODSWORD, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.SARADOMIN_GODSWORD, 1, 1, RARE).announce(),
            new MysteryItem(ItemId.IMBUED_HEART, 1, 1, RARE).announce(),
            // Mega Rare
            new MysteryItem(ItemId.DRAGONFIRE_WARD, 1, 1, MEGA_RARE).announce(),
            new MysteryItem(ItemId.DRAGON_WARHAMMER, 1, 1, MEGA_RARE).announce(),
            new MysteryItem(ItemId.ARCANE_SIGIL, 1, 1, MEGA_RARE).announce(),
            new MysteryItem(ItemId.ELYSIAN_SIGIL, 1, 1, MEGA_RARE).announce(),
            new MysteryItem(ItemId.LIME_WHIP, 1, 1, MEGA_RARE).announce(),
            new MysteryItem(ItemId.DEATH_CAPE, 1, 1, MEGA_RARE).announce(),
            new MysteryItem(ItemId.ENHANCED_CRYSTAL_WEAPON_SEED, 1, 1, MEGA_RARE).announce(),

        };

        totalWeight = MysteryItem.calculateTotalWeight(rewards);

        supplies = new MysterySupplyItem[] {
                new MysterySupplyItem(ItemId.COINS_995, 300_000, 500_000),
                new MysterySupplyItem(ItemId.CRYSTAL_KEY + 1, 1, 2),
                new MysterySupplyItem(ItemId.ANGLERFISH + 1, 50, 100),
                new MysterySupplyItem(ItemId.SUPER_COMBAT_POTION4 + 1, 20, 50),
                new MysterySupplyItem(ItemId.SANFEW_SERUM4 + 1, 20, 50),
                new MysterySupplyItem(ItemId.SARADOMIN_BREW4 + 1, 20, 50),
                new MysterySupplyItem(ItemId.SUPER_RESTORE4 + 1, 20, 50),
                new MysterySupplyItem(ItemId.RUNITE_BAR + 1, 50, 80),
                new MysterySupplyItem(ItemId.DRAGON_BONES + 1, 50, 100),
                new MysterySupplyItem(ItemId.MAGIC_LOGS + 1, 30, 80),
                new MysterySupplyItem(ItemId.DRAGON_BOLTS_UNF, 50, 100),
                new MysterySupplyItem(ItemId.DRAGON_ARROWTIPS, 50, 200),
                new MysterySupplyItem(ItemId.DRAGON_DART_TIP, 100, 200),
        };
    }

    @Override
    public int[] getItems() {
        return new int[] {CustomItemId.PVM_MYSTERY_BOX};
    }
}
