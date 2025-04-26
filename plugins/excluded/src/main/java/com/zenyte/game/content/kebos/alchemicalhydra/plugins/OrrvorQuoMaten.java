package com.zenyte.game.content.kebos.alchemicalhydra.plugins;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;

/**
 * @author Tommeh | 12/11/2019 | 00:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class OrrvorQuoMaten extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Human.", Expression.ORRVOR_QUO_MATEN);
                    options(TITLE, "What are you doing here?", "Do you have any of my items?", "I have to go.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(15)).onOptionThree(() -> setKey(20));
                    player(5, "What are you doing here?");
                    npc("This passage leads to the lair of what your kind call the Alchemical Hydra. It is an ancient beast, the only one of its kind.", Expression.ORRVOR_QUO_MATEN);
                    npc("I am here to protect it. For protecting it will protect the balance. I make sure that only those who have permission can fight it.", Expression.ORRVOR_QUO_MATEN);
                    player("How do I get permission?");
                    npc("Only Konar quo Maten can grant permission to fight the Alchemical Hydra. She will rarely grant this permission when the beast needs calming.", Expression.ORRVOR_QUO_MATEN);
                    options(TITLE, "Do you have any of my items?", "Thanks for the information.").onOptionOne(() -> setKey(15)).onOptionTwo(() -> setKey(40));
                    player(15, "Do you have any of my items?").executeAction(() -> itemRetrieval(player, npc));
                    player(20, "I have to go.");
                    player(40, "Thanks for the information.");
                    npc(50, "I don\'t have anything for you human. If I did have any of your items, but you died before collecting them, they\'ll now be lost.", Expression.ORRVOR_QUO_MATEN);
                }
            });
        });
        bind("Collect", (player, npc) -> itemRetrieval(player, npc));
    }

    private static void itemRetrieval(final Player player, final NPC npc) {
        final ItemRetrievalService service = player.getRetrievalService();
        if (service.getType() != ItemRetrievalService.RetrievalServiceType.ORRVOR_QUO_MATEN || service.getContainer().getSize() == 0) {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("I don\'t have anything for you human. If I did have any of your items, but you died before collecting them, they\'ll now be lost.", Expression.ORRVOR_QUO_MATEN);
                }
            });
            return;
        }
        GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ORRVOR_QUO_MATEN };
    }
}
