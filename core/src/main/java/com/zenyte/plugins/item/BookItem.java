package com.zenyte.plugins.item;

import com.zenyte.game.content.Book;
import com.zenyte.game.content.books.GoblinBook;
import com.zenyte.game.content.chambersofxeric.books.*;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 25. aug 2018 : 22:13:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class BookItem extends ItemPlugin {

	@Override
	public void handle() {
		bind("Read", (player, item, slotId) -> {
			switch(item.getId()) {
			case 20886:
				Book.openBook(new CreatureKeepersJournal(player));
				break;
			case 20899:
				Book.openBook(new DarkJournal(player));
				break;
			case 20897:
				Book.openBook(new HoundmastersDiary(player));
				break;
			case 20888:
				Book.openBook(new NistiriosManifesto(player));
				break;
			case 20890:
				Book.openBook(new TektonsJournal(player));
				break;
			case 20893:
				Book.openBook(new TransdimensionalNotes(player));
				break;
			case 20895:
				Book.openBook(new VanguardJudgement(player));
				break;
			case 10999:
				Book.openBook(new GoblinBook(player));
				break;
				default:
					player.sendMessage("Nothing interesting happens.");
					return;
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 10999, 20886, 20899, 20897, 20888, 20890, 20893, 20895 };
	}

}
