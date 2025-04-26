package com.zenyte.game.content.kebos.konar.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 02/11/2019 | 16:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BrimstoneRingCreation implements ItemOnItemAction {

    private static final Item[] parts = {
            new Item(ItemId.HYDRAS_HEART), new Item(ItemId.HYDRAS_EYE), new Item(ItemId.HYDRAS_FANG)
    };
    private static final Item brimstoneRing = new Item(ItemId.BRIMSTONE_RING);

    private static final Animation anim = new Animation(4462);
    private static final Graphics gfx = new Graphics(759);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (!player.getInventory().containsItems(parts)) {
            player.sendMessage("You do not have all the required parts to make a Brimstone ring.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(parts[0], parts[1], "Are you sure you wish to alchemically combine the Hydra's heart, eye and fang to create a Brimstone ring? This cannot be reversed.");
                options(TITLE, "Proceed with the alchemical combination.", "Cancel.")
                        .onOptionOne(() -> {
                            player.getInventory().deleteItemsIfContains(parts, () -> {
                                player.setAnimation(anim);
                                player.setGraphics(gfx);
                                player.getInventory().addItem(brimstoneRing);
                            });
                            setKey(5);
                        });
                item(5, brimstoneRing, "You successfully combine the Hydra's heart, eye and fang to create a Brimstone ring.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { ItemId.HYDRAS_HEART, ItemId.HYDRAS_EYE, ItemId.HYDRAS_FANG };
    }
}
