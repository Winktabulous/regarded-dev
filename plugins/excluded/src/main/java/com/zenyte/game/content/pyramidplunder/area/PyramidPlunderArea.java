package com.zenyte.game.content.pyramidplunder.area;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.pyramidplunder.PlunderRoom;
import com.zenyte.game.content.pyramidplunder.PyramidPlunderConstants;
import com.zenyte.game.content.pyramidplunder.object.PlunderRoomExit;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportMovementPlugin;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zenyte.game.content.pyramidplunder.object.PlunderDoor.ROOM_VARBIT;
import static com.zenyte.game.content.pyramidplunder.object.PlunderDoor.THIEVING_LEVEL_VARBIT;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class PyramidPlunderArea extends PolygonRegionArea implements CycleProcessPlugin, PartialMovementPlugin, TeleportMovementPlugin {
    private static final Logger log = LoggerFactory.getLogger(PyramidPlunderArea.class);
    private static final long DOOR_SHUFFLE_DELAY = TimeUnit.MINUTES.toTicks(3);
    private static final int TIMER_VARBIT = 2375;

    public static void resetTimer(final Player player) {
        player.getVarManager().sendBit(TIMER_VARBIT, 0);
    }

    private static void kickOutPlayer(final Player player) {
        PlunderRoomExit.leave(player);
        player.sendFilteredMessage("Your time is up...");
        player.getDialogueManager().start(new NPCChat(player, NpcId.GUARDIAN_MUMMY, "You\'ve had your five minutes of plunder. Now be off with you!"));
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {new RSPolygon(new int[][] {{1920, 4419}, {1985, 4419}, {1980, 4482}, {1918, 4480}}, 0)};
    }

    @Override
    public void enter(Player player) {
        GameInterface.PYRAMID_PLUNDER.open(player);
        updateRoomInfo(player, player.getLocation());
    }

    @Override
    public void leave(Player player, boolean logout) {
        PlunderRoomExit.leave(player, false);
        if (logout) {
            player.forceLocation(PyramidPlunderConstants.OUTSIDE_PYRAMID);
        }
    }

    @Override
    public void process() {
        try {
            final int tick = getAreaTimer();
            if (tick % DOOR_SHUFFLE_DELAY == 0) {
                PlunderRoom.shuffle();
            }
        } catch (Exception e) {
            log.error("Error while randomizing pyramid plunder room doors.", e);
        }
        try {
            for (Player player : getPlayers()) {
                player.getVarManager().incrementBit(TIMER_VARBIT, 1);
                final int currentPlunderTick = player.getVarManager().getBitValue(TIMER_VARBIT);
                if (currentPlunderTick >= 500) {
                    kickOutPlayer(player);
                }
            }
        } catch (Exception e) {
            log.error("Error while checking and removing player from pyramid plunder after time limit.", e);
        }
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        for (SpearTrap trap : SpearTrap.traps) {
            if (trap.execute(player, x, y)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String name() {
        return "Pyramid Plunder";
    }

    @Override
    public void processMovement(Player player, Location destination) {
        //Since you can only teleport to another room, there is no path to walking there, we only need to process room changes through this.
        final int currentRoom = player.getVarManager().getBitValue(ROOM_VARBIT);
        if (currentRoom < 1 || currentRoom > 8) {
            updateRoomInfo(player, destination);
            return;
        }
        final PlunderRoom plunderRoom = PlunderRoom.rooms[currentRoom - 1];
        if (plunderRoom.getPolygon().contains(destination)) {
            return;
        }
        updateRoomInfo(player, destination);
    }

    private final void updateRoomInfo(@NotNull final Player player, @NotNull final Location destination) {
        for (final PlunderRoom room : PlunderRoom.rooms) {
            if (room.getPolygon().contains(destination)) {
                player.getVarManager().sendBit(ROOM_VARBIT, room.getRoomId());
                player.getVarManager().sendBit(THIEVING_LEVEL_VARBIT, room.getLevel());
                return;
            }
        }
        //Not in a room
        player.getVarManager().sendBit(ROOM_VARBIT, 0);
        player.getVarManager().sendBit(THIEVING_LEVEL_VARBIT, 0);
    }
}
