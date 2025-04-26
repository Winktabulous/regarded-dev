package com.zenyte.game.world.entity.player.container;

import com.zenyte.game.item._Item;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import kotlinx.serialization.Serializable;

@Serializable
public class _Container<T extends _Item> {

    protected ContainerType type;
    protected ContainerPolicy policy;
    protected Int2ObjectLinkedOpenHashMap<T> items;

    public _Container(final ContainerType type, final ContainerPolicy policy, Int2ObjectLinkedOpenHashMap<T> items) {
        this.type = type;
        this.policy = policy;
        this.items = items;
    }

    public _Container(final ContainerType type, final ContainerPolicy policy) {
        this(type, policy, new Int2ObjectLinkedOpenHashMap<>());
    }

    public ContainerType getType() {
        return type;
    }

    public void setType(ContainerType type) {
        this.type = type;
    }

    public ContainerPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(ContainerPolicy policy) {
        this.policy = policy;
    }

    public Int2ObjectLinkedOpenHashMap<T> getItems() {
        return items;
    }

    public void setItems(Int2ObjectLinkedOpenHashMap<T> items) {
        this.items = items;
    }
}
