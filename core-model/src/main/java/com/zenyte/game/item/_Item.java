package com.zenyte.game.item;

import kotlinx.serialization.Serializable;

import java.util.Map;
import java.util.Objects;

@Serializable
public class _Item {

    protected int id;
    protected int amount;
    protected Map<String, Object> attributes;

    public _Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof _Item)) {
            return false;
        }
        final _Item item = (_Item) obj;
        return item.getId() == id && item.getAmount() == amount && Objects.equals(item.getAttributes(), attributes);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
