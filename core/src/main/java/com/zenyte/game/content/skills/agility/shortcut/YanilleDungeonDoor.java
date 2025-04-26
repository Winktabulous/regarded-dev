package com.zenyte.game.content.skills.agility.shortcut;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class YanilleDungeonDoor implements ObjectAction {
    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getY() > 9481) {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
        } else {
            player.setRouteEvent(new TileEvent(player, new TileStrategy(object.transform(Direction.SOUTH)), getRunnable(player, object, name, optionId, option), getDelay()));
        }
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final int y = player.getY();
        final boolean inHut = y > 9481;
        final boolean openOption = option.equalsIgnoreCase("Open");
        player.resetWalkSteps();
        player.addWalkSteps(object.getX(), object.getY());
        if (inHut) {
            if (!openOption) {
                player.addMovementLock(new MovementLock(System.currentTimeMillis() + 1000).setFullLock());
                player.sendMessage("The door is already unlocked.");
                WorldTasksManager.schedule(() -> {
                    player.sendFilteredMessage("You go through the door.");
                    walkThrough(player, object);
                });
            } else {
                player.addMovementLock(new MovementLock(System.currentTimeMillis() + 300));
                player.sendFilteredMessage("You go through the door.");
                walkThrough(player, object);
            }
            return;
        }
        if (option.equals("Pick-lock") || openOption) {
            if (openOption) {
                player.sendMessage("The door is locked.");
                return;
            }
            if (!player.getInventory().containsItem(ItemId.LOCKPICK, 1)) {
                player.getDialogueManager().start(new ItemChat(player, new Item(ItemId.LOCKPICK), "You need a lockpick to pick the lock of the door."));
                return;
            }
            if (player.getSkills().getLevel(SkillConstants.THIEVING) < 82) {
                player.getDialogueManager().start(new PlainChat(player, "You need a Thieving level of at least 82 to pick the lock."));
                return;
            }
            player.sendFilteredMessage("You attempt to pick the lock.");
            final boolean success = Utils.interpolateSuccess(20, 50, 82, 99, player.getSkills().getLevelForXp(SkillConstants.THIEVING));
            if (!success) {
                player.sendFilteredMessage("You fail to pick the lock.");
                return;
            }
            player.sendFilteredMessage("You manage to pick the lock.");
            player.getSkills().addXp(SkillConstants.THIEVING, 50);
            player.addMovementLock(new MovementLock(System.currentTimeMillis() + (player.hasWalkSteps() ? 1000 : 300)).setFullLock());
            WorldTasksManager.scheduleOrExecute(() -> walkThrough(player, object), player.hasWalkSteps() ? 0 : -1);
        }
    }

    private void walkThrough(@NotNull final Player player, @NotNull final WorldObject object) {
        object.setLocked(true);
        player.resetWalkSteps();
        final WorldObject door = Door.handleGraphicalDoor(object, null);
        player.addWalkSteps(player.getX(), object.getY() + (player.getY() == object.getY() ? -1 : 1), 1, false);
        WorldTasksManager.schedule(() -> {
            Door.handleGraphicalDoor(door, object);
            object.setLocked(false);
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.DOOR_11728};
    }
}
