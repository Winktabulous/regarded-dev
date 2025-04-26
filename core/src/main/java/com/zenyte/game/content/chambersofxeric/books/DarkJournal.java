package com.zenyte.game.content.chambersofxeric.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. nov 2017 : 7:14.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DarkJournal extends Book {

	private static final String CONTEXT = "Our exploration of the caves is complete, and has been a success. <br> "
			+ "Our lord's followers have dwindled in number, it is true. Few of us who shared his exile from Kourend "
			+ "are still alive to serve him today. <br> Many fell to the perils of the cave; they were weak, and our "
			+ "lord teaches us that the weak should expect no other fate. <br> Others forced our lord to discipline "
			+ "them, some for outright treachery, some for other failings. Let them remain in this cave as a "
			+ "testament to the supremacy of our lord! <br> Our lord has graciously appointed me as his new High "
			+ "priest, and I shall endeavour to be a more worthy servant than my vain predecessor. Nevertheless, "
			+ "though our party was reduced in numbers, our lord has led us to victory over the Guardian in the Deep, "
			+ "and has seized the power that he sought. <br> Now we make our camp in the Guardian's chamber, among "
			+ "the skulls of its former prey. <br> Although I doubt it has been permanently killed, the Guardian "
			+ "will not trouble anyone again for a long time. <br> The weapons and armour that we have developed in "
			+ "the caves can stay here, in this place of safety. As our lord now wields power over the crystals of "
			+ "the cave, he intends to create a secure seal around our store. He says it would take an earthquake to "
			+ "shatter it! <br> I am truly awestruck by the power that our lord now wields. If we were to return to "
			+ "Kourend, as he originally planned, I have no doubt that we would regain our former dominion over the "
			+ "city with very little effort. <br> However, while our lord remains as secretive as ever - in fact, he "
			+ "has become more so after the treachery of our former followers - I sense he has a new plan in mind. <br> "
			+ "Perhaps the throne of Kourend has become too small a trophy to satisfy the ambitions of Lord Xeric.";
	
	public DarkJournal(Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "Dark journal";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
