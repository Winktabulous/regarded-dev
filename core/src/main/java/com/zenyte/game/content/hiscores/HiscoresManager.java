package com.zenyte.game.content.hiscores;

import com.google.common.eventbus.Subscribe;
import com.google.common.reflect.TypeToken;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.plugins.events.LogoutEvent;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TextUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.BiPredicate;

public class HiscoresManager {

	private static final Logger logger = LoggerFactory.getLogger(HiscoresManager.class);
	public static Map<GameMode, Map<Integer, Map<HiscoresCategoryEntry, List<HiscoresScore>>>> categoryToHiscores = Collections.synchronizedMap(new HashMap<>());
	public static final Object2ObjectMap<HiscoresPlayerKey, Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>>> nameToHiscores = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
	private static final Map<GameMode, Path> dataFolders = new HashMap<>();

	@Subscribe
	public static void onLogout(@NotNull final LogoutEvent event) {
		CoresManager.slowExecutor.execute(() -> write(event.getPlayer()));
	}

	@Subscribe
	public static void onServerLaunch(final ServerLaunchEvent event) {
		try {
			long duration = System.currentTimeMillis();

			Path basePath = Paths.get("data", "characters-hiscores");
			for (GameMode gameMode : GameMode.values) {
				Path path = basePath.resolve(gameMode.getHiscoresFolder());
				if (Files.notExists(path)) {
					Files.createDirectories(path);
				}

				dataFolders.put(gameMode, path);

				File folder = path.toFile();
				for (File file : folder.listFiles()) {
					String fileName = file.getName();
					if (!fileName.endsWith(".json")) {
						continue;
					}

					int underscoreIndex = fileName.indexOf("_");
					if (underscoreIndex == -1) {
						continue;
					}

					String name = fileName.substring(0, underscoreIndex);
					int rate = Integer.parseInt(fileName.substring(underscoreIndex + 1, fileName.indexOf(".")));
					Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>> data = DefaultGson.getGson().fromJson(new FileReader(file), new TypeToken<Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>>>() {}.getType());
					nameToHiscores.put(new HiscoresPlayerKey(name, gameMode, rate), data);
				}
			}

			rebuildList();
			logger.info("Loading Hiscores took {}ms", (System.currentTimeMillis() - duration));
		} catch (IOException e) {
			logger.error("Failed to load Hiscores.", e);
		}
	}

	private static void write(Player player) {
		try {
			HiscoresPlayerKey key = new HiscoresPlayerKey(player.getName().toLowerCase(), player.getGameMode(), player.getCombatXPRate());
			boolean updated = false;
			for (HiscoresCategory category : HiscoresCategory.values) {
				BiPredicate<Player, long[]> applies = category.getApplies();
				Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>> hiscoresData = nameToHiscores.get(key);
				if (hiscoresData == null) {
					hiscoresData = new HashMap<>();
					nameToHiscores.put(key, hiscoresData);
				}

				Map<HiscoresCategoryEntry, HiscoresScore> hiscoresScoreMap = hiscoresData.get(category);
				if (hiscoresScoreMap == null) {
					hiscoresData.put(category, category.collectData(player));
					updated = true;
				} else {
					if (hiscoresScoreMap.size() != category.getEntries().length) {
						for (HiscoresCategoryEntry categoryEntry : category.getEntries()) {
							long[] newValues = categoryEntry.values(player);
							if (applies.test(player, newValues)) {
								HiscoresScore oldScore = hiscoresScoreMap.get(categoryEntry);
								if (oldScore == null) {
									hiscoresScoreMap.put(categoryEntry, new HiscoresScore(TextUtils.formatName(player.getUsername()), newValues));
									updated = true;
								} else {
									if (!Arrays.equals(oldScore.getValues(), newValues)) {
										hiscoresScoreMap.put(categoryEntry, new HiscoresScore(oldScore.getName(), newValues));
										updated = true;
									}
								}
							}
						}
					} else {
						for (Map.Entry<HiscoresCategoryEntry, HiscoresScore> entry : hiscoresScoreMap.entrySet()) {
							HiscoresCategoryEntry categoryEntry = entry.getKey();
							HiscoresScore score = entry.getValue();
							long[] newValues = categoryEntry.values(player);
							if (applies.test(player, newValues)) {
								//System.out.println(categoryEntry + ":" + Arrays.toString(score.getValues()) + ":" + Arrays.toString(newValues));
								if (!Arrays.equals(score.getValues(), newValues)) {
									hiscoresScoreMap.put(categoryEntry, new HiscoresScore(score.getName(), newValues));
									updated = true;
								}
							}
						}
					}
				}
			}

			logger.debug("Writing hiscores for {} {}", key.getName(), updated);
			if (updated) {
				Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>> hiscoresData = nameToHiscores.get(key);
				String fileName = key.getName() + "_" + key.getRate() + ".json";
				Path dataFolder = dataFolders.get(key.getGameMode());
				Path file = dataFolder.resolve(fileName);

				Path tempFile = Files.createTempFile(dataFolder, fileName, ".tmp");
				Files.writeString(tempFile, DefaultGson.getGson().toJson(hiscoresData), StandardCharsets.UTF_8);

				Files.move(tempFile, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

				rebuildList();
			}
		} catch (IOException e) {
			logger.error("Failed to write Hiscores for player " + player.getUsername() + ".", e);
		}
	}

	private static void rebuildList() {
		Map<GameMode, Map<Integer, Map<HiscoresCategoryEntry, List<HiscoresScore>>>> categoryToHiscores = Collections.synchronizedMap(new HashMap<>());

		for (Map.Entry<HiscoresPlayerKey, Map<HiscoresCategory, Map<HiscoresCategoryEntry, HiscoresScore>>> map : nameToHiscores.entrySet()) {
			HiscoresPlayerKey key = map.getKey();
			GameMode gameMode = key.getGameMode();
			int rate = key.getRate();
			for (Map<HiscoresCategoryEntry, HiscoresScore> dataEntry : map.getValue().values()) {
				for (Map.Entry<HiscoresCategoryEntry, HiscoresScore> entry : dataEntry.entrySet()) {
					Map<Integer, Map<HiscoresCategoryEntry, List<HiscoresScore>>> hiscores = categoryToHiscores.get(gameMode);
					if (hiscores == null) {
						hiscores = new HashMap<>();
						categoryToHiscores.put(gameMode, hiscores);
					}

					Map<HiscoresCategoryEntry, List<HiscoresScore>> entryToList = hiscores.get(rate);
					if (entryToList == null) {
						entryToList = new HashMap<>();
						hiscores.put(rate, entryToList);
					}

					List<HiscoresScore> list = entryToList.get(entry.getKey());
					if (list == null) {
						list = new ArrayList<>();
						entryToList.put(entry.getKey(), list);
					}

					list.add(entry.getValue());
				}
			}
		}

		for (Map<Integer, Map<HiscoresCategoryEntry, List<HiscoresScore>>> hiscores : categoryToHiscores.values()) {
			for (Map<HiscoresCategoryEntry, List<HiscoresScore>> map : hiscores.values()) {
				for (List<HiscoresScore> list : map.values()) {
					Collections.sort(list);
				}
			}
		}

		HiscoresManager.categoryToHiscores = categoryToHiscores;
	}

}
