package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Christopher
 * @since 1/24/2020
 */
public class LieveMcCracken extends NPCPlugin implements ItemOnNPCAction {

    private static final String ATTR = "stored_kraken_tentacles";

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("*hic* G'day! Do ya...<br>... well, do ya need any help with yer trident? *buuurrrp*");
                options(TITLE, new DialogueOption("Let's talk about tridents.", key(5)), new DialogueOption("I'll " + "be, heh, leaving you now...", key(10)));
                final int tentacleAmount = player.getNumericAttribute("stored_kraken_tentacles").intValue();
                if (tentacleAmount > 0 && tentacleAmount < 10) {
                    npc(5, "If ya bring me a trident, I can enhance it for ya. It'll store 20,000 charges, not just " + "2,500. Ya brought me " + tentacleAmount + " tentacles so far, but I'll want 10 of 'em to do the job.");
                    npc("Just pass me " + (10 - tentacleAmount) + " more tentacles, then give me yer trident an' I'll" + " get to work for ya.");
                } else if (tentacleAmount >= 10) {
                    npc(5, "Ya brought me " + tentacleAmount + " tentacles so far. Bring me a trident to enhance, an'" + " I'll get to work for ya.");
                } else {
                    npc(5, "If ya bring me a trident, I can enhance it for ya. It'll store 20,000 charges, not just " + "2,500. But I wants payin' fer the job");
                    npc("Give me 10 kraken tentacles first. I'll hold onto 'em for ya. Then ya can bring me a trident" + " to enhance, an' I'll get to work for ya.");
                }
                npc(10, "Wow, so original.");
            }
        }));
    }

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if(item.getId() == ItemId.KRAKEN_TENTACLE) {
            int stored = player.getNumericAttribute(ATTR).intValue();
            int amt = player.getInventory().getAmountOf(ItemId.KRAKEN_TENTACLE);
            int take = Math.min(amt, 10 - stored);

            if (take <= 0) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Ya brought me " + stored + " tentacles already. Bring me a trident to enhance, an'" + " I'll get to work for ya.");
                    }
                });
            }

            player.getInventory().deleteItem(ItemId.KRAKEN_TENTACLE, take);
            player.incrementNumericAttribute(ATTR, take);

            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    final int tentacleAmount = player.getNumericAttribute("stored_kraken_tentacles").intValue();
                    if (tentacleAmount > 0 && tentacleAmount < 10) {
                        npc("Just pass me " + (10 - tentacleAmount) + " more tentacles, then give me yer trident an' I'll" + " get to work for ya.");
                    } else if (tentacleAmount >= 10) {
                        npc("Ya brought me " + tentacleAmount + " tentacles so far. Bring me a trident to enhance, an'" + " I'll get to work for ya.");
                    }
                }
            });
        } else if(item.getId() == ItemId.TRIDENT_OF_THE_SEAS || item.getId() == ItemId.TRIDENT_OF_THE_SWAMP) {
            final int tentacleAmount = player.getNumericAttribute("stored_kraken_tentacles").intValue();

            if(tentacleAmount >= 10) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Are ya sure you want to do this?");
                        options("Enhance your "+item.getDefinitions().getName()+"?", "Yes", "no").onOptionOne(() -> {
                            player.getInventory().deleteItem(item);
                            player.incrementNumericAttribute(ATTR, -10);
                            Item enhanced = item.getId() == ItemId.TRIDENT_OF_THE_SEAS ? new Item(ItemId.TRIDENT_OF_THE_SEAS_E) : new Item(ItemId.TRIDENT_OF_THE_SWAMP_E);
                            int charges = item.getCharges();
                            enhanced.setCharges(charges);
                            player.getInventory().addItem(enhanced);
                            item(5, enhanced, "Lieve gives you your enhanced trident.");
                            setKey(5);
                        }).onOptionTwo(() -> {
                            npc(5, "Okay, come back if ya change yer mind.");
                            setKey(5);
                        });
                    }
                });
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        npc("Give me 10 kraken tentacles first. I'll hold onto 'em for ya. Then ya can bring me a trident" + "to enhance, an' I'll get to work for ya.");
                    }
                });
            }
        }

    }

    @Override
    public Object[] getItems() {
        return new Object[] {ItemId.KRAKEN_TENTACLE, ItemId.TRIDENT_OF_THE_SEAS, ItemId.TRIDENT_OF_THE_SWAMP};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {NpcId.LIEVE_MCCRACKEN};
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.LIEVE_MCCRACKEN };
    }
}
