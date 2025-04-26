package com.near_reality.game.content.gauntlet.actions;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.GauntletConstants;
import com.near_reality.game.content.gauntlet.objects.PhrenRoots;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.zenyte.game.item.ItemId.*;

/**
 *
 *
 * @author Andys1814.
 * @since 1/25/2022.
 */
public final class PhrenRootsAction extends Action {

    private static final Animation ANIMATION = new Animation(8324);

    private static final Animation ANIMATION_CORRUPTED = new Animation(8325);

    private static final String STOP_MESSAGE = "Your inventory is too full to hold any more bark.";

    private final PhrenRoots roots;

    private final boolean corrupted;

    private int ticks;

    public PhrenRootsAction(PhrenRoots roots, boolean corrupted) {
        this.roots = roots;
        this.corrupted = corrupted;
    }

    @Override
    public boolean start() {
        if (!hasAxe()) {
            player.getDialogueManager().start(new PlainChat(player, "You need an axe to chop bark from these roots."));
            return false;
        }

        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(STOP_MESSAGE);
            return false;
        }

        player.sendMessage("You swing your axe at the roots.");
        delay(2);

        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(STOP_MESSAGE);
            return false;
        }

        if (ticks++ % 4 == 0) {
            player.setAnimation(corrupted ? ANIMATION_CORRUPTED : ANIMATION);
        }

        return true;
    }

    @Override
    public int processWithDelay() {
        player.getInventory().addItem(new Item(roots.getResource()));
        player.sendMessage("You get some bark.");

        player.getSkills().addXp(SkillConstants.WOODCUTTING, 1);

        Gauntlet.rollShards(player, corrupted);

        roots.deplete();

        if (roots.isDepleted()) {
            roots.getDepletionMessage().ifPresent(message -> player.sendMessage(message));

            World.removeObject(roots);
            roots.setId(roots.getId() + 1); // Set roots to depleted object variant.
            World.spawnObject(roots);

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

    private boolean hasAxe() {
        if (player.getEquipment().containsItem(CRYSTAL_AXE_23862) || player.getInventory().containsItem(CRYSTAL_AXE_23862)) {
            return true;
        }

        return player.getEquipment().containsItem(CORRUPTED_AXE) || player.getInventory().containsItem(CORRUPTED_AXE);
    }

}
