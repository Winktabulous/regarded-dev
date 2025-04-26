package com.zenyte.game.content.chambersofxeric.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. nov 2017 : 7:13.03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class HoundmastersDiary extends Book {

	private static final String CONTEXT = "Please, dear reader, blame me not for my sins... <br> <br> <br> <br> <br> <br> <br> "
			+ "<col=a00808> Entry 1 <br> How did I find myself in this damp and smelly hell-hole? I fear "
			+ "I can no longer leave; woe to those brave enough to try. I had received a letter, a request for "
			+ "my skills in hound mastery, to replace a creature keeper who had 'retired'. Or so the letter "
			+ "claimed. <br> Sadly the truth is that anyone could have done this, but the pay was too good to "
			+ "refuse. <br> Admittedly the location was a little odd - a mountain top, far to the west? "
			+ "Unfortunately I am but a man controlled by greed, and for that I now pay dearly."
			+ " <br> <br> <col=a00808> Entry 2 <br> In the beginning, there were hounds to care for, more "
			+ "than a dozen strays. They seemed to enjoy my company, and I enjoyed theirs. <br> My employer, "
			+ "Lord Xeric, was conducting various experiments on the hounds. <br> Using the powers of this strange "
			+ "place, he sought to speed up the mutation that he claimed to be the destiny of all races. <br> I "
			+ "was too scared to say anything. I'd heard wild rumours of the things hidden within these dark "
			+ "walls. <br> Monsters? <br> Cursed employees who had failed Lord Xeric, perhaps? <br> I dared "
			+ "not think it - I focused all my energies on the task Lord Xeric had assigned me, so that I would "
			+ "not experience his displeasure for myself! <br> <br> <col=a00808> Entry 3 <br> The crystals? "
			+ "The beast below? Science? Something down here made Lord Xeric's experiment a success, or at least "
			+ "that's what he tells me. I fail to see the success of his creation. <br> It had to be her though, "
			+ "of course it did. She was the strongest of the pack, my personal favourite, now a twisted, scaly "
			+ "abomination. <br> <br> <br> <br> <br> <br> <br> <col=a00808> Entry 4 <br> Does she even remember me anymore? Would I want "
			+ "her to? <br> Certainly not after what I've done! <br> I'm not proud of it, but it keeps me alive "
			+ "enough to fulfill Master's demands, keeping her - no - keeping IT alive. <br> <col=a00808> Entry 5 "
			+ "<br> When I believed that this situation couldn't become worse, I was a fool! <br> Before Master "
			+ "began his wretched experiments, we failed to notice her... condition. There's two of them now. <br> "
			+ "Perhaps I'm not the true monster after all. <br> <br> <br> <br> <br> <br> <br> <br> <col=a00808> Entry 6 <br> A byproduct of the "
			+ "experiments is what keeps the creatures fed and well. My new role is more of a gardener than anything "
			+ "else. <br> Now I labour in an incredibly draining job for a master who hasn't been here for months. "
			+ "Is my position a little more permanent than I had realised? <br> <br> <br> <br> <br> <br> <br> <col=a00808> Entry 7 <br> The "
			+ "mountain just shook! Has Master returned to us? <br> I must check on them, for he certainly will "
			+ "return if I fail him.";
	
	public HoundmastersDiary(Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "Houndmaster's diary";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
