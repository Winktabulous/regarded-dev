package com.zenyte.plugins.object;

import com.zenyte.game.content.DonatorPin;
import com.zenyte.game.content.well.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

public class WellOfGoodWillAction implements ObjectAction, ItemOnObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
//        if(option.equalsIgnoreCase("Top-contributers")) {
//            player.getDialogueManager().start(new PlainChat(player, WellHandler.get().getTopContributers(WellPerk.DOUBLE_UNIQUES)));
//            return;
//        }
        if(WellConstants.WELL_DISABLED) {
            player.sendMessage("The Well is currently disabled as the economy balances during launch");
            return;
        }
        player.getDialogueManager().start(new WellDialogue(player));
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if(WellConstants.WELL_DISABLED) {
            player.sendMessage("The Well is currently disabled as the economy balances during launch");
            return;
        }
        DonatorPin pin = DonatorPin.forId(item.getId());
        if(pin == null)
            return;
        player.getDialogueManager().start(new WellDialoguePin(player, pin));
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<>(DonatorPin.VALUES.length);
        for (final DonatorPin pin : DonatorPin.VALUES) {
            list.add(pin.getItemId());
        }
        return list.toArray(new Object[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {WellConstants.WELL_OBJ_ID};
    }
}
