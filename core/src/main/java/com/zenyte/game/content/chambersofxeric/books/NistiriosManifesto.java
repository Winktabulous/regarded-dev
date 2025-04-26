package com.zenyte.game.content.chambersofxeric.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. nov 2017 : 6:51.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class NistiriosManifesto extends Book {

	private static final String CONTEXT = "Oh happy accident! Magic has been child's play to me, all my life. With a few words of power, "
			+ "I could twist the world to suit my pleasure. <br> Our Lord Xeric has made full use of my gifts; "
			+ "I dare say that my power was instrumental in his ascent to greatness, and he rightly rewarded "
			+ "me by appointing me as his High Priest. <br> It is, of course, a matter of regret that I could "
			+ "not sustain our dominion over the city of Kourend. But Kourend is a limited place, full of "
			+ "limited people. In these caverns, I have no doubt that we shall all learn to transcend such "
			+ "limits - indeed, my accident has already imbued me with power beyond my imaginings. <br> We "
			+ "had always known that this mountain housed great power; that is why we travelled here after "
			+ "our exile from Kourend. However, it is one thing to perceive power, but it is often much harder "
			+ "to wield it for oneself. And then came my accident. <br> I had entered a cavern in the great "
			+ "labyrinths under Mount Quidamortem, and found it to contain the same purple crystals that "
			+ "surround the Dark Altar. <br> Immediately I was drawn to investigate, for such crystals are "
			+ "a sign of the Dark Altar's magicks. <br> If we could learn to wield this power for ourselves, "
			+ "nothing could prevent our triumphant return to conquer Kourend once more. <br> While my "
			+ "attention was focused solely on the crystals, I failed to detect a tremor in the cavern. "
			+ "Suddenly the roof gave way, and I felt myself pinned to the ground by the crushing weight "
			+ "of the rocks falling from above. <br> I admit I panicked - Archeuus Elder though I am, I am "
			+ "not invulnerable, and the power within the mountain could have destroyed me. <br> In my shock, "
			+ "I failed to maintain the bindings of my incorporeal form, so my essence flowed outwards, "
			+ "intersecting with the rocks and the fragments of crystal within them. <br> That was how it "
			+ "happened. I should have faded away into oblivion, but instead I felt the rocks and crystal "
			+ "shards respond to my essence. <br> I could move them. I could control them. And I could control "
			+ "the power that resided within them. <br> Through such a simple accident, I had stumbled upon a "
			+ "method of wielding the power of the caves for myself. The rocks and crystals had become and avatar "
			+ "for my essence, and their power was mine to command. <br> I returned, transfigured, to the others. "
			+ "They were astonished by my new form, and feared for their lives. <br> They were right to be fearful. "
			+ "My power was beyond anything they could comprehend. With a mere thought I could have collapsed "
			+ "the mountain upon them all, then returned to claim the city as my own. <br> In fact, that would "
			+ "be fitting. Xeric appointed me as his High Priest, but the time of priests is over. <br> What is "
			+ "a priest? <br> A priest is a conduit between the mundane and the transcendent, yet the priest "
			+ "remains mundane. A priest is a guide, teaching the plodding folk of this world to look beyond "
			+ "the everyday toil of their labour, elevating their thoughts to seek the divine, yet the priest "
			+ "must plod among them. <br> I am not the priest. I am the god. <br> I am the god, and I need no "
			+ "priests. There is no need for a guide or conduit, not when my power is manifest. <br> If Xeric "
			+ "will not recognize me as his lord and master, away with him! <br> Our lord Xeric commanded that "
			+ "we preserve this document as a warning. Let the fate of Vasa Nistirio serve as an example to "
			+ "anyone who dares challenge Lord Xeric. <br> Although the former High Priest had indeed become "
			+ "mighty, Xeric was able to bind him to four great crystals of power, imprisoning his avatar forever "
			+ "in this place. <br> As a punishment for his hubris and treachery, he shall stand guard in Xeric's "
			+ "chambers until the mountain crumbles to dust and his essence is finally released into oblivion.";
	
	public NistiriosManifesto(Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "Nistirio's manifesto";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
