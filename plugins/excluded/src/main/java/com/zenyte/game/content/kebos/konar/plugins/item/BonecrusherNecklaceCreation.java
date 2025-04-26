package com.zenyte.game.content.kebos.konar.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import static com.zenyte.game.content.kebos.konar.plugins.item.BonecrusherNecklace.*;

/**
 * @author Tommeh | 26/10/2019 | 16:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BonecrusherNecklaceCreation implements ItemOnItemAction {


    private static final Animation ANIM = new Animation(4462);
    private static final Graphics GFX = new Graphics(759);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (!player.getInventory().containsItems(bonecrusher, dragonBoneNecklace, hydraTail)) {
            player.sendMessage("You do not have all the required items to create a Bonecrusher necklace.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(dragonBoneNecklace, hydraTail, "Are you sure you wish to alchemically combine the Bonecrusher, Dragonbone necklace and Hydra tail to create a Bonecrusher necklace?");
                options(TITLE, "Proceed with the alchemical combination.", "Cancel.")
                        .onOptionOne(() -> {
                            player.getInventory().deleteItemsIfContains(new Item[]{bonecrusher, dragonBoneNecklace, hydraTail}, () -> {
                                player.setAnimation(ANIM);
                                player.setGraphics(GFX);
                                player.getInventory().addItem(bonecrusherNecklace);
                            });
                            setKey(5);
                        });
                item(5, bonecrusherNecklace, "You successfully combine the Bonecrusher, Dragonbone necklace and Hydra tail to create a Bonecrusher necklace.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.BONECRUSHER, ItemId.DRAGONBONE_NECKLACE, ItemId.HYDRA_TAIL};
    }
}
