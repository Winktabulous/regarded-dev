package com.zenyte.plugins.equipment.equip;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 05/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CapeOfSkullsEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        if (slotId != -1 && player.getTemporaryAttributes().remove("Wear cape of skulls") == null) {
            player.getInterfaceHandler().closeInterfaces();
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Wearing this cape will give you a PK skull.",
                            new DialogueOption("Give me a PK skull.", () -> {
                                player.getTemporaryAttributes().put("Wear cape of skulls", true);
                                player.getEquipment().wear(slotId);
                            }),
                            new DialogueOption("Cancel."));
                }
            });
            return false;
        }
        return true;
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        player.getVariables().setSkull(true);
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.CAPE_OF_SKULLS
        };
    }
}
