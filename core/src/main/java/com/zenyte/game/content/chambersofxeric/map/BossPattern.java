package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.util.Utils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.zenyte.game.content.chambersofxeric.map.RaidRoom.*;

/**
 * @author Kris | 13. nov 2017 : 3:53.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BossPattern {
    ROTATION_1(TEKTON, VASA_NISTIRIO, GUARDIANS, DARK_ALTAR_ROOM, LIZARDMEN_SHAMAN, MUTTADILES, VANGUARD, VESPULA), ROTATION_2(TEKTON, MUTTADILES, GUARDIANS, VESPULA, LIZARDMEN_SHAMAN, VASA_NISTIRIO, VANGUARD, DARK_ALTAR_ROOM);
    private static final BossPattern[] values = values();
    private final RaidRoom[] rooms;

    BossPattern(@NotNull final RaidRoom... rooms) {
        this.rooms = rooms;
    }

    /**
     * Generates a random list of raid rooms based on the input length, from this pattern.
     *
     * @param length the number of rooms to generate.
     * @return a list of rooms.
     */
    public final List<RaidRoom> random(final int length) {
        assert length >= 0 && length <= rooms.length;
        final ObjectArrayList<RaidRoom> list = new ObjectArrayList<RaidRoom>(length);
        final int index = Utils.random(rooms.length - 1);
        for (int i = 0; i < length; i++) {
            list.add(rooms[(index + i) % rooms.length]);
        }
        return list;
    }

    public static BossPattern[] getValues() {
        return values;
    }

    public RaidRoom[] getRooms() {
        return rooms;
    }
}
