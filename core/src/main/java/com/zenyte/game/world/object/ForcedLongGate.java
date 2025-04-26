package com.zenyte.game.world.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Kris | 27/03/2019 19:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("all")
public class ForcedLongGate<T extends Entity> {
    @NotNull
    private final WorldObject clickedObject;
    @NotNull
    private final Pair<WorldObject, WorldObject> gatePair;
    @Nullable
    private Pair<WorldObject, WorldObject> graphicalPair;
    private final boolean vertical;
    @NotNull
    private final T entity;

    public ForcedLongGate(@NotNull final T entity, @NotNull final WorldObject object) {
        this.entity = entity;
        this.clickedObject = object;
        final com.zenyte.game.world.object.WorldObject other = Objects.requireNonNull(Gate.getOtherGate(object));
        gatePair = new Pair<>(object, other);
        final int rotation = object.getRotation();
        this.vertical = (rotation & 1) == 0;
    }

    public void handle(final Optional<Predicate<T>> predicate) {
        final com.zenyte.game.world.entity.pathfinding.events.RouteEvent<T, com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy> event = getEvent();
        if (event != null) {
            event.setEvent(() -> handle(predicate));
            entity.setRouteEvent(event);
            return;
        }
        handleSub(predicate);
    }

    private void handleSub(final Optional<Predicate<T>> predicate) {
        if (predicate.isPresent()) {
            if (!predicate.get().test(entity)) {
                return;
            }
        }
        final com.zenyte.game.world.object.WorldObject first = gatePair.getFirst();
        final com.zenyte.game.world.object.WorldObject second = gatePair.getSecond();
        final com.zenyte.game.world.object.WorldObject innerMapGate = Gate.getInnerMapGate(first, second);
        final com.zenyte.game.world.object.WorldObject outerMapGate = first == innerMapGate ? second : first;
        final com.zenyte.game.world.entity.Location[] tiles = Gate.getOpenLocations(innerMapGate);
        graphicalPair = new Pair<>(new WorldObject(Gate.getRespectiveId(innerMapGate.getId()), innerMapGate.getType(), (innerMapGate.getRotation() - 1) & 3, tiles[0]), new WorldObject(Gate.getRespectiveId(outerMapGate.getId()), outerMapGate.getType(), (outerMapGate.getRotation() - 1) & 3, tiles[1]));
        World.removeGraphicalDoor(first);
        World.removeGraphicalDoor(second);
        World.spawnGraphicalDoor(graphicalPair.getFirst());
        World.spawnGraphicalDoor(graphicalPair.getSecond());
        onStart();
        final int entityX = entity.getX();
        final int entityY = entity.getY();
        final int objX = clickedObject.getX();
        final int objY = clickedObject.getY();
        final int z = entity.getPlane();
        Location destination;
        final int rotation = clickedObject.getRotation();
        switch (rotation) {
        case 0: 
        case 2: 
            destination = new Location(entityX + (entityX > (objX + (rotation == 0 ? -1 : 0)) ? -1 : 1), entityY, z);
            break;
        default: 
            destination = new Location(entityX, entityY + (entityY > (objY + (rotation == 1 ? -1 : 0)) ? -1 : 1), z);
            break;
        }
        entity.lock(2);
        entity.addWalkSteps(destination.getX(), destination.getY(), 1, false);
        WorldTasksManager.schedule(() -> {
            World.removeGraphicalDoor(graphicalPair.getFirst());
            World.removeGraphicalDoor(graphicalPair.getSecond());
            World.spawnGraphicalDoor(first);
            World.spawnGraphicalDoor(second);
            onEnd();
        }, 1);
    }

    protected RouteEvent<T, TileStrategy> getEvent() {
        return null;
    }

    protected void onStart() {
    }

    protected void onEnd() {
    }
}
