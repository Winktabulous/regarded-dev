package com.zenyte.plugins.item.capes;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

@SuppressWarnings("unused")
public class MasoriAssemblerPlugin extends ItemPlugin implements ItemPlugin.ItemStatusOnDeath {
    @Override
    public ItemDeathStatus getStatus() {
        return ItemDeathStatus.KEEP_ON_DEATH;
    }

    @Override
    public void handle() {

    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.MASORI_ASSEMBLER};
    }
}
