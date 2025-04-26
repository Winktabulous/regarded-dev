package com.zenyte.game.model.item;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import dev.kord.core.entity.ReactionEmoji;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Kris | 15/03/2019 18:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum SkillcapePerk {

    ATTACK(9747, 9748, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    COOKING(9801, 9802, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    FARMING(9810, 9811, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    MINING(9792, 9793, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    RANGED(9756, 9757, 13342, 13337, 21898, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE),
    SLAYER(9786, 9787, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    THIEVING(9777, 9778, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    WOODCUTTING(9807, 9808, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    FIREMAKING(9804, 9805, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    RUNECRAFTING(9765, 9766, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    HITPOINTS(9768, 9769, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    HERBLORE(9774, 9775, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    AGILITY(9771, 9772, 13340, 13341, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    PRAYER(9759, 9760, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    SMITHING(9795, 9796, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    STRENGTH(9750, 9751, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    HUNTER(9948, 9949, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    FISHING(9798, 9799, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    CONSTRUCTION(9789, 9790, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    CRAFTING(9780, 9781, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    DEFENCE(9753, 9754, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    MAGIC(9762, 9763, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE),
    FLETCHING(9783, 9784, 13342, CustomItemId.ANCIENT_MAX_CAPE, CustomItemId.BANDOS_MAX_CAPE, CustomItemId.ARMADYL_MAX_CAPE, CustomItemId.SEREN_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE);

    private final int[] capes;
    private final int[] skillCapes;

    SkillcapePerk(int... capes) {
        this(IntArrayList.of(32243, 32245, 32247, 32249, 32251, 32253, 32255, 32257, 32259, 32261, 32263, 32265), capes);
    }

    SkillcapePerk(IntArrayList alwaysAdd, int... capes) {
        alwaysAdd.addAll(IntList.of(capes));
        this.capes = alwaysAdd.toArray(new int[0]);
        final IntArrayList list = new IntArrayList();
        for (final int cape : capes) {
            final ItemDefinitions definitions = ItemDefinitions.get(cape);
            if (definitions == null) continue;
            if (definitions.getName().equals("Null")) System.out.println("Null skillcape: " + cape);
            if (definitions.getName().toLowerCase().contains("max")) continue;
            list.add(cape);
        }
        this.skillCapes = list.toIntArray();
    }

    public boolean isEffective(@NotNull final Player player) {
        final int cape = player.getEquipment().getId(EquipmentSlot.CAPE);
        return cape != -1 && ArrayUtils.contains(capes, cape);
    }

    public boolean isCarrying(@NotNull final Player player) {
        return player.carryingAny(capes);
    }

    public int[] getCapes() {
        return capes;
    }

    public int[] getSkillCapes() {
        return skillCapes;
    }

}
