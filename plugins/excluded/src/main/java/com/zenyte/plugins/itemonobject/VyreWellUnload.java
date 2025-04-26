package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.utilities.StringFormatUtil;

import static com.zenyte.plugins.object.VyreWell.SET_CHARGE_COUNT;

/**
 * @author Kris | 18/01/2019 18:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VyreWellUnload implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                item(item, "Unload your " + (item.getName().toLowerCase()) + "\'s charges into the well?");
                options("Unload the charges?", new DialogueOption("Yes.", () -> unload(player, item)), new DialogueOption("No."));
            }
        });
    }

    private static final void unload(final Player player, final Item item) {
        final int existingCharges = Math.min(player.getNumericAttribute("vyre well charges").intValue(), 20);
        final int charges = item.getCharges();
        if (existingCharges >= 20) {
            player.getDialogueManager().start(new PlainChat(player, "The well can\'t hold anymore charges."));
            return;
        } else if (item.getCharges() < SET_CHARGE_COUNT) {
            player.getDialogueManager().start(new ItemChat(player, item, "Your weapon hasn\'t got enough charges to deposit back into the well."));
            return;
        }
        final int fillAmount = Math.min(20 - existingCharges, charges / SET_CHARGE_COUNT);
        player.addAttribute("vyre well charges", existingCharges + fillAmount);
        VarCollection.VYRE_WELL.updateSingle(player);
        item.setCharges(item.getCharges() - (fillAmount * SET_CHARGE_COUNT));
        final String name = item.getName().toLowerCase();
        if (item.getCharges() <= 0) {
            switch (item.getId()) {
                case ItemId.SCYTHE_OF_VITUR:
                    item.setId(ItemId.SCYTHE_OF_VITUR_UNCHARGED);
                    break;
                case ItemId.HOLY_SCYTHE_OF_VITUR:
                    item.setId(ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED);
                    break;
                case ItemId.SANGUINE_SCYTHE_OF_VITUR:
                    item.setId(ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED);
                    break;
                case ItemId.SANGUINESTI_STAFF:
                    item.setId(ItemId.SANGUINESTI_STAFF_UNCHARGED);
                    break;
                case ItemId.HOLY_SANGUINESTI_STAFF:
                    item.setId(ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED);
                    break;
            }
        }
        player.getInventory().refreshAll();
        player.getDialogueManager().start(new ItemChat(player, item, "You unload " + StringFormatUtil.format(fillAmount * SET_CHARGE_COUNT) + " charges from the " + name + "."));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.HOLY_SANGUINESTI_STAFF, ItemId.SANGUINESTI_STAFF, ItemId.SCYTHE_OF_VITUR, ItemId.HOLY_SCYTHE_OF_VITUR, ItemId.SANGUINE_SCYTHE_OF_VITUR };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 33085, ObjectId.VYRE_WELL_32985, ObjectId.VYRE_WELL };
    }
}
