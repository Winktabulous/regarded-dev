package com.zenyte.game.content.chambersofxeric.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. nov 2017 : 7:00.47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TransdimensionalNotes extends Book {

	private static final String CONTEXT = "This has not been our finest hour. <br> We have faced many dangers "
			+ "since our departure from Kourend, and every single one has been overcome. The privations of the "
			+ "journey to Quidamortem, the ferocious beasts that dwelt here, even the weakness and treachery of "
			+ "our former allies - we have survived them all, and marched on triumphantly. <br> For this we give "
			+ "thanks to our mighty lord, and we shall continue to serve him loyally as we explore these caverns. "
			+ "However, I must admit this latest experiment has not been a success. <br> In the caverns beneath "
			+ "Quidamortem, our lord sensed a disruption in the fabric of reality. <br> It is known, of course, "
			+ "that the Dark Altar can connect to other planes of existence. That is how it sustains our runecrafting "
			+ "altars, by linking Zeah to whatever far-off realms contain the true Blood and Soul Altars. However, "
			+ "it still surprised us to find that the same power was available here. <br> Naturally our lord sought "
			+ "to tap into this power - after all, the whole point of us coming to this cursed mountain was to seek "
			+ "new sources of power, so that we might eventually return to re-conquer Kourend. <br> Unfortunately, "
			+ "our lord's attempts to rupture space and time was only too successful! <br> Our lord managed, with "
			+ "much effort, to widen the disruption until a stable portal stood before us, allowing us to see out of "
			+ "this world and into some other realm. <br> Through the portal, we glimpsed a strange, red-hued world, "
			+ "where monstrous creatures lurched through the tendrils of a landscape that looked - and smelt - like "
			+ "the intestines of a rotting corpse. <br> In our awe, we looked for too long, forgetting that a door "
			+ "opens in two directions. We could see these creatures, and they could see us. <br> Suddenly a vast "
			+ "form squeezed its way through the portal towards us. I have no words for it, but if a demon were to "
			+ "mate with a wasp, this creature would surely be the ungodly fruit of the union. <br> We sought to "
			+ "drive it back with our magicks, but it seemed well protected from such attacks, and eventually our "
			+ "lord bade us to retreat. Some of our minor servants had to be left behind as a distraction for the "
			+ "creature; their sacrifice bought us time to block the chamber behind us. <br> Our lord fears that "
			+ "out experiment has permanently ruptured the walls between the worlds - even if we can find a way to "
			+ "close this portal, more may open spontaneously now that the way has opened. We shall have to tread "
			+ "carefully in the future as we explore these caverns, lest we stumble across more such portals. <br> "
			+ "However, the ability to open these portals may prove valuable, despite our current setback. <br> I "
			+ "have seen our lord defeated before, back when we were driven out of Kourend, and I know he does not "
			+ "accept defeat. <br> Just as he responded to our exile by leading us to Quidamortem in search of "
			+ "greater powers, I am sure he will respond to today's developments by finding a way to use them for "
			+ "his own purposes. After all, these portals would give us access to a limitless army of fearsome "
			+ "creatures, if we could find a way to control them. <br> It's almost a shame that creature keeper "
			+ "died so early in the expedition - he was a fool, but his talents might have served us well here. <br> "
			+ "Alternatively, if we were able to open a portal to a different place, we may even discover new "
			+ "worlds to conquer... <br> But that is speculation. For now, our lord commands us to return to the "
			+ "chamber of the portal and the demonic wasp - perhaps he has a plan to slay the creature and take "
			+ "control of the portal. <br> Our lord's council is smiling at me strangely as we prepare for our next "
			+ "battle with the creature. If I did not know better, I might suspect they were planning to use me to "
			+ "distract the beast, like we did with the servants before. <br> However, I have served Lord Xeric "
			+ "loyally for many years, through good times and bad. <br> I am sure he would never leave me to die "
			+ "like this.";
	
	public TransdimensionalNotes(Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "Transdimensional notes";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
