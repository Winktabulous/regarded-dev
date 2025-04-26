package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.FarmingProduct;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Kris | 17/02/2019 22:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScarecrowRemoval extends Action {

    private static final Animation animation = new Animation(830);
    private static final SoundEffect sound = new SoundEffect(1470);

    public ScarecrowRemoval(final FarmingSpot spot) {
        this.spot = spot;
    }

    private final FarmingSpot spot;

    @Override
    public boolean start() {
        if (!check()) {
            return false;
        }
        player.setAnimation(animation);
        player.sendSound(sound);
        return true;
    }

    @Override
    public boolean process() {
        return check();
    }

    private boolean check() {
        if (!player.getInventory().containsItem(ItemId.SPADE)) {
            player.sendMessage("You need a spade in order to remove the scarecrow.");
            return false;
        }
        return spot.getProduct() == FarmingProduct.SCARECROW;
    }

    @Override
    public int processWithDelay() {
        spot.setScarecrow(FarmingProduct.WEEDS);
        player.getInventory().addOrDrop(new Item(6059));
        player.sendFilteredMessage("You remove the scarecrow from the flower patch.");
        return -1;
    }
}
