package com.zenyte.game.content.skills.construction.objects.bedroom;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Kris | 24. veebr 2018 : 22:03.47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ServantsMoneybag implements ObjectInteraction, ItemOnObjectAction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SERVANTS_MONEYBAG };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (player.getCurrentHouse() != player.getConstruction()) {
            player.sendMessage("You cannot interact with someone else's servant's moneybag!");
            return;
        }
        player.getDialogueManager().start(new OptionDialogue(player, "Servant's moneybag: " + player.getConstruction().getServantsCash() + " coins.", new String[] { "Withdraw money.", "Add money.", "Nothing." }, new Runnable[] { () -> withdraw(player), () -> add(player), null }));
    }

    private void add(final Player player) {
        if (player.getConstruction().getServantsCash() >= 3000000) {
            player.sendMessage("Your servant's moneybag is already full.");
            return;
        }
        player.sendInputInt("How many coins would you like to enqueue?", amount -> {
            if (amount > player.getInventory().getAmountOf(995)) {
                amount = player.getInventory().getAmountOf(995);
            }
            if (amount > (3000000 - player.getConstruction().getServantsCash())) {
                amount = (3000000 - player.getConstruction().getServantsCash());
            }
            player.getConstruction().setServantsCash(player.getConstruction().getServantsCash() + amount);
            player.getInventory().deleteItem(995, amount);
            player.sendMessage("You deposit " + amount + " coins into the servant's moneybag.");
        });
    }

    private void withdraw(final Player player) {
        if (player.getConstruction().getServantsCash() <= 0) {
            player.sendMessage("Your servant's moneybag is already empty.");
            return;
        }
        player.sendInputInt("How many coins would you like to remove?", amount -> {
            if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(995, 1)) {
                player.sendMessage("You need some more free space to remove the coins.");
                return;
            }
            if (amount > player.getConstruction().getServantsCash()) {
                amount = player.getConstruction().getServantsCash();
            }
            final int inInv = player.getInventory().getAmountOf(995);
            if (amount + inInv < 0) {
                amount = Integer.MAX_VALUE - inInv;
            }
            if (amount == 0) {
                return;
            }
            player.getConstruction().setServantsCash(player.getConstruction().getServantsCash() - amount);
            player.getInventory().addItem(995, amount);
            player.sendMessage("You withdraw " + amount + " coins from the servant's moneybag.");
        });
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        if (player.getCurrentHouse() != player.getConstruction()) {
            player.sendMessage("You cannot interact with someone else's servant's moneybag!");
            return;
        }
        add(player);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 995 };
    }
}
