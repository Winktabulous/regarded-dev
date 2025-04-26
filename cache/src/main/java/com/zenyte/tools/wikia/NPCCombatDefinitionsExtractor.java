package com.zenyte.tools.wikia;

import com.google.common.io.Files;
import com.zenyte.game.parser.impl.NPCCombatDefinitionsLoader;
import com.zenyte.game.world.entity.npc.OldNPCCombatDefinitions;
import mgi.Indice;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class NPCCombatDefinitionsExtractor {

    private static final Map<Integer, String> REQUIRED_NPCS = new HashMap<>();
    public static final Map<Integer, OldNPCCombatDefinitions> DUMPED_DEFINITIONS = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(NPCCombatDefinitionsExtractor.class);

    public static void main(final String... args) {
        try {
            System.out.println("Archiving the existing combat definitions.");
            File archiveDir = new File("data/npcs/archive");
            if (!archiveDir.exists()) {
                archiveDir.mkdirs();
            }
            Files.copy(new File("data/npcs/combatDefinitions.json"),
                    new File("data/npcs/archive/combatdefinitions"
                            + LocalDateTime.now().toString().replaceAll(":", ".") + ".json"));
            System.out.println("Successfully archived the existing combat definitions.");
        } catch (final IOException e1) {
            System.err.println("Failure to archive existing combat definitions! Aborting.");
            e1.printStackTrace();
            return;
        }
        //Game.load();
        //CacheUtil.loadCache();
        NPCCombatDefinitionsLoader.loadCombatDefinitions();
        for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.NPC_DEFINITIONS); i++) {
            final NPCDefinitions defs = NPCDefinitions.get(i);
            if (!defs.containsOption("Attack")) continue;
            if (NPCCombatDefinitionsLoader.get(i) != null) continue;
            REQUIRED_NPCS.put(i, defs.getName());
        }
        System.err.println("Attempting to dump " + REQUIRED_NPCS.size() + " npcs.");
        REQUIRED_NPCS.forEach((k, v) -> {
            if (DUMPED_DEFINITIONS.containsKey(k)) {
                NPCCombatDefinitionsLoader.DEFINITIONS.put(k, DUMPED_DEFINITIONS.get(k));
                return;
            }
            final OldNPCCombatDefinitions defs = new OldNPCCombatDefinitions(k);
            defs.setAttackAnim(null);
            defs.setBlockAnim(null);
            defs.setDeathAnim(null);
            extract(k, v, defs);
            DUMPED_DEFINITIONS.put(k, defs);
            NPCCombatDefinitionsLoader.DEFINITIONS.put(k, defs);
        });

        NPCCombatDefinitionsLoader.save();
        System.err.println("Finished updating NPC definitions.");
    }

    public static void extract(final int id, final String name, final OldNPCCombatDefinitions defs) {
        try {
            final long startTime = System.currentTimeMillis();

            final String formattedName = name.replace(' ', '_');
            final URL url = new URL("https://oldschoolrunescape.wikia.com/wiki/" + formattedName);

            final Document doc = Jsoup.connect(url.toString()).get();

            final Element table = doc.select("table[class=wikitable infobox]").first();
            if (table == null) return;
            final Elements rows = table.select("tr");

            for (final Element row : rows) {
                String key = row.select("th")
                        .text()
                        .replace(" ", "");
                Object value = row.select("td")
                        .text()
                        .replace(" ", "");

                final String keyTrimmed = key.trim();
                if (keyTrimmed.length() == 0) continue;

                if (key.contains("Combat info")) continue;

                if (keyTrimmed.contains("Attack Styles")) {
                    value = row.nextElementSibling()
                            .text();
                }

                try {
                    if (key.equalsIgnoreCase("attack speed")) {
                        value = rows.select("img[alt^=Monster attack speed]")
                                .first()
                                .attr("alt")
                                .toLowerCase()
                                .replaceAll("monster attack speed", "")
                                .trim();
                    }
                } catch (final NullPointerException e) {
                    value = "0";
                }

                if (keyTrimmed.contains("Combat stats")
                        || keyTrimmed.contains("Aggressive stats")
                        || keyTrimmed.contains("Defensive stats")
                        || keyTrimmed.contains("Other bonuses")) {
                    value = row.nextElementSibling()
                            .nextElementSibling()
                            .select("td")
                            .stream()
                            .map(Element::text)
                            .collect(Collectors.toList());
                }

                final String valueTrimmed = value.toString().trim();
                if (valueTrimmed.length() == 0) continue;

                defs.setId(id);
                defs.parseTableRow(key, value);
            }

            System.out.println("\nName: " + name + "\nURL: " + url + "\n");

            System.out.println("\nParsed in " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (final HttpStatusException ignored) {
        } catch (final SocketTimeoutException e) {
            extract(id, name, defs);// connection timed out, lets try again
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}
