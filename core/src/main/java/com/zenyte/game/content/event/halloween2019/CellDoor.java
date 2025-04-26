package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.custom.halloween.HalloweenObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CellDoor implements ObjectAction, ItemOnObjectAction {
    private static final Location INSIDE = new Location(1624, 4702, 0);

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        pickLock(player, object);
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.LOCKPICK};
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Open")) {
            player.getDialogueManager().start(new PlainChat(player, "The door is locked."));
            return;
        }
        if (option.equalsIgnoreCase("Pick-lock")) {
            if (!player.getInventory().containsItem(ItemId.LOCKPICK, 1)) {
                player.getDialogueManager().start(new PlainChat(player, "You're going to need something to pick the " +
                        "lock with."));
                return;
            }
            pickLock(player, object);
        }
    }

    private void pickLock(@NotNull final Player player, @NotNull final WorldObject object) {
        final boolean shilopsCell = object.getX() != 1624;
        if (shilopsCell) {
            player.getInventory().deleteItem(ItemId.LOCKPICK, 1);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(new Item(ItemId.LOCKPICK), "The lockpick broke. It doesn't seem like a lockpick is going to " +
                            "be enough for this door...");
                }
            });
            return;
        }
        player.lock(3);
        player.addWalkSteps(object.getX(), object.getY());
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            private WorldObject door;
            @Override
            public void run() {
                switch (ticks++) {
                case 0: 
                    door = Door.handleGraphicalDoor(object, null);
                    return;
                case 1: 
                    if (player.getLocation().getPositionHash() == INSIDE.getPositionHash()) {
                        player.addWalkSteps(door.getX(), door.getY(), 1, false);
                    } else {
                        player.addWalkSteps(object.getX(), object.getY(), 1, false);
                    }
                    return;
                case 3: 
                    Door.handleGraphicalDoor(door, object);
                    stop();
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {HalloweenObject.CELL_DOOR.getRepackedObject()};
    }
}
