package com.zenyte.game.content.grandexchange;

import com.google.gson.annotations.Expose;
import com.zenyte.game.item._Item;
import com.zenyte.game.world.entity.player.container._Container;
import kotlinx.serialization.Serializable;

@Serializable
public class _ExchangeOffer<T extends _Item, C extends _Container<T>> {

    @Expose
    protected final T item;
    @Expose
    protected final int slot;
    @Expose
    protected final int price;
    @Expose
    protected final ExchangeType type;
    @Expose
    protected String username;
    @Expose
    protected int amount;
    @Expose
    protected boolean updated;
    @Expose
    protected boolean aborted;
    @Expose
    protected boolean cancelled;
    @Expose
    protected C container;

    public _ExchangeOffer(final String username, final T item, C container, final int slot, final int price, final ExchangeType type) {
        this.username = username;
        this.item = item;
        this.container = container;
        this.slot = slot;
        this.price = price;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public T getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ExchangeType getType() {
        return type;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public C getContainer() {
        return container;
    }

    public void setContainer(C container) {
        this.container = container;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
