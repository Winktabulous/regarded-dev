package com.zenyte.game.content;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Kris | 4. sept 2018 : 14:44:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public abstract class ChapteredBook extends Book {
	public ChapteredBook(final Player player) {
		super(player);
	}

	@Override
	@Deprecated
	public String getString() {
		return null;
	}

	public abstract String[] getChapters();

	public abstract String[] getContent();

	protected final Int2IntOpenHashMap mappedPages = new Int2IntOpenHashMap();

	@Override
	protected void sendBook(final boolean open) {
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 680);
		player.getPacketDispatcher().sendComponentText(680, 6, getTitle());
		sendPageNumbers();
		if (open) {
			int chapterIndex = 0;
			final ArrayList<String> list = new ArrayList<String>(100);
			final ArrayList<String[]> splitContext = splitIntoLine(getContent(), 29);
			list.add("Chapters");
			for (int i = 1; i < 15; i++) {
				list.add("");
			}
			for (final String[] arrays : splitContext) {
				mappedPages.put(chapterIndex++, list.size() / 30);
				int lineCount = 0;
				for (final String string : arrays) {
					list.add(string);
					lineCount++;
				}
				lineCount -= (lineCount / 15) * 15;
				while (lineCount++ < 15) {
					list.add("");
				}
			}
			context = list.toArray(new String[list.size()]);
			maxPages = (int) (Math.ceil(context.length / 30));
		}
		player.getPacketDispatcher().sendComponentVisibility(680, 108, page == 1);
		player.getPacketDispatcher().sendComponentVisibility(680, 110, page == maxPages);
		final int offset = (page - 1) * 30;
		int chapterCount = 0;
		final String[] chapters = getChapters();
		for (int i = 79; i <= 91; i ++) {
			if (offset == 0 && chapterCount < chapters.length) {
				player.getPacketDispatcher().sendComponentVisibility(680, i, false);
				player.getPacketDispatcher().sendComponentText(680, i, chapters[chapterCount++]);
				continue;
			}
			player.getPacketDispatcher().sendComponentVisibility(680, i, true);
		}
		for (int i = 45; i <= 59; i++) {
			if ((i - 45 + offset) >= context.length) {
				player.getPacketDispatcher().sendComponentText(680, i, "");
				continue;
			}
			player.getPacketDispatcher().sendComponentText(680, i, context[i - 45 + offset]);
		}
		final int nextOffset = offset + 15;
		for (int i = 61; i <= 75; i++) {
			if ((i - 61 + nextOffset) >= context.length) {
				player.getPacketDispatcher().sendComponentText(680, i, "");
				continue;
			}
			player.getPacketDispatcher().sendComponentText(680, i, context[i - 61 + nextOffset]);
		}
	}

	public static final ArrayList<String[]> splitIntoLine(final String[] strings, final int maxCharInLine) {
		final ArrayList<String[]> list = new ArrayList<String[]>();
		for (final String input : strings) {
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
					continue;
				}
				while (word.length() > maxCharInLine) {
					output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
					word = word.substring(maxCharInLine - lineLen);
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
			list.add(output.toString().split("\n"));
		}
		return list;
	}

	@Override
	public void handleButtons(final int componentId) {
		if (componentId == 108) {
			if (page > 1) {
				page--;
			}
		} else if (componentId == 110) {
			if (page < maxPages) {
				page++;
			}
		} else if (componentId == 9) {
			page = 1;
		}else if (componentId >= 79 && componentId <= 91) {
			final int chapterIndex = componentId - 79;
			final int pageIndex = mappedPages.get(chapterIndex);
			if (pageIndex != -1) {
				page = pageIndex + 1;
			}
		}
		sendBook(false);
	}

	@Override
	protected void sendPageNumbers() {
		player.getPacketDispatcher().sendComponentText(680, 10, "" + ((page * 2) - 1));
		player.getPacketDispatcher().sendComponentText(680, 11, "" + (page * 2));
	}
}
