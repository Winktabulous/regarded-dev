package com.zenyte.game.content.minigame.warriorsguild.dialogue;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. dets 2017 : 4:40.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HarrallakMenarousPlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Welcome to my gumble guild " + player.getPlayerInformation().getDisplayname() + ".");
                    options(TITLE, "Quite a place you've got here.", "You any good with a sword?", "Bye!").onOptionOne(() -> setKey(10)).onOptionTwo(() -> setKey(80)).onOptionThree(() -> setKey(70));
                    player(10, "Quite a place you've got here.");
                    npc("Indeed we do. Would you like to know more about it?");
                    options(TITLE, "Tell me more about the strength training area.", "Tell me more about the attack training area.", "Tell me more about the defence training area.", "Tell me more about the combat training area.", "Tell me about tokens.").onOptionOne(() -> setKey(20)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(40)).onOptionFour(() -> setKey(60)).onOptionFive(() -> setKey(150));
                    player(20, "Tell me about the strength training area.");
                    npc("Ahh the mighty warrior Sloane guards the strength training " + "area. This intriguing little area consists of two shotput " + "lanes for different weights of shot. It's fairly simple, the " + "Referee or Sloane can explain more. you");
                    npc("may find yourself panting for breath though.");
                    player("Oh? Why?");
                    npc("Well you see my dear boy, the games there really do sap your " + "energy quite a bit. You can find it just up the stairs behind " + "the bank.");
                    options(TITLE, "Tell me more about the attack training area.", "Tell me more about the defence training area.", "Tell me more about the combat training area.", "Tell me about tokens.").onOptionOne(() -> setKey(30)).onOptionTwo(() -> setKey(40)).onOptionThree(() -> setKey(60)).onOptionFour(() -> setKey(150));
                    player(30, "Tell me about the attack training area.");
                    npc("Ahhh, dummies.");
                    player("I'm no dummy, I just want to know what is there!");
                    npc("Oh no my dear man, I did not mean you at all! The training " + "area has mechanical dummies which pop up out of holes " + "in the floor. The noble dwarf Gamfred invented the mechanism " + "and Ajjat can explain more about what");
                    npc("to do there.");
                    player("Oh, ok, I'll have to try it out!");
                    npc("You can find it just down the corridor and on your right.");
                    options(TITLE, "Tell me more about the strength training area.", "Tell me more about the defence training area.", "Tell me more about the combat training area.", "Tell me about tokens.").onOptionOne(() -> setKey(20)).onOptionTwo(() -> setKey(40)).onOptionThree(() -> setKey(60)).onOptionFour(() -> setKey(150));
                    player(40, "Tell me about the defence training area.");
                    npc("To polish your defensive skills to the very highest level " + "we've employed a most inventive dwarf and a catapult.");
                    player("You're going to throw dwarves at me?");
                    npc("Oh my no! I think Gamffred would object to that most strongly.");
                    npc("He's an inventor you see and has built a marvellous contraption " + "that can throw all sorts of things at you including magic " + "missiles...");
                    player("Mmmm?");
                    npc("....spiked iron balls.....");
                    player("Err....");
                    npc("....spinning slashing blades....");
                    player("Ummmm...");
                    npc("... and even anvils.");
                    player("ANVILS!?");
                    npc("No need to be afraid, it's all under very controlled conditions! " + "You can find it just up the stairs behind the bank.");
                    player(60, "Tell me about the combat training area.");
                    npc("Ah yes, our resident magician from foreign lands created a " + "most amazing gadget which can turn your own armour " + "against you! It's really quite intriguing.");
                    player("The sounds dangerous! What if I'm wearing at the time?");
                    npc("So far that's not happened. You need to speak to Shanomi about " + "the specifics of the process, but as I understand it, " + "putting a suit of armour in one of these devices will " + "make it come to life some how. The better");
                    npc("the armour, the harder it is to 'kill'.");
                    player("Fighting mt own armour, that sounds... weird. I could be " + "killed by it...");
                    npc("Indeed we have had a few fatalities from warriors over " + "stretching themselves and not knowing their limited. " + "Start small and work up is my motto! That and go see Lidio " + "for some food if you need it.");
                    player("Ok, thanks for the warning.");
                    player(70, "Bye!");
                    npc("Farewell brave warrior, I do hope you enjoy my guild.");
                    player(80, "You any good with a sword?");
                    npc("Am I any good with....a sword... Have you any clue " + "who I am?");
                    player("Not really... no.");
                    npc("Why I could best any person alive in a rapier duel!");
                    player("Try me then!");
                    npc("My dear man, I couldn't possibly duel you, I might hurt you " + "and then what would happen to my reputation! Besides, " + "I have this wonderful guild to run. Why don't you " + "take a look at the various activities we");
                    npc("have. You might even collect enough tokens to be allowed " + "in to kill the strange beasts from the east!");
                    options(TITLE, "Tell me more about the strength training area.", "Tell me more about the attack training area.", "Tell me more about the defence training area.", "Tell me more about the combat training area.", "Tell me about tokens.").onOptionOne(() -> setKey(20)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(40)).onOptionFour(() -> setKey(60)).onOptionFive(() -> setKey(150));
                    player(150, "Tell me about tokens.");
                    npc("Ahh, yes, our token system is designed to allow you an " + "appropriate amount of time with my discovery in the " + "very top floor of the guild. I won't spoil the surprise as " + "to what that is. Go up and see for yourself. Now, the");
                    npc("amount of tokens you collect from the various activities " + "around the guild will dictate how long Kamfreena will " + "allow you in the enclosure on the very top floor. More " + "tokens equals more time.");
                    player("So what's up there?");
                    npc("If I told you it would spoil the surprise!");
                    player("Ok ok... so how do I earn and claim these tokens?");
                    npc("You can earn them simply by using the training " + "exercises around the guild, the staff will then enter this " + "into a ledger as you play. You can claim them by " + "simply asking any of the staff at the training areas, or");
                    npc("myself.");
                    player("Sounds easy enough.");
                    options(TITLE, "Tell me more about the strength training area.", "Tell me more about the attack training area.", "Tell me more about the defence training area.", "Tell me more about the combat training area.", "Tell me about tokens.").onOptionOne(() -> setKey(20)).onOptionTwo(() -> setKey(30)).onOptionThree(() -> setKey(40)).onOptionFour(() -> setKey(60)).onOptionFive(() -> setKey(150));
                }
            });
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.HARRALLAK_MENAROUS };
    }
}
