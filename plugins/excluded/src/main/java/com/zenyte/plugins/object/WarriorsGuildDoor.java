package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectHandler;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 16. dets 2017 : 1:49.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WarriorsGuildDoor implements ObjectAction {

    private static final Location LOCATION = new Location(2876, 3546, 0);

    private static final Location OUTSIDE = new Location(2877, 3546, 0);

    private static final WorldObject DOOR = new WorldObject(24318, 0, 1, OUTSIDE);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        handleDoor(player, object);
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(player.inArea("Warriors' guild") ? LOCATION : OUTSIDE), () -> {
            player.stopAll();
            player.faceObject(object);
            if (!ObjectHandler.handleOptionClick(player, optionId, object)) {
                return;
            }
            handleObjectAction(player, object, name, optionId, option);
        }));
    }

    private void handleDoor(final Player player, final WorldObject object) {
        if (object.isLocked()) {
            return;
        }
        player.setFaceLocation(player.getLocation().getPositionHash() == DOOR.getPositionHash() ? LOCATION : DOOR);
        if (player.getLocation().getPositionHash() == DOOR.getPositionHash()) {
            final int attack = player.getSkills().getLevelForXp(SkillConstants.ATTACK);
            final int strength = player.getSkills().getLevelForXp(SkillConstants.STRENGTH);
            final boolean canEnter = attack == 99 || strength == 99 || (attack + strength) >= 130;
            if (!canEnter) {
                World.whenFindNPC(2457, player, n -> n.setFaceLocation(new Location(player.getLocation())));
                player.getDialogueManager().start(new Dialogue(player, 2457) {

                    @Override
                    public void buildDialogue() {
                        npc("Adventurer, stop! You may not enter this guild yet.");
                        npc("You need either level 99 in Attack or Strength, or a combination of both of at least 130 to enter the Warriors' guild.");
                    }
                });
                return;
            }
            player.getAchievementDiaries().update(FaladorDiary.ENTER_WARRIORS_GUILD);
            handle(player, object, LOCATION);
        } else {
            handle(player, object, DOOR);
        }
    }

    private void handle(final Player player, final WorldObject object, final Location tile) {
        object.setLocked(true);
        World.spawnGraphicalDoor(DOOR);
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.addWalkSteps(tile.getX(), tile.getY(), 1, false);
                } else if (ticks == 1) {
                    World.spawnGraphicalDoor(World.getObjectWithType(DOOR, 0));
                    player.unlock();
                } else if (ticks == 2) {
                    object.setLocked(false);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOOR_24318 };
    }
}
