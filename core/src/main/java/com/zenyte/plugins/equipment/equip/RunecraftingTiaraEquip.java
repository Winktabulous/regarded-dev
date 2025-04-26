package com.zenyte.plugins.equipment.equip;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Kris | 27. march 2018 : 4:26.05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class RunecraftingTiaraEquip implements EquipPlugin {
    private static final Map<Integer, Integer> BITS =
            ImmutableMap.<Integer, Integer>builder().put(5527, 607).put(5529, 608).put(5531, 609).put(5533, 612).put(5535, 610).put(5537, 611).put(5539, 613).put(5541, 615).put(5543, 616).put(5545, 614).put(5547, 617).put(22121, 6220).put(5549, 13782).build();

    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        return true;
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        if (ArrayUtils.contains(SkillcapePerk.RUNECRAFTING.getCapes(), equippedItem.getId())) {
            for (final Integer bitId : BITS.values()) {
                player.getVarManager().sendBit(bitId, 1);
            }
            return;
        }
        final Integer bitId = BITS.get(equippedItem.getId());
        if (bitId == null || bitId == -1) {
            return;
        }
        player.getVarManager().sendBit(bitId, 1);
    }

    @Override
    public void onLogin(@NotNull Player player, @NotNull Item item, int slot) {
        onEquip(player, player.getEquipment().getContainer(), item);
    }

    @Override
    public void onUnequip(final Player player, final Container container, final Item unequippedItem) {
        if (ArrayUtils.contains(SkillcapePerk.RUNECRAFTING.getCapes(), unequippedItem.getId())) {
            for (final Integer bitId : BITS.values()) {
                player.getVarManager().sendBit(bitId, 0);
            }
            //Let's just run the on-equip plugin on the player's helmet, they may be wearing a tiara therefore one of the altars should still work.
            final Item helmet = player.getHelmet();
            if (helmet != null) {
                onEquip(player, player.getEquipment().getContainer(), helmet);
            }
            return;
        }
        final Integer bitId = BITS.get(unequippedItem.getId());
        if (bitId == null || bitId == -1) {
            return;
        }
        final Item cape = player.getCape();
        if (cape != null) {
            //If the user is wearing a cape, don't reset the bit.
            if (ArrayUtils.contains(SkillcapePerk.RUNECRAFTING.getCapes(), cape.getId())) {
                return;
            }
        }
        player.getVarManager().sendBit(bitId, 0);
    }

    @Override
    public int[] getItems() {
        return new int[] {5527, 5529, 5531, 5533, 5535, 5537, 5539, 5541, 5543, 5545, 5547, 5549, 22121, 9765, 9766, 10645, 13280, 13282, 13342};
    }
}
