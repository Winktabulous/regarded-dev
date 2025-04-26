package com.zenyte.game.content.area.waterbirthisland;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.impl.misc.PetRock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tommeh | 21 mrt. 2018 : 17:51:17
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class WaterbirthIslandObject implements ObjectAction {

    private static final Location INSIDE_ENTRANCE = new Location(2442, 10146, 0);

    private static final Location OUTSIDE_ENTRANCE = new Location(2523, 3740, 0);

    private static final int DKS_REGION_ID = 11589;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.CAVE_ENTRANCE_8929)
            player.setLocation(INSIDE_ENTRANCE);
        else if (object.getId() == ObjectId.STEPS_8966)
            player.setLocation(OUTSIDE_ENTRANCE);
        else if (object.getId() >= 8958 && object.getId() <= 8960) {
            final boolean[] active = new boolean[2];
            CharacterLoop.find(player.getLocation(), 3, Entity.class, p -> {
                if (p instanceof Player || p instanceof PetRock) {
                    if (p.getLocation().withinDistance(new Location(object.getX() - 1, object.getY() + 2, object.getPlane()), 0))
                        active[0] = true;
                    if (p.getLocation().withinDistance(new Location(object.getX() - 1, object.getY(), object.getPlane()), 0))
                        active[1] = true;
                }
                return true;
            });
            if (player.getX() > object.getX()) {
                player.sendMessage("The door can't be opened from this side.");
                return;
            }
            if (active[0] && active[1]) {
                World.spawnObject(new WorldObject(8963, object.getType(), object.getRotation(), object));
                WorldTasksManager.schedule(() -> World.spawnObject(object), 10);
            } else
                player.sendMessage("Both pressure pads must be stood on in order for the door to open");
        } else if (object.getId() == ObjectId.IRON_LADDER_10177) {
            if (optionId == 1) {
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        options(TITLE, "Climb up", "Climb down", "Cancel").onOptionOne(() -> setKey(5)).onOptionTwo(() -> {
                            player.useStairs(828, new Location(1799, 4406, 3), 1, 2);
                            finish();
                        }).onOptionThree(() -> finish());
                        plain(5, "You can't climb up this ladder.");
                    }
                });
            } else if (optionId == 2)
                player.getDialogueManager().start(new PlainChat(player, "You can't climb up this ladder."));
            else if (optionId == 3)
                player.useStairs(828, new Location(1799, 4406, 3), 1, 2);
        } else if (object.getId() == ObjectId.LADDER_10193)
            player.useStairs(828, new Location(2545, 10143, 0), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10195)
            player.useStairs(828, new Location(1809, 4405, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10196)
            player.useStairs(828, new Location(1807, 4405, 3), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10197)
            player.useStairs(828, new Location(1823, 4404, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10198)
            player.useStairs(828, new Location(1825, 4404, 3), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10199)
            player.useStairs(828, new Location(1834, 4388, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10200)
            player.useStairs(828, new Location(1834, 4390, 3), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10201)
            player.useStairs(828, new Location(1809, 4394, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10202)
            player.useStairs(828, new Location(1812, 4394, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10203)
            player.useStairs(828, new Location(1799, 4386, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10204)
            player.useStairs(828, new Location(1799, 4388, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10205)
            player.useStairs(828, new Location(1798, 4382, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10206)
            player.useStairs(828, new Location(1796, 4382, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10207)
            player.useStairs(828, new Location(1800, 4369, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10208)
            player.useStairs(828, new Location(1801, 4369, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10209)
            player.useStairs(828, new Location(1827, 4362, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10210)
            player.useStairs(828, new Location(1825, 4362, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10211)
            player.useStairs(828, new Location(1863, 4373, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10212)
            player.useStairs(828, new Location(1863, 4371, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10213)
            player.useStairs(828, new Location(1864, 4389, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10214)
            player.useStairs(828, new Location(1864, 4387, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10215)
            player.useStairs(828, new Location(1890, 4407, 0), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10216)
            player.useStairs(828, new Location(1890, 4406, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10219)
            player.useStairs(828, new Location(1824, 4379, 3), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10220)
            player.useStairs(828, new Location(1824, 4381, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10221)
            player.useStairs(828, new Location(1838, 4375, 2), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10222)
            player.useStairs(828, new Location(1838, 4377, 3), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10223)
            player.useStairs(828, new Location(1850, 4386, 1), 1, 2);
        else if (object.getId() == ObjectId.LADDER_10224)
            player.useStairs(828, new Location(1850, 4387, 2), 1, 2);
        else if (object.getId() == ObjectId.KINGS_LADDER)
            player.useStairs(828, new Location(1912, 4367, 0), 1, 2);
    }

    @Override
    public Object[] getObjects() {
        final List<Object> objects = new ArrayList<Object>();
        for (int i = 10193; i <= 10229; i++) objects.add(i);
        for (int i = 8958; i <= 8960; i++) objects.add(i);
        objects.add(10177);
        objects.add(8929);
        objects.add(8966);
        return objects.toArray(new Object[objects.size()]);
    }
}
