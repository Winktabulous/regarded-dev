package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 03/09/2019 23:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BonesTablet extends ItemPlugin {
    public static final int BONES = 1963;
    public static final int PEACHES = 6883;
    public static final Animation animation = new Animation(722);
    public static final Graphics graphics = new Graphics(141, 0, 92);
    public static final SoundEffect sound = new SoundEffect(114);

    /**
     * Iterates all the items in players' inventory, and turns any unnoted bones w/ bury option into the requested transformed item id.
     * @param player the player whose inventory to iterate.
     * @param transformedItem the item to which the bones are transformed into.
     * @return the amount of bones that were able to be transformed.
     */
    public static final int convertBones(@NotNull final Player player, final int transformedItem) {
        int amount = 0;
        for (final Item item : player.getInventory().getContainer().getItems().values()) {
            if (item == null) {
                continue;
            }
            final ItemDefinitions definitions = item.getDefinitions();
            if (definitions.isNoted() || !definitions.getName().toLowerCase().contains("bone") || !definitions.containsOption("Bury")) {
                continue;
            }
            item.setId(transformedItem);
            amount++;
        }
        return amount;
    }

    @Override
    public void handle() {
        bind("Break", (player, tablet, container, slotId) -> {
            final int amount = convertBones(player, tablet.getId() == 8014 ? BONES : PEACHES);
            if (amount > 0) {
                player.getInventory().deleteItem(new Item(tablet.getId(), 1));
                player.setAnimation(animation);
                player.setGraphics(graphics);
                player.sendSound(sound);
                player.getInventory().refreshAll();
                final String typeString = tablet.getId() == 8014 ? "bananas" : "peaches";
                player.sendFilteredMessage("You convert " + amount + " x bones to " + typeString + ".");
            } else {
                player.sendMessage("You aren't holding any bones!");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {8014, 8015};
    }
}
