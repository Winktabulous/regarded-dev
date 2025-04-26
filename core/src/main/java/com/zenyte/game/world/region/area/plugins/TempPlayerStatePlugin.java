package com.zenyte.game.world.region.area.plugins;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a plugin areas can extend to offer support for 'fake' or 'temporary' states.
 *
 * @author Stan van der Bend
 */
public interface TempPlayerStatePlugin {

    /**
     * Determines if the argued temp state should be enabled for the player at the given location.
     * We don't pass the player as an argument because if any of the state getters would be called
     * in implementations of this method, it could cause a stack overflow through recursion.
     *
     * @param location  the current location of the player.
     * @param type      the type of state to use the temporary version of.
     * @return true if the temp state should be used, false otherwise.
     */
    boolean enableTempState(@NotNull Location location, @NotNull TempPlayerStatePlugin.StateType type);

    enum StateType {
        INVENTORY,
        EQUIPMENT,
        RUNE_POUCH,
        RUNE_POUCH_SECONDARY,
        SKILLS,
        SPELLBOOK
    }

    public static boolean enableTempState(Player player, @NotNull TempPlayerStatePlugin.StateType type) {
        if (player == null)
            return false;
        else if (player.getArea() instanceof TempPlayerStatePlugin plugin)
            return plugin.enableTempState(player.getLocation(), type);
        else
            return false;
    }

    interface State {

        default boolean isTempVariant() {
            return false;
        }

        TempPlayerStatePlugin.StateType tempType();
    }

    static int transformTempState(Player player, int currentState, @NotNull TempPlayerStatePlugin.StateType type) {
        if (enableTempState(player, type)) {
            if (currentState == SWITCHED_REAL || currentState == STILL_REAL) {
                return SWITCHED_TEMP;
            }
        } else {
            if (currentState == STILL_TEMP || currentState == SWITCHED_TEMP) {
                return SWITCHED_REAL;
            }
        }
        return currentState;
    }

    static boolean isTemp(int state) {
        return state == SWITCHED_TEMP || state == STILL_TEMP;
    }

    static <T extends State> Optional<Pair<Integer, T>> getIfStateSwitched(Player player, int state, T temp, T real) {
        state = transformTempState(player, state, temp.tempType());
        return state == SWITCHED_TEMP ? Optional.of(new Pair<>(STILL_TEMP, temp))
                : state == SWITCHED_REAL ? Optional.of(new Pair<>(STILL_REAL, real))
                : Optional.empty();
    }


    int SWITCHED_TEMP = 0, STILL_TEMP = 1, SWITCHED_REAL = 2, STILL_REAL = 3;
}
