package com.zenyte.game.content.compcapes;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.achievementdiary.AchievementDiaries;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.achievementdiary.DiaryComplexity;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.follower.impl.SkillingPet;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.dialogue.MenuD;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.zenyte.game.world.entity.player.collectionlog.CollectionLogCategoryType.*;

@StaticInitializer
public class CompletionistCape {

	private static final List<CompletionistCapeRequirement> requirements = new ArrayList<>();

	@Subscribe
	public static void onLogin(final LoginEvent event) {
		Player player = event.getPlayer();
		int capeId = player.getEquipment().getId(EquipmentSlot.CAPE);
		int compCapeTier = isCompletionistCape(capeId);
		if (compCapeTier > 0) {
			int applicableTier = checkRequirements(player);
			if (compCapeTier > applicableTier) {
				player.getEquipment().set(EquipmentSlot.CAPE, null);
				Item item = new Item(capeId);
				ContainerResult result = player.getBank().add(item);
				if (!result.isFailure()) {
					player.sendMessage(Colour.RED.wrap("Your Completionist cape has been moved to the bank."));
				} else {
					player.sendMessage(Colour.RED.wrap("Your Completionist cape has been placed on the ground."));
					World.spawnFloorItem(item, player);
				}
			}
		}
	}

	@Subscribe
	public static void onServerLaunch(final ServerLaunchEvent event) {
		requirements.add(make("Unlock Music Tracks", player -> player.getMusic().unlockedMusicCount(), 200));
		requirements.add(make("Loot the Barrow's chest", player -> get(player, "Barrows"), 100));
		requirements.add(make("Defeat Nex", player -> get(player, "Nex"), 150));
		requirements.add(make("Defeat the Nightmare", player -> getMultiple(player, "The Nightmare", "Phosani's Nightmare"), 150));
		requirements.add(make("Defeat the Corporeal Beast", player -> get(player, "Corporeal Beast"), 150));
		requirements.add(make("Defeat the Grotesque Guardians", player -> get(player, "Grotesque Guardians"), 150));
		requirements.add(make("Defeat Zalcano", player -> get(player, "Zalcano"), 25));
		requirements.add(make("Defeat Zulrah", player -> get(player, "Zulrah"), 250));
		requirements.add(make("Defeat General Graardor", player -> get(player, "General Graardor"), 250));
		requirements.add(make("Defeat K'ril Tsutsaroth", player -> get(player, "K'ril Tsutsaroth"), 250));
		requirements.add(make("Defeat Kree'arra", player -> get(player, "Kree'Arra"), 250));
		requirements.add(make("Defeat Commander Zilyana", player -> get(player, "Commander Zilyana"), 250));
		requirements.add(make("Defeat Dagannoth Supreme", player -> get(player, "Dagannoth Supreme"), 250));
		requirements.add(make("Defeat Dagannoth Rex", player -> get(player, "Dagannoth Rex"), 250));
		requirements.add(make("Defeat Dagannoth Prime", player -> get(player, "Dagannoth Prime"), 250));
		requirements.add(make("Defeat the Alchemical Hydra", player -> get(player, "Alchemical Hydra"), 250));
		requirements.add(make("Defeat the Abyssal Sire", player -> get(player, "Abyssal Sire"), 250));
		requirements.add(make("Defeat the King Black Dragon", player -> get(player, "King Black Dragon"), 250));
		requirements.add(make("Defeat the Phantom Muspah", player -> get(player, "Phantom Muspah"), 150));

		//requirements.add(make("Defeat the Giant Mole", player -> get(player, "Giant Mole"), 250));
		requirements.add(make("Defeat the Kalphite Queen", player -> get(player, "Kalphite Queen"), 250));
		requirements.add(make("Defeat Cerberus", player -> get(player, "Cerberus"), 250));
		requirements.add(make("Defeat Vorkath", player -> get(player, "Vorkath"), 250));
		requirements.add(make("Defeat the Thermonucleur Smoke Devil", player -> get(player, "Thermonuclear smoke devil"), 250));
		requirements.add(make("Defeat the Kraken", player -> get(player, "Kraken"), 250));
		requirements.add(make("Defeat Skotizo", player -> get(player, "Skotizo"), 10));
		requirements.add(make("Defeat the Mimic", player -> get(player, "Mimic"), 3));
		requirements.add(make("Complete Medium Clue Scrolls", player -> player.getNumericAttribute("completed " + ClueLevel.MEDIUM.toString().toLowerCase() + " treasure trails").intValue(), 100));
		requirements.add(make("Complete Hard Clue Scrolls", player -> player.getNumericAttribute("completed " + ClueLevel.HARD.toString().toLowerCase() + " treasure trails").intValue(), 75));
		requirements.add(make("Complete Elite Clue Scrolls", player -> player.getNumericAttribute("completed " + ClueLevel.ELITE.toString().toLowerCase() + " treasure trails").intValue(), 50));
		requirements.add(make("Complete Master Clue Scrolls", player -> player.getNumericAttribute("completed " + ClueLevel.MASTER.toString().toLowerCase() + " treasure trails").intValue(), 25));
		requirements.add(make("Befriend at least 5 pets", player -> {
			int count = player.isDeveloper() ? 5 : 0;
			for (BossPet bossPet : BossPet.VALUES) {
				if (bossPet.hasPet(player)) {
					count++;
					if (count >= 5) {
						return count;
					}
				}
			}

			for (SkillingPet skillingPet : SkillingPet.VALUES) {
				if (skillingPet.hasPet(player)) {
					count++;
					if (count >= 5) {
						return count;
					}
				}
			}

			return count;
		}, 5));
	}

