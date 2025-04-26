package com.zenyte.game.content.chambersofxeric.books;

import com.zenyte.game.content.Book;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 19. nov 2017 : 6:57.25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class TektonsJournal extends Book {

	private static final String CONTEXT = "Tekton is strong! Tekton was always strong, but now Master made Tekton "
			+ "even stronger. Tekton is pleased. <br> Tekton has served Master for long time. Tekton built huge "
			+ "buildings, clever buildings with magic in the walls. Tekton made buildings for people to praise "
			+ "Master in, big buildings with pretty decorations so everyone knows how great Master is. <br> Then "
			+ "Master had to leave big city. People ungrateful, didn't understand they should obey Master like "
			+ "Tekton did. <br> Tekton left city with Master, and Master's loyal friends. Master came up mountain "
			+ "to find funny crystals. <br> Tekton was clever, understood all about crystals. Tekton knew how to "
			+ "draw power from crystals to make magic buildings for Master. <br> Tekton not sure how it worked "
			+ "anymore. Ever since Master made Tekton so strong, Tekton can't remember much about crystals. Tekton "
			+ "can't remember how to make buildings either. <br> Tekton can't remember much about Tekton anymore. "
			+ "Didn't Tekton have a spouse back in the big city? Tekton not sure what spouse is for anymore. <br> "
			+ "But that okay. Master made Tekton strong, so Master must want Tekton's strength, not Tekton's "
			+ "memory. <br> Mountain caves have magic that Master wanted, but Master didn't know how to take it. "
			+ "But Tekton clever - Tekton learnt to take the magic from the caves. <br> Tekton built Master a new "
			+ "temple on mountain top. Tekton's temple could focus cave magic, bring it out of caves so anyone "
			+ "could use it without needing to be as clever as Tekton. <br> Master used Tekton's temple to wield "
			+ "cave magic. It made Master very happy. <br> But Master not so happy when everyone else started "
			+ "wielding cave magic too, especially nasty high priest. Master didn't really like the high "
			+ "priest. <br> Master told Tekton to shut down temple. Master wanted Tekton to find new ways to "
			+ "wield cave magic, but so only Master could do it. Tekton broke temple like Master said - but "
			+ "only slightly, so Tekton can fix it again if Master ever changes his mind. <br> Tekton tried "
			+ "to teach Master how to wield cave magic without needing temple, but Master couldn't do it. Maybe "
			+ "Master didn't understand crystals as well as Tekton did. <br> Master not so happy about that. <br> "
			+ "One day Master took Tekton into cave, and promised to reward Tekton for all Tekton service. "
			+ "Master promised Tekton immortality! Tekton very happy. <br> Master created new body for Tekton "
			+ "out of rock and metal from caves, and transferred Tekton's mind into it. Tekton's old body "
			+ "disintegrated, but that okay now Tekton's mind was living in shiny new body that Master made. <br> "
			+ "Master enchanted Tekton's anvil, and brought it into cave, and taught Tekton to use it to fix "
			+ "the new body whenever it get damaged. Master may not understand crystals as well as Tekton, but "
			+ "Master still very powerful. <br> New body is very strong, stronger than Tekton's old body. Master "
			+ "protected it from magic damage and ranged damage too; anyone want to hit Tekton now, Tekton hit "
			+ "them straight back! <br> Now Tekton very strong, but Master's friends were rude. They called "
			+ "Tekton stupid. Tekton got confused - new body is what Master made, so new body must be better. "
			+ "But Master's friends not want to talk to Tekton anymore, especially nasty high priest. <br> So "
			+ "Tekton staying in cave now, near anvil. It safer for Tekton to stay near anvil, so Tekton can "
			+ "repair body if it get damaged. <br> Tekton wonders sometimes why new body doesn't understand "
			+ "crystals as well as old body did. Tekton wonders what happened to Tekton's spouse from big city, "
			+ "or Tekton's children. They sound important, but Tekton can't remember much. <br> But Tekton not "
			+ "worried. Master made Tekton this body, so this body must be what Tekton needs to serve Master "
			+ "best. <br> Tekton had better keep on using anvil to make body strong, until Master come back to "
			+ "give Tekton more work to do. <br> Until then, Tekton can wait for Master.";
	
	public TektonsJournal(Player player) {
		super(player);
	}

	@Override
	public String getTitle() {
		return "Tekton's journal";
	}

	@Override
	public String getString() {
		return CONTEXT;
	}

}
