package com.near_reality.game.content.gauntlet.actions;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.GauntletConstants;
import com.near_reality.game.content.gauntlet.objects.FishingSpot;
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
public final class FishingSpotAction extends Action {

    private static final Animation ANIMATION = new Animation(8336);

    private static final Animation ANIMATION_CORRUPTED = new Animation(8337);

    private static final String STOP_MESSAGE = "You can't carry any more fish.";

    private final FishingSpot spot;

    private final boolean corrupted;

    private int ticks;

    public FishingSpotAction(FishingSpot spot, boolean corrupted) {
        this.spot = spot;
        this.corrupted = corrupted;
    }

    @Override
    public boolean start() {
        if (!hasHarpoon()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a harpoon to catch these fish."));
            return false;
        }

        if (!player.getInventory().hasFreeSlots()) {
            player.getDialogueManager().start(new PlainChat(player, STOP_MESSAGE));
            return false;
        }

        player.sendMessage("You start harpooning fish.");
        delay(2);

        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().hasFreeSlots()) {
            player.getDialogueManager().start(new PlainChat(player, STOP_MESSAGE));
            return false;
        }

        if (ticks++ % 4 == 0) {
            player.setAnimation(corrupted ? ANIMATION_CORRUPTED : ANIMATION);
        }

        return true;
    }

    @Override
    public int processWithDelay() {
        player.getInventory().addItem(new Item(spot.getResource()));
        player.sendMessage("You manage to catch a fish.");

        player.getSkills().addXp(SkillConstants.FISHING, 1);

        Gauntlet.rollShards(player, corrupted);

        spot.deplete();

        if (spot.isDepleted()) {
            spot.getDepletionMessage().ifPresent(message -> player.sendMessage(message));

            World.removeObject(spot);
            spot.setId(spot.getId() + 1); // Set fishing spot to depleted object variant.
            World.spawnObject(spot);

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

    private boolean hasHarpoon() {
        if (player.getEquipment().containsItem(CRYSTAL_HARPOON_23864) || player.getInventory().containsItem(CRYSTAL_HARPOON_23864)) {
            return true;
        }

        return player.getEquipment().containsItem(CORRUPTED_HARPOON) || player.getInventory().containsItem(CORRUPTED_HARPOON);
    }

}
