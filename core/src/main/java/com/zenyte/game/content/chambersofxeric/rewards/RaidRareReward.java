package com.zenyte.game.content.chambersofxeric.rewards;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 22/09/2019 20:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum RaidRareReward {
    //Balance change for scrolls, 20 to 14 so it's easier to get other items.
    DEXTEROUS_PRAYER_SCROLL(new Item(21034), 14),
    ARCANE_PRAYER_SCROLL(new Item(21079), 14),
    TWISTED_BUCKLER(new Item(21000), 4),
    DRAGON_HUNTER_CROSSBOW(new Item(21012), 4),
    DINHS_BULWARK(new Item(21015), 3),
    ANCESTRAL_HAT(new Item(21018), 3),
    ANCESTRAL_ROBE_TOP(new Item(21021), 3),
    ANCESTRAL_ROBE_BOTTOM(new Item(21024), 3),
    DRAGON_CLAWS(new Item(ItemId.DRAGON_CLAWS), 3),
    ELDER_MAUL(new Item(21003), 2),
    KODAI_INSIGNIA(new Item(21043), 2),
    TWISTED_BOW(new Item(20997), 2);

    static final RaidRareReward[] values = values();
    public static final int TOTAL_WEIGHT;

    static {
        int weight = 0;
        for (final RaidRareReward value : values) {
            weight += value.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    private final Item item;
    private final int weight;

    RaidRareReward(Item item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    public Item getItem() {
        return item;
    }

    public int getWeight() {
        return weight;
    }
}
