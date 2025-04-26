package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.achievementdiary.diaries.DesertDiary;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 14 sep. 2018 | 22:03:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ShantayPassObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final boolean north = player.getY() > object.getY();
        if (north) {
            if (!DiaryUtil.eligibleFor(DiaryReward.DESERT_AMULET4, player) && !player.getInventory().containsItem(1854, 1)) {
                World.findNPC(4642, player.getLocation(), 15).ifPresent(npc -> player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        npc("You\'ll need a shantay pass if you wish to go through there!<br>Come purchase it from me.");
                    }
                }));
                return;
            }
            player.getInventory().deleteItem(1854, 1);
        }
        player.lock(2);
        player.setRunSilent(2);
        player.addWalkSteps(player.getX(), player.getY() + (north ? -2 : 2), 2, false);
        if (north && EquipmentUtils.containsFullDesertRobes(player)) {
            player.getAchievementDiaries().update(DesertDiary.ENTER_DESERT_WITH_DESERT_ROBES);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHANTAY_PASS };
    }
}
