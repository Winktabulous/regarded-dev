package com.zenyte.game.content.chambersofxeric.skills;

import com.zenyte.game.item.Item;

/**
 * The enum containing all the possible raid fish.
 *
 * @author Kris | 19. nov 2017 : 17:08.18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum RaidFish {

    PSYK_FISH(new Item(20855), 0, 2),
    SUPHI_FISH(new Item(20857), 15, 6),
    LECKISH_FISH(new Item(20859), 30, 10),
    BRAWK_FISH(new Item(20861), 45, 16),
    MYCIL_FISH(new Item(20863), 60, 23),
    ROQED_FISH(new Item(20865), 75, 30),
    KYREN_FISH(new Item(20867), 90, 38);

    static final RaidFish[] values = values();
    /**
     * The mutable fish item.
     */
    private final Item fish;
    /**
     * The fishing level requirement to fish the given fish.
     */
    private final int requirement;
    /**
     * The experience granted for catching a fish.
     */
    private final int experience;
    
    RaidFish(Item fish, int requirement, int experience) {
        this.fish = fish;
        this.requirement = requirement;
        this.experience = experience;
    }
    
    public Item getFish() {
        return fish;
    }
    
    public int getRequirement() {
        return requirement;
    }
    
    public int getExperience() {
        return experience;
    }
}
