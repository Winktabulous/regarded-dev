package com.zenyte.game.content.treasuretrails;

import com.google.common.collect.Sets;
import kotlin.ranges.IntRange;

import java.util.Optional;
import java.util.Set;

/**
 * @author Kris | 10/04/2019 17:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum DeviceTemperature {
    ICE_COLD("The device is ice cold", new IntRange(500, 5000)),
    VERY_COLD("The device is very cold", new IntRange(200, 499)),
    COLD("The device is cold", new IntRange(150, 199)),
    WARM("The device is warm", new IntRange(100, 149)),
    HOT("The device is hot", new IntRange(70, 99)),
    VERY_HOT("The device is very hot", new IntRange(30, 69)),
    BEGINNER_INCREDIBLY_HOT("The device is incredibly hot", new IntRange(4, 29)),
    BEGINNER_VISIBLY_SHAKING("The device is visibly shaking and burns to the touch. This must be the spot", new IntRange(0, 3)),
    MASTER_INCREDIBLY_HOT("The device is incredibly hot", new IntRange(5, 29)),
    MASTER_VISIBLY_SHAKING("The device is visibly shaking and burns to the touch. This must be the spot", new IntRange(0, 4)),
    MA2_INCREDIBLY_HOT("The device is incredibly hot", new IntRange(11, 29)),
    MA2_VISIBLY_SHAKING("The device is visibly shaking and burns to the touch. This must be the spot", new IntRange(0, 10));

    private final String message;
    private final IntRange range;
    public static final Set<DeviceTemperature> BEGINNER_HOT_COLD_TEMPERATURES = Sets.immutableEnumSet(ICE_COLD, VERY_COLD, COLD, WARM, HOT, VERY_HOT, BEGINNER_INCREDIBLY_HOT, BEGINNER_VISIBLY_SHAKING);
    public static final Set<DeviceTemperature> MASTER_HOT_COLD_TEMPERATURES = Sets.immutableEnumSet(ICE_COLD, VERY_COLD, COLD, WARM, HOT, VERY_HOT, MASTER_INCREDIBLY_HOT, MASTER_VISIBLY_SHAKING);
    public static final Set<DeviceTemperature> MA2_HOT_COLD_TEMPERATURES = Sets.immutableEnumSet(ICE_COLD, VERY_COLD, COLD, WARM, HOT, VERY_HOT, MA2_INCREDIBLY_HOT, MA2_VISIBLY_SHAKING);

    public static final Optional<DeviceTemperature> get(final Set<DeviceTemperature> temps, final int distance) {
        for (final DeviceTemperature e : temps) {
            if (e.range.contains(distance)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    DeviceTemperature(String message, IntRange range) {
        this.message = message;
        this.range = range;
    }

    public String getMessage() {
        return message;
    }

    public IntRange getRange() {
        return range;
    }
}
