package com.zenyte.plugins.renewednpc;

import com.near_reality.api.service.user.UserPlayerHandler;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.entity.player.privilege.ExpConfiguration;
import com.zenyte.game.world.entity.player.privilege.ExpConfigurations;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.utils.StringUtilities;
import kotlin.Unit;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 26/11/2018 18:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class IronmanTutorNPC extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                if (TreasureTrail.talk(player, npc)) {
                    return;
                }
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        if (player.isIronman()) {
                            npc("Hail, " + player.getGameMode().toString() + "!<br><br>What can I do for you?");
                        } else {
                            npc("Hello, " + player.getName() + ". I'm the Iron Man tutor.<br>What can I do for you?");
                        }
                        options(TITLE, "Tell me about Iron men.", "I'd like to review my Iron Man mode.", "Have you got any armour for me, please?", "I'm fine, thanks.").onOptionOne(() -> setKey(5)).onOptionTwo(() -> setKey(20)).onOptionThree(() -> setKey(30)).onOptionFour(() -> setKey(50));
                        player(5, "Tell me about Iron Men.");
                        npc("When you play as an <col=8b0000>Iron Man</col>, you do everything for yourself. You " +
                                "don't trade with other players, or take their items, or accept their help.");
                        npc("As an <col=8b0000>Iron Man</col>, you choose to have these restrictions imposed on you, " +
                                "everyone knows you're doing it properly.");
                        npc("If you think you have what it takes, you can choose to become a <col=ff0000>Hardcore Iron Man</col>");
                        npc("In addition to the standard restrictions, <col=ff0000>Hardcore Iron Men</col> only have <col=ff0000>one life</col>. In the event of a dangerous death, your <col=ff0000>Hardcore Iron Men</col> status will be downgraded to that of a <col=8b0000>standard Iron Man</col>, and your");
                        npc("stats will be frozen on the <col=ff0000>Hardcore Iron Man</col> hiscores.");
                        npc("For the ultimate challenge, you can choose to become an <col=00008b>Ultimate Iron Man</col>, a game mode inspired by the player <col=00008b>IronNoBank</col>.");
                        npc("In addition to the standard restrictions, <col=00008b>Ultimate Iron Men</col> are blocked from using the bank, and they drop all their items when they die.");
                        if (!player.isIronman()) {
                            npc("You are not an Iron Man so these restrictions don't apply to you.").executeAction(() -> setKey(2));
                        } else {
                            if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN)) {
                                npc("You're an <col=00008b>Ultimate Iron Man</col>. You can downgrade yourself to a " +
                                        "<col=8b0000>standard Iron Man</col> or even become a normal player.").executeAction(() -> setKey(2));
                            } else if (player.getGameMode().equals(GameMode.HARDCORE_IRON_MAN)) {
                                npc("You're an <col=ff0000>Hardcore Iron Man</col> You can downgrade yourself to a " +
                                        "<col=8b0000>standard Iron Man</col> or even become a normal player.").executeAction(() -> setKey(2));
                            } else {
                                npc("You're a <col=8b0000>standard Iron Man</col>. You can downgrade yourself to a " +
                                        "normal player.").executeAction(() -> setKey(2));
                            }
                        }
                        player(20, "I'd like to review my Iron Man mode.").executeAction(() -> setKey(55));
                        player(30, "Have you got any armour for me, please?").executeAction(() -> armour(player, npc));
                        player(50, "I'm fine, thanks.");
                        if (!player.isIronman()) {
                            npc(55, "You're not an ironman.");
                        } else if (player.getGameMode().isGroupIronman()) {
                            npc(55, "I cannot downgrade the rank of Group Ironman players.");
                        } else {
                            npc(55, "If you downgrade your game mode this will be permanent, you cannot undo this");
                            GameMode downGrade = player.getGameMode().equals(GameMode.HARDCORE_IRON_MAN) || player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN) ?
                                    GameMode.STANDARD_IRON_MAN: GameMode.REGULAR ;
                            options("Downgrade to "+StringUtilities.formatEnum(downGrade)+" gamemode?", "Yes please", "No thank you")
                                    .onOptionOne(() -> UserPlayerHandler.INSTANCE.updateGameMode(player, downGrade, (success) -> {
                                        final Dialogue responseDialogue;
                                        if (success) {
                                            ExpConfigurations config = ExpConfigurations.of(player.getGameMode());
                                            int currentIndex = config.getExpConfigurationIndex(player.getCombatXPRate(), player.getSkillingXPRate());

                                            ExpConfiguration[] configurations = ExpConfigurations.of(downGrade).getConfigurations();
                                            int newIndex = Math.min(currentIndex, configurations.length);
                                            ExpConfiguration newConfiguration = configurations[newIndex];

                                            player.setExperienceMultiplier(newConfiguration);
                                            responseDialogue = new Dialogue(player, npc) {
                                                @Override
                                                public void buildDialogue() {
                                                    player("Yes please");
                                                    npc("You're now a " + StringUtilities.formatEnum(downGrade) + ", and your new exp mode is " +
                                                            "" + player.getCombatXPRate() + "x combat, " + player.getSkillingXPRate() + "x skilling.");
                                                }
                                            };
                                        } else {
                                            responseDialogue = new Dialogue(player, npc) {
                                                @Override
                                                public void buildDialogue() {
                                                    npc("Something went wrong in changing your game-mode, please try again later.");
                                                }
                                            };
                                        }
                                        player.getDialogueManager().start(responseDialogue);
                                        return Unit.INSTANCE;
                                    }))
                                    .onOptionTwo(() -> setKey(60));
                        }
                        player(60, "No thank you");
                        npc("Okay algood");
                    }
                });
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                // npc.setInteractingWith(player);
            }
        });
        bind("Armour", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                armour(player, npc);
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                handle(player, npc);
                // npc.setInteractingWith(player);
            }
        });
    }

    public static void armour(final Player player, final NPC npc) {
        if (player.getGameMode().isGroupIronman()) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "Ah for group ironmans you should visit my twin at The Node!"));
            return;
        }

        player.stopAll();
        player.faceEntity(npc);
        if (!player.isIronman()) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "This feature is only available to Iron Men."));
            return;
        }
        final Item[] armour = ZenyteGuide.STARTER_ITEMS[player.getGameMode().ordinal()];
        final Item[] items = ItemUtil.concatenate(player.getInventory().getContainer().getItems().values().toArray(new Item[] {}), player.getEquipment().getContainer().getItems().values().toArray(new Item[] {}), player.getBank().getContainer().getItems().values().toArray(new Item[] {}));
        int added = 0;
        for (final Item item : armour) {
            if (!ArrayUtils.contains(items, item)) {
                player.getInventory().addItem(item).onFailure(i -> player.getBank().add(item).onFailure(i2 -> player.sendMessage("I'm not able to give you the following item due to insufficient space: " + i2.getName())));
                added++;
            }
        }
        final String message = added == 0 ? "I think you've already got the whole set." : "There you go. Wear it with pride.";
        player.getDialogueManager().start(new NPCChat(player, npc.getId(), message));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {311};
    }
}
