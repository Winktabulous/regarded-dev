package com.zenyte.game.content.achievementdiary;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Kris | 20. sept 2018 : 22:52:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public interface Diary {

	String[] task();

	int objectiveLength();

	boolean flagging();

	DiaryComplexity type();

	DiaryArea area();

	String title();

	String name();

	int diaryStarted();

	int taskMaster();

	int[][] diaryCompleted();

	boolean autoCompleted();

	DiaryChunk[] chunks();

	Map<DiaryComplexity, List<Diary>> map();

	Predicate<Player> predicate();

	default int[][] flow(final int start) {
		return new int[][] {new int[] {start, 1}, new int[] {start + 1, 1}, new int[] {start + 2, 1}, new int[] {start + 3, 1}};
	}

	default String objectiveName() {
		return area().getAreaName().toUpperCase() + " - " + name();
	}

	Predicate<Player> NO_PREDICATE = null;
	DiaryComplexity EASY = DiaryComplexity.EASY;
	DiaryComplexity MEDIUM = DiaryComplexity.MEDIUM;
	DiaryComplexity HARD = DiaryComplexity.HARD;
	DiaryComplexity ELITE = DiaryComplexity.ELITE;


	int INTERFACE = 119;
	int OFFSET = 4;

	static void sendDiary(final Player player, final Map<DiaryComplexity, List<Diary>> values) {
		if (values == null || values.isEmpty()) {
			throw new RuntimeException("Cannot open the diary of " + values);
		}
		final Diary firstEntry = values.get(EASY).get(0);
		final String name = firstEntry.area().getAreaName();
		final ArrayList<String> list = new ArrayList<>(100);
		final String prefix = "<str>";
		final AchievementDiaries diaries = player.getAchievementDiaries();
		final boolean ironman = player.isIronman();
		for (final DiaryComplexity difficulty : DiaryComplexity.VALUES) {
			final List<Diary> diary = values.get(difficulty);
			if (difficulty == EASY) {
				list.add(Colour.YELLOW + firstEntry.title() + " Tasks");
			}
			addSpaces(list, difficulty == EASY ? 1 : 2);
			list.add(Colour.YELLOW + diary.get(0).type().toString());
			list.add("<col=000000>");
			addSpaces(list, 1);
			for (final Diary task : diary) {
				final String pref = (diaries.getProgress(task) == task.objectiveLength()) || task.autoCompleted() ? prefix : "<col=000000>";
				for (final String line : task.task()) {
					if (ironman && task == ArdougneDiary.CLAIM_BUCKETS_OF_SAND) {
						list.add(pref + "Fill a bucket with sand using Bert's sandpit.");
						continue;
					}
					list.add(pref + line);
				}
			}
		}
		addSpaces(list, 1);
		sendJournal(player, "<col=800000>Achievement Diary - " + name, list);
	}

	static void addSpaces(final List<String> list, final int amount) {
		for (int i = 0; i < amount; i++) {
			list.add("");
		}
	}

	int LIMIT = 174;

	static void sendJournal(final Player player, final String title, final List<String> text) {
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		dispatcher.sendClientScript(2498, 1, 1);
		final int size = text.size();
		dispatcher.sendComponentText(INTERFACE, 2, title);
		for (int i = 174; i >= 4; i--) {
			dispatcher.sendComponentText(INTERFACE, i, "");
		}
		for (int index = 0; index < size; index++) {
			if (index > LIMIT) {
				break;
			}
			dispatcher.sendComponentText(INTERFACE, OFFSET + index, text.get(index));
		}
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE);
		if (size >= 10) {
			dispatcher.sendClientScript(2523, 1, size);
		}
	}
}