	private static CompletionistCapeRequirement make(String task, Function<Player, Integer> function, int requirement) {
		return new CompletionistCapeRequirement(task, function, requirement);
	}

	public static ObjectArrayList<String> ALLOWED_PLAYERS = new ObjectArrayList<>();

	public static int checkRequirements(Player player) {
		if (ALLOWED_PLAYERS.contains(player.getUsername().toLowerCase()))
			return 3;

		if (!player.getSkills().isMaxed())
			return 0;

		boolean hasAllRequirements = false;
		boolean hasAllCombatAchievements = player.getCombatAchievements().hasAllTiersCompleted();
		boolean hasAllDiaries = player.getAchievementDiaries().isAllCompleted();

		for (var requirement : requirements)
			if (requirement.test(player))
				hasAllRequirements = true;

		if (!hasAllDiaries) return 0;
		if (!hasAllRequirements && !hasAllCombatAchievements) return 1;
		if (hasAllRequirements && !hasAllCombatAchievements) return 2;
		if (!hasAllRequirements) return 2;

		return 3;
	}

	public static int isCompletionistCape(int id) {
		return switch (id) {
			case 32243, 32245, 32247, 32249, 32251, 32253, 32255, 32257, 32259, 32265 -> 3;
			case 32261 -> 1;
			case 32263 -> 2;
			default -> 0;
		};
	}

	public static int tierToCape(int id) {
		return switch (id) {
			case 1 -> 32261;
			case 2 -> 32263;
			case 3 -> 32265;
			default -> -1;
		};
	}

	private static final List<Map<DiaryComplexity, List<Diary>>> diaryList = List.of(ArdougneDiary.MAP, FaladorDiary.MAP, FremennikDiary.MAP, KaramjaDiary.MAP,
			KandarinDiary.MAP, DesertDiary.MAP, LumbridgeDiary.MAP, MorytaniaDiary.MAP, VarrockDiary.MAP, WildernessDiary.MAP, WesternProvincesDiary.MAP, KourendDiary.MAP);

