package com.zenyte.game.content.treasuretrails.clues.emote;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.Arrays;

/**
 * @author Kris | 20/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SlotLimitationRequirement implements ItemRequirement {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String description;
    private final EquipmentSlot[] slots;

    public SlotLimitationRequirement(String description, EquipmentSlot... slots) {
        this.description = description;
        this.slots = slots;
    }

    @Override
    public boolean fulfilledBy(int itemId) {
        return false;
    }

    @Override
    public boolean fulfilledBy(Item[] items) {
        for (EquipmentSlot slot : slots) {
            if (slot.getSlot() >= items.length) {
                continue; //We can't check the slot, because there is nothing in it, the array hasn't been resized
            }
            if (items[slot.getSlot()].getId() != -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IntArrayList getFulfillingItemsIndexes(Item[] items) {
        return null;
    }

    @Override
    public String toString() {
        return "SlotLimitationRequirement(description=" + this.description + ", slots=" + Arrays.deepToString(this.slots) + ")";
    }
}
