package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.CharacterLoop;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Corey
 * @since 14/12/2019
 */
public class AChristmasWarble {
    public static boolean hasStarted(final Player player) {
        return progressedAtLeast(player, ChristmasWarbleProgress.START);
    }

    public static ChristmasWarbleProgress getProgress(final Player player) {
        return ChristmasWarbleProgress.forStage(player.getNumericAttribute(ChristmasWarbleProgress.EVENT_ATTRIBUTE_KEY).intValue());
    }

    public static boolean progressedAtLeast(final Player player, final ChristmasWarbleProgress progress) {
        final AChristmasWarble.ChristmasWarbleProgress currentProgress = getProgress(player);
        if (currentProgress == null) {
            return false;
        }
        return currentProgress.getStage() >= progress.getStage();
    }

    public static void progressWithoutRefreshing(@NotNull final Player player, @NotNull final ChristmasWarbleProgress stage) {
        if (getProgress(player) != null && getProgress(player).getStage() >= stage.getStage()) {
            // we don't want to go backwards, but don't want to add checks everywhere we progress to player
            return;
        }
        player.addAttribute(ChristmasWarbleProgress.EVENT_ATTRIBUTE_KEY, stage.getStage());
    }

    public static void progress(final Player player, final ChristmasWarbleProgress stage) {
        if (getProgress(player) != null && getProgress(player).getStage() >= stage.getStage()) {
            // we don't want to go backwards, but don't want to add checks everywhere we progress to player
            return;
        }
        player.addAttribute(ChristmasWarbleProgress.EVENT_ATTRIBUTE_KEY, stage.getStage());
        ChristmasUtils.refreshAllVarbits(player);
    }

    public static void start(final Player player) {
        if (player.getAttributes().containsKey(ChristmasWarbleProgress.EVENT_ATTRIBUTE_KEY)) {
            return;
        }
        progress(player, ChristmasWarbleProgress.START);
        final List<NPC> list = CharacterLoop.find(player.getLocation(), 4, NPC.class, n -> n.getId() == ChristmasConstants.PARTYGOER_EVENT_INTRODUCER);
        if (list.isEmpty()) {
            return;
        }
        final NPC introducer = list.get(0);
        introducer.faceEntity(player);
        player.getDialogueManager().start(Partygoer.introductionDialogue(player, introducer));
    }

    public static boolean hasCompleted(final Player player) {
        return progressedAtLeast(player, ChristmasWarbleProgress.SANTA_FREED);
    }


    public enum ChristmasWarbleProgress {
        START(1), SPOKEN_TO_PERSONAL_IMP(2), SPOKEN_TO_SCOURGE(3), HAS_GHOST_COSTUME(4), FIND_OUT_ABOUT_SCOURGES_PAST(5), GHOST_OF_CHRISTMAS_PAST(6), FROZEN_GUESTS(7), GHOST_OF_CHRISTMAS_PRESENT(8), CHRISTMAS_PRESENT_FAILED(9), GHOST_OF_CHRISTMAS_FUTURE(10), SANTA_FREED(11), CAN_OPEN_PRESENT(12), EVENT_COMPLETE(100);
        public static final String EVENT_ATTRIBUTE_KEY = "christmas_warble_progress";
        private static final ChristmasWarbleProgress[] values = values();
        private static final Int2ObjectMap<ChristmasWarbleProgress> stages = new Int2ObjectOpenHashMap<>(values.length);

        static {
            for (ChristmasWarbleProgress progress : values) {
                stages.put(progress.getStage(), progress);
            }
        }

        private final int stage;

        ChristmasWarbleProgress(int stage) {
            this.stage = stage;
        }

        @Nullable
        public static ChristmasWarbleProgress forStage(final int stage) {
            return stages.getOrDefault(stage, null);
        }

        public int getStage() {
            return stage;
        }
    }
}
