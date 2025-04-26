package com.zenyte.plugins.object;

import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class MotherlodeCrates implements ObjectAction {

    private static final Item BRONZE_PICKAXE = new Item(1265, 1);

    private static final Item HAMMER = new Item(2347, 1);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if (TreasureTrail.search(player, object, option) || TreasureTrail.searchWithkey(player, object, option))
            return;

        if (object.getRegionId() == 14936) {
            final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
            final boolean hasPick = (player.getInventory().containsItem(BRONZE_PICKAXE) || (weapon != null && weapon.getName().toLowerCase().contains("pickaxe")));
            if (hasPick && player.getInventory().containsItem(HAMMER)) {
                player.sendMessage("You search the crate but find nothing.");
                return;
            }
            if (!player.getInventory().containsItem(HAMMER)) {
                if (player.getInventory().hasFreeSlots())
                    player.getDialogueManager().start(new ItemChat(player, HAMMER, "You've found a hammer. How handy."));
                player.getInventory()
                        .addItem(HAMMER)
                        .onFailure(remainder -> player.sendMessage("You have no space in your inventory!"));
                return;
            }
            if (player.getInventory().containsItem(HAMMER) && !hasPick) {
                if (player.getInventory().hasFreeSlots())
                    player.getDialogueManager().start(new ItemChat(player, BRONZE_PICKAXE, "You've found a bronze pickaxe. How handy."));
                player.getInventory()
                        .addItem(BRONZE_PICKAXE)
                        .onFailure(remainder -> player.sendMessage("You have no space in your inventory!"));
                return;
            }
        }
        player.sendMessage("You find nothing.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.CRATE_357};
    }
}
