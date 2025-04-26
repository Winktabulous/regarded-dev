package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.DoubleItemChat;

/**
 * @author Kris | 10/05/2019 15:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OilLampOnLanternFrame implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
            if (player.getSkills().getLevel(SkillConstants.CRAFTING) < 26) {
                player.getDialogueManager().start(new DoubleItemChat(player, from, to, "You need a Crafting level of at least 26 to put the oil lamp in the lantern frame."));
                return;
            }
            player.getInventory().deleteItem(from);
            player.getInventory().deleteItem(to);
            player.getInventory().addOrDrop(new Item(4535));
            player.sendMessage("You put the oil lamp inside the lantern frame.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(4540, 4522)
        };
    }
}
