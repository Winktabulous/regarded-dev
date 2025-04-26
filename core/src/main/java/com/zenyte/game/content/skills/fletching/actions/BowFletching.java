package com.zenyte.game.content.skills.fletching.actions;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.skills.fletching.FletchingDefinitions;
import com.zenyte.game.content.skills.fletching.FletchingDefinitions.BowFletchingData;
import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 25 aug. 2018 | 19:10:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class BowFletching extends Action {

    private final BowFletchingData data;
    private final int amount;
    private int cycle;

    @Override
    public boolean start() {
        if (!player.getInventory().containsItems(data.getMaterials())) {
            return false;
        }
        if (data.equals(BowFletchingData.DRAGON_CROSSBOW_U) && !player.getInventory().containsItem(Smithing.HAMMER)) {
            player.sendMessage("You need a hammer to do this.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().containsItems(data.getMaterials())) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(data.getAnimation());
        player.getInventory().deleteItemsIfContains(data.getMaterials(), () -> {
            if (data.equals(BowFletchingData.RUNE_CROSSBOW_U)) {
                player.getAchievementDiaries().update(ArdougneDiary.MAKE_RUNE_CROSSBOW, 0x4);
            } else if (data.equals(BowFletchingData.RUNE_CROSSBOW)) {
                player.getAchievementDiaries().update(ArdougneDiary.MAKE_RUNE_CROSSBOW, 0x16);
            } else if (data.equals(BowFletchingData.MAPLE_SHORTBOW)) {
                player.getAchievementDiaries().update(KandarinDiary.STRING_MAPLE_SHORTBOW);
            } else if (data.equals(BowFletchingData.YEW_LONGBOW)) {
                player.getAchievementDiaries().update(KandarinDiary.CREATE_YEW_LONGBOW, 0x4);
                SherlockTask.STRING_YEW_LONGBOW.progress(player);
            } else if (data.equals(BowFletchingData.OAK_SHORTBOW)) {
                player.getAchievementDiaries().update(WesternProvincesDiary.FLETCH_OAK_SHORTBOW);
            } else if (data.equals(BowFletchingData.MAGIC_LONGBOW)) {
                player.getAchievementDiaries().update(WesternProvincesDiary.FLETCH_MAGIC_LONGBOW);
            }
            player.getInventory().addItem(data.getProduct());
            player.getSkills().addXp(SkillConstants.FLETCHING, data.getXp());
            if (data.toString().contains("_U")) {
                player.sendFilteredMessage("You attach the stock to the limbs and create an unstrung crossbow.");
            } else {
                player.sendFilteredMessage("You add a string to the " + (data.getMaterials()[1].getId() == FletchingDefinitions.BOW_STRING.getId() ? "bow." : "crossbow."));
            }
        });
        cycle++;
        return 1;
    }

    public BowFletching(BowFletchingData data, int amount) {
        this.data = data;
        this.amount = amount;
    }

}
