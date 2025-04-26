package com.zenyte.game.content.skills.farming;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zenyte.game.content.skills.farming.FarmingProduct.*;

/**
 * @author Kris | 23/02/2019 11:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CompostBin {
    private final boolean isBig;
    private final List<Item> contents = new ArrayList<>(15);
    private static final IntArrayList compostableItems = new IntArrayList();
    private static final IntArrayList supercompostableItems = new IntArrayList();
    private static final FarmingProduct[] ignoredProducts = new FarmingProduct[] {TOMATOES, COMPOST, SUPERCOMPOST, ULTRACOMPOST};

    static {
        for (final FarmingProduct product : FarmingProduct.supercompostableProducts) {
            supercompostableItems.add(product.getProduct().getId());
        }
        supercompostableItems.add(ItemId.COCONUT_SHELL);
        supercompostableItems.add(ItemId.WHITE_TREE_FRUIT);
        for (final FarmingProduct prod : FarmingProduct.values()) {
            final Item product = prod.getProduct();
            if (product != null) {
                final int id = product.getId();
                if (!supercompostableItems.contains(id)) {
                    compostableItems.add(id);
                }
            }
        }
        for (final FarmingProduct ignored : ignoredProducts) {
            final Item product = ignored.getProduct();
            if (product != null) {
                compostableItems.rem(product.getId());
                supercompostableItems.rem(product.getId());
            }
        }
    }

    public static final boolean isCompostableItem(final int id) {
        return compostableItems.contains(id) || supercompostableItems.contains(id);
    }

    private CompostBinType type;

    /**
     * Adds an item into the compost bin.
     * @param item the item to add to the bin.
     * @return the amount that was added to the bin.
     */
    public int add(@NotNull final Item item) {
        final Item it = new Item(item.getId(), 1);
        int count;
        for (count = item.getAmount(); count > 0; count--) {
            if (isFull()) {
                break;
            }
            contents.add(it);
        }
        return item.getAmount() - count;
    }

    /**
     * Removes a random element from the contents of the bin to decrement the size of the bin.
     */
    void removeOne() {
        assert contents.size() > 0;
        contents.remove(0);
    }

    /**
     * Gets the optional type of the compost bin in a descending order, based on purity of the contents.
     * @return an optional compost bin type, returns an empty optional if the contents of the bin are empty.
     */
    public Optional<CompostBinType> getType() {
        if (contents.isEmpty()) {
            return Optional.empty();
        }
        if (type != null) {
            return Optional.of(type);
        }
        if (isRottenTomatoes()) {
            return Optional.of(CompostBinType.TOMATOES);
        }
        if (isSupercompost()) {
            return Optional.of(CompostBinType.SUPERCOMPOST);
        }
        return Optional.of(CompostBinType.COMPOST);
    }

    /**
     * Checks if the bin is full of tomatoes.
     * @return whether or not this bin contains only tomatoes.
     */
    private boolean isRottenTomatoes() {
        for (final Item item : contents) {
            if (item.getId() != 1982) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the bin is full only only supercompostable items.
     * @return whether or not this bin only contains supercompostable items.
     */
    private boolean isSupercompost() {
        for (final Item item : contents) {
            if (!supercompostableItems.contains(item.getId())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBigBin(final int... ids) {
        for (int id : ids) {
            if (id >= 33762 && id <= 33916 || id == 34631) {
                return true;
            }
        }
        return false;
    }

    public int getAmount() {
        return contents.size();
    }

    public boolean isFull() {
        return contents.size() == (isBig ? 30 : 15);
    }

    public boolean isEmpty() {
        return contents.size() == 0;
    }

    public CompostBin(boolean isBig) {
        this.isBig = isBig;
    }

    public static IntArrayList getCompostableItems() {
        return compostableItems;
    }

    public static IntArrayList getSupercompostableItems() {
        return supercompostableItems;
    }

    public boolean isBig() {
        return isBig;
    }

    public void setType(CompostBinType type) {
        this.type = type;
    }
}
