package com.zenyte.game.content.pyramidplunder.object;

import com.zenyte.game.content.pyramidplunder.PlunderRoom;
import com.zenyte.game.content.pyramidplunder.area.PyramidPlunderArea;
import com.zenyte.game.content.pyramidplunder.area.SpearTrap;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 4/2/2020
 */
public class SpeartrapObject implements ObjectAction {
    public static final Animation deactivateAnim = new Animation(832);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final PlunderRoom currentRoom = PlunderRoom.get(player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT));
        final SpearTrap trap = SpearTrap.get(currentRoom.getRoomId());
        attempt(player, object, currentRoom, trap);
    }

    private Direction getDirection(@NotNull final Player player, @NotNull WorldObject object, @NotNull final SpearTrap trap) {
        final boolean horizontal = trap.isHorizontal();
        if (horizontal) {
            return player.getX() < object.getX() ? Direction.EAST : Direction.WEST;
        } else {
            return player.getY() < object.getY() ? Direction.NORTH : Direction.SOUTH;
        }
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final int roomId = player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT);
        if (roomId == 0) {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
            return;
        }
        final PlunderRoom currentRoom = PlunderRoom.get(roomId);
        final SpearTrap trap = SpearTrap.get(currentRoom.getRoomId());
        final int rot = object.getRotation();
        final Location tile = trap.within(object.getX(), object.getY()) ? object : object.transform(rot == 1 ? Direction.NORTH : rot == 3 ? Direction.SOUTH : rot == 0 ? Direction.WEST : Direction.EAST);
        player.setRouteEvent(new TileEvent(player, new TileStrategy(tile, 1, trap.isHorizontal() ? (RouteEvent.BLOCK_FLAG_NORTH | RouteEvent.BLOCK_FLAG_SOUTH) : (RouteEvent.BLOCK_FLAG_EAST | RouteEvent.BLOCK_FLAG_WEST)), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    private void attempt(@NotNull final Player player, @NotNull WorldObject object, @NotNull final PlunderRoom room, @NotNull final SpearTrap trap) {
        player.lock(6);
        player.sendFilteredMessage("You carefully try to temporarily deactivate the trap mechanism.");
        WorldTasksManager.schedule(() -> player.setAnimation(deactivateAnim));
        WorldTasksManager.schedule(() -> player.setAnimation(deactivateAnim), 1);
        WorldTasksManager.schedule(() -> succeed(player, object, room, trap), 2);
    }

    private void succeed(@NotNull final Player player, @NotNull WorldObject object, @NotNull final PlunderRoom room, @NotNull final SpearTrap trap) {
        if (!player.inArea(PyramidPlunderArea.class)) {
            return;
        }
        player.getVarManager().sendBit(SpearTrap.TRAP_STATUS_VARBIT, SpearTrap.INACTIVE_STAGE);
        player.sendFilteredMessage("You deactivate the trap!");
        player.setRunSilent(true);
        player.addWalkSteps(getDirection(player, object, trap), 3, -1, false);
        WorldTasksManager.schedule(() -> {
            player.getSkills().addXp(SkillConstants.THIEVING, room.getTrapExp());
            player.setRunSilent(false);
            player.getVarManager().sendBit(SpearTrap.TRAP_STATUS_VARBIT, SpearTrap.ACTIVE_STAGE);
        }, 2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.SPEARTRAP/*_21280*/};//TODO
    }
}
