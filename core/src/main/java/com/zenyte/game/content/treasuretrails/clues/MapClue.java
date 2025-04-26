package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.DigRequest;
import com.zenyte.game.content.treasuretrails.challenges.GameObject;
import com.zenyte.game.content.treasuretrails.challenges.SearchRequest;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.StaticInitializer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.zenyte.game.content.treasuretrails.ClueLevel.*;

/**
 * Contains all the map clue scrolls; most of them are digging based, although
 * some will require the player to search a specific box.
 *
 * @author Kris | 29. march 2018 : 22:30.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@StaticInitializer
public enum MapClue implements Clue {
    MAP_1(337, new Location(2970, 3415, 0), EASY),
    MAP_2(338, new Location(3021, 3912, 0), HARD),
    MAP_3(339, new Location(2722, 3338, 0), HARD),
    //MAP_4(340, new Location(2536, 3865, 0), MEDIUM),
    MAP_5(341, new Location(3434, 3265, 0), MEDIUM),
    MAP_6(342, new Location(2454, 3230, 0), MEDIUM),
    MAP_7(343, new Location(2578, 3597, 0), MEDIUM),
    MAP_8(344, new Location(2666, 3562, 0), MEDIUM),
    MAP_9(346, new Location(3166, 3361, 0), EASY),
    MAP_10(347, new Location(3290, 3374, 0), EASY),
    MAP_11(348, new Location(3091, 3227, 0), MEDIUM),
    //MAP_12(349, new Location(2702, 3429, 0), EASY),
    MAP_13(350, 2620, new Location(3309, 3503, 0), HARD),
    MAP_14(351, new Location(3043, 3398, 0), EASY),
    MAP_15(352, new Location(2907, 3295, 0), MEDIUM),
    MAP_16(353, new Location(2615, 3078, 0), HARD),
    MAP_17(354, new Location(2612, 3482, 0), EASY),
    MAP_18(355, 357, new Location(2658, 3488, 0), MEDIUM),
    MAP_19(356, new Location(3110, 3152, 0), EASY),
    MAP_20(357, new Location(2488, 3308, 0), HARD),
    MAP_21(358, 18506, new Location(2457, 3182, 0), HARD),
    MAP_22(359, 354, new Location(3026, 3628, 0), HARD),
    MAP_23(360, new Location(2651, 3231, 0), MEDIUM),
    MAP_24(361, 354, new Location(2565, 3248, 0), MEDIUM),
    MAP_25(362, new Location(2924, 3210, 0), MEDIUM),
    MAP_26(86, new Location(2449, 3130, 0), ELITE),
    MAP_27(87, new Location(3300, 3291, 0), EASY),
    MAP_28(102, 6616, new Location(2703, 2716, 0), ELITE),
    //MAP_29(314, new Location(2953, 9523, 1), ELITE),
    MAP_30(316, new Location(3538, 3208, 0), ELITE),
    MAP_31(317, new Location(2202, 3062, 0), ELITE),
    MAP_32(318, new Location(1815, 3852, 0), ELITE),
    //Beginner
    MAP_33(346, new Location(3166, 3361, 0), BEGINNER),
    MAP_34(347, new Location(3290, 3374, 0), BEGINNER),
    MAP_35(351, new Location(3043, 3398, 0), BEGINNER),
    MAP_36(356, new Location(3110, 3152, 0), BEGINNER),
    MAP_37(348, new Location(3093, 3226, 0), BEGINNER),
    ;

    private final int interfaceId;
    private final int objectId;
    private final Location location;
    private final ClueChallenge challenge;
    private final ClueLevel level;

    MapClue(final int interfaceId, final Location location, final ClueLevel level) {
        this(interfaceId, -1, location, level);
    }

    MapClue(final int interfaceId, final int objectId, final Location location, final ClueLevel level) {
        this.interfaceId = interfaceId;
        this.objectId = objectId;
        this.location = location;
        this.level = level;
        this.challenge = objectId == -1 ? new DigRequest(location) : new SearchRequest(new GameObject[]{new GameObject(objectId, location, "Search")});
    }

    static {
        for (final MapClue clue : values()) {
            if (clue.objectId != -1) {
                CrypticClue.objectMap.computeIfAbsent(clue.objectId, a -> new ArrayList<>()).add(clue);
            }
        }
    }

    @Override
    public void view(@NotNull Player player, @NotNull Item item) {
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, interfaceId);
    }

    @Override
    public TreasureTrailType getType() {
        return TreasureTrailType.MAP;
    }

    @Override
    public String getEnumName() {
        return toString();
    }

    @Override
    public ClueChallenge getChallenge() {
        return challenge;
    }

    @NotNull
    @Override
    public ClueLevel level() {
        return level;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getObjectId() {
        return objectId;
    }

    public Location getLocation() {
        return location;
    }

    public ClueLevel getLevel() {
        return level;
    }
}
