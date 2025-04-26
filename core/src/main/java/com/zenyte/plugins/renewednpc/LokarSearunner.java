package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/04/2019 02:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LokarSearunner extends NPCPlugin {

    private static final Location RELLEKKA_LOCATION = new Location(2621, 3687, 0);

    private static final Location PIRATES_COVE_LOCATION = new Location(2213, 3794, 0);

    private void sail(final Player player, final boolean piratesCove) {
        new FadeScreen(player, () -> player.setLocation(piratesCove ? PIRATES_COVE_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (piratesCove ? "Pirates' Cove..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final boolean piratesCove = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (piratesCove ? "Pirates' Cove?" : "Rellekka?"));
                options("Sail to " + (piratesCove ? "Pirates' Cove?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.").onOptionOne(() -> sail(player, piratesCove)).onOptionTwo(this::finish);
            }
        }));
        bind("Travel", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 3855, 3854 };
    }
}
