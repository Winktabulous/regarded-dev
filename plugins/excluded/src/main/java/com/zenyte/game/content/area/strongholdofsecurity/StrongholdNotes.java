package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.content.Book;
import com.zenyte.game.content.ChapteredBook;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 4. sept 2018 : 21:33:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class StrongholdNotes extends ItemPlugin {

	private static final class StrongholdNotesBook extends ChapteredBook {

		public StrongholdNotesBook(final Player player) {
			super(player);
		}

		@Override
		public String[] getChapters() {
			return new String[] { "<col=000080>Description", "<col=000080>Level 1", "<col=000080>Level 2", "<col=000080>Level 3",
					"<col=000080>Level 4", "<col=000080>Navigation", "<col=000080>Diary" };
		}

		@Override
		public String[] getContent() {
			return new String[] {
					"<col=000080> Description </col> <br> This stronghold was unearthed by a miner prospecting for new ores around the Barbarian Village. After gathering some equipment he ventured into the maze of tunnels and was missing for a long time. He finally emerged along with copious notes regarding the new beasts and strange experiences which had befallen him. He also mentioned that there was treasure to be had, but no one has been able to wring a word from him about this, he simply flapped his arms and slapped his head. This book details his notes and my diary of exploration. I am exploring to see if I can find out more...",
					"<col=000080> Level 1 </col> <br> As well as goblins, creatures like a man but also like a cow infest this place! I have never seen anything like this before. The area itself is reminiscent of frontline castles, with many walls, doors, and skeletons of dead enemies. I'm sure I hear voices in my head each time I pass through the gates. I have dubbed this level War as it seems like an eternal battleground. I found only one small peaceful area here.",
					"<col=000080> Level 2 </col> <br> My supplies are running low and I find myself in barren passages with seemingly endless malnourished beasts attacking me, ravenous for food. Nothing appears to be able to grow, many adventurers have died through lack of food and the very air appears to such vitality from me. I've come to call this place famine.",
					"<col=000080> Level 3 </col> <br> Just breathing in this place makes me shudder at the thought of what foul disease I may contract. The walls and floor ooze and pulsate like something pox ridden. There is a very strange beast whom I narrowly escaped from. At first I thought it to be a cross between a cow and a sheep, something domesticated... but when it looked up at me I was overcome with weakness and barely got away with my life! Luckily I found a small place where I could heal myself and rest a while. I have named this area pestilence for it reeks with decay.",
					"<col=000080> Level 4 </col> <br> On my first escapade into this place, I was utterly shocked. The adventurers who had come before me must have made up a tiny proportion of the skeletons of the dead. Nothing truly alive exists here, even those beings who do wander the halls are not alive as such, but they do know that I am and I get the distinct impression that were they to have their way, I would not be for long! Death is everywhere and thus I shall name this place. There is one small place of life, which was gladdening to find and very worth my while!",
					"<col=000080> Navigation </col> <br> After getting lost several times I finally worked out the key to all the ladders and chains around this death infested place. All ropes and chains will take you to the start of the level that you are on. However most ladders will simply take you to the level above. The one exception is the ladder in the bottom level treasure room, which appears to lead through several extremely twisty passages and eventually takes you out of the dungeon completely. The portals may be used if you are of sufficient level or have already claimed your reward from the treasure room.",
					"<col=000080> Diary </col> <br> Day 1 <br> Today I set out to find out more about this place. From my research I knew about the strange creatures, so I have prepared with some good armour. <br> <br> "
							+ "Day 2 <br> I have fought my way through the fearsome beasts on the first level and am preparing myself to journey deeper. I hope that things are not too difficult further on as I am already sick of bread and cheese for dinner. <br> <br> "
							+ "Day 3 <br> I ventured down into the famine level today... I was wounded and have returned to the relative safety of the level above. I am going to try to make my way out through the goblins and mancow things... I hope I can make it..." };
		}

		@Override
		public String getTitle() {
			return "Stronghold of Security - Notes";
		}

	}

	@Override
	public void handle() {
		bind("Read", (player, item, slotId) -> Book.openBook(new StrongholdNotesBook(player)));
	}

	@Override
	public int[] getItems() {
		return new int[] { 9004 };
	}

}
