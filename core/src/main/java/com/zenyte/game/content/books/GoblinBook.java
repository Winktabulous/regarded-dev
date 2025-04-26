package com.zenyte.game.content.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 1. apr 2018 : 17:26.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class GoblinBook extends Book {
	
	private static final String CONTEXT = "In beginning all gods have big war. Each god find army to fight for him. "
			+ "Big High War God not have army. Big High War God go to short-beardy-people and ask, Will you fight in "
			+ "my army? But beardy-short-people say, No, we fight for god of shiny light. <br> Then Big High War God "
			+ "go to demons and ask, Will you fight in my army? But demons say, No, we fight for God of Dark Fire. <br> "
			+ "Then Big High War God go to tall people with keen blades and ask, Will you fight in my army? But tall "
			+ "people with keen blades say, No, some of us fight for god of shiny light and some of us fight for God "
			+ "of Dark Fire, but none of us fight for you! <br> Big high war god very sad. He travel east and west, "
			+ "north and south, across land looking for army to fight for him. <br> Then goblins say, We fight for you! "
			+ "At that time goblins very weak, very small, soft skin. Not like goblins today! But Big High War God say, "
			+ "I will make you my army. So Big High War God train goblins so they very strong. He give them good armour "
			+ "so they not be harmed. He make them strong in spirit so they not afraid of battle. He give them commanders "
			+ "so they know which way to go. He divide goblins into twelve tribes and send them into battle! <br> Goblin "
			+ "armies fight very good! When other gods see this, they very jealous. So they say, We want goblins to fight "
			+ "for us too! So Big High War God take some tribes and sell them, one to God of Shiny Light and one to God of "
			+ "Dark Fire, and other tribes to other gods. But most still worship Big High War God who created them! <br>  <br> "
			+ "The Several Commandments <br> These are commandments of Big High War God! Obey all commandments all time or "
			+ "Big High War God kill you very bad!! <br> Always to slay enemies of Big High War God. Enemies must die! "
			+ "Not to run from battle. Cowards must die! Not to show mercy. Merciful must die! Not to doubt Big High War "
			+ "God. Doubters must die! Not to make own plans. Thinkers must die! <br> The end of the war and the prophecy "
			+ "<br> The war of gods last many lifetimes. Battle is glorious and many heroes live and die! Then all gods "
			+ "leave world, leave their armies behind. But goblins still soldiers, still fight! Goblins fight against tall"
			+ " people with keen blades. But tall people build cities with walls, they not want to fight. They not true "
			+ "soldiers like goblins! But now goblins not have enough to eat, and have no commanders to tell them who to "
			+ "fight. So goblin tribes fight one another. At last all goblin tribes have big battle on plain of mud. Battle"
			+ " last many days and many goblins die, battle is glorious! But goblin corpses cover the ground and it look "
			+ "like all die. <br> Then Hopespear of the Narogoshunn tribe have vision of Big High War God. In night while "
			+ "soldiers rest he call leaders of all tribes together to give them message. <br> This is the word of Big High"
			+ " War God: Battle is indeed glorious and goblins are soldiers but if there too much battle all goblins die! "
			+ "No more must goblin fight against goblin or tribe against tribe. Goblins must find other enemies to fight, "
			+ "but not fight each other! <br> And this is the word of Big High War God: Today I cannot lead you, but someday"
			+ " I will send a new Commander to lead you. Under new Commander goblins will conquer all of RuneScape, every "
			+ "race and every god! And then Big High War God will return and sit on throne of bronze and rule over all. War "
			+ "will end in victory and victory will last forever! So leaders of tribes stop battle. And on plain of mud "
			+ "all tribes build temple to Big High War God and offer sacrifices.";

	public GoblinBook(final Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "The creation of the Goblins";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
