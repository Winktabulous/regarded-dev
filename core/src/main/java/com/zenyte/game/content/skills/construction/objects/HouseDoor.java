package com.zenyte.game.content.skills.construction.objects;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 27. nov 2017 : 23:10.55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HouseDoor implements ObjectInteraction {

    private static final Logger log = LoggerFactory.getLogger(HouseDoor.class);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_13100, ObjectId.DOOR_13101, ObjectId.DOOR_13102, ObjectId.DOOR_13103, ObjectId.DOOR_13118, ObjectId.DOOR_13119, ObjectId.DOOR_13120, ObjectId.DOOR_13121, ObjectId.DOOR_13107, ObjectId.DOOR_13108, ObjectId.DOOR_13109, ObjectId.DOOR_13110, ObjectId.DOOR_14753, ObjectId.DOOR_14754, ObjectId.DOOR_27084, ObjectId.DOOR_27085, ObjectId.DOOR_27086, ObjectId.DOOR_27087 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        try {
            final int x = object.getXInChunk();
            final int y = object.getYInChunk();
            if (y == 0 || y == 7) {
                if (x == 3)
                    handleDoor(World.getObjectWithType(new Location(object.getX() + 1, object.getY(), object.getPlane()), 0));
                else if (x == 4)
                    handleDoor(World.getObjectWithType(new Location(object.getX() - 1, object.getY(), object.getPlane()), 0));
            } else if (x == 0 || x == 7) {
                if (y == 3)
                    handleDoor(World.getObjectWithType(new Location(object.getX(), object.getY() + 1, object.getPlane()), 0));
                else if (y == 4)
                    handleDoor(World.getObjectWithType(new Location(object.getX(), object.getY() - 1, object.getPlane()), 0));
            }
            handleDoor(object);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void handleDoor(final WorldObject object) {
        final int x = object.getXInChunk();
        final int y = object.getYInChunk();
        final int rot = object.getRotation();
        final WorldObject door = new WorldObject(object);
        if (x == 0 && rot == 0 || x == 7 && rot == 2 || y == 0 && rot == 3 || y == 7 && rot == 1)
            openDoor(door, x, y);
        else
            closeDoor(door, x, y);
        World.spawnObject(door);
        World.removeObject(object);
    }

    private void openDoor(final WorldObject object, final int x, final int y) {
        if (y == 0 || y == 7) {
            object.moveLocation(0, y == 7 ? 1 : -1, 0);
            if (x == 3)
                object.setRotation(0);
            else if (x == 4)
                object.setRotation(2);
        } else {
            object.moveLocation(x == 7 ? 1 : -1, 0, 0);
            if (y == 3)
                object.setRotation(3);
            else if (y == 4)
                object.setRotation(1);
        }
        object.setId(object.getId() + 2);
    }

    private void closeDoor(final WorldObject object, final int x, final int y) {
        if (y == 0 || y == 7) {
            object.moveLocation(0, y == 7 ? 1 : -1, 0);
            object.setRotation(y == 7 ? 3 : 1);
        } else {
            object.moveLocation(x == 7 ? 1 : -1, 0, 0);
            object.setRotation(x == 7 ? 0 : 2);
        }
        object.setId(object.getId() - 2);
    }
}
