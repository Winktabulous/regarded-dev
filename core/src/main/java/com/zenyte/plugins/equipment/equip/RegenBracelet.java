package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 16/03/2019 00:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RegenBracelet implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        player.getVariables().addBoost(PlayerVariables.HealthRegenBoost.REGEN_BRACELET);
    }

    @Override
    public void onUnequip(final Player player, final Container container, final Item unequippedItem) {
        player.getVariables().removeBoost(PlayerVariables.HealthRegenBoost.REGEN_BRACELET);
    }

    @Override
    public void onLogin(@NotNull Player player, @NotNull Item item, int slot) {
        onEquip(player, player.getEquipment().getContainer(), item);
    }

    @Override
    public int[] getItems() {
        return new int[]{
                11133
        };
    }
}
