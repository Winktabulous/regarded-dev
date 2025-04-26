package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/04/2019 01:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MariaGunnars extends NPCPlugin {

    private static final Location RELLEKKA_LOCATION = new Location(2645, 3710, 0);

    private static final Location NEITZNOT_LOCATION = new Location(2311, 3782, 0);

    private void sail(final Player player, final boolean toNeitiznot) {
        new FadeScreen(player, () -> player.setLocation(toNeitiznot ? NEITZNOT_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (toNeitiznot ? "Neitiznot Islands..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final boolean toNeitiznot = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (toNeitiznot ? "Neitiznot Islands?" : "Rellekka?"));
                options("Sail to " + (toNeitiznot ? "Neitiznot Islands?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.").onOptionOne(() -> sail(player, toNeitiznot)).onOptionTwo(this::finish);
            }
        }));
        bind("Neitiznot", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
        bind("Rellekka", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MARIA_GUNNARS_1883, NpcId.MARIA_GUNNARS };
    }
}
