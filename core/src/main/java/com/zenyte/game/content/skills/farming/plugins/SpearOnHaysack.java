package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 22/05/2019 00:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpearOnHaysack implements PairedItemOnItemPlugin {
    @Override
    public ItemPair[] getMatchingPairs() {
        final ObjectArrayList<ItemOnItemAction.ItemPair> pairs = new ObjectArrayList<ItemPair>();
        for (final int spear : spears) {
            pairs.add(ItemPair.of(6057, spear));
        }
        return pairs.toArray(new ItemPair[0]);
    }

    private static final int[] spears = new int[] {1237, 1239, 1241, 1243, 1245, 1247, 1249, 1251, 1253, 1255, 1257, 1259, 1261, 1263, 4158, 4159, 4580, 4582, 4726, 4910, 4911, 4912, 4913, 5016, 5704, 5706, 5708, 5710, 5712, 5714, 5716, 5718, 5720, 5722, 5724, 5726, 5728, 5730, 5734, 5736, 7809, 11824, 20158, 20397, 22610};
    private static final int[] permittedSpears = new int[] {1237, 1251, 5704, 5718};

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item hay = from.getId() == 6057 ? from : to;
        final Item spear = hay == from ? to : from;
        if (!ArrayUtils.contains(permittedSpears, spear.getId())) {
            player.getDialogueManager().start(new ItemChat(player, spear, "Waste this spear on a scarecrow? I think not - maybe I should find a bronze spear instead."));
            return;
        }
        player.getInventory().deleteItem(spear);
        player.getInventory().deleteItem(hay);
        player.getInventory().addOrDrop(new Item(6058));
        player.sendMessage("You drive the spear through the hay.");
    }
}
