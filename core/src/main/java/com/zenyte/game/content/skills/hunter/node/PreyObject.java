package com.zenyte.game.content.skills.hunter.node;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kris | 27/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum PreyObject {
    CRIMSON_SWIFT_OBJECT(9373, 9349),
    GOLDEN_WARBLER_OBJECT(9377, 9376),
    COPPER_LONGTAIL_OBJECT(9379, 9378),
    CERULEAN_TWITCH_OBJECT(9375, 9374),
    TROPICAL_WAGTAIL_OBJECT(9348, 9347),

    FERRET_OBJECT(9384, 9394, 9395, 9396, 9397),
    CHINCHOMPA_OBJECT(9382, 9386, 9387, 9388, 9389),
    CARNIVOROUS_CHINCHOMPA_OBJECT(9383, 9390, 9391, 9392, 9393),
    BLACK_CHINCHOMPA_OBJECT(721, 2025, 2026, 2028, 2029),

    WILD_KEBBIT_OBJECT(20651, 20131, 20647),
    BARB_TAILED_KEBBIT_OBJECT(20650, 20129, 20130),
    PRICKLY_KEBBIT_OBJECT(20648, 19218, 19219),
    SABRE_TOOTHED_KEBBIT_OBJECT(20649, 19851, 20128),
    //MANIACAL_MONNKEY_OBJECT(-1/*, 28830, 28831*/),//TODO: Handle exception.

    SWAMP_LIZARD_OBJECT(9004, 9003),
    ORANGE_SALAMANDER_OBJECT(8734, 8972),
    RED_SALAMANDER_OBJECT(8986, 8985),
    BLACK_SALAMANDER_OBJECT(8996, 8993);

    private final int firstObject;
    private final int finalObject;
    private static final List<PreyObject> values = Collections.unmodifiableList(Arrays.asList(values()));
    @NotNull
    private final int[] objects;

    PreyObject(final int finalObject, @NotNull final int... objects) {
        this.finalObject = finalObject;
        this.objects = objects;
        this.firstObject = objects[0];
    }

    public static List<PreyObject> getValues() {
        return values;
    }

    public int getFirstObject() {
        return firstObject;
    }

    public int getFinalObject() {
        return finalObject;
    }

    public int[] getObjects() {
        return objects;
    }
}
