package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 06/07/2019 03:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CreatureKeeperObject implements ObjectAction {

    private static final Item JOURNAL = new Item(20886);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some more free space to search the body.");
                return;
            }
            if (player.getInventory().containsItem(JOURNAL)) {
                player.getDialogueManager().start(new PlainChat(player, "You already have a journal."));
                return;
            }
            player.getDialogueManager().start(new ItemChat(player, JOURNAL, "You find the creature keeper's journal."));
            player.getInventory().addItem(JOURNAL);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CREATURE_KEEPER };
    }
}
