package com.zenyte.game.content.event.easter2020;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.event.easter2020.plugin.npc.EasterBird;
import com.zenyte.game.content.event.easter2020.plugin.npc.EasterImpling;
import com.zenyte.game.content.event.easter2020.plugin.npc.Incubator;
import com.zenyte.game.content.event.easter2020.plugin.object.CoalSupplyObject;
import com.zenyte.game.content.event.easter2020.plugin.object.IncubatorControlsObject;
import com.zenyte.game.content.event.easter2020.plugin.object.IncubatorWaterTankObject;
import com.zenyte.game.content.event.easter2020.plugin.object.SeedBowl;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.plugins.events.LoginEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Corey
 * @since 28/03/2020
 */
@SkipPluginScan
public class SplittingHeirs {
    @Subscribe
    public static void onLogin(@NotNull final LoginEvent event) {
        for (int i = 3710; i < 3715; i++) {
            event.getPlayer().getVarManager().sendVar(i, 0);
        }
        refreshAllVarbits(event.getPlayer());
    }

    @NotNull
    public static Stage getProgress(final Player player) {
        return Objects.requireNonNull(Stage.forStage(player.getNumericAttribute(Stage.EVENT_ATTRIBUTE_KEY).intValue()));
    }

    public static boolean progressedAtLeast(final Player player, final Stage progress) {
        final Stage currentProgress = getProgress(player);
        return currentProgress.ordinal() >= progress.ordinal();
    }

    public static void advanceStageWithoutRefreshing(@NotNull final Player player, @NotNull final Stage stage) {
        if (getProgress(player).ordinal() >= stage.ordinal()) {
            // we don't want to go backwards, but don't want to add checks everywhere we progress to player
            return;
        }
        player.addAttribute(Stage.EVENT_ATTRIBUTE_KEY, stage.ordinal());
    }

    public static void advanceStage(@NotNull final Player player, @NotNull final Stage stage) {
        if (getProgress(player).ordinal() >= stage.ordinal()) {
            // we don't want to go backwards, but don't want to add checks everywhere we progress to player
            return;
        }
        advanceStageWithoutRefreshing(player, stage);
        refreshAllVarbits(player);
    }

    public static boolean isComplete(final Player player) {
        return progressedAtLeast(player, Stage.EVENT_COMPLETE);
    }

    public static void refreshAllVarbits(final Player player) {
        final Stage progress = getProgress(player);
        progress.sendVarbit(player);
        if (progressedAtLeast(player, Stage.BIRD_FED_SPEAK_TO_BUNNY_JR)) {
            EasterBird.Varbit.WATER_BOWL.sendVar(player);
            SeedBowl.getRequiredBowl(player).getVarbit().sendVar(player);
        }
        if (progressedAtLeast(player, Stage.IMPLINGS_GATHERED_SPEAK_TO_BUNNY_JR)) {
            for (EasterImpling.Npc impling : EasterImpling.Npc.values) {
                player.getVarManager().sendBit(impling.getVarbitId(), 1);
            }
        }
        if (progressedAtLeast(player, Stage.INCUBATOR_FIXED_SPEAK_TO_BUNNY_JR)) {
            Incubator.State.FULLY_FIXED.setState(player);
            CoalSupplyObject.fix(player);
            IncubatorWaterTankObject.fix(player);
            IncubatorControlsObject.fix(player);
        }
        if (progressedAtLeast(player, Stage.EVENT_COMPLETE)) {
            player.getEmotesHandler().unlock(Emote.AROUND_THE_WORLD_IN_EGGTY_DAYS);
            player.getEmotesHandler().unlock(Emote.RABBIT_HOP);
        }
    }
}
