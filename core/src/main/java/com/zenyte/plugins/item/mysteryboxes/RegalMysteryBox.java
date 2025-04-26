package com.zenyte.plugins.item.mysteryboxes;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author William Fuhrman | 2/11/2023 11:13 PM
 * @since 05/07/2022
 */
public class RegalMysteryBox extends ItemPlugin {

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

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, supplies));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, supplies));

        rewards = new MysteryItem[]{
                // Common = 2000
                new MysteryItem(12006, 1, 1, 2000).announce().charges(10_000), // Tentacle Whip
                new MysteryItem(12002, 1, 1, 2000).announce(), // Occult
                new MysteryItem(13265, 1, 1, 2000).announce(), // Abyssal Dagger
                new MysteryItem(11791, 1, 1, 2000).announce(), // Staff of the dead
                new MysteryItem(11802, 1, 1, 2000).announce(), // Armadyl godsword
                new MysteryItem(11785, 1, 1, 2000).announce(), // ACB
                new MysteryItem(19481, 1, 1, 2000).announce(), // Heavy Ballista
                new MysteryItem(13239, 1, 1, 2000).announce(), // Primordial Boots
                new MysteryItem(13237, 1, 1, 2000).announce(), // Pegasian Boots
                new MysteryItem(13235, 1, 1, 2000).announce(), // Eternal Boots
                new MysteryItem(19547, 1, 1, 2000).announce(), // Anguish
                new MysteryItem(19550, 1, 1, 2000).announce(), // Suffering
                new MysteryItem(19544, 1, 1, 2000).announce(), // Tormented
                new MysteryItem(19553, 1, 1, 2000).announce(), // Torture
                new MysteryItem(12902, 1, 1, 2000).announce(), // Toxic staff
                new MysteryItem(12932, 1, 1, 2000).announce(), // Magic fang
                new MysteryItem(12922, 1, 1, 2000).announce(), // Tanz fang
                new MysteryItem(11832, 1, 1, 2000).announce(), // bcp
                new MysteryItem(11834, 1, 1, 2000).announce(), // tassets

                // Super Rare = 1500
                new MysteryItem(13576, 1, 1, 1500).announce(), // DWH
                new MysteryItem(13036, 1, 1, 1500).announce(), // Gilded (lg)
                new MysteryItem(13038, 1, 1, 1500).announce(), // Gilded (sk)
                new MysteryItem(962, 1, 1, 1500).announce(), // Christmas Cracker

                // Rare+- = 1000
                new MysteryItem(32066, 1, 1, 1000).announce(), // Pink partyhat
                new MysteryItem(32068, 1, 1, 1000).announce(), // Orange partyhat
                new MysteryItem(32146, 1, 1, 1000).announce(), // Bandos Ornament Kit
                new MysteryItem(21034, 1, 1, 1000).announce(), // Dex Prayer
                new MysteryItem(21079, 1, 1, 1000).announce(), // Arc Prayer
                new MysteryItem(22610, 1, 1, 1000).announce(), // Vesta Spear
                new MysteryItem(22613, 1, 1, 1000).announce(), // Vesta Longsword
                new MysteryItem(22622, 1, 1, 1000).announce(), // Statius Warhammer
                new MysteryItem(22647, 1, 1, 1000).announce(), // Zuriel Staff
                new MysteryItem(22616, 1, 1, 1000).announce(), // Vesta Chainbody
                new MysteryItem(22619, 1, 1, 1000).announce(), // Vesta Plateskirt
                new MysteryItem(32169, 1, 1, 1000).announce(), // Vesta Helm
                new MysteryItem(22650, 1, 1, 1000).announce(), // Zuriel Hood
                new MysteryItem(22653, 1, 1, 1000).announce(), // Zuriel Top
                new MysteryItem(22656, 1, 1, 1000).announce(), // Zuriel Bottoms
                new MysteryItem(22638, 1, 1, 1000).announce(), // Morrigan Coif
                new MysteryItem(22641, 1, 1, 1000).announce(), // Morrigan Body
                new MysteryItem(22644, 1, 1, 1000).announce(), // Morrigan Chaps
                new MysteryItem(22625, 1, 1, 1000).announce(), // Statius Helm
                new MysteryItem(22628, 1, 1, 1000).announce(), // Statius Body
                new MysteryItem(22631, 1, 1, 1000).announce(), // Statius Legs

                // Extremely Rare = 500
                new MysteryItem(23185, 1, 1, 500).announce(), // ring of 3a
                new MysteryItem(32060, 1, 1, 500).announce(), // lime whip
                new MysteryItem(11847, 1, 1, 500).announce(), // Black h'ween
                new MysteryItem(13652, 1, 1, 500).announce(), // Dragon Claws
                new MysteryItem(21003, 1, 1, 500).announce(), // Elder Maul
                new MysteryItem(22322, 1, 1, 500).announce(), // Avernic

                // Rarity = 250
                new MysteryItem(21006, 1, 1, 250).announce(), // Kodai Wand
                new MysteryItem(24419, 1, 1, 250).announce(), // Inq helm
                new MysteryItem(24420, 1, 1, 250).announce(), // Inq top
                new MysteryItem(24421, 1, 1, 250).announce(), // Inq bottoms
                new MysteryItem(25862, 1, 1, 250).announce(), // Bofa
                new MysteryItem(12825, 1, 1, 250).announce(), // Arcane
                new MysteryItem(12821, 1, 1, 250).announce(), // specy shield
                new MysteryItem(26233, 1, 1, 250).announce(), // Ancient Godsword
                new MysteryItem(21018, 1, 1, 250).announce(), // ancestral hat
                new MysteryItem(21021, 1, 1, 250).announce(), // ancestral top
                new MysteryItem(21024, 1, 1, 250).announce(), // ancestral bottoms
                new MysteryItem(22324, 1, 1, 250).announce(), // g rapier
                new MysteryItem(22323, 1, 1, 250).announce(), // sang staff


                // 200
                new MysteryItem(11863, 1, 1, 200).announce(), // rainbow partyhat
                new MysteryItem(21295, 1, 1, 200).announce(), // Infernal
                new MysteryItem(12817, 1, 1, 200).announce() // elysian

        };

        totalWeight = MysteryItem.calculateTotalWeight(rewards);

        supplies = new MysterySupplyItem[]{
                new MysterySupplyItem(11232, 550, 1500),
                new MysterySupplyItem(22804, 400, 1000),
                new MysterySupplyItem(20849, 400, 800),
                new MysterySupplyItem(13440, 400, 800),
                new MysterySupplyItem(3143, 400, 800),
                new MysterySupplyItem(21976, 300, 600),
                new MysterySupplyItem(2, 4000, 7000),
                new MysterySupplyItem(11959, 400, 700),
                new MysterySupplyItem(21946, 400, 700),
                new MysterySupplyItem(21944, 400, 700),
                new MysterySupplyItem(21948, 400, 700),
                new MysterySupplyItem(12696, 130, 200),
                new MysterySupplyItem(10926, 130, 200),
                new MysterySupplyItem(22462, 80, 160),
                new MysterySupplyItem(ItemId.ZULRAHS_SCALES, 5000, 20000),
                new MysterySupplyItem(23962, 100, 200),
                new MysterySupplyItem(ItemId.ANCIENT_SHARD, 5, 15),
                new MysterySupplyItem(32149, 2, 5), // Larran's Booster
                new MysterySupplyItem(32150, 2, 5), // Ganodermic Booster
                new MysterySupplyItem(32151, 2, 5), // Slayer Booster
                new MysterySupplyItem(32152, 2, 5), // Pet Booster
                new MysterySupplyItem(32153, 2, 5), // Gauntlet Booster
                new MysterySupplyItem(32154, 2, 5), // Blood Money Booster
                new MysterySupplyItem(32155, 2, 5), // Clue Scroll Booster
                new MysterySupplyItem(32156, 2, 5), // ToB Booster
                new MysterySupplyItem(32157, 2, 5), // Slayer Task Picker
                new MysterySupplyItem(32158, 2, 5), // Slayer Task Reset
                new MysterySupplyItem(32166, 2, 5), // Rev Booster
                new MysterySupplyItem(32167, 2, 5), // Nex Booster
                new MysterySupplyItem(19782, 2, 5), // Xeric's Wisdom
        };
    }

    @Override
    public int[] getItems() {
        return new int[]{CustomItemId.REGAL_MYSTERY_BOX};
    }
}

