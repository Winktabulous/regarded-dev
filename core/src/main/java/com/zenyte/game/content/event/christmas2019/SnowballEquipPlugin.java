package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 17/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SnowballEquipPlugin implements EquipPlugin {

    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public void onUnequip(final Player player, final Container container, final Item unequippedItem) {
        player.setPlayerItemOnPlayerOption("Pelt", false);
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        player.setPlayerItemOnPlayerOption("Pelt", true, true);
    }

    @Override
    public void onLogin(@NotNull final Player player, @NotNull final Item item, final int slot) {
        player.setPlayerItemOnPlayerOption("Pelt", true, true);
    }

    @Override
    public int[] getItems() {
        return new int[]{
                ItemId.SNOWBALL
        };
    }

}
