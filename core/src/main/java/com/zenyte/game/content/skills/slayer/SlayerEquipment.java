package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author Kris | 30. aug 2018 : 15:41:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum SlayerEquipment {
    SLAYER_HELM(EquipmentSlot.HELMET, (item, player) -> item.getName().toLowerCase().contains("slayer helm")),
    EARMUFFS(EquipmentSlot.HELMET, (item, player) -> item.getId() == 4166 || item.getName().toLowerCase().contains("slayer " +
			"helm") || SlayerMountType.hasHelmMounted(player)),
    NOSE_PEG(EquipmentSlot.HELMET, (item, player) -> item.getId() == 4168 || item.getName().toLowerCase().contains("slayer " +
			"helm") || SlayerMountType.hasHelmMounted(player)),
    FACE_MASK(EquipmentSlot.HELMET, (item, player) -> item.getId() == 4164 || item.getName().toLowerCase().contains("slayer " +
			"helm") || SlayerMountType.hasHelmMounted(player)),
    SPINY_HELMET(EquipmentSlot.HELMET, (item, player) -> item.getId() == 4551 || item.getName().toLowerCase().contains("slayer " +
			"helm") || SlayerMountType.hasHelmMounted(player)),
    MIRROR_SHIELD(EquipmentSlot.SHIELD, (item, player) -> item.getId() == ItemId.MIRROR_SHIELD || item.getId() == ItemId.VS_SHIELD_24266),
    LIT_BUG_LANTERN(EquipmentSlot.SHIELD, (item, player) -> item.getId() == 7053),
    WITCHWOOD_ICON(EquipmentSlot.AMULET, (item, player) -> item.getId() == 8923),
    INSULATED_BOOTS(EquipmentSlot.BOOTS, (item, player) -> item.getId() == 7159),
	;

    private final EquipmentSlot slot;
    private final BiPredicate<Item, Player> predicate;

    SlayerEquipment(EquipmentSlot slot, BiPredicate<Item, Player> predicate) {
        this.slot = slot;
        this.predicate = predicate;
    }

    /**
     * Whether or not the player is wielding the given Slayer equipment.
     *
     * @param player the player whom to check.
     * @return whether the player is wearing the equipment or not.
     */
    public boolean isWielding(final Player player) {
        final Item item = player.getEquipment().getItem(slot);
        if (item == null) {
            return false;
        }
        return predicate.test(item, player);
    }
}
