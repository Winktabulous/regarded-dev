package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.RespawnPoint;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 7. nov 2017 : 0:21.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 */
public final class KrystiliaD extends Dialogue {

    public KrystiliaD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        npc("Yeah? What do you want?");
        options(TITLE, "I need another assignment.", "Have you any rewards for me, or anything to trade?",
                "Let's talk about the difficulty of my tasks.", "What can you do apart from Slayer Master stuff?",
                "Cancel my Task (150k GP)").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(20))
                .onOptionThree(() -> setKey(30)).onOptionFour(() -> setKey(40)).onOptionFive(() -> setKey(999));
        player(5, "I'd like another assignment.").executeAction(() -> {
            finish();
            player.getDialogueManager().start(new KrystiliaAssignmentD(player, npc));
        });
        player(20, "Have you any rewards for me, or anything to trade?");
        npc("I have quite a few rewards you can earn, and a wide variety of Slayer equipment for sale.");
        options(TITLE, "Look at rewards.", "Look at shop.", "Cancel.").onOptionOne(() -> {
            finish();
            player.getSlayer().openInterface();
        }).onOptionTwo(() -> player.openShop("Slayer Equipment"))
                .onOptionThree(this::finish);
        player(30, "Let's talk about the difficulty of my assignments.");
        npc("Other Slayer masters take your combat level into account when choosing tasks for you, so they won't "
                + "set you anything too hard. But I don't do that - if you get a task from me, it could be anything "
                + "for which you");
        npc("have the Slayer level, provided you can physically get to it.");
        player(40, "What can you do apart from Slayer Master stuff?");
        npc("Quite a lot! Those stupid guards think they've trapped me here for causing trouble, but I can get back "
                + "out to the Wilderness for some fun anytime I like.");
        npc("Hey, perhaps you'd like me to fix it so that you respawn in Edgeville whenever you die. "
                + "If you love the Wilderness as much as I do, you might enjoy that.");
        options(TITLE, "Tell me more.", "That doesn't interest me.").onOptionOne(() -> setKey(60))
                .onOptionTwo(this::finish);
        player(50, "Er... Nothing...");
        npc(60, "Alright, here's the deal: Whenever you drop dead, I'll try to make you reappear in Edgeville, "
                + "down by the southern ruins. You'll lose your items as normal.");
        npc("My magic won't affect you if you die in some fancy place like Castle Wars or the Duel arena; "
                + "those places have their own magic trying to make you 'safe'.");
        if (!player.getRespawnPoint().equals(RespawnPoint.LUMBRIDGE)) {
            npc("Would you like to set your respawn point back to Lumbridge?");
            options(TITLE, "Yes.", "No.").onOptionOne(() -> setKey(250)).onOptionTwo(this::finish);
            player(250, "Yes, please.").executeAction(() -> player.setRespawnPoint(RespawnPoint.LUMBRIDGE));
            npc("Done, come back soon!");
        } else {
            if (!player.getBooleanAttribute(Setting.EDGEVILLE_RESPAWN_POINT.toString()))
                npc("If you get your respawn changed later, come back to me and I'll set it back to Lumbridge.");
            options("What would you like to say?", "Okay, switch my respawn to Edgeville.", "I'm not interested")
                    .onOptionOne(() -> setKey(200));
            player(200, "Okay, switch my respawn to Edgeville.").executeAction(() -> {
                if (!player.getBooleanAttribute(Setting.EDGEVILLE_RESPAWN_POINT.toString())) {
                    player.setRespawnPoint(RespawnPoint.EDGEVILLE);
                    player.getSettings().toggleSetting(Setting.EDGEVILLE_RESPAWN_POINT);
                    setKey(300);
                } else {
                    player.setRespawnPoint(RespawnPoint.EDGEVILLE);
                    setKey(325);
                }
            });
            npc(300, "Done.");
        }
        player(999, "Cancel my Task (150k GP)").executeAction(() -> {
            finish();
            if(!player.getInventory().containsItem(995, 150_000)
                    || player.getInventory().deleteItem(995, 150_000).isFailure()) {
                player.sendMessage("You do not have the required funds to do that.");
            } else {
                player.getSlayer().removeTask();
            }
        });
    }
}
