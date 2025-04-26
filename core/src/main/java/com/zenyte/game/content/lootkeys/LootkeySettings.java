package com.zenyte.game.content.lootkeys;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;

import java.util.Optional;

public class LootkeySettings {

    private boolean enabled;
    private boolean dropFood;
    private boolean dropValuables;
    private int threshold;
    private int keysClaimed;
    private long totalValueClaimed;
    private long destroyedValue;
    private Int2ObjectLinkedOpenHashMap<Item> currentItemsInChest;

    private boolean withdrawAsNote;

    public boolean isWithdrawAsNote() {
        return withdrawAsNote;
    }

    public void setWithdrawAsNote(boolean withdrawAsNote) {
        this.withdrawAsNote = withdrawAsNote;
    }

    public LootkeySettings(boolean enabled, boolean dropFood, boolean dropValuables, int threshold, int keysClaimed, long totalValueClaimed) {
        this.enabled = enabled;
        this.dropFood = dropFood;
        this.dropValuables = dropValuables;
        this.threshold = threshold;
        this.keysClaimed = keysClaimed;
        this.totalValueClaimed = totalValueClaimed;
    }

    public long getDestroyedValue() {
        return destroyedValue;
    }

    public void setDestroyedValue(long destroyedValue) {
        this.destroyedValue = destroyedValue;
    }

    public void incrementDestroyedValue(long amount) {
        this.destroyedValue += amount;
    }

    public Int2ObjectLinkedOpenHashMap<Item> getCurrentItemsInChest() {
        return currentItemsInChest;
    }

    public Container getCurrentItemsInChest(Player player) {
        var container = new Container(ContainerPolicy.NORMAL, ContainerType.WILDERNESS_LOOT_KEY, Optional.of(player));
        container.setItems(currentItemsInChest);
        return container;
    }

    public void setCurrentItemsInChest(Int2ObjectLinkedOpenHashMap<Item> currentItemsInChest) {
        if (currentItemsInChest == null) {
            this.currentItemsInChest = null;
            return;
        }
        this.currentItemsInChest = currentItemsInChest.clone();
    }

    public int getKeysClaimed() {
        return keysClaimed;
    }

    public void incrementKeysClaimed(int amount) {
        this.keysClaimed += amount;
    }

    public void incrementKeysClaimed() {
        this.keysClaimed += 1;
    }


    public long getTotalValueClaimed() {
        return totalValueClaimed;
    }

    public void incrementTotalValueClaimed(long total) {
        this.totalValueClaimed += total;
    }

    public boolean isEnabled() {
        return false;
        //return enabled;
    }

    public boolean isDropFood() {
        return dropFood;
    }

    public boolean isDropValuables() {
        return dropValuables;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDropFood(boolean dropFood) {
        this.dropFood = dropFood;
    }

    public void setDropValuables(boolean dropValuables) {
        this.dropValuables = dropValuables;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setKeysClaimed(int keysClaimed) {
        this.keysClaimed = keysClaimed;
    }

    public void setTotalValueClaimed(long totalValueClaimed) {
        this.totalValueClaimed = totalValueClaimed;
    }


    public static void sendOpenChest(Player player) {
        player.getVarManager().sendBit(13651, 1);
    }

    public static void sendClosedChest(Player player) {
        player.getVarManager().sendBit(13651, 0);
    }

    public static void clear(Player player) {
        sendClosedChest(player);
        player.getLootkeySettings().setCurrentItemsInChest(null);
    }

}
