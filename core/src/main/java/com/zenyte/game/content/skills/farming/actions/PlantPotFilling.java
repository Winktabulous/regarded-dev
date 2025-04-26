package com.zenyte.game.content.skills.farming.actions;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.skills.farming.FarmingConstants;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;

/**
 * @author Kris | 08/02/2019 17:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PlantPotFilling extends Action {

    private static final Animation animation = new Animation(2272);
    private static final SoundEffect sound = new SoundEffect(2433);

    public PlantPotFilling(final FarmingSpot spot, final Item pot, final int slot) {
        this.spot = spot;
        this.pot = pot;
        this.slot = slot;
    }

    private final FarmingSpot spot;
    private final Item pot;
    private final int slot;

    @Override
    public boolean start() {
        if (!spot.isClear()) {
            player.sendMessage("You need to weed this patch first.");
            return false;
        } else if (spot.getState() != PatchState.WEEDS) {
            player.sendMessage("You can only fill a plantpot from an empty farming patch.");
            return false;
        }
        if (!player.getInventory().containsItem(FarmingConstants.GARDENING_TROWEL)) {
            player.sendMessage("You need a gardening trowel to do that.");
            return false;
        }
        player.setAnimation(animation);
        player.getPacketDispatcher().sendSoundEffect(sound);
        delay(2);
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        Preconditions.checkArgument(player.getInventory().getItem(slot) == pot);
        player.getInventory().set(slot, new Item(5354));
        return -1;
    }
}
