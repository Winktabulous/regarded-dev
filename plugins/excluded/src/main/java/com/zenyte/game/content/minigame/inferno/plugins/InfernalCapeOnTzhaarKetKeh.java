package com.zenyte.game.content.minigame.inferno.plugins;

import com.zenyte.game.content.area.tzhaar.TzHaar;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 17/12/2019 | 18:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class InfernalCapeOnTzhaarKetKeh implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if(!player.getInventory().containsItem(ItemId.INFERNAL_CAPE, 1)) {
            player.sendMessage(Colour.RS_RED.wrap("You have don't have an Infernal cape to gamble!"));
            return;
        }
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (BossPet.JAL_NIB_REK.hasPet(player)) {
                    npc("You seem to already have a Jal-Nib-Rek, I'm not giving you more than one!");
                    return;
                }
                player("I have a spare infenal cape, perhaps I could offer it<br><br>back to you for a littlun to take with me?");
                npc("Maybe. Hand it over and if you make lucky TokKul<br><br>toss, it can be yours JalYt.");
                npc("But do know, you do not get cape back.");
                options("Sacrifice infernal cape for a chance at Jal-Nib-Rek?", "Yes, I accept that I won't get my cape back.", "No, I want to keep my cape.")
                        .onOptionOne(() -> {
                            if(!player.getInventory().containsItem(Inferno.infernalCape)) {
                                player.sendMessage(Colour.RS_RED.wrap("You have don't have an Infernal cape to gamble!"));
                                return;
                            }
                            player.getInventory().deleteItem(Inferno.infernalCape);
                            if (Utils.random(1, 100) == 1) {
                                BossPet.JAL_NIB_REK.roll(player, 0);
                                setKey(5);
                            } else {
                                setKey(10);
                            }
                        })
                        .onOptionTwo(() -> setKey(15));
                npc(5, "Congratulations, JalYt! Here's your own Jal-Nib-Rek.");
                npc(10, "No Jal-Nib-Rek for you.");
                player(15, "No, I want to keep my cape.");
                npc("Very well.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.INFERNAL_CAPE};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {TzHaar.TZHAAR_KET_KEH};
    }
}
