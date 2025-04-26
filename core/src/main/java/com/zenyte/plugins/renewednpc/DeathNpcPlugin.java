package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.text.NumberFormat;

/**
 * @author Kris | 14/06/2022
 */
public class DeathNpcPlugin extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getDialogueManager().start(new Dialogue(player, npc) {
                    @Override
                    public void buildDialogue() {
                        if (player.getVarManager().getBitValue(10465) > 0) {
                            npc("Hello mortal. I see you have a gravestone, somewhere<br>in the world. Did you want to ask me about it?");
                            options(
                                    new DialogueOption("Can I collect the items from that gravestone now?", () -> {
                                        player.getGravestone().removeGravestone();
                                        GameInterface.DEATHS_OFFICE_RETRIEVAL.open(player);
                                    }),
                                    new DialogueOption("No, I want to talk about something else.", () -> setKey(10))
                            );
                            player(10, "No, I want to talk about something else.");
                            npc("Very well. Have you come to retrieve something you<br>lost from a previous gravestone?").executeAction(() -> setKey(15));
                        } else {
                            npc("Hello mortal. Have you come to retrieve something you<br>lost from a previous gravestone?").executeAction(() -> setKey(15));
                        }
                        options(
                                15,
                                new DialogueOption("Yes, have you got anything for me?", () -> setKey(300)),
                                new DialogueOption("Can I choose a different-looking gravestone?", () -> setKey(400)),
                                new DialogueOption("Not just now, thanks.", () -> setKey(500))
                        );
                        player(300, "Yes, have you got anything for me?").executeAction(() -> GameInterface.DEATHS_OFFICE_RETRIEVAL.open(player));
                        player(500, "Not just now, thanks.");
                        player(400, "Can I choose a different-looking gravestone?");
                        itemOptions(401,
                                "Death's coffer: " + NumberFormat.getIntegerInstance().format(player.getGravestone().getCoinsInCoffer()) + " coins",
                                24418,
                                24524,
                                "<br><br><br><br>Basic: Free",
                                "<br><br><br><br>Angel: 200k coins"
                        ).onOptionOne(() -> {
                            player.getVarManager().sendVarInstant(262, 0);
                            setKey(420);
                        }).onOptionTwo(() -> {
                            player.getVarManager().sendVarInstant(262, 1);
                            if (player.getGravestone().getCoinsInCoffer() + player.getInventory().getAmountOf(ItemId.COINS_995) >= 200_000) {
                                setKey(420);
                            } else {
                                setKey(410);
                            }
                        });
                        options(405, new DialogueOption("Can I choose a different-looking gravestone?", () -> setKey(400)),
                                new DialogueOption("Actually, never mind."));

                        player("Actually, never mind.");
                        npc(410, "You haven't got 200,000 coins to pay for that.<br>I can take coins from your inventory, or from my<br>Coffer.").executeAction(() -> setKey(405));

                        npc(420, "Most people won't see your grave, by the way. It<br>appears to you, but it aims to be invisible to everyone<br>else. Are you sure you want to buy a cosmetic<br>override for it?");
                        options(
                                new DialogueOption("Yes, I understand other people won't often see it.", () -> setKey(430)),
                                new DialogueOption("No thanks, it's not worth it.")
                        );
                        player(430, "Yes, I understand other people won't often see it.").executeAction(() -> {
                            final int option = player.getVarManager().getValue(262);
                            player.getVarManager().sendBitInstant(10467, option);
                            int amountToRemove = option == 0 ? 0 : 200_000;
                            if (amountToRemove > 0 && player.getGravestone().getCoinsInCoffer() > 0) {
                                final long toRemove = Math.min(amountToRemove, player.getGravestone().getCoinsInCoffer());
                                amountToRemove -= toRemove;
                                player.getGravestone().setCoinsInCoffer(player.getGravestone().getCoinsInCoffer() - toRemove);
                            }
                            if (amountToRemove > 0) {
                                player.getInventory().deleteItem(new Item(ItemId.COINS_995, amountToRemove));
                            }
                            setKey(option == 0 ? 450 : 440);
                        });
                        item(440, new Item(24524), "Your gravestone is now the Angel of Death.").executeAction(() -> setKey(460));
                        item(450, new Item(24418), "Your gravestone is now the basic statue.").executeAction(() -> setKey(460));
                        npc(460, "It is not for me to judge how people spend their<br>money.");
                    }
                });
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 3), () -> execute(player, npc), true));
            }

            @Override
            public void execute(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });

        bind("Collect", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                player.getGravestone().removeGravestone();
                GameInterface.DEATHS_OFFICE_RETRIEVAL.open(player);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 3), () -> execute(player, npc), true));
            }

            @Override
            public void execute(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 9855 };
    }
}
