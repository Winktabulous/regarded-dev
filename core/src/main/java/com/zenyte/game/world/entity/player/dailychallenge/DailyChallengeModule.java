package com.zenyte.game.world.entity.player.dailychallenge;

import com.google.common.eventbus.Subscribe;
import com.near_reality.game.world.PlayerEvent;
import com.near_reality.game.world.WorldEvent;
import com.near_reality.game.world.WorldEventListener;
import com.near_reality.game.world.WorldHooks;
import com.zenyte.game.model.RuneDate;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.InitializationEvent;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.StaticInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

@StaticInitializer
public final class DailyChallengeModule {

    private static int
            dayOfYear;

    private static boolean
            removePlayerPostProcessListener = false;

    private static final Logger
            log = LoggerFactory.getLogger(DailyChallengeManager.class);

    private static final WorldEventListener<PlayerEvent.PostProcess>
            CHECK_DATE_FOR_PLAYER = event -> RuneDate.checkDate(event.getPlayer());

    @Subscribe
    public static void onServerLaunch(final ServerLaunchEvent launchEvent) {
        final WorldHooks hooks = launchEvent.getWorldThread().getHooks();
        hooks.register(WorldEvent.Tick.class, tick -> {
            try {
                final int currentDayOfYear = dayOfYear;
                dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                if (currentDayOfYear != dayOfYear) {
                    hooks.register(PlayerEvent.PostProcess.class, CHECK_DATE_FOR_PLAYER);
                    removePlayerPostProcessListener = true;
                } else if (removePlayerPostProcessListener) {
                    hooks.remove(PlayerEvent.PostProcess.class, CHECK_DATE_FOR_PLAYER);
                    removePlayerPostProcessListener = false;
                }
            } catch (Throwable e) {
                log.error("Failed to set day of the year {}", dayOfYear, e);
            }
        });
    }

    @Subscribe
    public static void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final DailyChallengeManager otherManager = savedPlayer.getDailyChallengeManager();
        if (otherManager == null || otherManager.challengeProgression == null) {
            return;
        }
        player.getDailyChallengeManager().setChallengeProgression(otherManager.challengeProgression);
    }
}
