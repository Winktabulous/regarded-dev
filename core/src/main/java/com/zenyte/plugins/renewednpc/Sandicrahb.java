package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/04/2019 13:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Sandicrahb extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Hello.");
                    options(TITLE, new DialogueOption("Who are you?", key(5)), new DialogueOption("Can you take me to " + (npc.getId() == 7483 ? "Crabclaw Isle" : "Mainland") + ".", key(50)));
                    player(5, "I'm Sandicrahb. I travel people to and from the Crabclaw Isle for a feee.");
                    player(50, "Can you take me to " + (npc.getId() == 7483 ? "Crabclaw Isle" : "Mainland") + ".").executeAction(() -> travelRequest(player, npc));
                }
            });
        });
        bind("Travel", this::travelRequest);
    }

    private final void travelRequest(@NotNull final Player player, @NotNull final NPC npc) {
        player.getDialogueManager().finish();
        final boolean toCrabclaw = npc.getId() == 7483;
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                options(toCrabclaw ? "Travel to Crabclaw Isle?" : "Travel back to mainland?", new DialogueOption("Yes." + (toCrabclaw ? " (10,000 coins)" : ""), () -> {
                    if (toCrabclaw && !player.getInventory().containsItem(995, 10000)) {
                        setKey(5);
                        return;
                    }
                    if (toCrabclaw) {
                        player.getInventory().deleteItem(995, 10000);
                    }
                    new FadeScreen(player, () -> player.setLocation(toCrabclaw ? new Location(1778, 3417, 0) : new Location(1784, 3458, 0))).fade(2);
                    player.sendMessage("You travel to " + (toCrabclaw ? "Crabclaw Isle..." : "Mainland..."));
                }), new DialogueOption("No."));
                item(5, new Item(995, 10000), "You need 10,000 coins to travel to Crabclaw Isle.");
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SANDICRAHB, NpcId.SANDICRAHB_7484 };
    }
}
