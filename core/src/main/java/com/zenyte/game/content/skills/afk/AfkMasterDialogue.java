package com.zenyte.game.content.skills.afk;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class AfkMasterDialogue extends Dialogue {

    public AfkMasterDialogue(Player player) {
        super(player, AfkSkillingConstants.AFK_MASTER);
    }
    @Override
    public void buildDialogue() {
        npc("Hello, "+player.getUsername()+". How may I help you?");
        options("Select an Option", "What is this area?", "What are the rewards?", "Never mind...")
                .onOptionOne(() -> setKey(10))
                .onOptionTwo(() -> setKey(20))
                .onOptionThree(() -> setKey(30));
        player(10, "What is this area?");
        npc("This is an ultra-exclusive AFK Skilling Guild. You can train almost every skill here without lifting a finger! It's a bit slower than usual, but it's not bad!");
        npc("Many of our most loyal members spend their time here in-game while they're off to dream land in real life.");

        player(20, "What are the rewards?");
        npc("I'm glad you asked - when you skill here you receive AFK points, roughly 1,000 an hour. You can spend these tokens in my shop.");
        npc("All items in the shop are untradeable and always-kept-on-death. They're all cosmetic and fit most players nicely.");
        player("Very cool.");

        player(30, "Never mind....");

    }
}
