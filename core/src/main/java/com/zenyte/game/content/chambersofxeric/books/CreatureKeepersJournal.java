package com.zenyte.game.content.chambersofxeric.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. nov 2017 : 6:31.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class CreatureKeepersJournal extends Book {

	private static final String CONTEXT = "These scavengers are fascinating. I have read of similar things in the tales of the seafaring folk, "
			+ "who described voyages to distant lands where scuttling creatures called 'skavids' lurked in dark "
			+ "caves, stealing food and shiny objects from nearby settlements. <br> Our scavengers may be a related "
			+ "branch of the species, though there is no sign of any intelligence here. <br> Nevertheless, the larger "
			+ "scavengers have a degree of brute strength, and I envisage they could be useful as warriors if "
			+ "they can be suitably trained. <br> First I must teach my scavengers to obey me. I will begin by making "
			+ "them dependent upon me for food, so that I can use food to reward obedience. <br> I have collected a "
			+ "supply of cavern grub cocoons, to provide a fresh supply of grubs for my scavengers. These are "
			+ "securely locked away, so that the scavenger cannot obtain them for itself. <br> Next, I began to feed "
			+ "a large scavenger with the grubs. I have fed it daily in its food trough; it no longer even "
			+ "thinks of going scavenging for itself, so it is becoming completely dependent on me. <br> Soon I "
			+ "will begin to send it into battle against the other creatures that roam these caves, and teach "
			+ "it that it must fight if it wants to be fed. <br> This technique of instilling dependency is "
			+ "marvellous. If I can make it work on scavengers, consider how it could be applied to a wider "
			+ "range of minions. Perhaps one day I could even apply it to the citizens of Kourend, if we can "
			+ "ever return to that city, by taking administrative control of all means of production so that "
			+ "the citizens are fed only at the 'benign' hand of their masters. This may prove a more effective "
			+ "means of control then the brutish methods that failed us before. But that is a consideration for "
			+ "another day. <br> Meanwhile I have found some lizardmen in this cave that look like a suitable combat "
			+ "challenge for my scavenger - unfortunately the creatures saw me, and they attacked immediately. <br> "
			+ "I had no trouble retreating, though I have begun to feel unwell since the incident; I do hope "
			+ "their spit has not infected me.";
	
	public CreatureKeepersJournal(Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "Creature keeper's journal";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
