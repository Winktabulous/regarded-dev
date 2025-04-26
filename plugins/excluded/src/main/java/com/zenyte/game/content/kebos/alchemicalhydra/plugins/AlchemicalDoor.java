package com.zenyte.game.content.kebos.alchemicalhydra.plugins;

import com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RegionArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 02/11/2019 | 16:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class AlchemicalDoor implements ObjectAction {
    private static final Logger log = LoggerFactory.getLogger(AlchemicalDoor.class);
    private static final WorldObject[] graphicalDoors = {new WorldObject(34555, 10, 1, new Location(1356, 10259, 0)), new WorldObject(34556, 10, 3, new Location(1356, 10258, 0))};

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final RegionArea area = player.getArea();
        if (!(area instanceof AlchemicalHydraInstance)) {
            return;
        }
        final AlchemicalHydraInstance instance = (AlchemicalHydraInstance) area;
        if (player.getX() > object.getX() && !(instance.getHydra().isDead() || instance.getHydra().isFinished())) {
            player.sendMessage("The door seems to be jammed!");
            return;
        }
        if (option.equals("Open")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Enter the laboratory? The door is notorious for getting jammed.", "Yes", "No").onOptionOne(() -> open(player));
                }
            });
        } else if (option.equals("Quick-open")) {
            open(player);
        }
    }

    private static void open(final Player player) {
        final AlchemicalHydraInstance instance = (AlchemicalHydraInstance) player.getArea();
        for (final WorldObject door : instance.getDoors()) {
            World.removeObject(door);
        }
        for (final WorldObject door : instance.getGraphicalDoors()) {
            World.spawnObject(door);
        }
        player.addWalkSteps(player.getX() >= instance.getX(1356) ? (player.getX() - 1) : (player.getX() + 1), player.getY(), 5, false);
        player.lock(2);
        WorldTasksManager.schedule(() -> {
            for (final WorldObject door : instance.getDoors()) {
                World.spawnObject(door);
            }
            for (final WorldObject door : instance.getGraphicalDoors()) {
                World.removeObject(door);
            }
            instance.setEntered(true);
            player.getBossTimer().startTracking("Alchemical Hydra");
            player.getMusic().unlock(Music.get("Alchemical Attack!"));
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {"Alchemical door"};
    }
}
