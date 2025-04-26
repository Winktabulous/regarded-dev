package com.zenyte.game.content;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;

import java.util.StringTokenizer;

/**
 * @author Kris | 19. nov 2017 : 5:55.23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public abstract class Book {

	public Book(final Player player) {
		this.player = player;
	}

	protected final Player player;
	protected int page = 1;
	protected int maxPages;
	protected int interfaceId;
	protected String[] context;
	public abstract String getTitle();
	public abstract String getString();
	
	public static final String[] splitIntoLine(final String input, final int maxCharInLine){
		final StringTokenizer tok = new StringTokenizer(input, " ");
		final StringBuilder output = new StringBuilder(input.length());
	    int lineLen = 0;
	    while (tok.hasMoreTokens()) {
	        String word = tok.nextToken();
	        if (word.startsWith("<col")) {
	        	if (lineLen > 0) {
	        		output.append("\n");
	        		lineLen = 0;
	        	}
	        	output.append(word);
	        	continue;
	        }
	        if (word.equals("<br>")) {
				lineLen = 0;
				output.append("\n");
				output.append("\n");
				continue;
	        }
	        while(word.length() > maxCharInLine){
	            output.append(word.substring(0, maxCharInLine-lineLen) + "\n");
	            word = word.substring(maxCharInLine-lineLen);
	            lineLen = 0;
	        }
	        final int wordLength = word.startsWith("<col") ? 0 : word.length();
	        if (lineLen + wordLength > maxCharInLine) {
	            output.append("\n");
	            lineLen = 0;
			}
			if (wordLength > 0) {
				output.append(word).append(" ");
				lineLen += wordLength + 1;
			} else {
				output.append(word);
			}
	    }
	    return output.toString().split("\n");
	}
	
	protected void sendBook(final boolean open) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 392);
		player.getPacketDispatcher().sendComponentText(392, 6, getTitle());
		clearInterface();
		sendPageNumbers();
		if (open) {
			context = splitIntoLine(getString(), 25);
			maxPages = (int) (1 + Math.ceil(context.length / 29));
		}
		player.getPacketDispatcher().sendComponentVisibility(392, 75, page == 1);
		player.getPacketDispatcher().sendComponentVisibility(392, 77, page == maxPages);
		final int offset = (page - 1) * 29;
		for (int i = 44; i < 59; i++) {
			if ((i - 44 + offset) >= context.length) {
				player.getPacketDispatcher().sendComponentText(392, i, "");
				continue;
			}
			player.getPacketDispatcher().sendComponentText(392, i, context[i - 44 + offset]);
		}

		for (int i = 60; i < 75; i++) {
			if ((i - 44 + offset) >= context.length) {
				player.getPacketDispatcher().sendComponentText(392, i, "");
				continue;
			}
			player.getPacketDispatcher().sendComponentText(392, i, context[i - 60 + offset]);
		}
	}
	
	public void handleButtons(final int componentId) {
		if (componentId == 75) {
			if (page > 1) {
				page--;
			}
		} else if (componentId == 77) {
			if (page < maxPages) {
				page++;
			} 
		}
		sendBook(false);
	}
	
	private void clearInterface() {
		for (int i = 44; i < 59; i++) {
			player.getPacketDispatcher().sendComponentText(392, i, "");
		}
		for (int i = 60; i < 75; i++) {
			player.getPacketDispatcher().sendComponentText(392, i, "");
		}
	}
	
	protected void sendPageNumbers() {
		player.getPacketDispatcher().sendComponentText(392, 9, "" + ((page * 2) - 1));
		player.getPacketDispatcher().sendComponentText(392, 10, "" + (page * 2));
	}
	
	public static final void openBook(final Book book) {
		book.player.setAnimation(new Animation(1350));
		book.player.getTemporaryAttributes().put("book", book);
		book.sendBook(true);
		book.player.setCloseInterfacesEvent(() -> book.player.setAnimation(Animation.STOP));
	}
	
}
