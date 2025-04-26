package com.zenyte.tools.wikia;

import com.google.common.io.Files;
import com.zenyte.CacheManager;
import com.zenyte.game.parser.impl.JSONItemDefinitionsLoader;
import mgi.Indice;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.items.JSONItemDefinitions;
import mgi.utilities.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class ItemDefinitionsExtractor {

    private static final Map<Integer, String> REQUIRED_ITEMS = new HashMap<Integer, String>();
    private static final Logger log = LoggerFactory.getLogger(ItemDefinitionsExtractor.class);

    public static final void main(final String... strings) {
        try {
            System.out.println("Archiving the existing item definitions.");
            File archiveDir = new File("data/items/archive");
            if (!archiveDir.exists()) {
                archiveDir.mkdirs();
            }
            Files.copy(new File("data/items/ItemDefinitions.json"),
                    new File("data/items/archive/ItemDefinitions"
                            + LocalDateTime.now().toString().replaceAll(":", ".") + ".json"));
            System.out.println("Successfully archived the existing item definitions.");
        } catch (final IOException e1) {
            System.err.println("Failure to archive existing item definitions! Aborting.");
            e1.printStackTrace();
            return;
        }
        CacheManager.loadDetached("cache/data/cache");

        all();
    }

    private static void all() {
        try {
            new JSONItemDefinitionsLoader().parse();
        } catch (final Throwable e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.ITEM_DEFINITIONS); i++) {
            final ItemDefinitions defs = ItemDefinitions.get(i);
            if (JSONItemDefinitionsLoader.lookup(i) != null) {
                continue;
            }
            if (defs == null) {
                continue;
            }
            if (defs.getName().equalsIgnoreCase("null")) {
                continue;
            }
            if (defs.isNoted()) {
                continue;
            }
            REQUIRED_ITEMS.put(i, defs.getName());
        }
        System.err.println("Requesting: " + REQUIRED_ITEMS.size() + " items.");

        final Map<Integer, String> failedIds = new ConcurrentHashMap<>();

        ExecutorService service = Executors.newFixedThreadPool(4);

        REQUIRED_ITEMS.forEach((k, v) -> {
            final JSONItemDefinitions defs = new JSONItemDefinitions();
            defs.setId(k);
            service.submit(() -> {
                try {
                    extractAndSave(k, v, defs);
                } catch (IOException e) {
                    System.err.println("Failed to parse " + k + ", will try again later");
                    e.printStackTrace();
                    failedIds.put(k, v);
                }
            });
        });
        try {

            try {
                service.shutdown();
                service.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<Integer, String> failedIds2 = retry(failedIds);
//			if (!failedIds2.isEmpty()) {
//				failedIds2 = retry(failedIds2);
//			}
//			if (!failedIds2.isEmpty()) {
//				failedIds2 = retry(failedIds2);
//			}
//			if (!failedIds2.isEmpty()) {
//				failedIds2 = retry(failedIds2);
//			}
            if (failedIds2.isEmpty())
                System.out.println("Resolved all failed ids.");
            else
                System.err.println("Did not resolve " + failedIds2.size() + " but still saving.");
        } finally {
            JSONItemDefinitionsLoader.save();
        }
    }

    private static Map<Integer, String> retry(Map<Integer, String> failedIds) {
        if (!failedIds.isEmpty()) {
            System.out.println("Retrying " + failedIds.size() + " failed ids, sleeping for 10 seconds");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<Integer, String> failedIds2 = new ConcurrentHashMap<>();

            failedIds.forEach((k, v) -> {
                final JSONItemDefinitions defs = new JSONItemDefinitions();
                defs.setId(k);
                try {
                    extractAndSave(k, v, defs);
                } catch (Exception e) {
                    System.err.println("Failed to parse " + k + " for a second time, will try again later");
                    e.printStackTrace();
                    failedIds2.put(k, v);
                }
            });
            return failedIds2;
        }
        return new HashMap<>();
    }

    private static void extractAndSave(Integer k, String v, JSONItemDefinitions defs) throws IOException {
        extract(k, v, defs, ItemDefinitions.get(k).getNotedId());
        final JSONItemDefinitions def = JSONItemDefinitionsLoader.lookup(k);
        if (def != null) {
            if (def.getEquipmentDefinition() != null) {
                defs.setEquipmentDefinition(def.getEquipmentDefinition());
            }
            if (def.getEquipmentType() != null) {
                defs.setEquipmentType(def.getEquipmentType());
            }
        }
        synchronized (JSONItemDefinitionsLoader.definitions) {
            JSONItemDefinitionsLoader.definitions.put(k, defs);
        }
    }

    public static final void extract(final int id, final String name, final JSONItemDefinitions defs,
                                     final int notedId) throws IOException {
        try {
            final long startTime = System.currentTimeMillis();
            final String formattedName = name.replaceAll(" ", "_");
            final URL url = new URL("https://oldschool.runescape.wiki/w/" + formattedName);
            Document doc = null;
            doc = Jsoup.connect(url.toString()).get();
            final Element table = doc.select("table").first();
            Elements rows = null;
            try {
                rows = table.select("tr");
            } catch (final Exception e) {
                return;
            }

            final List<Element> mainInfoRows = rows.stream().collect(Collectors.toList());
            defs.parseMainTableData(mainInfoRows);
            final Elements bonusesTable = doc.select("table");
            for (final Element tab : bonusesTable) {
                if (tab.attr("class").toLowerCase().contains("infobox-bonuses")) {
                    final Elements bonuses = tab.select("tr");
                    final List<Element> bonusRows = bonuses.stream().collect(Collectors.toList());
                    defs.parseBonusesTable(bonusRows);

                    final Optional<String> slot = tab.select("[src]")
                            .stream()
                            .filter(img -> img.attr("alt").contains("slot"))
                            .map(img -> img.attr("alt")).findFirst();

                    if (slot.isPresent()) {
                        defs.parseItemSlot(slot.get());
                    }
                    break;
                }
            }

            if (notedId != -1) {
                final JSONItemDefinitions def = new JSONItemDefinitions();
                def.setId(notedId);
                def.setTradable(defs.getTradable());
                synchronized (JSONItemDefinitionsLoader.definitions) {
                    JSONItemDefinitionsLoader.definitions.put(notedId, def);
                }
            }
            System.out.println("Name: " + name + ", id: " + id + "\nURL: " + url);

            System.out.println("\nParsed in " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (final IOException e) {
            throw e;
        }
    }

}
