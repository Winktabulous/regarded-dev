package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.skills.farming.FarmingPatch;
import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchType;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;

/**
 * @author Kris | 04/02/2019 17:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HealthChecking extends Action {
    public HealthChecking(final FarmingSpot spot) {
        this.spot = spot;
    }

    private final FarmingSpot spot;

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        final PatchType type = spot.getProduct().getType();
        final FarmingProduct product = spot.getProduct();
        if (Utils.random(50) == 0) {
            player.getInventory().addOrDrop(new Item(22875, 1));
            player.sendMessage("You find a Hespori seed.");
        }
        if (type.equals(PatchType.CALQUAT_PATCH)) {
            player.getAchievementDiaries().update(KaramjaDiary.CHECK_CALQUAT_HEALTH);
        } else if (type.equals(PatchType.FRUIT_TREE_PATCH)) {
            player.getAchievementDiaries().update(KaramjaDiary.GROW_A_HEALTHY_FRUIT_TREE);
            if (product.equals(FarmingProduct.PALM)) {
                //TODO: Verify conditions for these dangling diary!
                player.getAchievementDiaries().update(KaramjaDiary.CHECK_PALM_TREE_HEALTH);
                if (spot.getPatch().equals(FarmingPatch.GNOME_MAZE_FRUIT_TREE)) {
                    player.getAchievementDiaries().update(ArdougneDiary.CHECK_PALM_TREE_HEALTH);
                } else if (spot.getPatch().equals(FarmingPatch.LLETYA_FRUIT_TREE)) {
                    player.getAchievementDiaries().update(WesternProvincesDiary.CHECK_PALM_TREE_HEALTH);
                }
            } else if (product.equals(FarmingProduct.PAPAYA)) {
                player.getDailyChallengeManager().update(SkillingChallenge.CHECK_HEALTH_PAPAYA_TREES);
            }
        } else if (product.equals(FarmingProduct.MAGIC)) {
            player.getDailyChallengeManager().update(SkillingChallenge.CHECK_HEALTH_MAGIC_TREES);
        }
        if (type == PatchType.GRAPEVINE_PATCH) {
            player.sendMessage("Your vine is in perfect health.");
        } else {
            player.sendMessage("You examine the " + (spot.getProduct().getType() == PatchType.BUSH_PATCH ? "bush" : spot.getProduct().getType() == PatchType.CACTUS_PATCH ? "cactus" : "tree") + " for signs of disease and find that it is in perfect health.");
        }
        player.getSkills().addXp(SkillConstants.FARMING, spot.getProduct().getCheckHealthXP());
        player.getFarming().handleContractCompletion(player, spot.getProduct());
        spot.setHealthChecked();
        return -1;
    }
}
