package com.zenyte.plugins.itemonnpc;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Christopher
 * @since 1/28/2020
 */
public class LieveMcCrackenEnhanceAction implements ItemOnNPCAction {

    public static final ImmutableMap<Integer, Integer> TRIDENT_MAPPING = ImmutableMap.of(ItemId.UNCHARGED_TOXIC_TRIDENT, ItemId.UNCHARGED_TOXIC_TRIDENT_E, ItemId.UNCHARGED_TRIDENT, ItemId.UNCHARGED_TRIDENT_E);

    private static final int KRAKEN_TENTACLE_NOTED = ItemId.KRAKEN_TENTACLE + 1;

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        if (item.getId() == ItemId.KRAKEN_TENTACLE || item.getId() == KRAKEN_TENTACLE_NOTED) {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    final int tentacleAmount = player.getInventory().getAmountOf(item.getId());
                    final int newAmount = player.getNumericAttribute("stored_kraken_tentacles").intValue() + tentacleAmount;
                    npc("I see ya got " + format(tentacleAmount) + " there. Do ya want me to hold onto \'em for ya? For every 10 ya gives me, I\'ll enhance a trident for ya.");
                    npc("Remember, I won\'t give \'em back. Once ya gives me a tentacle, I keep it.");
                    options("Lieve will not return your tentacles.", new DialogueOption("Give them to her.", () -> {
                        player.getInventory().deleteItems(new Item(item.getId(), tentacleAmount));
                        player.addAttribute("stored_kraken_tentacles", newAmount);
                        setKey(5);
                    }), new DialogueOption("Do not give them to her.", this::finish));
                    npc(5, "Thanks. I\'ve got " + format(newAmount) + " stored for ya now. For every 10, ya can hand me a trident, an\' I\'ll enhance it.");
                }
            });
        } else {
            final int tentacleAmount = player.getNumericAttribute("stored_kraken_tentacles").intValue();
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    if (tentacleAmount >= 10) {
                        item(item, "Lieve enhances your trident. She has " + format(tentacleAmount - 10) + " stored for you.");
                        player.addAttribute("stored_kraken_tentacles", tentacleAmount - 10);
                        player.getInventory().replaceItem(TRIDENT_MAPPING.get(item.getId()), 1, slot);
                    } else {
                        if (tentacleAmount > 0) {
                            npc("If ya bring me a trident, I can enhance it for ya. It\'ll store 20,000 charges, not just 2,500. Ya brought me " + format(tentacleAmount) + " so far, but I\'ll want 10 of \'em to do the job.");
                            npc("Just pass me " + (10 - tentacleAmount) + " more tentacles, then give me yer trident an\' I\'ll get to work for ya.");
                        } else {
                            npc("If ya bring me a trident, I can enhance it for ya. It\'ll store 20,000 charges, not just 2,500. But I wants payin\' fer the job");
                            npc("Give me 10 kraken tentacles first. I\'ll hold onto \'em for ya. Then ya can bring me a trident to enhance, an\' I\'ll get to work for ya.");
                        }
                    }
                }
            });
        }
    }

    private String format(final int amount) {
        return amount == 0 ? "no tentacles" : amount == 1 ? "one tentacle" : amount + " tentacles";
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.KRAKEN_TENTACLE, KRAKEN_TENTACLE_NOTED, ItemId.UNCHARGED_TOXIC_TRIDENT, ItemId.UNCHARGED_TRIDENT };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 10587 };
    }
}
