package com.zenyte.game.content.skills.hunter.node;

import com.zenyte.utils.Ordinal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 25/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@Ordinal
public enum BirdHouseState {
    EMPTY,
    IN_PROGRESS,
    FINISHED;

    private static final List<BirdHouseState> values = Collections.unmodifiableList(Arrays.asList(values()));

    public static List<BirdHouseState> getValues() {
        return values;
    }
}