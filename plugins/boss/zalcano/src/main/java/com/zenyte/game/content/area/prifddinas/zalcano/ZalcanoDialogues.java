package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.content.area.prifddinas.zalcano.plugins.ZalcanoObjectActions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class ZalcanoDialogues {

    public static Dialogue barrier(Player player) {
        return new Dialogue(player) {
            @Override
            public void buildDialogue() {

                plain("Zalcano's Prison is a dangerous environment and dying within will not be " +
                        "considered a safe death. Are you sure you wish to enter?", 35);

                options("Are you sure you wish to enter?", "Yes.", "Yes, and don't remind me again.", "No.")
                        .onOptionOne(() -> ZalcanoObjectActions.walkThroughBarrier(player))
                        .onOptionTwo(() -> {
                            player.addAttribute(ZalcanoConstants.WARNING_NAME, 1);
                            ZalcanoObjectActions.walkThroughBarrier(player);
                        });

            }
        };
    }

}
