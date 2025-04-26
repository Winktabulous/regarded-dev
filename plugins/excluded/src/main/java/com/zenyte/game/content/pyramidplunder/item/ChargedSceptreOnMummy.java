package com.zenyte.game.content.pyramidplunder.item;

import com.zenyte.game.content.pyramidplunder.npc.GuardianMummyDialogue;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemChain;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Chris
 * @since May 20 2020
 */
public class ChargedSceptreOnMummy implements ItemOnNPCAction {
    private static final int REMOVE_CHARGES_KEY = 5;
    private static final int SHINY_KEY = 10;

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                options(TITLE,
                        new DialogueOption("Please remove the charges from this sceptre.", key(REMOVE_CHARGES_KEY)),
                        new DialogueOption("Look at my shiny sceptre.", key(SHINY_KEY)));

                {
                    if (player.carryingAny(ItemChain.PHARAOH_SCEPTRE.getAllButFirst())) {
                        npc("You don't have any sceptres.");
                    } else {
                        player(REMOVE_CHARGES_KEY, "Please remove the charges from this sceptre.").executeAction(() -> {
                            GuardianMummyDialogue.uncharge(player);
                        });
                        npc("It is done.");
                    }
                }

                {
                    player(SHINY_KEY, "Look at my shiny sceptre.");
                    npc("You shouldn't have that thing in the first place, thief!");
                }
            }
        });
    }

    @Override
    public Object[] getItems() {
        return ArrayUtils.toObject(ItemChain.PHARAOH_SCEPTRE.getAllButFirst());
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{NpcId.GUARDIAN_MUMMY};
    }
}
