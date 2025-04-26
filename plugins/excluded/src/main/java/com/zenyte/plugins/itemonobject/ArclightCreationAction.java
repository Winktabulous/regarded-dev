package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 31-1-2019 | 19:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ArclightCreationAction implements ItemOnObjectAction {

    private static final Item SHARDS = new Item(19677, 3);

    private static final Item DARKLIGHT = new Item(6746);

    private static final Item ARCLIGHT = new Item(19675, 1, 1000);

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getInventory().containsItem(SHARDS)) {
            player.getDialogueManager().start(new ItemChat(player, SHARDS, "You need 3 ancient shards to do this."));
            return;
        }
        if (!player.getInventory().containsItem(DARKLIGHT)) {
            player.getDialogueManager().start(new ItemChat(player, DARKLIGHT, "You need a darklight to do this."));
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Combine Darklight with three ancient shards?", "Yes.", "No.").onOptionOne(() -> {
                    player.getInventory().deleteItemsIfContains(new Item[] { SHARDS, DARKLIGHT }, () -> player.getInventory().addItem(ARCLIGHT));
                    setKey(5);
                });
                item(5, ARCLIGHT, "You combine Darklight with the shards and it seems to glow in your hands creating Arclight. Your Arclight has 1000 charges.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 6746, 19677 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ALTAR_28900 };
    }
}