	private static ObjectArrayList<String> buildAchievementList(final Player player, final Map<DiaryComplexity, List<Diary>> values) {
		final Diary firstEntry = values.get(Diary.EASY).get(0);
		final ObjectArrayList<String> list = new ObjectArrayList<>(100);
		final String prefix = "<str>";
		final AchievementDiaries diaries = player.getAchievementDiaries();
		final boolean ironman = player.isIronman();
		for (final DiaryComplexity difficulty : DiaryComplexity.VALUES) {
			final List<Diary> diary = values.get(difficulty);
			if (difficulty == Diary.EASY) {
				list.add(Colour.YELLOW.wrap(firstEntry.title() + " Tasks"));
			}
			Diary.addSpaces(list, difficulty == Diary.EASY ? 1 : 2);
			list.add(Colour.YELLOW.wrap(diary.get(0).type().toString()));
			Diary.addSpaces(list, 1);
			for (final Diary task : diary) {
				final String pref = (diaries.getProgress(task) == task.objectiveLength()) || task.autoCompleted() ? prefix : "<col=000000>";
				for (final String line : task.task()) {
					if (ironman && task == ArdougneDiary.CLAIM_BUCKETS_OF_SAND) {
						list.add(Colour.MENU_COLOR.wrap(pref + "Fill a bucket with sand using Bert's sandpit."));
						continue;
					}
					list.add(Colour.MENU_COLOR.wrap(pref + line));
				}
			}
			Diary.addSpaces(list, 3);
		}
		Diary.addSpaces(list, 3);
		return list;
	}

	//Wrap the lists with color to avoid the hover hook inside scripts
	public enum CompletionistCapeRequirementSection {
		MAX(0, "Max", player -> {
			ObjectArrayList<String> listOfNames = new ObjectArrayList<>();
			for (int i = 0; i < SkillConstants.COUNT; i++) {
				if (i != SkillConstants.CONSTRUCTION) {
					int level = player.getSkills().getLevelForXp(i);
					listOfNames.add(Colour.MENU_COLOR.wrap((level >= 99 ? "<str>" : "") + SkillConstants.SKILLS[i] + " - " + level + "/99"));
				}
			}
			return listOfNames;
		}),
		ACHIEVEMENTS(1, "Achievements", player -> {
			ObjectArrayList<String> listOfNames = new ObjectArrayList<>();
			for (Map<DiaryComplexity, List<Diary>> map : diaryList) {
				listOfNames.addAll(buildAchievementList(player, map));
			}
			return listOfNames;
		}),
		TASKS(2, "Tasks", player -> {
			ObjectArrayList<String> listOfNames = new ObjectArrayList<>();
			for (CompletionistCapeRequirement requirement : requirements) {
				int value = requirement.getFunction().apply(player);
				int max = requirement.getRequirement();
				listOfNames.add(Colour.MENU_COLOR.wrap((value >= max ? "<str>" : "") + requirement.getTask() + " - " + value + "/" + max));
			}
			return listOfNames;
		}),
		COMBAT_TASKS(3, "Combat Tasks", player -> {
			ObjectArrayList<String> listOfNames = new ObjectArrayList<>();
			for (CATierType tier : CATierType.values) {
				listOfNames.add(Colour.YELLOW.wrap(tier.getName() + " Tasks"));
				Diary.addSpaces(listOfNames, 2);
				List<CAType> list = CAType.tierToType.get(tier);
				for (CAType caType : list) {
					if (caType.isDisabled()) continue;
					listOfNames.add(Colour.MENU_COLOR.wrap((player.getCombatAchievements().taskCompleted(caType) ? "<str>" : "") + caType.getName()));
				}
				Diary.addSpaces(listOfNames, 4);
			}
			return listOfNames;
		})
		;

		private final int id;
		private final String name;
		private final Function<Player, ObjectArrayList<String>> display;

		CompletionistCapeRequirementSection(int id, String name, Function<Player, ObjectArrayList<String>> display) {
			this.id = id;
			this.name = name;
			this.display = display;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Function<Player, ObjectArrayList<String>> getDisplay() {
			return display;
		}

	}

	public static int countTotal() {
		return requirements.size();
	}

	public static int countFinished(Player player) {
		return (int) requirements.stream().filter(requirement -> requirement.test(player)).count();
	}

	public static void noRequirements(Player player) {
		player.sendMessage(Colour.RED.wrap("You cannot equip this as you have not met the Completionist requirements. Please check the Character Summary tab to see what you have left to accomplish."));
	}

	public static void showRequirements(Player player, CompletionistCapeRequirementSection section) {
		player.getDialogueManager().start(new MenuD(player, section.getName() + " Requirements Overview", section.getDisplay().apply(player).toArray(new String[0])));
	}

}
