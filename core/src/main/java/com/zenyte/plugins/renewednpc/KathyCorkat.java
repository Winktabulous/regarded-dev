package com.zenyte.plugins.renewednpc;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 26/11/2018 18:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KathyCorkat extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                if (npc.getId() == 6207) {
                    npc("Hello dearie. Heading up north to the Fishing Colony, are we?");
                    options("What would you like to say?", new DialogueOption("Yes please.", key(20)), new DialogueOption("No thanks.", key(30)));
                } else {
                    npc("Are ye wantin' a lift up the river, dearie?");
                    options("What would you like to say?", new DialogueOption("Why don't you row right into the Colony?", key(10)), new DialogueOption("Yes please.", key(20)), new DialogueOption("No thanks.", key(30)));
                    player(10, "Why don't you row right into the Colony? It'd make it much easier to get in there.");
                    npc("Oh, Franklin doesn't like it if I take my boat too close. He says I'll damage the nets and " + "scare the fish. But were you wanting a lift up to the river?");
                    options("What would you like to say?", new DialogueOption("Yes please.", key(20)), new DialogueOption("No thanks.", key(30)));
                }
                player(20, "Yes please.").executeAction(() -> {
                    player.getDialogueManager().finish();
                    row(player, npc.getId() != 6207);
                });
                player(30, "No thanks.");
            }
        }));
        bind("Travel", (player, npc) -> row(player, npc.getId() != 6207));
    }

    private static final Location SOUTH = new Location(2367, 3486, 0);

    private static final Location NORTH = new Location(2356, 3639, 0);

    private final void row(final Player player, final boolean up) {
        player.getDialogueManager().start(new PlainChat(player, up ? "Kathy Corkat rows you up the river..." : "Kathy Corkat rows you down the river to the sea..."));
        final FadeScreen fade = new FadeScreen(player, () -> player.setLocation(up ? SOUTH : NORTH));
        fade.fade();
        WorldTasksManager.schedule(fade::unfade, 2);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KATHY_CORKAT_4299, 6207 };
    }
}
