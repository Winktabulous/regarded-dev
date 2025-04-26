package com.zenyte.game.world.region;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenyte.game.world.DefaultGson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Kris | 7. jaan 2018 : 21:44.02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class XTEALoader {
	private static final Logger log = LoggerFactory.getLogger(XTEALoader.class);

	private static final Int2ObjectMap<XTEA> regionToXTEA = new Int2ObjectOpenHashMap<>();
	private static final int[] defaultKeys = new int[4];

	public static void load(String path) throws FileNotFoundException {
		try (BufferedReader br = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
			Gson gson = DefaultGson.getGson();
			final XTEA[] xteas = gson.fromJson(br, XTEA[].class);
			for (final XTEA xtea : xteas) {
				if (xtea == null) continue;
				regionToXTEA.put(xtea.getMapsquare(), xtea);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the default xtea keys of {0, 0, 0, 0} or
	 * the correct keys for the home area (egdeville)
	 * as we repack the maps upon cache update.
	 * @param region
	 * @return
	 */
	public static int[] getXTEAs(final int region) {
//		if (region == 12342) {
//			return getXTEAKeys(region);
//		}
		return defaultKeys;
//		return getXTEAKeys(region);
	}

	/**
	 * Gets the actual xtea keys.
	 * @param regionId
	 * @return
	 */
	public static int[] getXTEAKeys(final int regionId) {
		final XTEA xtea = regionToXTEA.get(regionId);
		if (xtea == null) {
			return defaultKeys;
		}
		return xtea.getKey();
	}

	public static void save() {
		final File saveFile = new File("filtered xteas.json");

		final Int2ObjectMap<XTEA> xteas = new Int2ObjectRBTreeMap<>();
		for (final Int2ObjectMap.Entry<XTEA> entry : regionToXTEA.int2ObjectEntrySet()) {
			final int k = entry.getIntKey();
			final XTEA v = entry.getValue();

			if (v.getKey()[0] != 0 && v.getKey()[1] != 0 && v.getKey()[2] != 0 && v.getKey()[3] != 0) {
				xteas.put(k, v);
			}
		}

		final String toJson = new GsonBuilder().setPrettyPrinting().create().toJson(xteas.values());
		PrintWriter writer;
		try {
			writer = new PrintWriter(saveFile, StandardCharsets.UTF_8);
			writer.println(toJson);
			writer.close();
		} catch (final Exception e) {
			log.error("", e);
		}
	}

}
