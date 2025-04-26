package com.zenyte.game.content.event.easter2020.plugin;

import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 11/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class EasterCarrotEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        player.setPlayerItemOnPlayerOption("Whack", true);
    }

    @Override
    public void onUnequip(final Player player, final Container container, final Item unequippedItem) {
        player.setPlayerItemOnPlayerOption("Whack", false);
    }

    @Override
    public void onLogin(@NotNull final Player player, @NotNull final Item item, final int slot) {
        player.setPlayerItemOnPlayerOption("Whack", true);
    }

    @Override
    public int[] getItems() {
        return new int[]{
                EasterConstants.EasterItem.EASTER_CARROT.getItemId()
        };
    }
}
