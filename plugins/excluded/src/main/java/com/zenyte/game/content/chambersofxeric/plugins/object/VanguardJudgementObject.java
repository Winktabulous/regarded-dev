package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 06/07/2019 03:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class VanguardJudgementObject implements ObjectAction {

    private static final Item VANGUARD_JUDGEMENT = new Item(20895);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> {
            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need some more free space to pick this up.");
                return;
            }
            if (player.getInventory().containsItem(VANGUARD_JUDGEMENT)) {
                player.getDialogueManager().start(new PlainChat(player, "You already have a journal."));
                return;
            }
            player.getDialogueManager().start(new ItemChat(player, VANGUARD_JUDGEMENT, "You take the judgement of the vanguard."));
            player.getInventory().addItem(VANGUARD_JUDGEMENT);
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.VANGUARD_JUDGEMENT };
    }
}
