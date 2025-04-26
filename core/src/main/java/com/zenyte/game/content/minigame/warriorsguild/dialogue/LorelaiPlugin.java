package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 18. dets 2017 : 0:11.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class LorelaiPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello! I'm Lorelai. Welcome to the Warriors' Guild basement!");
                player("What are you doing down here?");
                npc("I'm training this gorup of special Cyclopes to be stronger and more powerful for Kamfreena.");
                options();
                player(10, "What's special about these Cyclopes?");
                npc("They have a chance of dropping a new, more powerful defender. Kamfreena doesn't want just anyone" + " getting their hands on one so she moved them down here and tasked me with training them.");
                options();
                player(30, "How exactly are you training them?");
                npc("With a punishing routine of combat, fighting and the odd death!");
                player("So, you just let them fight each other and hope they get stronger?");
                npc("Of course not, that would just be cruel! Kamfreena and I only allow the most worthy of adventurers to fight these cyclopes.");
                player("I am a mighty adventurer! Can I fight them?");
                final boolean carryingRuneDefender = player.getInventory().containsItem(ItemId.RUNE_DEFENDER, 1) || player.getInventory().containsItem(ItemId.RUNE_DEFENDER_T, 1) || player.getEquipment().getId(EquipmentSlot.SHIELD) == ItemId.RUNE_DEFENDER || player.getEquipment().getId(EquipmentSlot.SHIELD) == ItemId.RUNE_DEFENDER_T;
                if (carryingRuneDefender) {
                    npc("Yes, you have already proved yourself to me, the door is unlocked.");
                } else {
                    npc("No, you need to show me a rune defender before I allow you in through those doors.");
                }
                player(50, "Can I fight them?");
                player("I am a mighty adventurer! Can I fight them?");
                if (carryingRuneDefender) {
                    npc("Yes, you have already proved yourself to me, the door is unlocked.");
                } else {
                    npc("No, you need to show me a rune defender before I allow you in through those doors.");
                }
                player(70, "Bye!");
                npc("See you around.");
            }

            private final void options() {
                options(TITLE, "What's special about these Cyclopes?", "How exactly are you training them?", "Can I fight them?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(50)).onOptionFour(() -> setKey(70));
            }
        }));
        bind("Claim-tokens", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                player("May I claim my tokens please?");
                if (player.getNumericAttribute("warriorsGuildTokens").intValue() > 0) {
                    if (player.getInventory().hasFreeSlots() || player.getInventory().containsItem(8851, 1)) {
                        npc("Of course! Here you go, you've earned " + player.getNumericAttribute("warriorsGuildTokens").intValue() + " tokens!").executeAction(() -> {
                            player.getInventory().addItem(new Item(8851, player.getNumericAttribute("warriorsGuildTokens").intValue()));
                            player.getAttributes().remove("warriorsGuildTokens");
                        });
                        player("Thanks!");
                    } else
                        plain("You need some free inventory space before you can accept the tokens.");
                } else {
                    npc("I'm afraid you have not earned any tokens yet. Try some of the activities around the guild " + "to earn some.");
                    player("Okay, I'll go see what I can find.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.LORELAI };
    }
}
