package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.FarmingConstants;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.plugins.dialogue.DoubleItemChat;

/**
 * @author Kris | 09/02/2019 13:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GrapevineTreating extends Action {

    private static final Animation animation = new Animation(2272);
    private static final SoundEffect sound = new SoundEffect(2433);

    private final FarmingSpot spot;

    @Override
    public boolean start() {
        if (spot.getValue() >= 1) {
            player.sendMessage("The patch has already been treated with saltpetre.");
            return false;
        }
        if (!player.getInventory().containsItem(FarmingConstants.GARDENING_TROWEL) || !player.getInventory().containsItem(FarmingConstants.SALTPETRE)) {
            player.getDialogueManager().start(new DoubleItemChat(player, FarmingConstants.GARDENING_TROWEL,
                    FarmingConstants.SALTPETRE, "You can treat the patch by digging saltpetre into it with a trowel."));
            return false;
        }
        player.lock(3);
        player.setAnimation(animation);
        player.getPacketDispatcher().sendSoundEffect(sound);
        delay(3);
        return true;
    }

    @Override
    public boolean process() {
        return spot.getValue() == 0;
    }

    @Override
    public int processWithDelay() {
        player.getInventory().deleteItem(FarmingConstants.SALTPETRE);
        spot.setTreated();
        return -1;
    }

    public GrapevineTreating(FarmingSpot spot) {
        this.spot = spot;
    }
}
