package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.plugins.object.AltarOPlugin.PRAY_ANIM;

/**
 * @author Kris | 26/06/2019 13:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ElidinisStatuette implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Pray-at") || option.equals("Pray")) {
            final int toRestore = player.getSkills().getLevelForXp(SkillConstants.PRAYER) - player.getPrayerManager().getPrayerPoints();
            if (EquipmentUtils.containsFullInitiate(player) && object.getId() == 410) {
                player.getAchievementDiaries().update(FaladorDiary.PRAY_ALTAR_OF_GUTHIX);
            } else if (EquipmentUtils.containsFullProselyte(player)) {
                player.getAchievementDiaries().update(FaladorDiary.RECHARGE_PRAYER);
            } else if (object.getId() == 10389) {
                player.getAchievementDiaries().update(DesertDiary.PRAY_AT_ELIDINIS_STATUETTE);
            } else if (toRestore >= 85 && object.getId() == 20377) {
                player.getAchievementDiaries().update(DesertDiary.RESTORE_85_PRAYER_POINTS);
            } else if (player.getPrayerManager().isActive(Prayer.SMITE)) {
                player.getAchievementDiaries().update(VarrockDiary.PRAY_AT_VARROCK_ALTAR);
                player.getAchievementDiaries().update(LumbridgeDiary.RECHARGE_PRAYER);
            }
            player.getAchievementDiaries().update(WildernessDiary.PRAY_AT_CHAOS_ALTAR);
            player.getAchievementDiaries().update(ArdougneDiary.USE_EAST_ARDOUGNE_ALTAR);
            player.lock();
            player.sendMessage("You pray to the gods...");
            player.setAnimation(PRAY_ANIM);
            player.sendSound(2674);
            WorldTasksManager.schedule(() -> {
                player.getPrayerManager().restorePrayerPoints(99);
                final int hp = player.getMaxHitpoints() + 7;
                if (player.getHitpoints() < hp) {
                    player.setHitpoints(hp);
                }
                player.getVariables().setRunEnergy(100);
                player.getCombatDefinitions().setSpecialEnergy(100);
                player.getToxins().reset();
                player.sendMessage("... and recharge your prayer.");
                player.unlock();
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ELIDINIS_STATUETTE, 10389 };
    }
}
