package com.zenyte.plugins.item.trident;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

/**
 * @author Christopher
 * @since 2/20/2020
 */
public interface Trident {
    Animation chargingAnim();

    String itemName();

    String missingMaterialMessage();

    int unchargedId();

    int chargedId();

    int maxCharges();

    Item[] returnedMaterials();

    Item[] chargingMaterials();

    default boolean charge(final Player player, final Item tridentItem, final int tridentSlot) {
        final Inventory inventory = player.getInventory();
        if (canCharge(player, tridentItem)) {
            final int charges = tridentItem.getCharges();
            final int chargesToAdd = getChargesToAdd(inventory, charges);
            deleteMaterials(inventory, chargesToAdd);
            tridentItem.setCharges(charges + chargesToAdd);
            player.setAnimation(chargingAnim());
            player.sendMessage("You charge the trident with " + chargesToAdd + " charges.");
            tridentItem.setId(chargedId());
            inventory.refresh(tridentSlot);
            return true;
        }
        return false;
    }

    default boolean uncharge(final Player player, final Item tridentItem, final int slotId) {
        final Inventory inventory = player.getInventory();
        final boolean hasSpace = inventory.hasSpaceFor(returnedMaterials());
        final int charges = tridentItem.getId() == ItemId.TRIDENT_OF_THE_SEAS_FULL ? 2500 : tridentItem.getCharges();
        if (!hasSpace) {
            player.sendMessage("You need some more free inventory space to uncharge the trident.");
            return false;
        }
        for (Item material : returnedMaterials()) {
            inventory.addOrDrop(new Item(material.getId(), charges * material.getAmount()));
        }
        inventory.replaceItem(unchargedId(), 1, slotId);
        player.sendMessage("You uncharge the " + itemName() + ".");
        return true;
    }

    default boolean canCharge(final Player player, final Item item) {
        if (player.getSkills().getLevel(SkillConstants.MAGIC) < 75) {
            player.sendMessage("You need at least level 75 Magic to charge the trident.");
            return false;
        }
        if (item.getCharges() >= maxCharges() && item.getId() != unchargedId()) {
            player.sendMessage("Your " + itemName() + " is already fully charged.");
            return false;
        }
        if (!player.getInventory().containsItems(chargingMaterials())) {
            player.sendMessage(missingMaterialMessage());
            return false;
        }
        return true;
    }

    default void deleteMaterials(final Inventory inventory, final int charges) {
        for (Item material : chargingMaterials()) {
            inventory.deleteItem(material.getId(), material.getAmount() * charges);
        }
    }

    default int getChargesToAdd(final Inventory inventory, final int currentCharges) {
        int chargesToAdd = maxCharges() - currentCharges;
        for (Item material : chargingMaterials()) {
            final int invAmount = inventory.getAmountOf(material.getId());
            final int maxChargesForItem = invAmount / material.getAmount();
            if (maxChargesForItem < chargesToAdd) {
                chargesToAdd = maxChargesForItem;
            }
        }
        return chargesToAdd;
    }
}
