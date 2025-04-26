package com.zenyte.game.model.item;

import com.google.common.base.Preconditions;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntLists;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

public enum ItemChain {
    OGRE_BELLOWS(ItemId.OGRE_BELLOWS, ItemId.OGRE_BELLOWS_1, ItemId.OGRE_BELLOWS_2, ItemId.OGRE_BELLOWS_3), PHARAOH_SCEPTRE(ItemId.PHARAOHS_SCEPTRE, ItemId.PHARAOHS_SCEPTRE_1, ItemId.PHARAOHS_SCEPTRE_2, ItemId.PHARAOHS_SCEPTRE_3, ItemId.PHARAOHS_SCEPTRE_4, ItemId.PHARAOHS_SCEPTRE_5, ItemId.PHARAOHS_SCEPTRE_6, ItemId.PHARAOHS_SCEPTRE_7, ItemId.PHARAOHS_SCEPTRE_8), DEFENDERS(ItemId.BRONZE_DEFENDER, ItemId.IRON_DEFENDER, ItemId.STEEL_DEFENDER, ItemId.BLACK_DEFENDER, ItemId.MITHRIL_DEFENDER, ItemId.ADAMANT_DEFENDER, ItemId.RUNE_DEFENDER, ItemId.RUNE_DEFENDER_T, ItemId.DRAGON_DEFENDER, ItemId.DRAGON_DEFENDER_T);
    private final IntLists.UnmodifiableList items;
    private final int[] allButFirst;
    private final int[] allButLast;

    ItemChain(final int... items) {
        this.items = (IntLists.UnmodifiableRandomAccessList) IntLists.unmodifiable(IntArrayList.wrap(items));
        this.allButFirst = this.items.subList(1, this.items.size()).toIntArray();
        this.allButLast = this.items.subList(0, this.items.size() - 1).toIntArray();
    }

    public final int before(@NotNull final Item item) {
        return before(item.getId());
    }

    public final int before(@NonNegative int item) {
        final int currentIndex = items.indexOf(item);
        return get(currentIndex - 1);
    }

    public final int after(@NotNull final Item item) {
        return after(item.getId());
    }

    public final int after(final int item, final int defaultId) {
        final int currentIndex = items.indexOf(item);
        return currentIndex == -1 ? defaultId : get(currentIndex + 1);
    }

    public final int after(@NonNegative final int item) {
        final int currentIndex = items.indexOf(item);
        return get(currentIndex + 1);
    }

    public final int get(@NonNegative final int index) {
        Preconditions.checkArgument(index >= 0 && index < items.size(), "Out of bounds index [" + index + "] for " + name());
        return items.getInt(index);
    }

    public final int getHighestCarrying(final Player player) {
        for (int index = items.size() - 1; index >= 0; index--) {
            final int id = get(index);
            if (player.carryingItem(id)) {
                return id;
            }
        }
        return -1;
    }

    public final int first() {
        Preconditions.checkArgument(items.size() > 0, "Tried getting first item of empty item chain.");
        return items.getInt(0);
    }

    public final int last() {
        Preconditions.checkArgument(items.size() > 0, "Tried getting last item of empty item chain.");
        return items.getInt(items.size() - 1);
    }

    public IntLists.UnmodifiableList getItems() {
        return items;
    }

    public int[] getAllButFirst() {
        return allButFirst;
    }

    public int[] getAllButLast() {
        return allButLast;
    }
}
