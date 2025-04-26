package com.zenyte.game.content.partyroom;

import com.google.common.eventbus.Subscribe;
import com.zenyte.cores.CoresManager;
import com.zenyte.cores.ScheduledExternalizable;
import com.zenyte.game.GameConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.DefaultGson;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.LoginEvent;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @author Kris | 07/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class BirthdayEventRewardList implements ScheduledExternalizable {

    private static final Logger log = LoggerFactory.getLogger(BirthdayEventRewardList.class);

    private static final Set<String> usernames = new ObjectOpenHashSet<>();
    private static final String path = "data/birthday event.json";

    private static void load(final BufferedReader reader) {
        final String[] set = DefaultGson.getGson().fromJson(reader, String[].class);
        for (final String username : set) {
            usernames.add(StringFormatUtil.formatUsername(username));
        }
    }

    public static void reload() {
        CoresManager.getServiceProvider().submit(() -> {
            try {
                load(new BufferedReader(new FileReader(new File(path))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static void refreshAll() {
        for (final String username : usernames) {
            World.getPlayer(username).ifPresent(BirthdayEventRewardList::addReward);
        }
    }

    private static void addReward(@NotNull final Player player) {
        if (player.getTrackedHolidayItems().contains(ItemId.BIRTHDAY_CAKE)) {
            return;
        }
        player.getTrackedHolidayItems().add(ItemId.BIRTHDAY_CAKE);
        player.sendMessage(Colour.RS_GREEN.wrap("You have received a birthday cake for participating in the 2020 " + GameConstants.SERVER_NAME + " birthday event."));
        player.sendMessage(Colour.RS_GREEN.wrap("Should you lose the cake, you can reclaim it from Diango in Draynor Village."));
        player.getInventory().addOrDrop(new Item(ItemId.BIRTHDAY_CAKE));
    }

    @Subscribe
    public static void onLogin(final LoginEvent event) {
        if (usernames.contains(event.getPlayer().getUsername())) {
            addReward(event.getPlayer());
        }
    }

    public static void addUsername(@NotNull final String username) {
        if (usernames.add(StringFormatUtil.formatUsername(username))) {
            CoresManager.getServiceProvider().submit(() -> {
                final String[] array = usernames.toArray(new String[0]);
                final String toJson = DefaultGson.getGson().toJson(array);
                try {
                    final PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);
                    pw.println(toJson);
                    pw.close();
                } catch (final Exception e) {
                    log.error("", e);
                }
            });
            World.getPlayer(username).ifPresent(BirthdayEventRewardList::addReward);
        }
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 0;
    }

    @Override
    public void read(final @NotNull BufferedReader reader) {
        load(reader);
    }

    @Override
    public void write() {
    }

    @Override
    public String path() {
        return path;
    }
}
