package com.zenyte.plugins.item;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.plugins.item.capes.NewMaxCapes;

/**
 * @author Kris | 16/03/2019 02:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FletchingCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Search", (player, item, slotId) -> {
            final int searches = player.getVariables().getGrappleAndCrossbowSearches();
            if (searches >= NewMaxCapes.MAX_GRAPPLE_SEARCHES) {
                player.sendMessage("You may only receive a grapple and crossbow three times per day. Try again tomorrow.");
                return;
            }
            if (player.getInventory().containsItem(9174, 1) && player.getInventory().containsItem(9419, 1)) {
                player.sendMessage("You already have a crossbow and a mith grapple with you.");
                return;
            }
            player.getVariables().setGrappleAndCrossbowSearches(searches + 1);
            player.getInventory().addOrDrop(new Item(9174));
            player.getInventory().addOrDrop(new Item(9419));
            player.sendMessage("You search the cape and find a " + Colour.RS_RED.wrap("Bronze crossbow") + " and " + Colour.RS_RED.wrap("Mithril grapple."));
            final int remaining = NewMaxCapes.MAX_GRAPPLE_SEARCHES - (searches + 1);
            if (remaining >= 1) {
                final String remainingString = remaining == 2 ? "twice more today" : "one more time today";
                player.sendMessage("You may search the cape " + remainingString);
            } else {
                player.sendMessage("You search the cape for the final time today.");
            }
            player.setAnimation(new Animation(1376));
        });
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.FLETCHING.getSkillCapes();
    }
}
