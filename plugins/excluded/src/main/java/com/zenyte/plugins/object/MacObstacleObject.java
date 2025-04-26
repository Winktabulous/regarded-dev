package com.zenyte.plugins.object;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 15 aug. 2018 | 20:08:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class MacObstacleObject implements ObjectAction {

    private static final Animation JUMP_ANIM = new Animation(839);

    private static final Animation BUILD_ANIM = new Animation(898);

    private static final Location RAFT_DESTINATION = new Location(2793, 3538, 0);

    private static final Location RETURN_DESTINATION = new Location(2807, 3535, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        Location destination;
        switch(object.getId()) {
            case 27061:
                if (player.getSkills().getLevel(SkillConstants.AGILITY) < 99) {
                    player.sendMessage("You need a Agility level of 99 to pass through this obstacle.");
                    return;
                }
                if (player.getX() > object.getX()) {
                    destination = new Location(object.getX() - 1, object.getY(), object.getPlane());
                } else {
                    destination = new Location(object.getX() + 2, object.getY(), object.getPlane());
                }
                player.setAnimation(JUMP_ANIM);
                player.setForceMovement(new ForceMovement(destination, 90, player.getX() > object.getX() ? ForceMovement.WEST : ForceMovement.EAST));
                player.lock(3);
                WorldTasksManager.schedule(() -> player.setLocation(destination), 2);
                return;
            case 27063:
                if (player.getSkills().getLevel(SkillConstants.AGILITY) < 99) {
                    player.sendMessage("You need a Agility level of 99 to pass through this obstacle.");
                    return;
                }
                if (player.getX() > object.getX()) {
                    destination = new Location(object.getX() - 1, object.getY(), object.getPlane());
                } else {
                    destination = new Location(object.getX() + 2, object.getY() + 2, object.getPlane());
                }
                player.setAnimation(JUMP_ANIM);
                player.setForceMovement(new ForceMovement(destination, 90, player.getX() > object.getX() ? ForceMovement.WEST : ForceMovement.EAST));
                player.lock(3);
                WorldTasksManager.schedule(() -> player.setLocation(destination), 2);
                return;
            case 27067:
                final int value = player.getVarManager().getBitValue(4801);
                if (value == 0) {
                    if (player.getSkills().getLevel(SkillConstants.CRAFTING) < 99) {
                        player.sendMessage("You need a Crafting level of 99 to make this raft.");
                        return;
                    }
                    if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < 99) {
                        player.sendMessage("You need a Construction level of 99 to make this raft.");
                        return;
                    }
                    player.sendMessage("You use your construction and crafing skills to fashion a raft.");
                    player.setAnimation(BUILD_ANIM);
                    player.lock(3);
                    WorldTasksManager.schedule(() -> {
                        player.getVarManager().sendBit(4801, 1);
                    }, 2);
                } else {
                    player.sendMessage("You board the raft with trepidation...");
                    new FadeScreen(player, () -> {
                        player.sendMessage("You fall off the raft, onto the beach of the little island, but the raft floats away.");
                        player.setLocation(RAFT_DESTINATION);
                        player.getVarManager().sendBit(4801, 0);
                    }).fade(3);
                }
                return;
            default:
                player.sendMessage("You board the raft with trepidation...");
                new FadeScreen(player, () -> {
                    player.sendMessage("You fall off the raft, onto the main land, but the raft floats away.");
                    player.setLocation(RETURN_DESTINATION);
                }).fade(3);
                return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TREE_STUMP_27061, ObjectId.ROCKSLIDE_27063, ObjectId.OLD_LOG, ObjectId.ROW_BOAT_27066, 27067 };
    }
}
