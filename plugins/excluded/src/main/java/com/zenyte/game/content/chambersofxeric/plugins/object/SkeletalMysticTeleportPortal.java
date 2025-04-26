package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.room.DarkAltarRoom;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

import static com.zenyte.game.world.object.ObjectId.*;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-27
 */
public class SkeletalMysticTeleportPortal implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getRaid().ifPresent(raid -> {
            if (object.getDefinitions() == null) return;
            RaidArea room = raid.getRoom(player.getLocation());
            if (room instanceof DarkAltarRoom altarRoom) {
                switch(option.toLowerCase()) {
                    case "travel" ->
                        player.getDialogueManager().start(
                            new OptionDialogue(player,
                                "Where would you like to go?",
                                new String[] {"Start", "End", "Cancel."},
                                new Runnable[] {
                                    () -> player.teleport(getTeleportLocation(altarRoom, 0)),
                                    () -> player.teleport(getTeleportLocation(altarRoom, 2)),
                                    null
                            })
                        );
                    case "travel-end" -> {
                        var location = getTeleportLocation(altarRoom, 2);
                        player.teleport(location);
                    }
                    case "travel-start" -> {
                        var location = getTeleportLocation(altarRoom, 0);
                        player.teleport(location);
                    }
                }
            }
        });
    }

    private Location getTeleportLocation(RaidArea room, int index) {
        return room.getObjectLocation(DarkAltarRoom.portalLocations[room.getIndex()][index], 1, 1, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {PORTAL_49996, PORTAL_49997, PORTAL_49998 };
    }
}
