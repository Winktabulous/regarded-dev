package com.zenyte.plugins.item.mysteryboxes;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;

/**
 * @author William Fuhrman | 11/28/2022 2:18 AM
 * @since 05/07/2022
 */

public class EnchancedUltimateMysteryBox extends ItemPlugin {

    public static int totalWeight;
    public static MysteryItem[] rewards;
    public static MysterySupplyItem[] supplies;

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, supplies));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, supplies));

        rewards = new MysteryItem[]{
                // Common = 2000
                new MysteryItem(20724, 1, 1, 2000), // Imbued Heart
                new MysteryItem(ItemId.DRAGON_CROSSBOW, 1, 1, 2000), // Dragon Crossbow
                new MysteryItem(11806, 1, 1, 2000).announce(), // Saradomin godsword
                new MysteryItem(11804, 1, 1, 2000).announce(), // Bandos godsword
                new MysteryItem(11808, 1, 1, 2000).announce(), // Zamorak godsword
                new MysteryItem(12006, 1, 1, 2000).announce().charges(10_000), // Tentacle Whip
                new MysteryItem(12002, 1, 1, 2000).announce(), // Occult
                new MysteryItem(13265, 1, 1, 2000).announce(), // Abyssal Dagger
                new MysteryItem(11791, 1, 1, 2000).announce(), // Staff of the dead

                //Rare+ = 1500
                new MysteryItem(11802, 1, 1, 1500).announce(), // Armadyl godsword
                new MysteryItem(11785, 1, 1, 1500).announce(), // ACB
                new MysteryItem(19481, 1, 1, 1500).announce(), // Heavy Ballista
                new MysteryItem(13239, 1, 1, 1500).announce(), // Primordial Boots
                new MysteryItem(13237, 1, 1, 1500).announce(), // Pegasian Boots
                new MysteryItem(13235, 1, 1, 1500).announce(), // Eternal Boots
                new MysteryItem(19547, 1, 1, 1500).announce(), // Anguish
                new MysteryItem(19550, 1, 1, 1500).announce(), // Suffering
                new MysteryItem(19544, 1, 1, 1500).announce(), // Tormented
                new MysteryItem(19553, 1, 1, 1500).announce(), // Torture
                new MysteryItem(12902, 1, 1, 1500).announce(), // Toxic staff
                new MysteryItem(12932, 1, 1, 1500).announce(), // Magic fang
                new MysteryItem(12922, 1, 1, 1500).announce(), // Tanz fang
                new MysteryItem(11832, 1, 1, 1500).announce(), // bcp
                new MysteryItem(11834, 1, 1, 1500).announce(), // tassets
                new MysteryItem(CustomItemId.REGAL_MYSTERY_BOX, 1, 1, 1500).announce(), // Royal Mystery Box

                // Super Rare = 1000
                new MysteryItem(13576, 1, 1, 1000).announce(), // DWH
                new MysteryItem(13036, 1, 1, 1000).announce(), // Gilded (lg)
                new MysteryItem(13038, 1, 1, 1000).announce(), // Gilded (sk)
                new MysteryItem(962, 1, 1, 1000).announce(), // Christmas Cracker
                new MysteryItem(23258, 1, 1, 1000).announce(), // Gilded coif
                new MysteryItem(23261, 1, 1, 1000).announce(), // Gilded vambs
                new MysteryItem(23264, 1, 1, 1000).announce(), // Gilded body
                new MysteryItem(23267, 1, 1, 1000).announce(), // Gilded chaps
                new MysteryItem(23276, 1, 1, 1000).announce(), // Gilded pickaxe
                new MysteryItem(23279, 1, 1, 1000).announce(), // Gilded axe
                new MysteryItem(23282, 1, 1, 1000).announce(), // Gilded spade

                // Rare+- = 500
                new MysteryItem(32066, 1, 1, 500).announce(), // Pink partyhat
                new MysteryItem(32068, 1, 1, 500).announce(), // Orange partyhat
                new MysteryItem(5607, 1, 1, 500).announce(), // Grain
                new MysteryItem(32146, 1, 1, 500).announce(), // Bandos Ornament Kit

                // Extremely Rare = 250
                new MysteryItem(22840, 1, 1, 250).announce(), // Golden Tench
                new MysteryItem(10330, 1, 1, 250).announce(), // 3a r top
                new MysteryItem(10332, 1, 1, 250).announce(), // 3a r bottoms
                new MysteryItem(10334, 1, 1, 250).announce(), // 3a r coif
                new MysteryItem(10336, 1, 1, 250).announce(), // 3a r vambs
                new MysteryItem(10338, 1, 1, 250).announce(), // 3a mage top
                new MysteryItem(10340, 1, 1, 250).announce(), // 3a mage robe
                new MysteryItem(10342, 1, 1, 250).announce(), // 3a mage hat
                new MysteryItem(10344, 1, 1, 250).announce(), // 3a ammy
                new MysteryItem(10346, 1, 1, 250).announce(), // 3a m platelegs
                new MysteryItem(23242, 1, 1, 250).announce(), // 3a m plateskirt
                new MysteryItem(10348, 1, 1, 250).announce(), // 3a m platebody
                new MysteryItem(10350, 1, 1, 250).announce(), // 3a m full helm
                new MysteryItem(10352, 1, 1, 250).announce(), // 3a m kiteshield
                new MysteryItem(23185, 1, 1, 250).announce(), // ring of 3a
                new MysteryItem(32060, 1, 1, 250).announce(), // lime whip
                new MysteryItem(11847, 1, 1, 250).announce(), // Black h'ween

                // Rarity = 150
                new MysteryItem(11863, 1, 1, 150).announce(), // rainbow partyhat
                new MysteryItem(12422, 1, 1, 150).announce(), // 3a wand
                new MysteryItem(12424, 1, 1, 150).announce(), // 3a bow
                new MysteryItem(12426, 1, 1, 150).announce(), // 3a sword
                new MysteryItem(20011, 1, 1, 150).announce(), // 3a axe
                new MysteryItem(20014, 1, 1, 150).announce(), // 3a pickaxe
                new MysteryItem(23336, 1, 1, 150).announce(), // 3a druidic top
                new MysteryItem(23339, 1, 1, 150).announce(), // 3a druidic bottoms
                new MysteryItem(23342, 1, 1, 150).announce(), // 3a druidic staff
                new MysteryItem(23345, 1, 1, 150).announce() // 3a druidic cloak
        };

        totalWeight = MysteryItem.calculateTotalWeight(rewards);

        supplies = new MysterySupplyItem[]{
                new MysterySupplyItem(11232, 550, 1100),
                new MysterySupplyItem(22804, 400, 800),
                new MysterySupplyItem(20849, 400, 800),
                new MysterySupplyItem(13440, 300, 600),
                new MysterySupplyItem(3143, 300, 600),
                new MysterySupplyItem(21976, 200, 300),
                new MysterySupplyItem(2, 2500, 4000),
                new MysterySupplyItem(11959, 300, 600),
                new MysterySupplyItem(21946, 300, 600),
                new MysterySupplyItem(21944, 300, 600),
                new MysterySupplyItem(21948, 300, 600),
                new MysterySupplyItem(12696, 90, 160),
                new MysterySupplyItem(10926, 90, 160),
                new MysterySupplyItem(22462, 60, 120),
                new MysterySupplyItem(ItemId.ZULRAHS_SCALES, 3000, 12500),
                new MysterySupplyItem(23962, 80, 150),
                new MysterySupplyItem(ItemId.ANCIENT_SHARD, 3, 10)
        };
    }

    @Override
    public int[] getItems() {
        return new int[]{32206};
    }
}
