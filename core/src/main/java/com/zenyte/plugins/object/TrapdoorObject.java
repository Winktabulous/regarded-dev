package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.Door;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.Trapdoor;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 10/01/2019 12:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrapdoorObject implements ObjectAction {
    private static final Animation OPEN_TRAPDOOR_ANIM = new Animation(536);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Trapdoor trapdoor = Trapdoor.trapdoors.get(object.getPositionHash());
        if (trapdoor == null) return;
        if (option.equals("Open")) {
            final WorldObject obj = new WorldObject(object);
            World.sendSoundEffect(object, Door.doorOpenSound);
            obj.setId(trapdoor.getOpen());
            World.spawnObject(obj);
            player.setAnimation(OPEN_TRAPDOOR_ANIM);
            player.sendMessage("You open the trapdoor.");
        } else if (option.equals("Close")) {
            World.sendSoundEffect(object, Door.doorCloseSound);
            final WorldObject obj = new WorldObject(object);
            obj.setId(trapdoor.getClosed());
            World.spawnObject(obj);
            player.setAnimation(OPEN_TRAPDOOR_ANIM);
            player.sendMessage("You close the trapdoor.");
        } else if (option.equalsIgnoreCase("Climb-down") || option.equals("Enter")) {
            player.lock(1);
            player.setAnimation(Trapdoor.CLIMB_DOWN);
            WorldTasksManager.schedule(() -> player.setLocation(trapdoor.getDestination()));
        }
    }

    @Override
    public Object[] getObjects() {
        final IntOpenHashSet list = new IntOpenHashSet();
        for (final Trapdoor door : Trapdoor.values) {
            list.add(door.getOpen());
            list.add(door.getClosed());
        }
        list.remove(-1);
        return list.toArray();
    }
}
