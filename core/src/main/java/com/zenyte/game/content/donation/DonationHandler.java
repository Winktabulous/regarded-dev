package com.zenyte.game.content.donation;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;

import java.util.List;

public class DonationHandler {
    private static final String NONE = Colour.BRICK.wrap("There are currently no donations for you to claim!");

    public static void claim(final Player player, final List<Item> rewards) {
        WorldTasksManager.schedule(() -> {
            if (player == null) return;
            if (rewards == null || rewards.isEmpty()) {
                player.getDialogueManager().finish();
                player.sendMessage(NONE);
                return;
            }
            // todo, total amount donated
            for (final Item reward : rewards) {
                if (reward == null) continue;
                player.getInventory().addItem(reward).onFailure(remainder -> {
                    player.sendMessage("There was no room in your inventory, so " + Colour.RED.wrap(remainder.getAmount() + "x " + remainder.getName()) + " have been added to your bank!");
                    player.getBank().add(remainder).onFailure(remainder2 -> {
                        player.sendMessage("There was no room in your bank, so " + Colour.RED.wrap(remainder2.getAmount() + "x " + remainder2.getName()) + " have been dropped to the floor!");
                        World.spawnFloorItem(remainder2, player);
                    });
                });
                player.sendMessage("You have successfully claimed " + Colour.GREEN.wrap(reward.getAmount() + "x " + reward.getName()) + "!");
            }
        });
    }
}
