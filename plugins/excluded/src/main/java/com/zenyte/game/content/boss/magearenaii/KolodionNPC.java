package com.zenyte.game.content.boss.magearenaii;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.types.config.items.ItemDefinitions;

/**
 * @author Kris | 22/06/2019 15:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KolodionNPC extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Hello.");
                options(TITLE, new DialogueOption("Are there any challenges available?", key(5)), new DialogueOption("What have I brought you so far?", key(500)), new DialogueOption("Cancel."));
                player(500, "What have I brought you so far?");
                final int handCount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(21797).getName() + " count").intValue();
                final int entCount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(21798).getName() + " count").intValue();
                final int demonCount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(21799).getName() + " count").intValue();
                npc("You\'ve currently brought me " + handCount + " x Justiciar\'s hand, " + demonCount + " x Demon\'s heart, " + entCount + " x Ent\'s roots.");
                player(5, "Are there any challenges available?");
                if (player.getSkills().getLevelForXp(SkillConstants.MAGIC) < 75) {
                    npc("Yes, however not for you.");
                    plain("You need a Magic level of at least 75 to start the Mage Arena II minigame.");
                    return;
                }
                npc("I am one of the most powerful mages in existence. But even my power has limitations. There are some brings however that have power exceeding even my own.");
                options(TITLE, new DialogueOption("Great, I\'ve been waiting for an improvement!", key(100)), new DialogueOption("Actually, my current cape is fine.", key(25)));
                player(25, "Actually, my current cape is fine.");
                player(100, "Great. I\'ve been waiting for an improvement!");
                player("What do you need me to do?");
                npc("I will need you to kill these three beings and bring me their remains. Even in death, their power should be great enough for me to harness it for our own use.");
                player("Where am I supposed to find these beings?");
                if (!player.containsItem(21800)) {
                    if (!player.getInventory().hasFreeSlots()) {
                        plain("You need some free inventory space to continue.");
                        return;
                    }
                    npc("Here, take this enchanted symbol of the gods. It will guide you to the creatures.").executeAction(() -> player.getInventory().addOrDrop(new Item(21800)));
                    item(new Item(21800), "Kolodion hands you an enchanted symbol.");
                } else {
                    npc("The enchanted symbol I previously gave to you will guide you to the creatures.");
                }
                npc("Bring me the remains of all three magical beings. Do this and I will imbue their combined power into a single god cape of your choice.");
                player("Thanks, bye.");
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KOLODION };
    }

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        final int id = item.getId();
        if (id >= 21797 && id <= 21799) {
            player.getInventory().deleteItem(item);
            player.addAttribute("handed in " + item.getName() + " count", player.getNumericAttribute("handed in " + item.getName() + " count").intValue() + 1);
            player.getDialogueManager().start(new ItemChat(player, item, "You hand the " + item.getName() + " to Kolodion."));
            return;
        }
        final int handCount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(21797).getName() + " count").intValue();
        final int entCount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(21798).getName() + " count").intValue();
        final int demonCount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(21799).getName() + " count").intValue();
        final boolean completedOnce = player.getNumericAttribute("claimed one mage arena 2 cape").intValue() != 0;
        final int idToBringIfCompletedOnce = id == ItemId.SARADOMIN_CAPE ? ItemId.JUSTICIARS_HAND : id == ItemId.GUTHIX_CAPE ? ItemId.ENTS_ROOTS : ItemId.DEMONS_HEART;
        final int ifCompletedOnceAmount = player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(idToBringIfCompletedOnce).getName() + " count").intValue();
        if (completedOnce ? (ifCompletedOnceAmount == 0) : (handCount < 1 || entCount < 1 || demonCount < 1)) {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    if (completedOnce) {
                        npc("I\'ll need 1 x " + ItemDefinitions.getOrThrow(idToBringIfCompletedOnce).getName() + " to imbue that!<br>You\'ve currently brought me " + handCount + " x Justiciar\'s hand, " + demonCount + " x Demon\'s heart, " + entCount + " x Ent\'s roots.");
                    } else {
                        npc("You need to bring me a Justiciar\'s hand, A Demon\'s heart and some Ent\'s roots!<br>You\'ve currently brought me " + handCount + " x Justiciar\'s hand, " + demonCount + " x Demon\'s heart, " + entCount + " x Ent\'s roots.");
                    }
                }
            });
            return;
        }
        final int nextId = id == 2412 ? 21791 : id == 2413 ? 21793 : 21795;
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                item(new Item(nextId), "Imbue the " + item.getName() + "?");
                options(TITLE, new DialogueOption("Imbue it.", () -> {
                    if (player.getInventory().getItem(slot) != item) {
                        return;
                    }
                    player.addAttribute("claimed one mage arena 2 cape", 1);
                    player.getInventory().deleteItem(item);
                    player.getInventory().addOrDrop(new Item(nextId));
                    if (completedOnce) {
                        player.addAttribute("handed in " + ItemDefinitions.getOrThrow(idToBringIfCompletedOnce).getName() + " count", (player.getNumericAttribute("handed in " + ItemDefinitions.getOrThrow(idToBringIfCompletedOnce).getName() + " count").intValue()) - 1);
                    } else {
                        player.addAttribute("handed in " + ItemDefinitions.getOrThrow(21797).getName() + " count", handCount - 1);
                        player.addAttribute("handed in " + ItemDefinitions.getOrThrow(21798).getName() + " count", entCount - 1);
                        player.addAttribute("handed in " + ItemDefinitions.getOrThrow(21799).getName() + " count", demonCount - 1);
                    }
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 21797, 21798, 21799, 2412, 2413, 2414 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 1603 };
    }
}
