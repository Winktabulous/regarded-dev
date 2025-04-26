package com.zenyte.plugins.item.sceptre;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-08-29
 */
public class AncientSceptre {

    protected Dialogue getScepterCombiningDialogue(Player player, Item gem, Item scepter, int finalSceptre) {
        final var sceptreToReward = new Item(finalSceptre);
        final var message = String.format("You're about to combine the %s and Ancient sceptre to create an %s.", gem.getName(), sceptreToReward.getName());
        return new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("<col=FF0040>Warning!</col><br>".concat(message));
                options("Are you sure you wish to do this?", "Yes.", "No.")
                    .onOptionOne(() -> {
                        if (player.getInventory().deleteItems(gem, scepter).getResult() == RequestResult.SUCCESS)
                            player.getInventory().addItem(sceptreToReward);
                    });
            }
        };
    }

}
