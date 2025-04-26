package com.zenyte.game.content.kebos.konar.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 24/10/2019 | 23:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DevoutBootsCreation implements ItemOnItemAction {

    private static final Item holySandals = new Item(ItemId.HOLY_SANDALS);
    private static final Item drakesTooth = new Item(ItemId.DRAKES_TOOTH);
    private static final Item devoutBoots = new Item(ItemId.DEVOUT_BOOTS);

    private static final Animation anim = new Animation(4462);
    private static final Graphics gfx = new Graphics(759);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(holySandals, drakesTooth, "Are you sure you wish to alchemically combine the Holy sandals and Drake's tooth to create Devout boots? This cannot be reversed.");
                options(TITLE, "Proceed with the alchemical combination.", "Cancel.")
                        .onOptionOne(() -> {
                            player.getInventory().deleteItemsIfContains(new Item[]{holySandals, drakesTooth}, () -> {
                                player.setAnimation(anim);
                                player.setGraphics(gfx);
                                player.getInventory().addItem(devoutBoots);
                            });
                            setKey(5);
                        });
                item(5, devoutBoots, "You successfully combine the Holy sandals and Drake's tooth to create Devout boots.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{holySandals.getId(), drakesTooth.getId()};
    }
}
