package com.zenyte.game.content.skills.construction.objects.workspace;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 21:16.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Tools implements ObjectInteraction {

    private static final String[][] OPTIONS = new String[][] { new String[] { "A saw.", "A hammer.", "A chisel.", "A pair of shears." }, new String[] { "A bucket.", "A knife.", "A spade.", "A tinderbox." }, new String[] { "A brown apron.", "A glassblowing pipe.", "A needle." }, new String[] { "An amulet mould.", "A necklace mould.", "A ring mould.", "A holy mould.", "Next selection." }, new String[] { "A rake.", "A spade.", "A trowel.", "A seed dibber.", "Next selection." } };

    private static final String[][] OPTIONS2 = new String[][] { new String[] { "A bracelet mould.", "A tiara mould.", "Previous selection." }, new String[] { "A watering can.", "A pair of secateurs.", "Previous selection." } };

    private static final int[][] ITEM_IDS = new int[][] { new int[] { 8794, 2347, 1755, 1735 }, new int[] { 1925, 946, 952, 590 }, new int[] { 1757, 1785, 1733 }, new int[] { 1595, 1597, 1592, 1599, 11065, 5523 }, new int[] { 5341, 952, 5325, 5343, 5340, 5329 } };

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TOOLS, ObjectId.TOOLS_6787, ObjectId.TOOLS_6788, ObjectId.TOOLS_6789, ObjectId.TOOLS_6790 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        final int id = object.getId();
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("What would you like to take?", OPTIONS[id - 6786]).onOptionOne(() -> {
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
                }).onOptionFive(id >= 6789 ? () -> setKey(5) : () -> {
                    addItem(player, 4, id);
                    finish();
                });
                if (id >= 6789) {
                    options(5, "What would you like to take?", OPTIONS2[id - 6789]).onOptionOne(() -> {
                        addItem(player, 4, id);
                        finish();
                    }).onOptionTwo(() -> {
                        addItem(player, 5, id);
                        finish();
                    }).onOptionThree(() -> setKey(1));
                }
            }
        });
    }

    private final void addItem(final Player player, final int option, final int objectId) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some more free inventory space to take this.");
            return;
        }
        player.getInventory().addItem(ITEM_IDS[objectId - 6786][option], 1);
    }
}
