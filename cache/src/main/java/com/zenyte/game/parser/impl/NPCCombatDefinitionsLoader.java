package com.zenyte.game.parser.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenyte.game.parser.Parse;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.entity.npc.OldNPCCombatDefinitions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class NPCCombatDefinitionsLoader implements Parse {
	private static final Logger log = LoggerFactory.getLogger(NPCCombatDefinitionsLoader.class);
	public static final Int2ObjectMap<OldNPCCombatDefinitions> DEFINITIONS = new Int2ObjectOpenHashMap<>();

	@Override
	public void parse() throws Throwable {
		final BufferedReader br = new BufferedReader(new FileReader("data/npcs/archive/combatDefinitions old system latest.json"));
		final OldNPCCombatDefinitions[] item_definitions = DefaultGson.getGson().fromJson(br, OldNPCCombatDefinitions[].class);
		for (final OldNPCCombatDefinitions def : item_definitions) {
			if (def != null) {
				DEFINITIONS.put(def.getId(), def);
			}
		}
	}

	public static void loadCombatDefinitions() {
		try {
			new NPCCombatDefinitionsLoader().parse();
		} catch (final Throwable e) {
			log.error("", e);
		}
	}

	/**
	 * Looks up a definition based on the key value in the map.
	 *
	 * @param npcId the key value we using to search for the respective
	 *               definition.
	 * @return
	 */
	public static OldNPCCombatDefinitions get(final int npcId) {
		return DEFINITIONS.get(npcId);
	}

	public static void save() {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final Collection<OldNPCCombatDefinitions> values = DEFINITIONS.values();
		final Comparator<OldNPCCombatDefinitions> comparator = (npc1, npc2) -> {
			if (npc1 == null || npc2 == null) return 0;
			return npc1.getId() > npc2.getId() ? 1 : -1;
		};
		final List<OldNPCCombatDefinitions> list = new ArrayList<>(values);
		list.sort(comparator);
		final String toJson = gson.toJson(list);
		try {
			final PrintWriter pw = new PrintWriter("data/npcs/combatDefinitions.json", StandardCharsets.UTF_8);
			pw.println(toJson);
			pw.close();
		} catch (final Exception e) {
			log.error("", e);
		}
	}

}
