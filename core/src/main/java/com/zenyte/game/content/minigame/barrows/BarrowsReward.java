package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 30/11/2018 20:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BarrowsReward {

    COINS(new Item(995, 5306), 1, 380, false),
    MIND_RUNE(new Item(558, 2889), 381, 505, true),
    CHAOS_RUNE(new Item(562, 885), 506, 630, true),
    DEATH_RUNE(new Item(560, 578), 631, 755, true),
    BLOOD_RUNE(new Item(565, 236), 756, 880, true),
    BOLT_RACK(new Item(4740, 231), 881, 1005, false),
    LOOP_KEY_HALF(new Item(987), 1006, 1, false),
    TOOTH_KEY_HALF(new Item(985), 1006, 1, false),
    DRAGON_MED_HELM(new Item(1149), 1012, 1, false);

    static BarrowsReward[] values = values();
    final Item item;
    final int requiredPotential;
    final int maximumPotential;
    final boolean diaryMod;

    BarrowsReward(Item item, int requiredPotential, int maximumPotential, boolean diaryMod) {
        this.item = item;
        this.requiredPotential = requiredPotential;
        this.maximumPotential = maximumPotential;
        this.diaryMod = diaryMod;
    }

}
