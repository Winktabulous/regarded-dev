package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Kris | 03/02/2019 02:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Raking extends Action {
    private static final Item RAKE = new Item(5341);
    private static final Animation RAKING = new Animation(2273);
    private static final SoundEffect RAKING_SOUND = new SoundEffect(2442);
    private static final int XP = 4;
    private final FarmingSpot spot;

    public Raking(final FarmingSpot spot) {
        this.spot = spot;
    }

    @Override
    public boolean start() {
        if (!player.hasBoon(SwissArmyMan.class) && !player.getInventory().containsItem(RAKE.getId(), RAKE.getAmount())) {
            player.sendMessage("You need a rake to get rid of the weeds.");
            return false;
        }
        if (!spot.isFullOfWeeds()) {
            player.sendMessage("This " + spot.getPatch().getType().getSanitizedName() + " doesn't need weeding right " +
                    "now.");
            return false;
        }
        rake();
        delay(4);
        return true;
    }

    @Override
    public boolean process() {
        return spot.isFullOfWeeds();
    }

    @Override
    public int processWithDelay() {
        if (Utils.random(5) < 4) {
            spot.refreshTimer();
            final int value = spot.getValue();
            spot.setValue(value + 1);
            spot.refresh();
            player.getInventory().addItem(ItemId.WEEDS, 1);
            player.getSkills().addXp(SkillConstants.FARMING, XP);
            if (!spot.isFullOfWeeds()) {
                return -1;
            }
        }
        rake();
        return 3;
    }

    private void rake() {
        player.setAnimation(RAKING);
        player.getPacketDispatcher().sendSoundEffect(RAKING_SOUND);
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }
}
