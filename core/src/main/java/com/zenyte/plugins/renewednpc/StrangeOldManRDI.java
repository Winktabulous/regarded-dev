package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class StrangeOldManRDI extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, 16058) {
            @Override
            public void buildDialogue() {
                npc("Which crypt would you like to visit?");
                options(1, "Which crypt do you want to visit?",
                        "Dharok", "Ahrim", "Torag", "Guthan", "Next")
                        .onOptionOne(() -> option(0, player))
                        .onOptionTwo(() -> option(1, player))
                        .onOptionThree(() -> option(2, player))
                        .onOptionFour(() -> option(3, player))
                        .onOptionFive(() -> setKey(2));
                options(2, "Which crypt do you want to visit?", "Karil", "Verac", "Previous")
                        .onOptionOne(() -> option(4, player))
                        .onOptionTwo(() -> option(5, player))
                        .onOptionThree(() -> setKey(1));
            }
        }));
    }

    public void option(int option, Player player) {
        player.setLocation(new Location(2851, 7077 + (option * 64), 3));

    }

    @Override
    public int[] getNPCs() {
        return new int[] {16058};
    }
}
