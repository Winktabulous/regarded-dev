package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Kris | 24. veebr 2018 : 15:30.20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OakLarder implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LARDER_13566 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        player.getDialogueManager().start(new OptionDialogue(player, "What would you like to take?", new String[] { "Tea leaves.", "A bucket of milk.", "An egg.", "A pot of flour.", "Nothing." }, new Runnable[] { () -> addItem(player, 0), () -> addItem(player, 1), () -> addItem(player, 2), () -> addItem(player, 3), null }));
    }

    private final void addItem(final Player player, final int option) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some more free inventory space to take this.");
            return;
        }
        player.sendInputInt("How many would you like to take?", amount -> {
            if (amount > player.getInventory().getFreeSlots()) {
                amount = player.getInventory().getFreeSlots();
            }
            if (amount == 0) {
                player.sendMessage("You don't have enough free space to take this.");
                return;
            }
            switch(option) {
                case 0:
                    player.getInventory().addItem(7738, amount);
                    break;
                case 1:
                    player.getInventory().addItem(1927, amount);
                    break;
                case 2:
                    player.getInventory().addItem(1944, amount);
                    break;
                default:
                    player.getInventory().addItem(1933, amount);
                    break;
            }
        });
    }
}
