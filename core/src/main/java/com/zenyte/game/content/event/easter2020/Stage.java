package com.zenyte.game.content.event.easter2020;

import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.SkipPluginScan;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

/**
 * @author Corey
 * @since 28/03/2020
 */
@Ordinal
@SkipPluginScan
public enum Stage {
    //Since attribute key 0 is not really tracked, we need to include this.
    NOT_STARTED, START, CHECK_TODO, BIRD_ASLEEP, FEED_BIRD(20), BIRD_FED_SPEAK_TO_BUNNY_JR(25), GATHER_IMPLINGS(30), IMPLINGS_GATHERED_SPEAK_TO_BUNNY_JR(35), FIX_INCUBATOR(40), SPOKEN_WITH_INCUBATOR_WORKER(40), INCUBATOR_FIXED_SPEAK_TO_BUNNY_JR(50), RETRAIN_SQUIRRELS(55), SQUIRRELS_RETRAINED_SPEAK_T0_BUNNY_JR(65), MEET_ME_IN_MY_OFFICE(70), POST_MEETING_CUTSCENE(75), POST_UNDRESS_CUTSCENE(80), EVENT_COMPLETE(85);
    public static final String EVENT_ATTRIBUTE_KEY = "splitting_heirs_progress";
    private static final Stage[] values = values();
    private final int varbitValue;

    Stage() {
        this.varbitValue = -1;
    }

    @Nullable
    public static Stage forStage(final int stage) {
        return values[stage];
    }

    private static final Int2ObjectMap<ImmutableLocation> mappedPositions = new Int2ObjectOpenHashMap<>();

    static {
        mappedPositions.put(25, new ImmutableLocation(2202, 4371, 0));
        mappedPositions.put(35, new ImmutableLocation(2203, 4364, 0));
        mappedPositions.put(50, new ImmutableLocation(2205, 4380, 0));
        mappedPositions.put(65, new ImmutableLocation(2192, 4371, 0));
        mappedPositions.put(70, new ImmutableLocation(2208, 4395, 0));
    }

    private static final Graphics poof = new Graphics(2508);

    public void sendVarbit(final Player player) {
        if (varbitValue != -1) {
            final int currentValue = player.getVarManager().getBitValue(EasterConstants.GENERAL_EVENT_VARBIT);
            player.getVarManager().sendBit(EasterConstants.GENERAL_EVENT_VARBIT, varbitValue);
            final ImmutableLocation position = mappedPositions.get(currentValue);
            if (position != null) {
                player.getPacketDispatcher().sendGraphics(poof, position);
            }
            final ImmutableLocation newPosition = mappedPositions.get(varbitValue);
            if (newPosition != null) {
                player.getPacketDispatcher().sendGraphics(poof, newPosition);
            }
        }
    }

    Stage(int varbitValue) {
        this.varbitValue = varbitValue;
    }

    public int getVarbitValue() {
        return varbitValue;
    }
}
