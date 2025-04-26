package com.zenyte.game.content.partyroom;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

/**
 * @author Kris | 26/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyBalloon extends WorldObject {
    public PartyBalloon(int id, int x, int y, int plane) {
        super(id, 10, Utils.random(3), x, y, plane);
        assert id >= 115 && id <= 122;
    }

    private int ticks = 300;
    private boolean dead;
    private final Set<String> usernames = new ObjectOpenHashSet<>();

    public boolean alive() {
        if (dead) {
            final WorldObject obj = new WorldObject(this);
            obj.setId(obj.getId() + 8);
            assert obj.getId() >= 123 && obj.getId() <= 130;
            World.spawnObject(obj);
            WorldTasksManager.schedule(() -> World.removeObject(obj), 1);
            return false;
        }
        if (!usernames.isEmpty()) {
            dead = true;
            //The loot will appear one tick later, anyone who clicked the balloon initially will be eligible.
            WorldTasksManager.schedule(() -> FaladorPartyRoom.getPartyRoom().roll(this));
            return true;
        }
        if (ticks-- == 300) {
            World.spawnObject(this);
        } else if (ticks <= 0) {
            World.removeObject(this);
            return false;
        }
        return true;
    }

    public Set<String> getUsernames() {
        return usernames;
    }
}
