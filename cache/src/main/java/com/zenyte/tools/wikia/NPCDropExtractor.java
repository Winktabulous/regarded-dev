package com.zenyte.tools.wikia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenyte.CacheManager;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.apache.tools.ant.types.CharSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Savions.
 */
public final class NPCDropExtractor {

	private static final List<NPCDrop> NPC_DROPS = new ArrayList<NPCDrop>();
	public static final String DIRECTORY = "cache/data/npcs/drops/";
	private static final int[] NPCS_TO_SCRAPE = { 11992, 11993, 11998, 6610, 6503, 6611, 11989, 11990 };

	public static final void main(final String[] args) {
		CacheManager.loadDetached("cache/data/cache");

		for (int npcId : NPCS_TO_SCRAPE) {
			final File file = new File(DIRECTORY + npcId + ".drops.json");
			System.out.printf("%s drops for npc=%d\n", file.exists() ? "Overwriting" : "Creating", npcId);
			final String name = NPCDefinitions.get(npcId).getName();
			final String formattedName = name.replace(' ', '_');
			final ArrayList<String> failureList = new ArrayList<>();
			try {
				final URL url = new URL("https://oldschool.runescape.wiki/" + formattedName);
				Document doc = Jsoup.connect(url.toString()).get();
				doc.outputSettings().charset(CharSet.getUtf8().getCharset());
				final Elements tableElements = doc.select("table.item-drops");
				final List<DropEntry> drops = new ArrayList<DropEntry>();
				for (Element tableElement : tableElements) {
					final Elements rowElement = tableElement.select("tbody").select("tr:not(:first-child)");
					for (Element entryElement : rowElement) {
						if (entryElement.hasClass("mw-empty-elt")) {
							continue;
						}
						final Elements dropElement = entryElement.select("td");
						final Element nameElement = dropElement.select(".item-col").first();
						assert nameElement != null;
						final String itemName = nameElement.text().trim();
						try {
							final Element quantityElement = dropElement.get(2);
							String quantityElementText = quantityElement.text();
							if ("Nothing".equals(itemName)) {
								continue;
							}
							int id = -1;
							for (ItemDefinitions defs : ItemDefinitions.getDefinitions()) {
								if (defs != null && itemName.equals(defs.getName()) && (quantityElementText.contains("noted") == defs.isNoted())) {
									id = defs.getId();
									break;
								}
							}
							assert id != -1;
							quantityElementText = quantityElementText.replace("(noted)", "").replace(",", "").trim();
							//utf 8 trickery
							final byte[] oldQuantityBytes = quantityElementText.getBytes();
							byte[] quantityBytes = new byte[oldQuantityBytes.length];
							for (int indx = 0; indx < oldQuantityBytes.length; indx++) {
								quantityBytes[indx] = oldQuantityBytes[indx] < 0 ? 32 : oldQuantityBytes[indx];
							}
							quantityElementText = new String(quantityBytes, StandardCharsets.UTF_8);
							int minAmount = 0;
							int maxAmount = 0;
							if (quantityElementText.contains(" ")) {
								final String[] split = quantityElementText.split(" ");
								minAmount = Integer.parseInt(split[0]);
								maxAmount = Integer.parseInt(split[1]);
							} else {
								minAmount = maxAmount = Integer.parseInt(quantityElementText);
							}
							assert minAmount > 0 && minAmount < maxAmount;
							final Element rateElement = dropElement.get(3);
							double rate = 1 / Double.parseDouble(rateElement.attr("data-sort-value"));
							assert rate > 0 && rate <= 1;
							drops.add(new DropEntry(id, minAmount, maxAmount, (int) (rate * 100_000)));

						} catch (Exception e) {
							System.err.printf("Failure to parse item=%s\n", itemName);
							e.printStackTrace();
						}
					}
				}
				NPC_DROPS.add(new NPCDrop(npcId, drops.toArray(new DropEntry[0])));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		NPC_DROPS.forEach(npcDrop -> {
			try {
				final Gson gson = new GsonBuilder().setPrettyPrinting().create();
				final FileWriter writer = new FileWriter(DIRECTORY + npcDrop.npcId + ".drops.json");
				gson.toJson(npcDrop, writer);
				writer.close();
				System.out.printf("Writing drops for npc id=%d\n", npcDrop.npcId);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private static class NPCDrop {

		private final int npcId;
		private final DropEntry[] drops;

		public NPCDrop(int npcId, DropEntry[] drops) {
			this.npcId = npcId;
			this.drops = drops;
		}
	}

	private static class DropEntry {
		private final int itemId;
		private final int minAmount;
		private final int maxAmount;
		private final int  rate;

		public DropEntry(int itemId, int minAmount, int maxAmount, int rate) {
			this.itemId = itemId;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
			this.rate = rate;
		}

		public int getItemId() {
			return itemId;
		}

		public int getMinAmount() {
			return minAmount;
		}

		public int getMaxAmount() {
			return maxAmount;
		}

		public int getRate() {
			return rate;
		}
	}
}