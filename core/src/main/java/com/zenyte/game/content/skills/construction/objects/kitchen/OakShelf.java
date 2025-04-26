package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 16:17.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class OakShelf implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHELVES_13548, ObjectId.SHELVES_13556 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        final int id = object.getId();
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("What would you like to take?", "A kettle.", "A teapot.", id == 13556 ? "A porcelain cup." : "A clay cup.", "An empty beer glass.", id == 13556 ? "Next selection." : "A bowl.").onOptionOne(() -> {
                    addItem(player, 0, id);
                    finish();
                }).onOptionTwo(() -> {
                    addItem(player, 1, id);
                    finish();
                }).onOptionThree(() -> {
                    addItem(player, 2, id);
                    finish();
                }).onOptionFour(() -> {
                    addItem(player, 3, id);
                    finish();
                }).onOptionFive(id == 13556 ? () -> setKey(5) : () -> {
                    addItem(player, 4, id);
                    finish();
                });
                options(5, "What would you like to take?", "A bowl.", "A cake tin.", "Previous selection.").onOptionOne(() -> {
                    addItem(player, 4, id);
                    finish();
                }).onOptionTwo(() -> {
                    addItem(player, 5, id);
                    finish();
                }).onOptionThree(() -> setKey(1));
            }
        });
    }

    private final void addItem(final Player player, final int option, final int objectId) {
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
                    player.getInventory().addItem(7688, amount);
                    break;
                case 1:
                    player.getInventory().addItem(objectId == 13556 ? 7714 : 7702, amount);
                    break;
                case 2:
                    player.getInventory().addItem(objectId == 13556 ? 7732 : 7728, amount);
                    break;
                case 3:
                    player.getInventory().addItem(1919, amount);
                    break;
                case 4:
                    player.getInventory().addItem(1923, amount);
                    break;
                case 5:
                    player.getInventory().addItem(1887, amount);
                    break;
            }
        });
    }
}
