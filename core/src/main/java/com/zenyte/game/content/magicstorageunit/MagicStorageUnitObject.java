package com.zenyte.game.content.magicstorageunit;

import com.zenyte.game.content.StorageRoom;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.testinterfaces.StorageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class MagicStorageUnitObject implements ObjectAction {
    private static final int MAGIC_STORAGE_UNIT_OBJECT_ID = 60000;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final MagicStorageUnit unit = player.getMagicStorageUnit();
        //If player hasn't paid to unlock it yet
        if (unit.getUnlockPayment() == 0) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    final int cost = player.getGameMode() == GameMode.ULTIMATE_IRON_MAN ? MagicStorageUnit.STORAGE_UNIT_UIM_UNLOCK_COST : MagicStorageUnit.STORAGE_UNIT_NORMAL_UNLOCK_COST;
                    plain("Unlocking the magic storage unit lets you place hundreds of various items and item sets inside it. You will be able to add and remove your items from the storage unit for free. The cost to unlock the storage unit is " + StringFormatUtil.format(cost) + " coins.");
                    plain("Once Construction skill is released, you will no longer be able to deposit items in this storage unit. You will still be able to withdraw whatever you had put in them though. Upon withdrawing the last remaining item from the storage unit, you will be refunded the original cost to unlock the storage unit.");
                    options("Unlock the magic storage unit?", new DialogueOption("Yes.", () -> {
                        final Inventory inventory = player.getInventory();
                        if (!inventory.containsItem(ItemId.COINS_995, cost)) {
                            setKey(10);
                            return;
                        }
                        inventory.deleteItem(ItemId.COINS_995, cost);
                        unit.setUnlockPayment(cost);
                        unit.refeshVarbit(player);
                    }), new DialogueOption("No."));
                    plain(10, "You do not have enough coins to unlock the storage unit. You need at least " + StringFormatUtil.format(cost) + " coins.");
                }
            });
            return;
        }

        player.getDialogueManager().start(new OptionsMenuD(player, "What would you like to search?", StorageType.names) {
            @Override
            public void handleClick(int slotId) {
                StorageRoom.open(player, StorageType.values[slotId]);
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {MAGIC_STORAGE_UNIT_OBJECT_ID};
    }

}
