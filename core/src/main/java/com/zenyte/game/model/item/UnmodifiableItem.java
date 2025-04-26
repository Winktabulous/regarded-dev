package com.zenyte.game.model.item;

import com.zenyte.game.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 19 2020
 */
public class UnmodifiableItem extends Item {

    public UnmodifiableItem(int id, int amount) {
        super(id, amount);
    }

    public UnmodifiableItem(int id) {
        super(id);
    }

    public UnmodifiableItem(@NotNull final Item item) {
        super(item);
    }

    @Override
    public void setId(final int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAmount(final int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(final String key, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetAttributes() {
        throw new UnsupportedOperationException();
    }
}
