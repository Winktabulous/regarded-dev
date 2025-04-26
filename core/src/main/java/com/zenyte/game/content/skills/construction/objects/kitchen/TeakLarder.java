package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 15:35.09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TeakLarder implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LARDER_13567 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("What would you like to take?", "Tea leaves.", "A bucket of milk.", "An egg.", "A pot of flour.", "Next selection.").onOptionOne(() -> {
                    addItem(player, 0);
                    finish();
                }).onOptionTwo(() -> {
                    addItem(player, 1);
                    finish();
                }).onOptionThree(() -> {
                    addItem(player, 2);
                    finish();
                }).onOptionFour(() -> {
                    addItem(player, 3);
                    finish();
                }).onOptionFive(() -> setKey(5));
                options(5, "What would you like to take?", "A potato.", "A garlic.", "An onion.", "Cheese.", "Previous selection.").onOptionOne(() -> {
                    addItem(player, 4);
                    finish();
                }).onOptionTwo(() -> {
                    addItem(player, 5);
                    finish();
                }).onOptionThree(() -> {
                    addItem(player, 6);
                    finish();
                }).onOptionFour(() -> {
                    addItem(player, 7);
                    finish();
                }).onOptionFive(() -> setKey(1));
            }
        });
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
                case 3:
                    player.getInventory().addItem(1933, amount);
                    break;
                case 4:
                    player.getInventory().addItem(1942, amount);
                    break;
                case 5:
                    player.getInventory().addItem(1550, amount);
                    break;
                case 6:
                    player.getInventory().addItem(1957, amount);
                    break;
                default:
                    player.getInventory().addItem(1985, amount);
                    break;
            }
        });
    }
}
