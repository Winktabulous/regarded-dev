package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.skills.crafting.actions.SodaAshCrafting;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.skills.SodaAshD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class SodaAshCraftingAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getInventory().containsItem(SodaAshCrafting.SEAWEED)) {
            player.getDialogueManager().start(new ItemChat(player, SodaAshCrafting.SEAWEED, "You need seaweed to make soda ash."));
            return;
        }
        player.getDialogueManager().start(new SodaAshD(player, object.getName().toLowerCase().equals("range")));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 401 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Range", "Fire" };
    }
}
