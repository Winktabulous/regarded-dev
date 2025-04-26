package com.zenyte.plugins.item.mysteryboxes;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;

/**
 * @author Kris | 10/06/2019 06:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SuperMysteryBox extends ItemPlugin {

    public static int totalWeight;
    public static MysteryItem[] rewards;
    public static MysterySupplyItem[] supplies;

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, supplies));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, supplies));

        rewards = new MysteryItem[] {
                new MysteryItem(4151, 1, 1, 1000), // Abyssal Whip
                new MysteryItem(12873, 1, 1, 1000), // Guthans
                new MysteryItem(12875, 1, 1, 1000), // Veracs
                new MysteryItem(12877, 1, 1, 1000), // Dharoks
                new MysteryItem(12879, 1, 1, 1000), // Torags
                new MysteryItem(12881, 1, 1, 1000), // Ahrims
                new MysteryItem(12883, 1, 1, 1000), // Karils
                new MysteryItem(12851, 1, 1, 1000), // Ammy of the damned
                new MysteryItem(6585, 1, 1, 1000), // Fury
                new MysteryItem(10551, 1, 1, 1000).setCheckHasItem(), // Torso
                new MysteryItem(12457, 1, 1, 1000), // Infinity hat
                new MysteryItem(12458, 1, 1, 1000), // Infinity top
                new MysteryItem(12459, 1, 1, 1000), // Infinity robe
                new MysteryItem(12954, 1, 1, 1000).setCheckHasItem(), // Dragon defender
                new MysteryItem(6570, 1, 1, 1000).setCheckHasItem(), // fire cape
                new MysteryItem(6889, 1, 1, 1000), // mages book
                new MysteryItem(32149, 3, 6, 1000), // Larrans booster
                new MysteryItem(32150, 3, 6, 1000), // Gano booster
                new MysteryItem(32151, 3, 6, 1000), // Slayer booster
                new MysteryItem(32152, 3, 6, 1000), // Pet booster
                new MysteryItem(32153, 3, 6, 1000), // Gauntlet booster
                new MysteryItem(32154, 3, 6, 1000), // Blood money booster
                new MysteryItem(32155, 3, 6, 1000), // Clue booster
                new MysteryItem(32156, 3, 6, 1000), // ToB booster
                new MysteryItem(32157, 3, 6, 1000), // Task Picker
                new MysteryItem(32158, 3, 6, 1000), // Task Reset
                new MysteryItem(32166, 3, 6, 1000), // Rev Booster
                new MysteryItem(32167, 3, 6, 1000), // Nex Booster
                new MysteryItem(19782, 2, 5, 1000), // Xerics Wisdom

                //Rare = 200
                new MysteryItem(ItemId.DRAGON_PICKAXE, 1, 1, 500), // dragon pickaxe
                new MysteryItem(ItemId.DRAGON_CROSSBOW, 1, 1, 500).announce(), // dragon crossbow
                new MysteryItem(11824, 1, 1, 500), // zamorak spear
                new MysteryItem(6731, 1, 1, 500), // seers ring
                new MysteryItem(6733, 1, 1, 500), // archers ring
                new MysteryItem(6735, 1, 1, 500), // warrior ring
                new MysteryItem(6737, 1, 1, 500), // berserker ring
                new MysteryItem(11133, 1, 1, 500), // regen bracelet

                //Rare+ = 200
                new MysteryItem(30031, 1, 1, 250).announce(), // pet mbox
                new MysteryItem(20724, 1, 1, 250), // imbued heart
                new MysteryItem(11808, 1, 1, 250).announce(), // zgs
                new MysteryItem(11804, 1, 1, 250).announce(), // bgs
                new MysteryItem(11806, 1, 1, 250).announce(), // sgs
                new MysteryItem(19707, 1, 1, 250).announce(), // eternal glory
                new MysteryItem(12605, 1, 1, 250).announce(), // treasonous ring
                new MysteryItem(12603, 1, 1, 250).announce(), // tyrannical ring
                new MysteryItem(12601, 1, 1, 250).announce(), // ring of the gods
                new MysteryItem(12006, 1, 1, 250).announce().charges(10_000), // tentacle whip
                new MysteryItem(12002, 1, 1, 250).announce(), // occult
                new MysteryItem(13265, 1, 1, 250).announce(), // abby dagger
                new MysteryItem(11791, 1, 1, 250).announce(), // staff of the dead

                // Super Rare = 100
                new MysteryItem(11802, 1, 1, 100).announce(), // ags
                new MysteryItem(11785, 1, 1, 100).announce(), // acb
                new MysteryItem(1037, 1, 1, 100).announce(), // bunny ears
                new MysteryItem(1419, 1, 1, 100).announce(), // scythe
                new MysteryItem(19481, 1, 1, 100).announce(), // ballista
                new MysteryItem(12932, 1, 1, 100).announce(), // Magic fang
                new MysteryItem(12922, 1, 1, 100).announce(), // Tanz fang

                // Extremely Rare = 30
                new MysteryItem(13576, 1, 1, 30).announce(), // dwh
                new MysteryItem(13036, 1, 1, 30).announce(), // gilded set (lg)
                new MysteryItem(13038, 1, 1, 30).announce(), // gilded set (sk)
                new MysteryItem(962, 1, 1, 30).announce(), // xmas cracker

                // Uber Rare = 20
                new MysteryItem(11847, 1, 1, 20).announce(), // black h'ween
        };

        totalWeight = MysteryItem.calculateTotalWeight(rewards);

        supplies = new MysterySupplyItem[]{
                new MysterySupplyItem(11232, 250, 400),
                new MysterySupplyItem(22804, 200, 300),
                new MysterySupplyItem(20849, 200, 300),
                new MysterySupplyItem(13440, 200, 400),
                new MysterySupplyItem(2364, 100, 200),
                new MysterySupplyItem(537, 150, 250),
                new MysterySupplyItem(2, 1000, 2500),
                new MysterySupplyItem(11959, 150, 400),
                new MysterySupplyItem(9243, 300, 500),
                new MysterySupplyItem(9244, 250, 350),
                new MysterySupplyItem(5316, 5, 10),
                new MysterySupplyItem(12696, 25, 75),
                new MysterySupplyItem(10926, 25, 75),
                new MysterySupplyItem(22462, 30, 60),
                new MysterySupplyItem(ItemId.ZULRAHS_SCALES, 1000, 5000),
                new MysterySupplyItem(23962, 50, 100),
                new MysterySupplyItem(ItemId.ANCIENT_SHARD, 1, 7)
        };
    }

    @Override
    public int[] getItems() {
        return new int[] {32164};
    }
}
