package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/04/2019 01:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MordGunnars extends NPCPlugin {

    private static final Location RELLEKKA_LOCATION = new Location(2645, 3710, 0);

    private static final Location JATISZO_LOCATION = new Location(2421, 3782, 0);

    private void sail(final Player player, final boolean toJatizso) {
        new FadeScreen(player, () -> player.setLocation(toJatizso ? JATISZO_LOCATION : RELLEKKA_LOCATION)).fade(2);
        player.sendMessage("You travel to " + (toJatizso ? "Jatiszo..." : "Rellekka..."));
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final boolean toJatizso = player.getLocation().withinDistance(RELLEKKA_LOCATION, 20);
                npc("Good day, " + player.getPlayerInformation().getDisplayname() + ". Would you like to sail to " + (toJatizso ? "Jatizso?" : "Rellekka?"));
                options("Sail to " + (toJatizso ? "Jatizso?" : "Rellekka?"), "Yes, take me there.", "No, I'd rather not.").onOptionOne(() -> sail(player, toJatizso)).onOptionTwo(this::finish);
            }
        }));
        bind("Jatizso" + "", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
        bind("Rellekka", (player, npc) -> sail(player, player.getLocation().withinDistance(RELLEKKA_LOCATION, 20)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MORD_GUNNARS, NpcId.MORD_GUNNARS_1940 };
    }
}
