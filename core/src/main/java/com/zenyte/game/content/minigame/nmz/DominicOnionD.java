package com.zenyte.game.content.minigame.nmz;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.OptionMessage;

public class DominicOnionD extends Dialogue {
	
	private final String option;
	private String mode;
	protected String difficulty;

	public DominicOnionD(Player player, String option) {
		super(player, 1120);
		this.option = option;
	}

	@Override
	public void buildDialogue() {
		if(option.equals("dream"))
			dreams();
		
		npc("Welcome to the Nightmare Zone!<br> Would you like me to create a dream for you?");
		showOptions();
			
		player(10, "Who are you?");
		npc("My name's Dominic Onion but you can call me Dom, or Mr Onion.");
		player("Mr Oni...Dom, how did you get that name?");
		npc("Well that's a long story...but I can tell you that it involved my parents, 3 blurberry specials, and a chinchompa.");
		player("...and a chinchompa?");
		npc("Oh yes, Nigel played a crucial role in the whole debacle. It's all very amusing now that I think about it.");
		player("Right...");
		npc("Good times...");
		showOptions();
			
		player(20, "What is this place?");
		npc("This is the Nightmare Zone!");
		player("Wwwwhat?!");
		npc("Don't worry, it's not as bad as it sounds...most of the time.");
		player("That's comforting.");
		npc("Truth be told, that name wasn't my first choice, it's what the local folk have been calling it and so it sort of stuck.");
		npc("My original plan came to me in a vivid dream when I was younger you see, I saw myself running a successful business from a great structure in a strange land.");
		npc("Well, I say strange, but I'd never left Lunar Isle at the time and this place was very hot and dry, unlike anything I'd seen before.");
		npc("When I told my father about the dream he laughed and said I should become a banker like him and his father before him but in that moment I knew that it wasn't the life for me.");
		npc("I had to find out what this dream meant and if it was a vision of the future or not.");
		npc("Growing up, I studied hard to find out more about magic, especially oneiromancy and the interpretation of dreams.");
		npc("Having learned as much as I could on Lunar Isle I set out to find and learn from other great wizards I'd heard of in stories as a child.");
		npc("I set off to travel the world, discovering more about dreams to enable me to understand the dream I'd had as a child.");
		npc("I needed to know what this dream meant and if it was truly a vision of the future");
		npc("If so I knew I had to try and find this location and build the huge tower I saw");
		npc("I was going to call it 'Dom Onion's Tower', but I've not found this strange place or gathered enough money to build it just yet.");
		npc("That's why I've setup this small business venture in the mean time selling dreams to people. I just need a big enough crowd to kickstart my business.");
		npc("It's all part of my five year plan, and who knows, perhaps my dream will come true; I'll discover this place and build Dom Onion's Tower!");
		showOptions();
			
		player(40, "Can you tell me about the dreams?");
		npc("Certainly, I can delve into your memories and search for key events that have affected you on your adventures.");
		npc("I then take the essence of these memories and put that in the vial you see on the plinth");
		npc("I imbue a bit of magic, then give the vial a bit of a shake, and when you drink it you'll be able to re-live an encounter close to the original.");
		npc("Don't worry though, when you're in a dream you're perfectly safe even if it does seem very real.");
		npc("If you successfully overcome the dream or die trying, you'll leave the dream state and return to normal with your grey matter and appendages intact.");
		player("Are all dreams the same or are there different options?");
		npc("Oh no, they can vary wildly! You can choose from one of three main options");
		npc("Firstly, I can create a one-on-one encounter with an adversary which you've defeated in your adventures. People like to practice in these dreams");
		npc("Then there's a sort of Endurance dream, where you face enemies in encounter after encounter, and try to survive to the end");
		npc("And finally there's something I like to call a Rumble, where you face multiple adversaries at the same time. I imbue a bit more magic and shake the vial harder to create that one!");
		npc("You can invite friends to fight alongside you in a Rumble dream. Once I've set up the dream for you, step into the enclosure and invite up to 4 friends.");
		npc("For my Endurance and Rumble dreams, I charge a fee for my work. I don't like handling the money directly, so instead I've set up a secure coffer.");
		npc("After you've asked me to set up a dream, put some money in my coffer. I'll deduct the fee from the coffer when you start the dream");
		npc("There's a red vial on the other plinth. Your friends can drink from it to enter your dream as a spectator to observe your progress.");
		npc("Finally here's one other option you get, and that's how difficult the encounters are. As well as the normal dreams, the bravest adventurers can choose a harder version*.");
		plain("*Terms and conditions apply: Mr Onion, henceforth known as Dom, cannot be held liable for accidental loss of limbs (yours or otherwise) and or psychological damage as a payload of entering the Nightmare Zone. Please note there is a strict no-refund policy, and all dream purchases are final.");
		showOptions();
			
		player(60, "Can I choose a dream?");
		npc("Certainly. You can practice for free, but the larger dreams require more skill to imbue the potions and so there's a fee for those.");
		dreams();
					
		options(65, mode + " - choose a difficulty mode",
				"Normal",
				"Hard")
		.onOptionOne( () -> difficulty = "normal" )
		.onOptionTwo( () -> difficulty = "hard" );
		
		player(99, "I'll come back another time.");
	}
	
	private void showOptions() {
		options(TITLE,
				"Who are you?",
				"What is this place?",
				"Can you tell me about the dreams?",
				"Can I choose a dream?",
				"I'll come back another time.")
		.onOptionOne( () -> setKey(10) )
		.onOptionTwo( () -> setKey(20) )
		.onOptionThree( () -> setKey(40) )
		.onOptionFour( () -> setKey(60) )
		.onOptionFive( () -> setKey(99) );
	}
	
	private void dreams() {
		options("Which dream would you like to experience?",
				"Practice",
				"Endurance",
				"Rumble",
				"Previous: Customisable Rumble (hard)")
		.onOptionOne( () -> {
			((OptionMessage) dialogue.get(65)).setTitle("Practice - choose a difficulty mode");
			setKey(65);
		})
		.onOptionTwo( () -> { 
			((OptionMessage) dialogue.get(65)).setTitle("Endurance - choose a difficulty mode");
			setKey(65);
		})
		.onOptionThree( () -> { 
			((OptionMessage) dialogue.get(65)).setTitle("Rumble - choose a difficulty mode");
			setKey(65);
		})
		.onOptionFour( () -> { 
			((OptionMessage) dialogue.get(65)).setTitle("Last - choose a difficulty mode");
			setKey(65);
		});
	}
	
	
}
