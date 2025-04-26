package com.zenyte.game.content.glider;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.Ordinal;

/**
 * @author Tommeh | 13-3-2019 | 16:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@Ordinal
public enum GliderLocation {

    TA_QUIR_PRIW(new Location(2465, 3501, 3)),
    GANDIUS(new Location(2969, 2973, 0)),
    KAR_HEWO(new Location(3284, 3214, 0)),
    LEMANTO_ANDRA(new Location(3321, 3429, 0)),
    SINDARPOS(new Location(2848, 3498, 0)),
    LEMANTOLLY_UNDRI(new Location(2543, 2970, 0)),
    OOKOOKOLLY_UNDRI(new Location(2711, 2801, 0));

    private final Location location;
    private final int[] values;

    GliderLocation(final Location location, final int... values) {
        this.location = location;
        this.values = values;
    }

    private static final GliderLocation[] VALUES = values();

    public static GliderLocation getGlider(final Player player) {
        for (final GliderLocation glider : VALUES) {
            if (glider.getLocation().withinDistance(player, 10)) {
                return glider;
            }
        }
        throw new IllegalStateException("Unknown glider access for player: " + player.getName() + " - location:" + player.getLocation());
    }

    public Location getLocation() {
        return location;
    }

    public int[] getValues() {
        return values;
    }
}
