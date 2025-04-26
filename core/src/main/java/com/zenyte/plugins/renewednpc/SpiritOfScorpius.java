package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Christopher
 * @since 1/27/2020
 */
public class SpiritOfScorpius extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", ((player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Who treads upon my grave?");
                options(TITLE, new DialogueOption("I have come to seek a blessing.", key(5)), new DialogueOption("I need another unholy mould.", key(10)), new DialogueOption("I have come to kill you ", key(15)));
                player(5, "I have come to seek a blessing.");
                if (player.carryingItem(ItemId.UNPOWERED_SYMBOL)) {
                    npc("I see you have the unholy symbol of our Lord. I will bless it for you.").executeAction(() -> {
                        bless(player, npc);
                        player.lock(5);
                    });
                } else if (player.carryingItem(ItemId.UNHOLY_SYMBOL)) {
                    npc("I see you have the unholy symbol of our Lord. It is blessed with the Lord Zamorak's power.");
                    npc("Come to me when your faith weakens.");
                } else {
                    npc("No blessings will be given to those who have no symbol of our Lord's love.");
                }
                player(10, "I need another mould for the unholy symbol");
                npc("To lose an object is easy to replace. To lose the affections of our Lord is impossible to forgive.").executeAction(() -> {
                    if (player.getInventory().hasFreeSlots()) {
                        player.getInventory().addItem(ItemId.UNHOLY_MOULD, 1);
                    }
                });
                player(15, "I have come to kill you");
                npc("The might of mortals to me, is as dust to the sea.");
            }
        })));
    }

    private void bless(Player player, NPC npc) {
        WorldTasksManager.schedule(() -> {
            player.sendMessage("The ghosts mutters in a strange voice");
            player.sendMessage("The unholy symbol throbs with power.");
            final int slot = player.getInventory().getContainer().getSlotOf(ItemId.UNPOWERED_SYMBOL);
            player.getInventory().replaceItem(ItemId.UNHOLY_SYMBOL, 1, slot);
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("The symbol of our lord has been blessed with power!");
                    npc("My master calls.");
                }
            });
        }, 5);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.SPIRIT_OF_SCORPIUS };
    }
}
