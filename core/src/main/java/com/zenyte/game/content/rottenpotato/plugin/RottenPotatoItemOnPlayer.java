package com.zenyte.game.content.rottenpotato.plugin;

import com.zenyte.game.content.rottenpotato.RottenPotatoAction;
import com.zenyte.game.content.rottenpotato.RottenPotatoActionType;
import com.zenyte.game.content.rottenpotato.RottenPotatoDialogue;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnPlayerPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.Optional;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class RottenPotatoItemOnPlayer implements ItemOnPlayerPlugin {
    @Override
    public void handleItemOnPlayerAction(Player player, Item item, int slot, Player target) {
        if (!player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)) {
            player.getInventory().deleteItem(item);
            return;
        }
        player.getDialogueManager().start(new RottenPotatoDialogue(player, "Using on " + target.getName(),
                Optional.of(target),
                RottenPotatoAction.getActions(player, RottenPotatoActionType.ITEM_ON_PLAYER)));
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.ROTTEN_POTATO};
    }
}
