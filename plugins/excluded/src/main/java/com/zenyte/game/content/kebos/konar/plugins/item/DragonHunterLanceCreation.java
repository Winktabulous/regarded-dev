package com.zenyte.game.content.kebos.konar.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 24/10/2019 | 23:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DragonHunterLanceCreation implements ItemOnItemAction {

    private static final Item zamorakianHasta = new Item(ItemId.ZAMORAKIAN_HASTA);
    private static final Item hydraClaw = new Item(ItemId.HYDRAS_CLAW);
    private static final Item dragonHunterLance = new Item(ItemId.DRAGON_HUNTER_LANCE);

    private static final Animation anim = new Animation(4462);
    private static final Graphics gfx = new Graphics(759);

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(zamorakianHasta, hydraClaw, "Are you sure you wish to combine the Hydra claw and the Zamorakian hasta to create the Dragon hunter lance? This cannot be reversed.");
                options(TITLE, "Proceed with the alchemical combination.", "Cancel.")
                        .onOptionOne(() -> {
                            player.getInventory().deleteItemsIfContains(new Item[]{zamorakianHasta, hydraClaw}, () -> {
                                player.setAnimation(anim);
                                player.setGraphics(gfx);
                                player.getInventory().addItem(dragonHunterLance);
                            });
                            setKey(5);
                        });
                item(5, dragonHunterLance, "You successfully combine the Hydra claw and the Zamorakian hasta to create the Dragon hunter lance.");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{zamorakianHasta.getId(), hydraClaw.getId()};
    }
}