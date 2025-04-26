package com.zenyte.plugins.flooritem;

import com.zenyte.game.content.lootkeys.LootkeyConstants;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.plugins.SkipPluginScan;
import org.jetbrains.annotations.NotNull;

@SkipPluginScan
public class LootKeyFloorPlugin implements FloorItemPlugin {

	@Override
	public void handle(Player player, FloorItem item, int optionId, String option) {
		World.takeFloorItem(player, item);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	@Override
	public void telegrab(@NotNull Player player, @NotNull FloorItem item) {
		FloorItemPlugin.super.telegrab(player, item);
		player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
	}

	@Override
	public boolean overrideTake() {
		return true;
	}

	@Override
	public int[] getItems() {
		return new int[] { LootkeyConstants.LOOT_KEY_ITEM_ID };
	}

}
