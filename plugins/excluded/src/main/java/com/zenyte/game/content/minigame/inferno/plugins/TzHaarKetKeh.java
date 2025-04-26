package com.zenyte.game.content.minigame.inferno.plugins;

import com.zenyte.game.content.area.tzhaar.TzHaar;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.InfernoCompletions;
import com.zenyte.game.content.minigame.inferno.model.InfernoWave;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 25/11/2018 16:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TzHaarKetKeh extends NPCPlugin {
    private static final Logger log = LoggerFactory.getLogger(TzHaarKetKeh.class);
    private static final int WHAT_IS_THIS_PLACE = 10;
    private static final int WHAT_HAPPENED = 25;
    private static final int CAN_I_GO_DOWN = 50;
    private static final int NEVERMIND = 75;
    private static final int SACRIFICE_CAPE = 100;
    private static final int HAND_OVER_CAPE = 125;
    private static final Item practiceFee = new Item(ItemId.COINS_995, 1500000);

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                final Object hasSpoken = player.getAttributes().get("spokenToTzhaarKetKeh");
                if (hasSpoken == null) {
                    player("Wow what has happened here? That doesn\'t look good!");
                    npc("We pushed it too far! Way too far. JalYt would not understand. The memories, we needed them. Now big Inferno.").executeAction(() -> player.getAttributes().put("spokenToTzhaarKetKeh", true));
                }
                options(TITLE, new DialogueOption("What is this place?", key(WHAT_IS_THIS_PLACE)), new DialogueOption("What happened here?", key(WHAT_HAPPENED)), new DialogueOption("Can I go down there?", key(CAN_I_GO_DOWN)), new DialogueOption("Talk about Practice Mode", () -> practiceMode(player, npc)), new DialogueOption("Nevermind.", key(NEVERMIND)));
                player(WHAT_IS_THIS_PLACE, "What is this place?");
                npc("This is our birthing pool. TzHaar are born from eggs incubated in this large pool here.");
                npc("When hatched, we retain memories. Memories and knowledge passed on from ancestors who returned to the lava.");
                player("Something doesn\'t seem right... There\'s a large opening.");
                options(TITLE, new DialogueOption("What happened here?", key(WHAT_HAPPENED)), new DialogueOption("Can I go down there?", key(CAN_I_GO_DOWN)), new DialogueOption("Nevermind.", key(NEVERMIND)));
                player(WHAT_HAPPENED, "What happened here?");
                npc("We went too far... way too far, we need the memories. JalYt would not understand.");
                player("Please help me to understand then.");
                npc("We TzHaar have special ability. When incubated in lava, a hatched TzHaar retains knowledge and memories of ancestors who returned to the lava.");
                npc("Not all memories though, those about earliest ancestors were not being retained. We experiment.");
                npc("We increased the depth at which the eggs are incubated. Over time this worked, newly hatched Tzhaar had more memories of lost history!");
                player("That sounds fantastic though, uncovering the past to determine where your species originated?");
                npc("Yes, but JalYt do not understand, we kept pushing it. We wanted answers, but too deep we went.");
                npc("Eventually the pool collapsed into a sink hole, huge inferno there. This was a big ancient incubation chamber.");
                npc("We hatched eggs in there and it was not a good decision. They were so different, bigger, stronger and fought eachother for dominance.");
                npc("Instead of knowledge, we now have a prison of dangerous TzHaar creatures - it gets worse but I cannot trust JalYt with this knowledge yet.");
                options(TITLE, new DialogueOption("What is this place?", key(WHAT_IS_THIS_PLACE)), new DialogueOption("Can I go down there?", key(CAN_I_GO_DOWN)), new DialogueOption("Nevermind.", key(NEVERMIND)));
                player(CAN_I_GO_DOWN, "Can I go down there?");
                npc("JalYt, we need help, but this is extremely dangerous. TzHaar not strong enough to take control back.");
                npc("We are worried about trusting JalYt to take on this task for us.");
                player("Let me prove it.");
                if (player.getNumericAttribute("infernoVar").intValue() == 2) {
                    npc("It appears that you already have sacrificed your fire cape to me. Whenever you\'re ready, jump into the Inferno and take on the challenge!");
                } else {
                    npc("I have one idea, if you could sacrifice your fire cape to me, I can take your word.");
                    options("Sacrifice your Fire cape?", new DialogueOption("Yes, I want to sacrifice my cape.", key(SACRIFICE_CAPE)), new DialogueOption("No, I want to keep it!"));
                    player(SACRIFICE_CAPE, "Okay, I\'d like to sacrifice my fire cape.");
                    if (player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                        npc("Very well, just hand it over.");
                        options("<col=ff0000>Really sacrifice your Fire cape?", new DialogueOption("Yes, hand it over.", key(HAND_OVER_CAPE)), new DialogueOption("No, I want to keep it!"));
                        item(HAND_OVER_CAPE, TzHaar.FIRE_CAPE_ITEM, "You hand your cape to TzHaar-Ket-Keh.").executeAction(() -> {
                            if (player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM)) {
                                player.getInventory().deleteItem(TzHaar.FIRE_CAPE_ITEM);
                                player.addAttribute("infernoVar", 2);
                                VarCollection.TZHAAR_UNLOCKS.updateSingle(player);
                            }
                        });
                        npc("Give it your best shot JalYt-Mej-Xo-" + player.getName() + ".");
                    } else {
                        npc("Sorry JalYt, I don\'t think you do have a cape on you.");
                    }
                }
                player(NEVERMIND, "Nevermind.");
            }
        }));
        bind("Practice Mode", TzHaarKetKeh::practiceMode);
    }

    private static void practiceMode(final Player player, final NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                final GameMode mode = player.getGameMode();
                final String feeLocation = mode.equals(GameMode.ULTIMATE_IRON_MAN) ? "inventory" : "bank";
                if (player.getNumericAttribute("infernoVar").intValue() < 2) {
                    npc("You haven\'t sacrificed your a Fire cape yet, until then you cannot try out the Inferno Practice Mode.");
                    return;
                }
                npc("Would you like to try out the Inferno Practice Mode? Completing the Inferno however will not grant you the Infernal cape or Tokkul.");
                npc("You can start the Inferno at any wave you wish at a cost of " + StringFormatUtil.format(practiceFee.getAmount()) + " coins. The fee will be taken from your " + feeLocation + ".");
                npc("Would you like to continue? The fee will be taken off your " + feeLocation + ".");
                options("Continue with the Inferno Practice Mode (costs " + StringFormatUtil.format(practiceFee.getAmount()) + " coins).", "Yes - continue.", "No.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(20));
                player(10, "Yes, let\'s carry on.").executeAction(() -> {
                    if ((mode.equals(GameMode.ULTIMATE_IRON_MAN) && player.getInventory().containsItem(practiceFee)) || (!mode.equals(GameMode.ULTIMATE_IRON_MAN) && player.getBank().containsItem(practiceFee))) {
                        setKey(15);
                    } else {
                        setKey(25);
                    }
                });
                npc(15, "Very well, JalYt.");
                npc(16, "At which wave would you like to start your practice run?").executeAction(() -> {
                    finish();
                    player.sendInputInt("Start the Inferno Practice run at wave (1 - 69):", value -> {
                        player.getDialogueManager().setLastDialogue(this);
                        final InfernoWave wave = InfernoWave.get(value);
                        if (wave == null) {
                            setKey(30);
                            return;
                        }
                        player.getDialogueManager().start(new Dialogue(player, npc) {
                            @Override
                            public void buildDialogue() {
                                npc("So wave " + wave.getWave() + " it is. Are you sure JalYt?");
                                options("Do you wish to start at wave " + wave.getWave() + "?", "Yes.", "No.").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(20));
                                player(10, "Yes, wave " + wave.getWave() + " it is!");
                                npc("Very well, JalYt.").executeAction(() -> {
                                    try {
                                        InfernoCompletions.addPracticeRun(player);
                                        if (player.getGameMode().equals(GameMode.ULTIMATE_IRON_MAN)) {
                                            player.getInventory().deleteItem(practiceFee);
                                        } else {
                                            player.getBank().remove(practiceFee);
                                        }
                                        final AllocatedArea area = MapBuilder.findEmptyChunk(11, 10);
                                        final Inferno inferno = new Inferno(player, area, true);
                                        inferno.setWave(wave);
                                        inferno.constructRegion();
                                    } catch (OutOfSpaceException e) {
                                        log.error("", e);
                                    }
                                });
                                player(20, "No.");
                            }
                        });
                    });
                });
                player(20, "No.");
                npc(25, "You don\'t seem to have enough gold in your " + feeLocation + ", JalYt. Come back when you do.");
                npc(30, "You entered an invalid wave, JalYt. Try again.").executeAction(() -> setKey(16));
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {TzHaar.TZHAAR_KET_KEH};
    }
}
