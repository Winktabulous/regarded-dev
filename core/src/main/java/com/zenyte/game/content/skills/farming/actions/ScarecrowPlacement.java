package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.farming.FarmingPatch;
import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Kris | 17/02/2019 22:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScarecrowPlacement extends Action {

    private static final SoundEffect sound = new SoundEffect(2435);

    public ScarecrowPlacement(final FarmingSpot spot) {
        this.spot = spot;
    }

    private final FarmingSpot spot;

    @Override
    public boolean start() {
        if (!player.getInventory().containsItem(6059, 1)) {
            player.sendMessage("You need a scarecrow to do this.");
            return false;
        }
        if (spot.getPatch().getType() != PatchType.FLOWER_PATCH) {
            player.sendMessage("You can only place that in the flowers patch.");
            return false;
        }
        return spot.getProduct() == FarmingProduct.WEEDS;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        if (spot.getPatch().equals(FarmingPatch.PHASMATYS_FLOWER)) {
            player.getAchievementDiaries().update(MorytaniaDiary.PLACE_A_SCARECROW);
        } else if (spot.getPatch().equals(FarmingPatch.FALADOR_FLOWER)) {
            player.getAchievementDiaries().update(FaladorDiary.PLACE_A_SCARECROW);
        }
        player.getInventory().deleteItem(new Item(6059));
        spot.setScarecrow(FarmingProduct.SCARECROW);
        player.sendSound(sound);
        player.sendFilteredMessage("You place the scarecrow in the flower patch.");
        return -1;
    }
}
