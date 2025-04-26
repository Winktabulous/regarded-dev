package com.zenyte.game.content.minigame.inferno.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 28/12/2019 | 23:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class FireCapeOnTzhaarKet implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (player.getNumericAttribute("infernoVar").intValue() >= 1) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You have already shown to me that you're worthy on entering Mor Ul Rek, JalYt."));
            return;
        }
        player.addAttribute("infernoVar", 1);
        player.getDialogueManager().start(new ItemChat(player, item, "You hold out your fire cape and show it to TzHaar-Ket. TzHaar-Ket nods, and allows you to pass through."));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.FIRE_CAPE, ItemId.FIRE_MAX_CAPE_21186, ItemId.FIRE_MAX_CAPE, ItemId.FIRE_MAX_CAPE_BROKEN };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LOOSE_RAILING_2186, ObjectId.STAIRS_2187 };
    }
}
