package com.zenyte.game.content.achievementdiary;

import com.zenyte.game.world.entity.player.Player;

import java.util.List;

public class DiaryUtil {

    public static boolean isKaramjaGlovesEquipped(final Player player) {
        return isEquipped(DiaryReward.KARAMJA_GLOVES1, player) || isEquipped(DiaryReward.KARAMJA_GLOVES2, player) || isEquipped(DiaryReward.KARAMJA_GLOVES3, player) || isEquipped(DiaryReward.KARAMJA_GLOVES4, player);
    }

    public static boolean isEquipped(final DiaryReward diaryReward, final Player player) {
        if (!eligibleFor(diaryReward, player)) {
            return false;
        }

        return player.getEquipment().getId(diaryReward.getSlot()) == diaryReward.getItem().getId();
    }

    public static boolean eligibleFor(final DiaryReward diaryReward, final Player player) {
        for (final Diary diary : diaryReward.getRequiredTasks()) {
            if (diary.autoCompleted()) continue;
            if (player.getAchievementDiaries().getProgress(diary) != diary.objectiveLength()) {
                return false;
            }
        }
        return true;
    }

    public static DiaryReward getBestEligibleReward(final Player player, final DiaryArea area) {
        final List<DiaryReward> rewards = DiaryReward.get(area);
        for (int i = rewards.size() - 1; i >= 0; i--) {
            final DiaryReward reward = rewards.get(i);
            if (reward == null) {
                continue;
            }
            if (eligibleFor(reward, player)) {
                return reward;
            }
        }
        return null;
    }
}
