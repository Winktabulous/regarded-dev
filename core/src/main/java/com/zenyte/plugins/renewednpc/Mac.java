package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.AccomplishmentCape;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 25/11/2018 20:09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Mac extends NPCPlugin {

    private static final Item MAX_CAPE_COST = new Item(995, 2277000);

    private static final Item UNTRIMMED_CAPE_COST = new Item(995, 99000);

    private static final Item MAX_CAPE = new Item(13342);

    private static final Item MAX_HOOD = new Item(13281);

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                final int untrimmed = player.getNumericAttribute("first_99_skill").intValue();
                final AccomplishmentCape untrimmedCape = AccomplishmentCape.getBySkill(untrimmed);
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        player("Hello.");
                        plain("The man glances at you and grunts something unintelligble.");
                        options(TITLE, "Who are you?", "What do you have in your sack?", "Why are you so dirty?", "Talk about the untrimmed skillcape.", "Bye.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(10)).onOptionThree(() -> setKey(15)).onOptionFour(() -> setKey(20)).onOptionFive(() -> setKey(80));
                        player(5, "Who are you?");
                        npc("Mac. What's it to you?");
                        player("Only trying to be friendly.").executeAction(() -> setKey(3));
                        player(10, "What do you have in your sack?");
                        npc("S'me cape.");
                        player("Your cape?");
                        options(TITLE, "Can I have it?", "Why do you keep it in a sack?").onOptionOne(() -> setKey(25)).onOptionTwo(() -> setKey(35));
                        player(15, "Why are you so dirty?");
                        npc("Bath XP waste.").executeAction(() -> setKey(3));
                        player(20, "Can I claim my untrimmed skillcape?");
                        npc("Mebe, let me see first.").executeAction(() -> {
                            if (untrimmed == -1) {
                                setKey(120);
                            } else {
                                setKey(22);
                            }
                        });
                        npc(22, "So <col=00080>" + untrimmedCape + " skillcape</col>, that will be " + StringFormatUtil.format(UNTRIMMED_CAPE_COST.getAmount()) + " coins.");
                        options("Buy the <col=00080>" + untrimmedCape + " skillcape</col> for " + StringFormatUtil.format(UNTRIMMED_CAPE_COST.getAmount()) + " coins?", "Yes.", "Nevermind").onOptionOne(() -> {
                            if (!player.getInventory().checkSpace(2)) {
                                setKey(70);
                            } else if (!player.getInventory().containsItem(UNTRIMMED_CAPE_COST)) {
                                setKey(90);
                            } else {
                                player.getInventory().deleteItem(UNTRIMMED_CAPE_COST);
                                player.getInventory().addItem(untrimmedCape.getUntrimmed(), 1);
                                player.getInventory().addItem(untrimmedCape.getHood(), 1);
                                setKey(100);
                            }
                        });
                        player(25, "Can I have it?");
                        npc("Mebe.").executeAction(() -> {
                            if (!player.getSkills().isMaxed()) {
                                setKey(110);
                            } else {
                                setKey(27);
                            }
                        });
                        player(27, "I'm sure I could make it worth your while.");
                        npc("How much?").executeAction(() -> {
                            if (player.getInventory().containsItem(MAX_CAPE_COST)) {
                                setKey(30);
                            } else {
                                setKey(50);
                            }
                        });
                        player(30, "How about " + StringFormatUtil.format(MAX_CAPE_COST.getAmount()) + " gold?");
                        options("Buy Mac's Cape for " + StringFormatUtil.format(MAX_CAPE_COST.getAmount()) + " gold?", "Yes, pay the man.", "No.").onOptionOne(() -> setKey(55));
                        player(35, "Why do you keep it in a sack?");
                        npc("Get it dirty.").executeAction(() -> setKey(3));
                        npc("Well you can come back when you think you do.");
                        player(50, "Actually now that I think about I probably don't have enough.");
                        npc(55, "Here you go lad.").executeAction(() -> {
                            if (player.getInventory().getFreeSlots() >= 2) {
                                setKey(60);
                                player.getInventory().deleteItem(MAX_CAPE_COST);
                                player.getInventory().addItem(MAX_CAPE);
                                player.getInventory().addItem(MAX_HOOD);
                            } else {
                                setKey(70);
                            }
                        });
                        doubleItem(60, MAX_HOOD, MAX_CAPE, "Mac grunts and hands over his cape, pocketing your money swiftly.");
                        npc(70, "It seems that you don't have enough space at the moment, come back when you do.");
                        player(80, "Bye.");
                        npc("Later.");
                        npc(90, "Not enough money on you. Come back when you do.");
                        npc(100, "Enjoy the cape matey.");
                        npc(110, "You don't seem to be worthy of this cape yet. Come back and mebe I will sell you one.");
                        npc(120, "Get 99 first then mebe I give you.");
                    }
                });
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
        bind("Trade", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                Shop.get("Accomplishment Cape Shop", false, player).open(player);
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MAC };
    }
}
