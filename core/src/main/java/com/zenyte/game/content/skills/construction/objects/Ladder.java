package com.zenyte.game.content.skills.construction.objects;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.RoomType;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 19:09.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class Ladder implements ObjectInteraction {

    private static final Animation CLIMB = new Animation(828);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_13328, ObjectId.LADDER_13329, ObjectId.LADDER_13330 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("climb")) {
            final WorldObject trapDoor = World.getObjectWithType(new Location(object.getX(), object.getY(), object.getPlane() + 1), 22);
            if (trapDoor == null || !(trapDoor.getId() >= 13675 && trapDoor.getId() <= 13680)) {
                player.sendMessage("You can't go up there, there's no trapdoor.");
                return;
            }
            if (trapDoor.getId() < 13678) {
                trapDoor.setLocked(true);
                World.spawnObject(new WorldObject(trapDoor.getId() + 3, trapDoor.getType(), trapDoor.getRotation(), trapDoor));
            }
            player.lock(1);
            player.setAnimation(CLIMB);
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    player.setLocation(new Location(player.getX(), player.getY(), player.getPlane() + 1));
                }
            });
            return;
        } else if (option.equals("remove-room")) {
            if (!construction.isBuildingMode()) {
                player.sendMessage("You can only do this in building mode.");
                return;
            }
            final RoomReference room = construction.getReference(reference.getX(), reference.getY(), 1);
            if (room == null || room.getRoom() != RoomType.OUBLIETTE) {
                player.sendMessage(room == null ? "There's no room up there." : "You can't remove the room up there from here.");
                return;
            }
            construction.getReferences().remove(room);
            construction.enterHouse(true, construction.getRelationalSpawnTile());
        }
    }
}
