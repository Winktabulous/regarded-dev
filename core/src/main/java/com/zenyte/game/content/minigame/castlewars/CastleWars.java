package com.zenyte.game.content.minigame.castlewars;

import com.zenyte.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.Chunk;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.content.minigame.castlewars.CastleWarsLobby.*;
import static com.zenyte.plugins.object.CastleWarsFlagObject.SARADOMIN_FLAG;
import static com.zenyte.plugins.object.CastleWarsFlagObject.ZAMORAK_FLAG;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastleWars {
    private static final Logger log = LoggerFactory.getLogger(CastleWars.class);
    private static boolean active;
    private static boolean timerStarted;
    public static final IntList allowedItems = new IntArrayList();
    private static final Object2IntOpenHashMap<CWarsOverlayVarbit> saraVarbits = new Object2IntOpenHashMap<>();
    private static final Object2IntOpenHashMap<CWarsOverlayVarbit> zamVarbits = new Object2IntOpenHashMap<>();
    public static final List<WorldObject> SPAWNED_OBJECTS = new ArrayList<>();
    public static final List<Player> SARADOMIN_TEAM = new ArrayList<>();
    public static final List<Player> ZAMORAK_TEAM = new ArrayList<>();
    /* Static item declarations */
    public static final Item TOOLKIT = new Item(4051);
    public static final Item EXPLOSIVE_POTION = new Item(4045);
    public static final Item CATAPULT_ROCK = new Item(4043);
    public static final Item CASTLEWARS_TICKET = new Item(4067);
    private static final Item[] CASTLE_WARS_ITEMS = new Item[] {EXPLOSIVE_POTION, new Item(4049), CATAPULT_ROCK, new Item(954), new Item(590), TOOLKIT, new Item(4053), new Item(1925), new Item(1265), SARADOMIN_FLAG, ZAMORAK_FLAG, SARADOMIN_CLOAK, ZAMORAK_CLOAK};

    public static void preloadItemMap() {
        for (final ItemDefinitions defs : ItemDefinitions.getDefinitions()) {
            // filter based on option
            if (defs == null) {
                continue;
            }
            // skip store-bought castlewars hood, cloak, and flags.
            if (defs.getName().equalsIgnoreCase("castlewars hood") || defs.getName().equalsIgnoreCase("castlewars cloak") || defs.getId() == SARADOMIN_FLAG.getId() || defs.getId() == ZAMORAK_FLAG.getId()) {
                continue;
            }
            if (defs.containsOption("Equip") || defs.containsOption("Wear") || defs.containsOption("Wield") || defs.containsOption("Drink")) {
                allowedItems.add(defs.getId());
                continue;
            }
            if (!defs.getName().contains("rune")) {
                continue;
            } else {
                final String[] nameArray = defs.getName().split(" ");
                // all rune formats are two-word names.
                if (nameArray.length != 2) {
                    continue;
                }
                if (nameArray[1].equalsIgnoreCase("rune")) {
                    allowedItems.add(defs.getId());
                }
            }
        }
        allowedItems.add(RunePouch.RUNE_POUCH.getId());
        allowedItems.add(RunePouch.DIVINE_RUNE_POUCH.getId());
        allowedItems.add(20724); // imbued heart
        allowedItems.add(7509); // rock cake
        allowedItems.add(7510); // rock cake 2
        allowedItems.add(CASTLEWARS_TICKET.getId());
    }

    public static void start() {
        setActive(true);
        populateVarbitDefaults();
        for (final Player player : SARADOMIN_LOBBY) {
            processBracelet(player);
            CastleWarsOverlay.processVarbits(player);
            CastleWars.SARADOMIN_TEAM.add(player);
            player.setLocation(CastleWarsTeam.SARADOMIN.getRespawn());
        }
        SARADOMIN_LOBBY.clear();
        for (final Player player : ZAMORAK_LOBBY) {
            processBracelet(player);
            CastleWarsOverlay.processVarbits(player);
            CastleWars.ZAMORAK_TEAM.add(player);
            player.setLocation(CastleWarsTeam.ZAMORAK.getRespawn());
        }
        ZAMORAK_LOBBY.clear();
    }

    private static final void processBracelet(@NotNull final Player player) {
        final int braceletId = player.getEquipment().getId(EquipmentSlot.HANDS);
        if (braceletId == ItemId.CASTLE_WARS_BRACELET3 || braceletId == ItemId.CASTLE_WARS_BRACELET2 || braceletId == ItemId.CASTLE_WARS_BRACELET1) {
            player.getEquipment().set(EquipmentSlot.HANDS, braceletId == ItemId.CASTLE_WARS_BRACELET1 ? null : braceletId == ItemId.CASTLE_WARS_BRACELET3 ? new Item(ItemId.CASTLE_WARS_BRACELET2) : new Item(ItemId.CASTLE_WARS_BRACELET1));
            player.getTemporaryAttributes().put("castle wars bracelet effect", true);
        }
    }

    public static void finish() {
        final ArrayList<Player> players = new ArrayList<Player>();
        players.addAll(SARADOMIN_TEAM);
        players.addAll(ZAMORAK_TEAM);
        for (final Player player : players) {
            processPlayerGameFinish(player);
        }
        saraVarbits.clear();
        zamVarbits.clear();
        setActive(false);
        setTimerStarted(false);
        clearArea();
        CastleWarsLobby.setTicks(500);
    }

    public static void processPlayerGameFinish(final Player player) {
        final int tickets = getTicketAmount(getTeam(player));
        player.getInventory().addItem(CASTLEWARS_TICKET.getId(), tickets);
        player.getTemporaryAttributes().remove("castle wars bracelet effect");
        final boolean saradomin = getTeam(player).equals(CastleWarsTeam.SARADOMIN);
        final int saraScore = getVarbit(CastleWarsTeam.SARADOMIN, CWarsOverlayVarbit.SARADOMIN_SCORE);
        final int zamScore = getVarbit(CastleWarsTeam.SARADOMIN, CWarsOverlayVarbit.ZAMORAK_SCORE);
        if (CastleWarsArea.hasFlag(player)) {
            CastleWarsArea.handleFlagDrop(player, false);
        }
        removeCwarsItems(player, true);
        if ((saraScore == zamScore)) {
            player.getDialogueManager().start(new ItemChat(player, CASTLEWARS_TICKET, "You have received " + tickets + " Castle wars tickets for tying the game!"));
        } else {
            if ((saraScore > zamScore && saradomin) || (zamScore > saraScore && !saradomin)) {
                player.getDialogueManager().start(new ItemChat(player, CASTLEWARS_TICKET, "You have received " + tickets + " Castle wars tickets for winning the game!"));
            } else {
                player.getDialogueManager().start(new ItemChat(player, CASTLEWARS_TICKET, "You have received " + tickets + " Castle wars tickets for losing the game!"));
            }
        }
        player.setLocation(MAIN_LOBBY_SPAWN);
    }

    public static int getTicketAmount(final CastleWarsTeam team) {
        final boolean saradomin = team.equals(CastleWarsTeam.SARADOMIN);
        final int saraScore = getVarbit(team, CWarsOverlayVarbit.SARADOMIN_SCORE);
        final int zamScore = getVarbit(team, CWarsOverlayVarbit.ZAMORAK_SCORE);
        if (zamScore == saraScore) {
            return zamScore > 0 ? 1 : 2;
        }
        if (zamScore > 0 && saraScore == 0 || saraScore > 0 && zamScore == 0) {
            return zamScore > 0 ? (saradomin ? 1 : 3) : (saradomin ? 3 : 1);
        }
        if (zamScore > saraScore || zamScore < saraScore) {
            return zamScore > saraScore ? (saradomin ? 1 : 2) : (saradomin ? 2 : 1);
        }
        return 1;
    }

    public static int getVarbit(final CastleWarsTeam team, final CWarsOverlayVarbit k) {
        return (team.equals(CastleWarsTeam.SARADOMIN) ? saraVarbits : zamVarbits).getInt(k);
    }

    public static void setVarbit(final CastleWarsTeam team, final CWarsOverlayVarbit k, final int v) {
        (team.equals(CastleWarsTeam.SARADOMIN) ? saraVarbits : zamVarbits).replace(k, v);
        if (!k.isUniversal()) {
            final List<Player> teamList = team.equals(CastleWarsTeam.SARADOMIN) ? SARADOMIN_TEAM : ZAMORAK_TEAM;
            for (final Player player : teamList) {
                player.getVarManager().sendBit(k.getId(), v);
            }
        } else {
            for (final Player player : SARADOMIN_TEAM) {
                player.getVarManager().sendBit(k.getId(), v);
            }
            for (final Player player : ZAMORAK_TEAM) {
                player.getVarManager().sendBit(k.getId(), v);
            }
        }
    }

    public static boolean isUserPlaying(final Player player) {
        return SARADOMIN_TEAM.contains(player) || ZAMORAK_TEAM.contains(player);
    }

    public static CastleWarsTeam getTeam(final Player player) {
        return SARADOMIN_TEAM.contains(player) ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK;
    }

    public static CastleWarsTeam getOppositeTeam(final Player player) {
        return !SARADOMIN_TEAM.contains(player) ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK;
    }

    public static void populateVarbitDefaults() {
        for (final CWarsOverlayVarbit varbit : CWarsOverlayVarbit.VALUES) {
            saraVarbits.put(varbit, varbit.getDefaultV());
            zamVarbits.put(varbit, varbit.getDefaultV());
        }
    }

    public static void removeCwarsItems(final Player player, final boolean fullExit) {
        for (final Item item : CASTLE_WARS_ITEMS) {
            player.getInventory().deleteItem(item.getId(), player.getInventory().getAmountOf(item.getId()));
        }
        if (fullExit) {
            if (player.getEquipment().getId(EquipmentSlot.CAPE) == CastleWarsLobby.SARADOMIN_CLOAK.getId() || player.getEquipment().getId(EquipmentSlot.CAPE) == CastleWarsLobby.ZAMORAK_CLOAK.getId()) {
                player.getEquipment().set(EquipmentSlot.CAPE, null);
                player.getEquipment().refresh();
            }
        }
    }

    /**
     * Clears the castle-wars game-area without disrupting any game logic.
     * Removes the region from existing regions cache and immediately after reloads the region cleanly from the cache.
     * Wipes every single chunk held within the castle boundaries, meaning all spawned objects, removed objects and ground items are wiped.
     * Iterates all players in and around the castle, updating the chunks within their scene w/ the new reloaded region;
     * Meaning there won't be remnants of old ground items or objects visible to them.
     */
    public static final void clearArea() {
        final long nano = System.nanoTime();
        //Remove previous region and create a new one off of what map info is found in the cache.
        World.regions.remove(9520);
        World.loadRegion(9520);
        CharacterLoop.forEachChunk(9520, Chunk::wipe);
        for (int z = 0; z < 4; z++) {
            CharacterLoop.forEach(new Location(2399, 3104, z), 100, Player.class, player -> {
                player.getChunksInScope().clear();
                player.updateScopeInScene();
            });
        }
        log.info("Consumed " + (System.nanoTime() - nano) + " nanoseconds to wipe the Castle-Wars region.");
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        CastleWars.active = active;
    }

    public static boolean isTimerStarted() {
        return timerStarted;
    }

    public static void setTimerStarted(boolean timerStarted) {
        CastleWars.timerStarted = timerStarted;
    }
}
