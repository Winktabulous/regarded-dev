package com.zenyte.game.content.chompy.plugins;

import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.chompy.Chompy;
import com.zenyte.game.content.chompy.ChompyBirdHat;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Rantz extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    player("Hey, I hear you're giving away free hats!");
                    npc("Huh, dat's a true thing but you's godda be da good shooter wiv da stabbie chucker and stick " + "lotsachompies for da hatsies.");
                    final Item bow = new Item(ItemId.OGRE_BOW);
                    final int offset = ChompyBirdHat.values.length;
                    options(TITLE, new DialogueOption("Where can I get an ogre bow?", key(5)), new DialogueOption("How many chompies do I have to stick?", key(15)), new DialogueOption("Can I have a hat please?", key(20)), new DialogueOption("Can I have an ogre bellows please?", key(35 + offset)), new DialogueOption("Ok thanks."));
                    final int amount = Utils.random(500, 550);
                    player(5, "Where can I get an ogre bow?");
                    npc("Mee's has, but is not for freeness, dis one cost " + amount + " of da gold coinses. Does you want it?");
                    options(TITLE, new DialogueOption("Yes, I'll buy the bow", key(10)), new DialogueOption("No thanks..."));
                    final Item coins = new Item(ItemId.COINS_995, amount);
                    player(10, "Yes, I'll buy the bow.").executeAction(() -> {
                        if (player.carryingItem(coins) && player.getInventory().hasFreeSlots()) {
                            player.getInventory().deleteItem(coins);
                            player.getInventory().addItem(bow);
                        } else {
                            player.sendMessage("You do not have a free inventory space or enough coins to buy the bow.");
                        }
                    });
                    if (player.carryingItem(coins) && player.getInventory().hasFreeSlots()) {
                        doubleItem(coins, bow, "You hand over the coins... and Rantz hands over the bow.");
                        player("Thanks.");
                    }
                    player(15, "How many Chompies do I have to stick?");
                    npc("We'll dat's da fing wiv da hatsies, dey's many hatsies for da many chompies dat you stick. " + "But you's godda get at least this many for a hat. <col=0000FF>~Rantz flashes fingers at " + "you, you think he means 30.~");
                    final ObjectArrayList<ChompyBirdHat> hats = getHats(player);
                    player(20, "Can I have a hat please?");
                    npc("You's creature show me da bow den!");
                    item(bow, "You show Rantz the marks on your bow.");
                    if (hats.isEmpty()) {
                        npc("Sorry, but der's no new hats for you this time! Go shoot some more chompies.");
                    } else {
                        npc("Hey, you did good wiv da stabbie chucker, der's a flappy hat for ya.");
                        hats.iterator().forEachRemaining(hat -> {
                            final Item item = new Item(hat.getItemId(), 1);
                            if (player.getInventory().hasFreeSlots()) {
                                item(item, "<col=0000FF> ~ Rantz shows you a new hat that you can take ~<br>~ as a reward for your chompy bird kill total. ~<br>~ This is the hat for <col=FF0000>" + hat.getTitle() + " ~").executeAction(() -> {
                                    updateDiaries(player, hat);
                                    player.getInventory().addOrDrop(item);
                                });
                            }
                        });
                    }
                    final Item bellows = new Item(ItemId.OGRE_BELLOWS, 1);
                    player(35 + offset, "Can I have an ogre bellows please?");
                    if (player.containsItem(ItemId.OGRE_BELLOWS)) {
                        npc("You's already got da bellows. If you's lose it come back to me.");
                    } else {
                        npc("Sure, you's godda haf a bellows to hunt da chompies.").executeAction(() -> player.getInventory().addItem(bellows));
                        if (player.getInventory().hasFreeSlots()) {
                            item(bellows, "Rantz hands you an ogre bellows.");
                        } else {
                            player.sendMessage("You need a free slot to take the ogre bellows from Rantz.");
                        }
                    }
                }
            });
        });
    }

    // Repetitive code, idc.
    private void updateDiaries(final Player player, final ChompyBirdHat hat) {
        player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT);
        if (hat == ChompyBirdHat.OGRE_FORESTER) {
            player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT_125_KILLS);
            player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT_300_KILLS);
            player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT_1000_KILLS);
        } else if (hat == ChompyBirdHat.MARKSMAN) {
            player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT_125_KILLS);
            player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT_300_KILLS);
        } else if (hat == ChompyBirdHat.YEOMAN) {
            player.getAchievementDiaries().update(WesternProvincesDiary.CLAIM_CHOMPY_BIRD_HAT_125_KILLS);
        }
    }

    private ObjectArrayList<ChompyBirdHat> getHats(final Player player) {
        final ObjectArrayList<ChompyBirdHat> list = new ObjectArrayList<ChompyBirdHat>();
        for (ChompyBirdHat hat : ChompyBirdHat.values) {
            if (!player.containsItem(hat.getItemId()) && player.getNumericAttribute(Chompy.KILL_ATTRIB).intValue() >= hat.getKills()) {
                list.add(hat);
            }
        }
        return list;
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.RANTZ };
    }
}
