package com.near_reality.game.content.gauntlet.actions;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.objects.LinumTirinum;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 *
 *
 * @author Andys1814.
 * @since 1/25/2022.
 */
public final class LinumTirinumAction extends Action {

    private static final Animation ANIMATION = new Animation(2282);

    private static final SoundEffect SOUND = new SoundEffect(2437);

    private static final String STOP_MESSAGE = "Your inventory is too full to hold any more fibre.";

    private final LinumTirinum linum;

    private final boolean corrupted;

    public LinumTirinumAction(LinumTirinum linum, boolean corrupted) {
        this.linum = linum;
        this.corrupted = corrupted;
    }

    @Override
    public boolean start() {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(STOP_MESSAGE);
            return false;
        }

        player.setAnimation(ANIMATION);
        delay(2);

        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(STOP_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(ANIMATION);

        player.getInventory().addItem(new Item(linum.getResource()));
        player.sendMessage("You pick some fibre from the plant.");

        player.getPacketDispatcher().sendSoundEffect(SOUND);
        player.getSkills().addXp(SkillConstants.FARMING, 1);

        Gauntlet.rollShards(player, corrupted);

        linum.deplete();

        if (linum.isDepleted()) {
            linum.getDepletionMessage().ifPresent(message -> player.sendMessage(message));

            World.removeObject(linum);
            linum.setId(linum.getId() + 1); // Set linum to depleted object variant.
            World.spawnObject(linum);

            return -1;
        }

        return 2;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

}

