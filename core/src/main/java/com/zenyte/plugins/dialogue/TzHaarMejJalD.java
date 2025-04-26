package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.area.tzhaar.TzHaar;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class TzHaarMejJalD extends Dialogue {
    private final boolean exchange;

    public TzHaarMejJalD(final Player player, final NPC npc, final boolean exchange) {
        super(player, npc);
        this.exchange = exchange;
    }

    @Override
    public void buildDialogue() {
        if (exchange) {
            if (!player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                player.sendMessage(Colour.RS_RED.wrap("You have no firecape to exchange!"));
                return;
            }
            if (player.containsItem(13225) || player.getFollower() != null && player.getFollower().getPet() == BossPet.TZREK_JAD) {
                npc("You seem to already have a Tzrek-Jad, I'm not giving you more than one!");
                return;
            }
            doubleItem(TzHaar.FIRE_CAPE_ITEM, TzHaar.TZREK_JAD_ITEM, "Are you sure you want to exchange a firecape? You have a <col=006000>1/200</col> chance of receiving a Tzrek-Jad.");
            options("Would you like to trade your firecape?", "Trade my firecape for 1,500 tokkul", "Gamble my firecape for a pet <col=600000>(1/200)</col>", "Nevermind").onOptionOne(() -> {
                if (!player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                    player.sendMessage(Colour.RS_RED.wrap("You have no firecape to exchange!"));
                } else {
                    if (!player.getInventory().hasFreeSlots()) {
                        player.sendMessage(Colour.RS_RED.wrap("You need to make space in your inventory!"));
                    }
                    player.getInventory().deleteItem(TzHaar.FIRE_CAPE_ITEM);
                    player.getInventory().addItem(6529, 1500);
                    player.sendMessage(Colour.RS_GREEN.wrap("You have sold your firecape to TzHaar-Mej-Jal for 1,500 tokkul!"));
                }
            }).onOptionTwo(() -> gambleFirecape());
        } else {
            npc("I am the guardian of these Fight caves, can I help you JalYt?");
            options("Select an option", "How many waves are the Fight caves?", "Can I sell you a firecape?", "Nevermind").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(20));
            player(10, "How many waves are the Fight caves?");
            npc(11, "The fight caves are 2 waves, starting at wave 62.");
            player(20, "Can I sell you a firecape?");
            if (player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                npc(21, "You can sell me your firecape for 1,500 tokkul, or use it on me / exchange to gamble it for a Tzrek-Jad pet.");
            } else {
                npc(21, "You could, but you don't have a firecape to sell me!");
                setKey(99);
            }
            options("Would you like to trade your firecape?", "Trade my firecape for 1,500 tokkul", "Gamble my firecape for a pet <col=600000>(1/200)</col>", "Nevermind").onOptionOne(() -> {
                if (!player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                    player.sendMessage(Colour.RS_RED.wrap("You have no firecape to exchange!"));
                } else {
                    if (!player.getInventory().hasFreeSlots()) {
                        player.sendMessage(Colour.RS_RED.wrap("You need to make space in your inventory!"));
                    }
                    player.getInventory().deleteItem(TzHaar.FIRE_CAPE_ITEM);
                    player.getInventory().addItem(6529, 1500);
                    player.sendMessage(Colour.RS_GREEN.wrap("You have sold your firecape to TzHaar-Mej-Jal for 1,500 tokkul!"));
                }
            }).onOptionTwo(() -> gambleFirecape());
        }
    }

    public void gambleFirecape() {
        if (!player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
            player.sendMessage(Colour.RS_RED.wrap("You have no firecape to exchange!"));
            return;
        }
        final int roll = Utils.random(1, 200);
        player.getInventory().deleteItem(TzHaar.FIRE_CAPE_ITEM);
        if (roll == 1) {
            player.getInventory().addItem(TzHaar.TZREK_JAD_ITEM);
            player.getDialogueManager().start(new NPCChat(player, 2180, "You lucky. Better train him good else TzTok-Jad find you, JalYt."));
            WorldBroadcasts.broadcast(player, BroadcastType.GAMBLE_FIRECAPE);
        } else {
            player.getDialogueManager().start(new NPCChat(player, 2180, "You not lucky. Maybe next time, JalYt."));
        }
    }
}
