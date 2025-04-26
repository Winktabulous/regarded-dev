package com.zenyte.plugins.object;

import com.near_reality.game.content.skills.woodcutting.AxeDefinition;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.packet.out.LocAdd;
import com.zenyte.game.packet.out.LocDel;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.Optional;

/**
 * @author Tommeh | 24-3-2019 | 23:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BrimhavenDungeonVinesObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final int level = object.getId() == 21731 ? 10 : 34;
        if (player.getSkills().getLevel(SkillConstants.WOODCUTTING) < level) {
            player.sendMessage("You need a Woodcutting level of at least " + level + " to cut the vines.");
            return;
        }
        if (player.isUnderCombat()) {
            player.sendMessage("You need to be out of combat to do this.");
            return;
        }
        final Optional<Woodcutting.AxeResult> optionalAxeResult = Woodcutting.getAxe(player);
        if (!optionalAxeResult.isPresent()) {
            player.sendMessage("You need an axe to cut the vines.");
            return;
        }
        final int rotation = object.getRotation();
        final AxeDefinition axe = optionalAxeResult.get().getDefinition();
        player.lock();
        player.setAnimation(axe.getTreeCutAnimation());
        player.setRunSilent(true);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                int toX = player.getX();
                int toY = player.getY();
                if (rotation == 1 || rotation == 3) {
                    if (player.getX() < object.getX()) {
                        toX += 2;
                    } else {
                        toX -= 2;
                    }
                }
                if (rotation == 0 || rotation == 2) {
                    if (player.getY() < object.getY()) {
                        toY += 2;
                    } else {
                        toY -= 2;
                    }
                }
                if (ticks == 3) {
                    if (Utils.random(5) == 0) {
                        player.applyHit(new Hit(1, HitType.REGULAR));
                        player.sendMessage("You fail to cut the vines.");
                    } else {
                        player.sendZoneUpdate(object.getX(), object.getY(), new LocDel(object));
                        player.addWalkSteps(toX, toY, 2, false);
                        player.sendMessage("You cut through the vines.");
                    }
                } else if (ticks == 4) {
                    player.setRunSilent(false);
                    player.getAchievementDiaries().update(KaramjaDiary.CHOP_THE_BRIMHAVEN_DUNGEON_VINES);
                    player.unlock();
                } else if (ticks == 6) {
                    player.sendZoneUpdate(object.getX(), object.getY(), new LocAdd(object));
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.VINES_21731, ObjectId.VINES_21732, ObjectId.VINES_21733, ObjectId.VINES_21734, ObjectId.VINES_21735 };
    }
}
