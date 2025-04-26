package com.zenyte.game.content.treasuretrails.clues.emote;

import com.zenyte.game.item.Item;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Arrays;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AnyRequirementCollection implements ItemRequirement {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String name;
    private final ItemRequirement[] requirements;

    public AnyRequirementCollection(String name, ItemRequirement... requirements) {
        this.name = name;
        this.requirements = requirements;
    }

    @Override
    public boolean fulfilledBy(int itemId) {
        for (ItemRequirement requirement : requirements) {
            if (requirement.fulfilledBy(itemId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean fulfilledBy(Item[] items) {
        for (ItemRequirement requirement : requirements) {
            if (requirement.fulfilledBy(items)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IntArrayList getFulfillingItemsIndexes(Item[] items) {
        for (final ItemRequirement requirement : requirements) {
            final IntArrayList indexes = requirement.getFulfillingItemsIndexes(items);
            if (indexes != null) {
                return indexes;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "AnyRequirementCollection(name=" + this.name + ", requirements=" + Arrays.deepToString(this.requirements) + ")";
    }
}
