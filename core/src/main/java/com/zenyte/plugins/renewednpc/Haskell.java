package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author William | 31/07/2022 05:39
 */
public class Haskell extends NPCPlugin {

    private static final Location RELLEKKA_LOCATION = new Location(2471, 3995, 0);

    private static final Location ISLE_OF_STONE_LOCATION = new Location(2213, 3794, 0);

    private void sail(final Player player, final boolean isleStone) {
        new FadeScreen(player, () -> player.setLocation(isleStone ? ISLE_OF_STONE_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (isleStone ? "The Island of Stone..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final boolean isleStone = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (isleStone ? "Island of Stone?" : "Rellekka?"));
                options("Sail to " + (isleStone ? "Island of Stone?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.").onOptionOne(() -> sail(player, isleStone)).onOptionTwo(this::finish);
            }
        }));
        bind("Travel", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 9271, 9272 };
    }
}
