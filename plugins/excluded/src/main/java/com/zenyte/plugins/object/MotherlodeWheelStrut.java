package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.minigame.motherlode.MotherlodeArea;
import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public final class MotherlodeWheelStrut implements ObjectAction {

    private static final Animation FIX = new Animation(7199);

    private static final Map<Boolean, Boolean> WHEELS = new HashMap<>() {

        {
            put(true, false);
            put(false, false);
        }
    };

    private static final List<WorldObject> WATER_OBJECTS = Arrays.asList(new WorldObject(10459, 22, 0, new Location(3748, 5672, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5671, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5670, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5669, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5668, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5667, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5666, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5665, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5664, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5663, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5662, 0)), new WorldObject(10459, 22, 0, new Location(3748, 5661, 0)), new WorldObject(10459, 22, 1, new Location(3748, 5660, 0)), new WorldObject(10459, 22, 1, new Location(3747, 5660, 0)), new WorldObject(10459, 22, 1, new Location(3746, 5660, 0)), new WorldObject(10459, 22, 1, new Location(3745, 5660, 0)), new WorldObject(10459, 22, 1, new Location(3744, 5660, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5660, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5661, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5662, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5663, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5664, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5665, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5666, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5667, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5668, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5669, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5670, 0)), new WorldObject(10459, 22, 2, new Location(3743, 5671, 0)), new WorldObject(10459, 22, 3, new Location(3743, 5672, 0)), new WorldObject(10459, 22, 3, new Location(3744, 5672, 0)), new WorldObject(10459, 22, 3, new Location(3745, 5672, 0)), new WorldObject(10459, 22, 3, new Location(3746, 5672, 0)), new WorldObject(10459, 22, 3, new Location(3747, 5672, 0)));

    private static final WorldObject STRUT_FIXED_TOP = new WorldObject(26669, 10, 0, new Location(3742, 5669, 0));

    private static final WorldObject STRUT_FIXED_BOTTOM = new WorldObject(26669, 10, 0, new Location(3742, 5663, 0));

    private static final WorldObject STRUT_BROKEN_TOP = new WorldObject(26670, 10, 0, new Location(3742, 5669, 0));

    private static final WorldObject STRUT_BROKEN_BOTTOM = new WorldObject(26670, 10, 0, new Location(3742, 5663, 0));

    private static final WorldObject WHEEL_FIXED_TOP = new WorldObject(26671, 10, 0, new Location(3743, 5668, 0));

    private static final WorldObject WHEEL_FIXED_BOTTOM = new WorldObject(26671, 10, 0, new Location(3743, 5662, 0));

    private static final WorldObject WHEEL_BROKEN_TOP = new WorldObject(26672, 10, 0, new Location(3743, 5668, 0));

    private static final WorldObject WHEEL_BROKEN_BOTTOM = new WorldObject(26672, 10, 0, new Location(3743, 5662, 0));

    private static final WorldObject BUBBLES_TOP = new WorldObject(2016, 10, 1, new Location(3743, 5671, 0));

    private static final WorldObject BUBBLES_BOTTOM = new WorldObject(2016, 10, 1, new Location(3743, 5665, 0));

    private static final WorldObject TOP_CURVE_BUBBLES = new WorldObject(2018, 10, 2, new Location(3744, 5672, 0));

    private static final WorldObject BOTTOM_BUBBLES_WEST = new WorldObject(2018, 10, 0, new Location(3744, 5660, 0));

    private static final WorldObject BOTTOM_BUBBLES_EAST = new WorldObject(2018, 10, 0, new Location(3748, 5660, 0));

    private static final WorldObject HOPPER_BUBBLES = new WorldObject(2018, 10, 3, new Location(3748, 5671, 0));

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().containsItem(Smithing.HAMMER)) {
            player.getDialogueManager().start(new ItemChat(player, Smithing.HAMMER, "You need a hammer to re-align the strut."));
            return;
        }
        final boolean top = object.getY() == 5669;
        if (WHEELS.get(top) == null || MotherlodeArea.WATER_WHEELS.get(top))
            return;
        if (!WHEELS.get(top)) {
            WHEELS.replace(top, true);
            player.lock();
            player.faceObject(object);
            player.setAnimation(FIX);
            /* This is the code to successfully replace the object and release it */
            WorldTasksManager.schedule(() -> {
                World.spawnObject(top ? STRUT_FIXED_TOP : STRUT_FIXED_BOTTOM);
                World.spawnObject(top ? WHEEL_FIXED_TOP : WHEEL_FIXED_BOTTOM);
                World.spawnObject(top ? BUBBLES_TOP : BUBBLES_BOTTOM);
                if (!MotherlodeArea.WATER_WHEELS.get(true) && !MotherlodeArea.WATER_WHEELS.get(false)) {
                    for (final WorldObject water : WATER_OBJECTS) World.spawnObject(water);
                }
                WHEELS.replace(top, false);
                MotherlodeArea.WATER_WHEELS.replace(top, true);
                if (!World.containsObjectWithId(HOPPER_BUBBLES, 2018))
                    World.spawnObject(HOPPER_BUBBLES);
                if (!World.containsObjectWithId(TOP_CURVE_BUBBLES, 2018))
                    World.spawnObject(TOP_CURVE_BUBBLES);
                if (!World.containsObjectWithId(BOTTOM_BUBBLES_EAST, 2018))
                    World.spawnObject(BOTTOM_BUBBLES_EAST);
                if (!World.containsObjectWithId(BOTTOM_BUBBLES_WEST, 2018))
                    World.spawnObject(BOTTOM_BUBBLES_WEST);
                player.unlock();
                player.getSkills().addXp(SkillConstants.SMITHING, (player.getSkills().getLevelForXp(SkillConstants.SMITHING) * 1.5));
                player.getAchievementDiaries().update(FaladorDiary.REPAIR_BROKEN_STRUT);
            }, 6);
            WorldTasksManager.schedule(() -> {
                World.spawnObject(top ? STRUT_BROKEN_TOP : STRUT_BROKEN_BOTTOM);
                World.spawnObject(top ? WHEEL_BROKEN_TOP : WHEEL_BROKEN_BOTTOM);
                World.removeObject(top ? BUBBLES_TOP : BUBBLES_BOTTOM);
                WHEELS.replace(top, false);
                MotherlodeArea.WATER_WHEELS.replace(top, false);
                if (!MotherlodeArea.WATER_WHEELS.get(true) && !MotherlodeArea.WATER_WHEELS.get(false)) {
                    World.removeObject(HOPPER_BUBBLES);
                    World.removeObject(TOP_CURVE_BUBBLES);
                    World.removeObject(BOTTOM_BUBBLES_WEST);
                    World.removeObject(BOTTOM_BUBBLES_EAST);
                    for (final WorldObject water : WATER_OBJECTS) World.removeObject(water);
                }
            }, 200);
        } else {
            player.getDialogueManager().start(new PlainChat(player, "This is currently being fixed by another player!"));
        }
    }

    /**
     * This code should be set to break the strut / wheel after a period of time
     */
    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BROKEN_STRUT };
    }
}
