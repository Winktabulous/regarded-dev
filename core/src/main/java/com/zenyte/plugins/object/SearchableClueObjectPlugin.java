package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class SearchableClueObjectPlugin {
    private static final Object2ObjectOpenHashMap<WorldObject, WorldObject> activeDrawers = new Object2ObjectOpenHashMap<>();

    protected abstract Int2IntOpenHashMap map();

    private final WorldObject getObject(@NotNull final WorldObject current) {
        WorldObject obj = activeDrawers.get(current);
        if (obj == null) {
            obj = new WorldObject(current);
            obj.setId(map().get(obj.getId()));
        }
        return obj;
    }

    protected final void swapObject(@NotNull final WorldObject object) {
        object.setLocked(true);
        final WorldObject obj = getObject(object);
        obj.setLocked(false);
        WorldTasksManager.schedule(() -> {
            World.spawnObject(obj);
            if (activeDrawers.remove(object) == null) {
                activeDrawers.put(obj, object);
                WorldTasksManager.schedule(() -> {
                    final WorldObject matchingObject = activeDrawers.remove(obj);
                    if (matchingObject != object) {
                        return;
                    }
                    World.spawnObject(matchingObject);
                }, 100);
            }
            object.setLocked(false);
        });
    }
}
