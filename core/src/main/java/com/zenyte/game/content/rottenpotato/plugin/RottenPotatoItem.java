package com.zenyte.game.content.rottenpotato.plugin;

import com.zenyte.game.content.rottenpotato.RottenPotatoAction;
import com.zenyte.game.content.rottenpotato.RottenPotatoDialogue;
import com.zenyte.game.content.rottenpotato.RottenPotatoItemOption;
import com.zenyte.game.content.rottenpotato.handler.RottenPotatoActionHandler;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.List;
import java.util.Optional;

/**
 * @author Christopher
 * @since 3/23/2020
 */
public class RottenPotatoItem extends ItemPlugin {
    @Override
    public void handle() {
        for (RottenPotatoItemOption itemOption : RottenPotatoAction.itemOptionMap.keys()) {
            if (itemOption == RottenPotatoItemOption.NONE) {
                continue;
            }
            bind(itemOption.getItemOption(), ((player, item, slotId) -> {
                if (!player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT)) {
                    player.getInventory().deleteItem(item);
                    return;
                }
                final String title = itemOption.getDialogueTitle();
                final List<RottenPotatoActionHandler> actions = RottenPotatoAction.getActions(player, itemOption);
                player.getDialogueManager().start(new RottenPotatoDialogue(player, title, Optional.empty(), actions));
            }));
        }
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.ROTTEN_POTATO};
    }
}
