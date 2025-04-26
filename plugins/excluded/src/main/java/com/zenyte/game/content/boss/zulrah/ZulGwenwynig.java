package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 27/11/2018 11:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZulGwenwynig extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                ItemRetrievalService service = player.getRetrievalService();
                if (service.getType() == ItemRetrievalService.RetrievalServiceType.ZUL_GWENWYNIG
                        && service.getContainer().getSize() > 0) {
                    npc("You left some stuff at Zulrah's shrine when you left earlier.").executeAction(() -> GameInterface.ITEM_RETRIEVAL_SERVICE.open(player));
                    return;
                }
                npc("You left the shrine.<br>Did Zulrah reject your sacrifice?");
                options(TITLE, new DialogueOption("I killed Zulrah.", key(5)),
                        new DialogueOption("Remind me how this ritual works.", key(10)),
                        new DialogueOption("I'm off.", key(25)));
                player(5, "I killed Zulrah.");
                npc("Is that what you think? Board the boat again, and I am sure you will find Zulrah has " +
                        "overcome whatever pitiful attacks you used against him.");
                player(10, "Remind me how this ritual works.");
                npc("Once I have brought you to the shrine, I will leave you there as an offering to Zulrah. You cannot use the boat to return.");
                npc("You can use your magical powers to flee from Zulrah's grasp, though that would defeat the point of the sacrifice, and you'll have to try again.");
                npc("When Zulrah inevitably kills you, I may be able to retrieve your dropped possessions. If so, I will hold them here.");
                npc("Now board the boat and prepare to be sacrificed.");
                player(25, "I'm off.");
            }
        }));
        bind("Collect", (player, npc) -> {
            ItemRetrievalService service = player.getRetrievalService();
            if (service.getType() != ItemRetrievalService.RetrievalServiceType.ZUL_GWENWYNIG || service.getContainer().getSize() == 0) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("I'm afraid I don't have anything for you to collect. If I had any of your items, but you died before collecting them from me, I'd lose them.");
                    }
                });
                return;
            }
            GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[]{
                NpcId.PRIESTESS_ZULGWENWYNIG, NpcId.PRIESTESS_ZULGWENWYNIG_2033
        };
    }
}
