package com.near_reality.game.content.gauntlet.actions;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;

/**
 *
 *
 * @author Andys1814.
 * @since 1/25/2022.
 */
public final class GrymRootAction extends Action {

    private static final Animation ANIMATION = new Animation(2282);

    private static final SoundEffect SOUND = new SoundEffect(2437);

    private static final Item GRYM_HERB = new Item(23875);

    private static final String STOP_MESSAGE = "Your inventory is too full to hold any more herbs.";

    private final WorldObject herb;

    private final boolean corrupted;

    public GrymRootAction(WorldObject herb, boolean corrupted) {
        this.herb = herb;
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
        World.removeObject(herb);
        herb.setId(herb.getId() + 1); // Set herb to depleted object variant.
        World.spawnObject(herb);

        player.getInventory().addItem(GRYM_HERB);
        player.sendMessage("You pick a herb from the roots.");

        player.getPacketDispatcher().sendSoundEffect(SOUND);

        player.getSkills().addXp(SkillConstants.FARMING, 1);

        Gauntlet.rollShards(player, corrupted);

        player.setAnimation(Animation.STOP);

        return -1;
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

}

