package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/03/2019 01:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AmuletOfTheDamnedEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        if (slotId != -1 && player.getTemporaryAttributes().remove("Wear amulet of the damned") == null) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(item, "Are you sure you wish to equip the amulet?<br>" +
                            "Equipping it degrades it and makes it untradeable. " +
                            "After 15 hours of wielding the amulet, it will degrade to dust.");
                    options("Equip the amulet?", new DialogueOption("Yes.", () -> {
                        if (player.getInventory().getItem(slotId) != item) {
                            return;
                        }
                        player.getTemporaryAttributes().put("Wear amulet of the damned", true);
                        player.getEquipment().wear(slotId);
                    }), new DialogueOption("No."));
                }
            });
            return false;
        }
        return true;
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        equippedItem.setId(12853);
        equippedItem.setCharges(DegradableItem.getFullCharges(equippedItem.getId()));
        player.getEquipment().refreshAll();
    }

    @Override
    public int[] getItems() {
        return new int[] {
                12851
        };
    }
}
