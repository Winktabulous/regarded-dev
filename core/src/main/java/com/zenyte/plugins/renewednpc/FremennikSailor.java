package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/04/2019 02:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FremennikSailor extends NPCPlugin {
    private static final Location RELLEKKA_LOCATION = new Location(2629, 3692, 0);
    private static final Location MISCELLANIA_LOCATION = new Location(2581, 3845, 0);

    private void sail(final Player player, final boolean toMiscellania) {
        new FadeScreen(player, () -> player.setLocation(toMiscellania ? MISCELLANIA_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (toMiscellania ? "Miscellania..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                final boolean toMiscellania = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (toMiscellania ? "Miscellania?" : "Rellekka?"));
                options("Sail to " + (toMiscellania ? "Miscellania?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.")
                        .onOptionOne(() -> sail(player, toMiscellania)).onOptionTwo(this::finish);
            }
        }));
        bind("Travel", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));

    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                3936, 3680
        };
    }
}
