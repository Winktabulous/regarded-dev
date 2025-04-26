package com.zenyte.plugins.item.mysteryboxes;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.plugins.PluginPriority;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author William Fuhrman | 11/28/2022 1:23 AM
 * @since 05/07/2022
 */
@PluginPriority(9990)
public class ThirdAgeMysteryBox extends ItemPlugin {

    public static int totalWeight;
    public static MysteryItem[] rewards;

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

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, null));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, null));

        rewards = new MysteryItem[] {
                // Main Third Age
                new MysteryItem(10330, 200).announce(), // 3rd age range top
                new MysteryItem(10332, 200).announce(), // 3rd age range legs
                new MysteryItem(10334, 200).announce(), // 3rd age range coif
                new MysteryItem(10336, 200).announce(), // 3rd age range vambs
                new MysteryItem(10338, 200).announce(), // 3rd age robe top
                new MysteryItem(10340, 200).announce(), // 3rd age robe
                new MysteryItem(10342, 200).announce(), // 3rd age mage hat
                new MysteryItem(10344, 200).announce(), // 3rd age amulet
                new MysteryItem(10346, 200).announce(), // 3rd age plate legs
                new MysteryItem(10348, 200).announce(), // 3rd age platebody
                new MysteryItem(10350, 200).announce(), // 3rd age helmet
                new MysteryItem(10352, 200).announce(), // 3rd age kiteshield
                new MysteryItem(23185, 200).announce(), // 3rd age ring

                // Rare Third Age
                new MysteryItem(12426, 100).announce(), // 3rd age longsword
                new MysteryItem(12422, 100).announce(), // 3rd age wand
                new MysteryItem(12424, 100).announce(), // 3rd age bow

                // Third Age Tools
                new MysteryItem(20011, 75).announce(), // 3rd age axe
                new MysteryItem(12437, 75).announce(), // 3rd age cloak
                new MysteryItem(20014, 75).announce(), // 3rd age pickaxe

                // Druidic
                new MysteryItem(23336, 50).announce(), // druidic top
                new MysteryItem(23339, 50).announce(), // druidic bottoms
                new MysteryItem(23345, 50).announce(), // druidic cloak
                new MysteryItem(23342, 50).announce() // druidic staff

        };

        totalWeight = MysteryItem.calculateTotalWeight(rewards);
    }

    @Override
    public int[] getItems() {
        return new int[] {32209};
    }
}
