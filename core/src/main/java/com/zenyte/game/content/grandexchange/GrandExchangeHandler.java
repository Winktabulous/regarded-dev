package com.zenyte.game.content.grandexchange;

import com.google.gson.Gson;
import com.zenyte.game.parser.Parse;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.utils.TextUtils;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tommeh | 26 nov. 2017 : 21:36:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class GrandExchangeHandler implements Parse {

    private static final Logger log = LoggerFactory.getLogger(GrandExchangeHandler.class);
    private static Map<String, Int2ObjectOpenHashMap<ExchangeOffer>> offers;
    private static Map<String, Int2ObjectOpenHashMap<ExchangeOffer>> quarantinedOffers;
    public static final String OFFERS_FILE_DIRECTORY = "./data/grandexchange/offers.json";
    public static final String QUARANTINE_FILE_DIRECTORY = "./data/grandexchange/banned_offers.json";
    public static final String PRICES_FILE_DIRECTORY = "./data/grandexchange/prices.json";
    private static boolean loaded;
    public static final MutableBoolean status = new MutableBoolean();

    public static void init() {
        try {
            new GrandExchangeHandler().parse();
            new JSONGEItemDefinitionsLoader().parse();
            loaded = true;
        } catch (final Throwable e) {
            log.error("", e);
        }
    }

    public static void save() {
        try {
            if (!loaded) return;
            status.setTrue();
            final Gson gson = DefaultGson.getGson();
            final ArrayList<ExchangeOffer> list = new ArrayList<>(offers.size());
            for (final Int2ObjectOpenHashMap<ExchangeOffer> map : offers.values()) {
                if (map.isEmpty()) {
                    continue;
                }
                list.addAll(map.values());
            }
            final String toJson = gson.toJson(list);
            try {
                final PrintWriter pw = new PrintWriter(OFFERS_FILE_DIRECTORY);
                pw.println(toJson);
                pw.close();
            } catch (final Exception e) {
                log.error("", e);
            }

            final ArrayList<ExchangeOffer> qList = new ArrayList<>(quarantinedOffers.size());
            for (final Int2ObjectOpenHashMap<ExchangeOffer> map : quarantinedOffers.values()) {
                if (map.isEmpty()) {
                    continue;
                }
                qList.addAll(map.values());
            }
            final String qToJson = gson.toJson(qList);
            try {
                final PrintWriter pw = new PrintWriter(QUARANTINE_FILE_DIRECTORY);
                pw.println(qToJson);
                pw.close();
            } catch (final Exception e) {
                log.error("", e);
            }

            status.setFalse();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @NotNull
    static Int2ObjectOpenHashMap<ExchangeOffer> getOffers(String username) {
        username = TextUtils.formatNameForProtocol(username);
        Int2ObjectOpenHashMap<ExchangeOffer> offers = GrandExchangeHandler.offers.get(username);
        if (offers == null) {
            offers = new Int2ObjectOpenHashMap<>(8);
            GrandExchangeHandler.offers.put(username, offers);
        }
        return offers;
    }

    @NotNull
    static Map<String, Int2ObjectOpenHashMap<ExchangeOffer>> getAllOffers() {
        return offers;
    }

    static void addOffer(final String username, final ExchangeOffer offer) {
        getOffers(username).put(offer.getSlot(), offer);
    }

    static void remove(final String username, final int slot) {
        getOffers(username).remove(slot);
    }

    @Override
    public void parse() throws Throwable {
        offers = new HashMap<>();
        quarantinedOffers = new HashMap<>();

        final File file = new File(OFFERS_FILE_DIRECTORY);
        if (file.exists()) {

            final BufferedReader br = new BufferedReader(new FileReader(file));
            final ExchangeOffer[] loadedOffers = DefaultGson.getGson().fromJson(br, ExchangeOffer[].class);

            for (final ExchangeOffer offer : loadedOffers) {
                offer.setUsername(TextUtils.formatNameForProtocol(offer.getUsername()));
                Int2ObjectOpenHashMap<ExchangeOffer> currentMap = offers.get(offer.getUsername());
                if (currentMap == null) {
                    currentMap = new Int2ObjectOpenHashMap<>(8);
                    offers.put(offer.getUsername(), currentMap);
                }
                final Container container = offer.getContainer();
                offer.setContainer(new Container(ContainerPolicy.ALWAYS_STACK,
                        ContainerType.GE_COLLECTABLES_CONTAINERS[offer.getSlot()], Optional.empty()));
                offer.getContainer().setContainer(container);
                offer.getContainer().setFullUpdate(true);
                if (offer.getLastUpdateTime() <= 0) {
                    offer.refreshUpdateTime();
                }
                currentMap.put(offer.getSlot(), offer);
            }
        }

        final File qfile = new File(QUARANTINE_FILE_DIRECTORY);
        if (qfile.exists()) {

            final BufferedReader br = new BufferedReader(new FileReader(qfile));
            final ExchangeOffer[] loadedOffers = DefaultGson.getGson().fromJson(br, ExchangeOffer[].class);

            for (final ExchangeOffer offer : loadedOffers) {
                offer.setUsername(TextUtils.formatNameForProtocol(offer.getUsername()));
                Int2ObjectOpenHashMap<ExchangeOffer> currentMap = quarantinedOffers.get(offer.getUsername());
                if (currentMap == null) {
                    currentMap = new Int2ObjectOpenHashMap<>(8);
                    quarantinedOffers.put(offer.getUsername(), currentMap);
                }
                final Container container = offer.getContainer();
                offer.setContainer(new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.GE_COLLECTABLES_CONTAINERS[offer.getSlot()], Optional.empty()));
                offer.getContainer().setContainer(container);
                offer.getContainer().setFullUpdate(true);
                if (offer.getLastUpdateTime() <= 0) {
                    offer.refreshUpdateTime();
                }
                currentMap.put(offer.getSlot(), offer);
            }
        }
    }

    public static Map<String, Int2ObjectOpenHashMap<ExchangeOffer>> getOffers() {
        return offers;
    }

    public static void quarantineOffers(@Nullable Player player) {
        if(player == null)
            return;

        String username = TextUtils.formatNameForProtocol(player.getUsername());
        Int2ObjectOpenHashMap<ExchangeOffer> playerOffers = getOffers(player.getUsername());
        quarantinedOffers.put(username, playerOffers);
        offers.remove(username);
    }

    public static void quarantineOffers(String player) {
        String username = TextUtils.formatNameForProtocol(player);
        Int2ObjectOpenHashMap<ExchangeOffer> playerOffers = getOffers(username);
        quarantinedOffers.put(username, playerOffers);
        offers.remove(username);
    }

    public static void restoreOffers(@NotNull String offender) {
        String username = TextUtils.formatNameForProtocol(offender);
        Int2ObjectOpenHashMap<ExchangeOffer> playerOffers = quarantinedOffers.get(username);
        if(playerOffers == null)
            return;
        offers.put(username, playerOffers);
        quarantinedOffers.remove(username);
    }

    public static boolean isQuarantined(String playerName) {
        String username = TextUtils.formatNameForProtocol(playerName);
        return quarantinedOffers.containsKey(username);
    }
}
