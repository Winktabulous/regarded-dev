package com.zenyte.game.content.skills.construction.objects.kitchen;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 16:28.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TeakShelf implements ObjectInteraction {

    private static final String[] FIRST_SHELF = new String[] { "A bowl.", "A pie dish.", "An empty pot.", "Previous selection." };

    private static final String[] SECOND_SHELF = new String[] { "A bowl.", "A pie dish.", "An empty pot.", "A Chef's hat.", "Previous selection." };

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHELVES_13557, ObjectId.SHELVES_13558 };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        final int id = object.getId();
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("What would you like to take?", "A kettle.", "A teapot.", "A porcelain cup.", "An empty beer glass.", "Next selection.").onOptionOne(() -> {
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
                }).onOptionFive(() -> setKey(5));
                options(5, "What would you like to take?", id == 13558 ? SECOND_SHELF : FIRST_SHELF).onOptionOne(() -> {
                    addItem(player, 4, id);
                    finish();
                }).onOptionTwo(() -> {
                    addItem(player, 5, id);
                    finish();
                }).onOptionThree(() -> {
                    addItem(player, 6, id);
                    finish();
                }).onOptionFour(id == 13558 ? () -> {
                    addItem(player, 7, id);
                    finish();
                } : () -> setKey(1)).onOptionFive(() -> setKey(1));
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
                    player.getInventory().addItem(objectId == 13558 ? 7726 : 7714, amount);
                    break;
                case 2:
                    player.getInventory().addItem(objectId == 13558 ? 7735 : 7732, amount);
                    break;
                case 3:
                    player.getInventory().addItem(1919, amount);
                    break;
                case 4:
                    player.getInventory().addItem(1923, amount);
                    break;
                case 5:
                    player.getInventory().addItem(2313, amount);
                    break;
                case 6:
                    player.getInventory().addItem(1931, amount);
                    break;
                case 7:
                    player.getInventory().addItem(1949, amount);
                    break;
            }
        });
    }
}
