package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.alternatetables.AlternateTable;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.drop.matrix.*;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.ItemDropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.NPCDropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.server.Attributes;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.types.Definitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingDouble;

/**
 * @author Tommeh | 16-4-2019 | 14:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class DropViewerInterface extends Interface {
    private static final Logger log = LoggerFactory.getLogger(DropViewerInterface.class);
    private static final Int2ObjectOpenHashMap<String> CUSTOM_NAMES = new Int2ObjectOpenHashMap<>();

    static {
        CUSTOM_NAMES.put(12195, "Duke Sucellus (awknd)");
    }

    @Override
    protected void attach() {
        put(11, "Select NPC");
        put(13, "Select Item");
        put(19, "View Result");
        put(31, "Rarity Display");
        put(10, "Search button");//TODO for tom: Make it so clicking anywhere in the search box opens this.
    }

    @Override
    public void open(Player player) {
        Analytics.flagInteraction(player, Analytics.InteractionType.DROP_VIEWER);
        reset(player);
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        player.getPacketDispatcher().sendClientScript(2158);
    }

    @Override
    protected void build() {
        bind("Select NPC", player -> {
            player.addTemporaryAttribute("drop_viewer_search_type", 0);
            final Object attr = player.getTemporaryAttributes().get("drop_viewer_input");
            if (!(attr instanceof String)) {
                return;
            }
            final String input = (String) attr;
            search(player, input);
        });
        bind("Select Item", player -> {
            player.addTemporaryAttribute("drop_viewer_search_type", 1);
            final Object attr = player.getTemporaryAttributes().get("drop_viewer_input");
            if (!(attr instanceof String)) {
                return;
            }
            final String input = (String) attr;
            search(player, input);
        });
        bind("View Result", (player, slotId, itemId, option) -> {
            final Object attr = player.getTemporaryAttributes().get("drop_viewer_results");
            if (!(attr instanceof List || attr instanceof Map<?,?>)) {
                return;
            }
            if(attr instanceof List) {
                final List<Definitions> results = (List<Definitions>) attr;
                final Definitions result = results.get(slotId);
                if (result == null) {
                    return;
                }
                final List<DropViewerEntry> entries = getEntries(player, result);
                populateRows(player, true, result, entries);
                player.getPacketDispatcher().sendClientScript(2239);
            } else {
                final Map<Integer, AlternateTable> results = (Map<Integer, AlternateTable>) attr;
                AlternateTable table = results.get(slotId);
                if(table == null)
                    return;
                player.currentViewIndex = slotId;

                final List<DropViewerEntry> entries = getEntries(player, table);
                populateRows(player, true, table, entries);
                player.getPacketDispatcher().sendClientScript(2239);
            }
        });
        bind("Rarity Display", (player, slotId, itemId, option) -> {
            player.toggleBooleanAttribute("drop_viewer_fractions");
            VarCollection.DROP_VIEWER_FRACTIONS.updateSingle(player);
            final Object rowsAttr = player.getTemporaryAttributes().get("drop_viewer_rows");
            if (!(rowsAttr instanceof List)) {
                final Object attr = player.getTemporaryAttributes().get("drop_viewer_results");
                final Map<Integer, AlternateTable> results = (Map<Integer, AlternateTable>) attr;
                AlternateTable table = results.get(player.currentViewIndex);
                if(table == null)
                    return;

                final List<DropViewerEntry> entries = getEntries(player, table);
                populateRows(player, true, table, entries);
                player.getPacketDispatcher().sendClientScript(2239);
                return;
            }
            final List<DropViewerEntry> rows = (List<DropViewerEntry>) rowsAttr;
            final Object resultAttr = player.getTemporaryAttributes().get("drop_viewer_search_result");
            if (!(resultAttr instanceof Definitions)) {
                return;
            }
            final Definitions searchResult = (Definitions) resultAttr;
            populateRows(player, false, searchResult, rows);
        });
    }

    private static final Int2IntMap transformedIds = new Int2IntOpenHashMap();

    static {
        //Re-point all the vorkath versions.
        transformedIds.put(8026, 8061);
        transformedIds.put(8058, 8061);
        transformedIds.put(8059, 8061);
        transformedIds.put(8060, 8061);
    }

    public static void open(@NotNull final Player player, final int npcId) {
        final int id = transformedIds.getOrDefault(npcId, npcId);
        final NPCDefinitions result = NPCDefinitions.get(id);
        final List<DropViewerEntry> entries = getEntries(player, result);
        if (entries.isEmpty()) {
            player.sendMessage("No drops found for " + NPCDefinitions.getOrThrow(id).getName().toLowerCase() + ".");
            return;
        }
        GameInterface.DROP_VIEWER.open(player);
        populateRows(player, true, result, entries);
        player.getPacketDispatcher().sendClientScript(2239);
    }

    public static void openToAlternateTables(@NotNull final Player player) {
        GameInterface.DROP_VIEWER.open(player);
        sendFakeResults(player);
        player.getPacketDispatcher().sendClientScript(2239);
    }

    private static void sendFakeResults(Player player) {
        int offsetY = 0;
        player.getPacketDispatcher().sendClientScript(10104, 1); //set searchtype (item/npc)
        Int2ObjectOpenHashMap<AlternateTable> mapped_values = new Int2ObjectOpenHashMap<>();
        for (int index = 0; index < AlternateTable.values().length; index++) {
            final AlternateTable def = AlternateTable.values()[index];
            final String name = def.getDisplayName();
            final String cb = "";
            String builder = "";

            final int width = Utils.getTextWidth(494, name + cb);
            final int height = width > 114 ? 22 : 13;
            player.getPacketDispatcher().sendClientScript(10105, index, offsetY, height, width, name, cb,
                    builder); //append npc search result
            offsetY += height;
            mapped_values.put(index, def);
        }
        player.getPacketDispatcher().sendClientScript(2239); //disable keyboard input on chatbox
        player.getPacketDispatcher().sendComponentSettings(GameInterface.DROP_VIEWER.getId(),
                GameInterface.DROP_VIEWER.getPlugin().get().getComponent("View Result"), 0, AlternateTable.values().length,
                AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendClientScript(10108, offsetY); //rebuild scrollllayer/bar
        player.addTemporaryAttribute("drop_viewer_results", mapped_values);
    }

    public static List<DropViewerEntry> getEntries(final Player player, final AlternateTable type) {
        return type.entries().stream().sorted(Collections.reverseOrder(comparingDouble(DropViewerEntry::rate))).collect(Collectors.toList());
    }

    public static List<DropViewerEntry> getEntries(final Player player, final Definitions r) {
        final LinkedList<DropViewerEntry> list = new LinkedList<>();
        if (r instanceof NPCDefinitions) {
            final NPCDefinitions result = (NPCDefinitions) r;
            final int id = result.getId();
            final NPCDrops.DropTable table = NPCDrops.getTable(id);
            final List<DropProcessor> processors = DropProcessorLoader.get(id);
            final HashMap<Integer, List<ItemDropViewerEntry>> rows = new HashMap<>();
            if (table != null) {
                for (final Drop drop : table.getDrops()) {
                    if (drop.getItemId() == ItemId.TOOLKIT) {
                        continue;
                    }
                    rows.computeIfAbsent(drop.getItemId(), l -> new LinkedList<>()).add(new ItemDropViewerEntry(drop.getItemId(), drop.getMinAmount(), drop.getMaxAmount(), (drop.getBaseRate() == 100000) ? 100 : ((drop.getBaseRate() / (float) table.getWeight()) * 100), ""));
                }
            }
            if (processors != null) {
                for (final DropProcessor processor : processors) {
                    for (final DropProcessor.DisplayedDrop drop : processor.getBasicDrops()) {
                        if (drop.getPredicate() != null && !drop.getPredicate().test(player, id)) {
                            continue;
                        }
                        double rate = drop.getRate(player, id);
                        rows.computeIfAbsent(drop.getId(), l -> new LinkedList<>()).add(new ItemDropViewerEntry(drop.getId(), drop.getMinAmount(), drop.getMaxAmount(), 1.0 / rate * 100, ""));
                    }
                    for (final Long2ObjectMap.Entry<DropProcessor.PredicatedDrop> entry :
                            processor.getInfoMap().long2ObjectEntrySet()) {
                        try {
                            final long packed = entry.getLongKey();
                            final int item = (int) (packed);
                            final int npc = (int) (packed >> 32);
                            if (id != npc) {
                                continue;
                            }
                            final List<ItemDropViewerEntry> tableDropList = rows.get(item);
                            final DropProcessor.PredicatedDrop drop = entry.getValue();
                            if (tableDropList == null) {
                                continue;
                            }
                            rows.remove(item);
                            for (final ItemDropViewerEntry tableDrop : tableDropList) {
                                rows.computeIfAbsent(item, l -> new LinkedList<>()).add(new ItemDropViewerEntry(tableDrop.getItem(), tableDrop.minAmount(), tableDrop.maxAmount(), tableDrop.rate(), drop.getInformation()));
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            }
            rows.forEach((key, value) -> list.addAll(value));
        } else {
            final HashMap<Integer, Object2ObjectMap<String, NPCDropViewerEntry>> rows = new HashMap<>();
            final LinkedList<ItemDefinitions> definitionsList = new LinkedList<>();
            definitionsList.add((ItemDefinitions) r);
            if (((ItemDefinitions) r).getNotedOrDefault() != ((ItemDefinitions) r).getId()) {
                definitionsList.add(ItemDefinitions.getOrThrow(((ItemDefinitions) r).getNotedOrDefault()));
            }
            for (final ItemDefinitions def : definitionsList) {
                final List<ItemDrop> drops = NPCDrops.getTableForItem(def.getId());
                if (drops != null) {
                    for (final ItemDrop d : drops) {
                        final NPCDrops.DisplayedNPCDrop drop = d.getDrop();
                        if (!NPCSpawnLoader.dropViewerNPCs.contains(d.getNpcId()) || (drop.getPredicate() != null && !drop.getPredicate().test(player, d.getNpcId()))) {
                            continue;
                        }
                        rows.computeIfAbsent(drop.getItemId(), l -> new Object2ObjectOpenHashMap<>()).put(NPCDefinitions.getOrThrow(d.getNpcId()).getName(), new NPCDropViewerEntry(drop.getItemId(), d.getNpcId(), drop.getMinAmount(), drop.getMaxAmount(), drop.getFunction().apply(player, d.getNpcId()), ""));
                    }
                }
            }
            rows.forEach((key, value) -> list.addAll(value.values()));
        }
        return list.stream().sorted(Collections.reverseOrder(comparingDouble(DropViewerEntry::rate))).collect(Collectors.toList());
    }

    public static void populateRows(final Player player, final boolean completeRefresh,
                                    final Definitions searchResult, final List<DropViewerEntry> rows) {
        int offsetY = 0;
        int index = 0;
        player.addTemporaryAttribute("drop_viewer_rows", rows);
        player.addTemporaryAttribute("drop_viewer_search_result", searchResult);
        if (searchResult != null) {
            if (searchResult instanceof NPCDefinitions) {
                player.getPacketDispatcher().sendClientScript(10103, ((NPCDefinitions) searchResult).getName(),
                        completeRefresh ? 1 : 0); //set layout title
            } else {
                player.getPacketDispatcher().sendClientScript(10103, ((ItemDefinitions) searchResult).getName(),
                        completeRefresh ? 1 : 0); //set layout title
            }
        }
        for (final DropViewerEntry r : rows) {
            final String formatted = StringFormatUtil.format((int) Math.round(1 / r.rate() * 100));
            String rarity;
            if (player.getBooleanAttribute("drop_viewer_fractions")) {
                rarity = "1 / " + (formatted.length() > 8 ? formatted.charAt(0) + " million" : formatted);
            } else {
                rarity = r.rate() == 100 ? "Always" : formatDropRate(r.rate()) + "%";
            }
            if (r instanceof ItemDropViewerEntry) {
                final ItemDropViewerEntry row = (ItemDropViewerEntry) r;
                player.getPacketDispatcher().sendClientScript(10114, index, offsetY, row.getItem(), row.info(),
                        row.minAmount(), row.maxAmount(), rarity); //append item row
            } else {
                final NPCDropViewerEntry row = (NPCDropViewerEntry) r;
                final String name = NPCDefinitions.get(row.getNpc()).getName();
                player.getPacketDispatcher().sendClientScript(10121, index, offsetY, row.getItemId(), name,
                        row.info(), row.minAmount(), row.maxAmount(), rarity); //append npc row
            }
            offsetY += r.isPredicated() ? 70 : 40;
            index++;
        }
        if (searchResult != null) {
            player.getPacketDispatcher().sendClientScript(10115, offsetY, completeRefresh ? 1 : 0); //rebuild
            // scrolllayer/bar
        }
    }

    public static void populateRows(final Player player, final boolean completeRefresh,
                                    final AlternateTable table, final List<DropViewerEntry> rows) {
        int offsetY = 0;
        int index = 0;
        player.addTemporaryAttribute("drop_viewer_rows", rows);
        player.addTemporaryAttribute("drop_viewer_search_result", table);
        if (table != null) {
            player.getPacketDispatcher().sendClientScript(10103, (table).getDisplayName(), completeRefresh ? 1 : 0); //set layout title
            for (final DropViewerEntry r : rows) {
                if (r instanceof OtherDropViewerEntry row) {
                    String rarity;
                    if (player.getBooleanAttribute("drop_viewer_fractions")) {
                        rarity = row.toFractional();
                    } else {
                        rarity = row.toPercentage();
                    }
                    final String name = ItemDefinitions.nameOf(row.itemId());
                    player.getPacketDispatcher().sendClientScript(10121, index, offsetY, row.itemId(), name,
                            row.info(), row.minAmount(), row.maxAmount(), rarity); //append npc row
                }
                offsetY += r.isPredicated() ? 70 : 40;
                index++;
            }
            player.getPacketDispatcher().sendClientScript(10115, offsetY, completeRefresh ? 1 : 0); //rebuild
        }
    }

    public static String formatDropRate(double rate) {
        return String.format(rate < 0.001 ? "%.4f" :
                rate < 0.01 ? "%.3f" : "%.2f", rate);
    }

    public static void search(final Player player, final String rawInput) {
        final String input = rawInput.toLowerCase();
        player.addTemporaryAttribute("drop_viewer_input", input);
        final int type = player.getNumericTemporaryAttribute("drop_viewer_search_type").intValue();
        if (type == 0) {
            final Object2ObjectOpenHashMap<NPCDefinitions, Set<NPCDefinitions>> map = new Object2ObjectOpenHashMap<>();
            final LinkedList<NPCDefinitions> results = new LinkedList<>();
            loop:
            for (final NPCDefinitions def : NPCDefinitions.getDefinitions()) {

                if (def == null)
                    continue;

                if (!def.containsOption("Attack"))
                    continue;

                if (def.getCombatLevel() == 0)
                    continue;

                if (NPCSpawnLoader.ignoredMonsters.contains(def.getId()))
                    continue;

                if (NPCDrops.getTable(def.getId()) == null && DropProcessorLoader.get(def.getId()) == null)
                    continue;

                if (!NPCSpawnLoader.dropViewerNPCs.contains(def.getId()))
                    continue;

                if (def.getLowercaseName().contains(input)) {
                    for (final NPCDefinitions result : results) {
                        if (result.getName().equalsIgnoreCase(def.getName()) && result.getCombatLevel() == def.getCombatLevel()) {
                            if (NPCDrops.equalsIgnoreRates(result.getId(), def.getId())) {
                                map.computeIfAbsent(result,
                                        r -> new ObjectOpenHashSet<>(Collections.singleton(result))).add(def);
                                continue loop;
                            }
                        }
                    }
                    map.computeIfAbsent(def, r -> new ObjectOpenHashSet<>(Collections.singleton(def)));
                    results.add(def);
                }
            }
            results.sort((o1, o2) -> {
                String x1 = o1.getName();
                String x2 = o2.getName();
                int sComp = x1.compareToIgnoreCase(x2);
                if (sComp != 0) {
                    return sComp;
                }
                return Integer.compare(o1.getCombatLevel(), o2.getCombatLevel());
            });
            int offsetY = 0;
            player.getPacketDispatcher().sendClientScript(10104, type); //set searchtype (item/npc)
            for (int index = 0; index < results.size(); index++) {
                final NPCDefinitions def = results.get(index);
                String name = def.getName();
                if(CUSTOM_NAMES.containsKey(def.getId()))
                    name = CUSTOM_NAMES.get(def.getId());
                final String cb = Utils.getPreciseLevelColour(player.getCombatLevel(), def.getCombatLevel()) + " (lvl" +
                        "-" + def.getCombatLevel() + ")";
                final ArrayList<String> allAreas = new ArrayList<>();
                map.get(def).forEach(definition -> {
                    final Set<String> areas = NPCSpawnLoader.getFoundLocations(definition.getId());
                    if (areas == null) {
                        return;
                    }
                    areas.forEach(string -> {
                        if (!allAreas.contains(string)) {
                            allAreas.add(string);
                        }
                    });
                });
                //Sorting alphabetically w/ the exception of the 'Undefined' string which will always appear in the
                // bottom.
                allAreas.sort((s1, s2) -> {
                    if (s1.equals("Undefined area")) {
                        return 1;
                    } else if (s2.equals("Undefined area")) {
                        return -1;
                    }
                    return s1.compareTo(s2);
                });
                final StringBuilder builder = new StringBuilder();
                for (final String area : allAreas) {
                    builder.append("- ").append(area).append("<br>");
                }
                final int width = Utils.getTextWidth(494, name + cb);
                final int height = width > 114 ? 22 : 13;
                player.getPacketDispatcher().sendClientScript(10105, index, offsetY, height, width, name, cb,
                        builder.toString()); //append npc search result
                offsetY += height;
            }
            player.getPacketDispatcher().sendClientScript(2239); //disable keyboard input on chatbox
            player.getPacketDispatcher().sendComponentSettings(GameInterface.DROP_VIEWER.getId(),
                    GameInterface.DROP_VIEWER.getPlugin().get().getComponent("View Result"), 0, results.size(),
                    AccessMask.CLICK_OP1);
            player.getPacketDispatcher().sendClientScript(10108, offsetY); //rebuild scrollllayer/bar
            player.addTemporaryAttribute("drop_viewer_results", results);
            if (results.size() == 0) {
                player.getPacketDispatcher().sendClientScript(10109, "No results were found with your search.");
                //set response msg
            } else {
                final NPCDefinitions result = results.get(0);
                if (result == null) {
                    return;
                }
                final List<DropViewerEntry> entries = getEntries(player, result);
                populateRows(player, true, results.get(0), entries); //populate rows of first result
                player.getPacketDispatcher().sendClientScript(10107, 0); //highlight first result
            }
        } else {
            final LinkedList<ItemDefinitions> results = new LinkedList<>();
            for (final ItemDefinitions def : searchableItemDefinitions) {
                if (def.getLowercaseName().contains(input)) {
                    results.add(def);
                }
            }
            int offsetY = 0;
            player.getPacketDispatcher().sendClientScript(10104, type); //set search type (item/npc)
            results.removeIf(def -> {
                final List<DropViewerEntry> entries = getEntries(player, def);
                return entries == null || entries.isEmpty();
            });
            results.removeIf(result -> {
                if (result.isNoted()) {
                    final int unnotedId = result.getUnnotedOrDefault();
                    return results.contains(ItemDefinitions.getOrThrow(unnotedId));
                }
                return false;
            });
            results.sort(Comparator.comparing(ItemDefinitions::getName));
            for (int index = 0; index < results.size(); index++) {
                final ItemDefinitions def = results.get(index);
                final String name = def.getName();
                final int width = Utils.getTextWidth(494, name);
                final int height = width > 114 ? 22 : 13;
                player.getPacketDispatcher().sendClientScript(10120, index, offsetY, height, width, name); //append
                // item search result
                offsetY += height;
            }
            player.getPacketDispatcher().sendClientScript(2239); //disable keyboard input on chatbox
            player.getPacketDispatcher().sendComponentSettings(GameInterface.DROP_VIEWER.getId(),
                    GameInterface.DROP_VIEWER.getPlugin().get().getComponent("View Result"), 0, results.size(),
                    AccessMask.CLICK_OP1);
            player.getPacketDispatcher().sendClientScript(10108, offsetY); //rebuild scrolllayer/bar
            player.addTemporaryAttribute("drop_viewer_results", results);
            if (results.size() == 0) {
                player.getPacketDispatcher().sendClientScript(10109, "No results were found with your search.");
                //set response msg
            } else {
                final ItemDefinitions result = results.get(0);
                if (result == null) {
                    return;
                }
                final List<DropViewerEntry> entries = getEntries(player, result);
                populateRows(player, true, results.get(0), entries); //populate rows of first result
                player.getPacketDispatcher().sendClientScript(10107, 0); //highlight first result
            }
        }
    }

    private static final List<ItemDefinitions> searchableItemDefinitions = new LinkedList<>();

    public static void populateDropViewerData() {
        NPCDrops.populateDropViewerData(searchableItemDefinitions);
    }

    private void reset(final Player player) {
        player.getTemporaryAttributes().remove("drop_viewer_results");
        player.getTemporaryAttributes().remove("drop_viewer_search_type");
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DROP_VIEWER;
    }

}
