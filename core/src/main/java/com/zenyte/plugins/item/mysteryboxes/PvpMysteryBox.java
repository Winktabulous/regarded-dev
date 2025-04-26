package com.zenyte.plugins.item.mysteryboxes;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;

/**
 * @author William Fuhrman | 11/28/2022 2:24 AM
 * @since 05/07/2022
 */

public class PvpMysteryBox extends ItemPlugin {

    public static int totalWeight;
    public static MysteryItem[] rewards;
    public static MysterySupplyItem[] supplies;

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, supplies));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, supplies));

        rewards = new MysteryItem[] {
                // Common = 2000
                new MysteryItem(ItemId.DRAGON_CROSSBOW, 1, 1, 2000), // Dragon Crossbow
                new MysteryItem(12881, 3, 5, 2000), // Ahrims
                new MysteryItem(12877, 3, 5, 2000), // Dharoks
                new MysteryItem(12883, 3, 5, 2000), // Karils
                new MysteryItem(12006, 1, 1, 2000).charges(10_000), // Tentacle Whip
                new MysteryItem(12002, 1, 1, 2000), // Occult
                new MysteryItem(13265, 1, 1, 2000), // Abyssal Dagger
                new MysteryItem(11791, 1, 1, 2000), // Staff of the dead
                new MysteryItem(11770, 1, 1, 2000), // Seers (i)
                new MysteryItem(11771, 1, 1, 2000), // Archers (i)
                new MysteryItem(11773, 1, 1, 2000), // Berserker (i)

                //Rare+ = 500
                new MysteryItem(11802, 1, 1, 1000), // Armadyl godsword
                new MysteryItem(11785, 1, 1, 1000).announce(), // ACB
                new MysteryItem(19481, 1, 1, 1000), // Heavy Ballista
                new MysteryItem(13239, 1, 1, 1000).announce(), // Primordial Boots
                new MysteryItem(13237, 1, 1, 1000).announce(), // Pegasian Boots
                new MysteryItem(13235, 1, 1, 1000).announce(), // Eternal Boots
                new MysteryItem(19547, 1, 1, 1000), // Anguish
                new MysteryItem(19550, 1, 1, 1000), // Suffering
                new MysteryItem(19544, 1, 1, 1000), // Tormented
                new MysteryItem(19553, 1, 1, 1000), // Torture
                new MysteryItem(11832, 1, 1, 1000), // bcp
                new MysteryItem(11834, 1, 1, 1000), // tassets
                new MysteryItem(11924, 1, 1, 1000).announce(), // Malediction Ward
                new MysteryItem(11926, 1, 1, 1000).announce(), // Odium Ward
                new MysteryItem(21034, 1, 1, 1000).announce(), // Dex Prayer
                new MysteryItem(21079, 1, 1, 1000).announce(), // Arc Prayer

                // Rare+- = 250
                new MysteryItem(22610, 1, 1, 250).announce(), // Vesta Spear
                new MysteryItem(22613, 1, 1, 250).announce(), // Vesta Longsword
                new MysteryItem(22622, 1, 1, 250).announce(), // Statius Warhammer
                new MysteryItem(22647, 1, 1, 250).announce(), // Zuriel Staff
                new MysteryItem(22616, 1, 1, 250).announce(), // Vesta Chainbody
                new MysteryItem(22619, 1, 1, 250).announce(), // Vesta Plateskirt
                new MysteryItem(32169, 1, 1, 250).announce(), // Vesta Helm
                new MysteryItem(22650, 1, 1, 250).announce(), // Zuriel Hood
                new MysteryItem(22653, 1, 1, 250).announce(), // Zuriel Top
                new MysteryItem(22656, 1, 1, 250).announce(), // Zuriel Bottoms
                new MysteryItem(22638, 1, 1, 250).announce(), // Morrigan Coif
                new MysteryItem(22641, 1, 1, 250).announce(), // Morrigan Body
                new MysteryItem(22644, 1, 1, 250).announce(), // Morrigan Chaps
                new MysteryItem(22625, 1, 1, 250).announce(), // Statius Helm
                new MysteryItem(22628, 1, 1, 250).announce(), // Statius Body
                new MysteryItem(22631, 1, 1, 250).announce(), // Statius Legs

                new MysteryItem(13652, 1, 1, 100).announce(), // Dragon Claws
                new MysteryItem(21003, 1, 1, 100).announce(), // Elder Maul

                // Rarity = 25
                new MysteryItem(21295, 1, 1, 100).announce(), // Infernal
                new MysteryItem(21006, 1, 1, 100).announce(), // Kodai Wand
                new MysteryItem(24419, 1, 1, 100).announce(), // Inq helm
                new MysteryItem(24420, 1, 1, 100).announce(), // Inq top
                new MysteryItem(24421, 1, 1, 100).announce(), // Inq bottoms
                new MysteryItem(25862, 1, 1, 100).announce(), // Bofa
                new MysteryItem(12825, 1, 1, 100).announce(), // Arcane
                new MysteryItem(26233, 1, 1, 100).announce(), // Ancient Godsword
                new MysteryItem(ItemId.KORASI, 1, 1, 100).announce(), // Korasi Sword

                // Uber Rare = 10
                new MysteryItem(ItemId.VOIDWAKER_27690, 1, 1, 25).announce() // Voidwaker

        };

        totalWeight = MysteryItem.calculateTotalWeight(rewards);

        supplies = new MysterySupplyItem[]{
                new MysterySupplyItem(13307, 50, 150), // blood money
                new MysterySupplyItem(12696, 100, 200), // super combats
                new MysterySupplyItem(3145, 300, 500), // karams
                new MysterySupplyItem(6686, 100, 200), // saradomin brews
                new MysterySupplyItem(10926, 100, 150), // sanfews
                new MysterySupplyItem(3025, 100, 200), // restores
                new MysterySupplyItem(2445, 125, 250), // ranging potion
                new MysterySupplyItem(13442, 150, 200), // anglerfish
                new MysterySupplyItem(22462, 100, 200), // bastion potion
                new MysterySupplyItem(22636, 35, 70), // morrigan javs
                new MysterySupplyItem(22634, 40, 80), // morrigan throwing axes
                new MysterySupplyItem(21932, 100, 200), // dragon opal bolts
                new MysterySupplyItem(24615, 75, 200), // blighted tp sack
                new MysterySupplyItem(24607, 75, 200), // blighted ice
                new MysterySupplyItem(26705, 75, 200), // blighted surge
                new MysterySupplyItem(24621, 75, 200), // blighted veng
                new MysterySupplyItem(32149, 3, 5), // Larran's Booster
                new MysterySupplyItem(32150, 3, 5), // Ganodermic Booster
                new MysterySupplyItem(32154, 3, 5), // Blood Money Booster
        };
    }

    @Override
    public int[] getItems() {
        return new int[]{CustomItemId.PVP_MYSTERY_BOX};
    }
}

