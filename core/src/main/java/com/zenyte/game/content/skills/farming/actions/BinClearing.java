package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.CompostBin;
import com.zenyte.game.content.skills.farming.CompostBinType;
import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchType;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Kris | 23/02/2019 14:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BinClearing extends Action {
    private static final Animation animation = new Animation(832);
    private static final SoundEffect sound = new SoundEffect(2436);
    private final FarmingSpot spot;

    @Override
    public boolean start() {
        assert spot.getPatch().getType() == PatchType.COMPOST_BIN;
        assert !spot.getCompostBin().isEmpty();
        if (!process()) {
            return false;
        }
        play();
        delay(3);
        return true;
    }

    @Override
    public boolean process() {
        if (spot.getCompostBin().getType().orElseThrow(RuntimeException::new) == CompostBinType.TOMATOES && !player.getInventory().checkSpace()) {
            return false;
        }
        if (!player.getInventory().containsItem(1925, 1)) {
            player.sendMessage("You need a suitable bucket to do that.");
            return false;
        }
        return true;
    }

    @Override
    public int processWithDelay() {
        final CompostBin bin = spot.getCompostBin();
        final CompostBinType type = bin.getType().orElseThrow(RuntimeException::new);
        if (type != CompostBinType.TOMATOES) {
            player.getInventory().deleteItem(1925, 1);
        }
        if (type == CompostBinType.ULTRACOMPOST) {
            player.getSkills().addXp(SkillConstants.FARMING, 10);
        } else if (type == CompostBinType.SUPERCOMPOST) {
            player.getSkills().addXp(SkillConstants.FARMING, 8.5);
        }
        player.getInventory().addItem(spot.removeCompostableItem());
        if (spot.getCompostBin().isEmpty()) {
            player.sendMessage("The compost bin is now empty.");
            return -1;
        }
        if (!process()) {
            return -1;
        }
        play();
        return 2;
    }

    private void play() {
        player.setAnimation(animation);
        player.sendSound(sound);
    }

    public BinClearing(FarmingSpot spot) {
        this.spot = spot;
    }
}
