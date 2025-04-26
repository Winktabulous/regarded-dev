package com.zenyte.game.content.minigame.inferno.plugins;

import com.zenyte.game.content.area.tzhaar.TzHaar;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.entity.player.var.VarCollection;

/**
 * @author Tommeh | 29/12/2019 | 12:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class FireCapeOnTzhaarKetKeh implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (player.getNumericAttribute("infernoVar").intValue() == 2) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You have already sacrificed your Fire cape, JalYt."));
            return;
        }
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                player("I'd like to sacrifice my fire cape in order to enter the Inferno.");
                if (player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                    npc("Very well, just hand it over.");
                    options("<col=ff0000>Really sacrifice your Fire cape?", new DialogueOption("Yes, hand it over.", key(10)),
                            new DialogueOption("No, I want to keep it!")
                    );
                    item(10, TzHaar.FIRE_CAPE_ITEM, "You hand your cape to TzHaar-Ket-Keh.").executeAction(() -> {
                        if (player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                            player.getInventory().deleteItem(TzHaar.FIRE_CAPE_ITEM);
                            player.addAttribute("infernoVar", 2);
                            VarCollection.TZHAAR_UNLOCKS.updateSingle(player);
                        }
                    });
                    npc("Give it your best shot JalYt-Mej-Xo-" + player.getName() + ".");
                } else {
                    npc("Sorry JalYt, I don't think you do have a cape on you.");
                }
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.FIRE_CAPE};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { TzHaar.TZHAAR_KET_KEH };
    }
}
