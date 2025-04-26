package com.zenyte.game.content.treasuretrails;

import com.zenyte.game.content.treasuretrails.clues.*;
import com.zenyte.game.util.Utils;
import com.zenyte.utils.StaticInitializer;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Set;

/**
 * @author Kris | 06/04/2019 17:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@StaticInitializer
public enum TreasureTrailType {
    ANAGRAM(Anagram.class), CIPHER(CipherClue.class), COORDINATE(CoordinateClue.class), CRYPTIC(CrypticClue.class), EMOTE(EmoteClue.class), MAP(MapClue.class), HOT_COLD(HotColdClue.class), SHERLOCK(SherlockTask.class), MUSIC(MusicClue.class), CHARLIE(CharlieTask.class), FALO_THE_BARD(FaloTheBardClue.class);
    private final Class<? extends Enum<? extends Clue>> internalEnum;
    private final Enum<? extends Clue>[] constants;
    private final Object2IntOpenHashMap<ClueLevel> weights;
    private final Object2ObjectOpenHashMap<ClueLevel, Set<Clue>> clues;
    private static final Object2IntOpenHashMap<ClueLevel> totalWeights;
    private static final EnumMap<ClueLevel, EnumMap<TreasureTrailType, Set<Clue>>> allClues;
    private static final Object2ObjectMap<String, Clue> namedClues;

    public static final Object2ObjectMap<String, Clue> getNamedClues() {
        return namedClues;
    }

    TreasureTrailType(final Class<? extends Enum<? extends Clue>> e) {
        this.internalEnum = e;
        this.constants = internalEnum.getEnumConstants();
        weights = new Object2IntOpenHashMap<>(ClueLevel.values.length);
        this.clues = new Object2ObjectOpenHashMap<>();
        int weight;
        for (final ClueLevel level : ClueLevel.values) {
            weight = 0;
            for (final Enum<? extends Clue> constant : constants) {
                final Clue clue = ((Clue) constant);
                final ClueLevel constantLevel = clue.level();
                if (constantLevel == level) {
                    weight++;
                }
                clues.computeIfAbsent(clue.level(), f -> new ObjectOpenHashSet<>()).add(clue);
            }
            weights.put(level, weight);
        }
    }

    static TreasureTrailType[] values = values();

    static {
        totalWeights = new Object2IntOpenHashMap<>();
        allClues = new EnumMap<>(ClueLevel.class);
        final Object2ObjectOpenHashMap<String, Clue> labelledClues = new Object2ObjectOpenHashMap<String, Clue>();
        for (final TreasureTrailType value : values) {
            for (final Object2IntMap.Entry<ClueLevel> weightMap : value.weights.object2IntEntrySet()) {
                final ClueLevel level = weightMap.getKey();
                totalWeights.put(level, totalWeights.getInt(level) + weightMap.getIntValue());
                final Set<Clue> values = value.clues.get(level);
                if (values != null) {
                    for (final Clue clue : values) {
                        final String name = clue.getEnumName();
                        if (labelledClues.containsKey(name)) {
                            System.err.println("Overlapping clue: " + name + ", " + clue);
                        }
                        labelledClues.put(name, clue);
                    }
                    allClues.computeIfAbsent(level, __ -> new EnumMap<>(TreasureTrailType.class)).computeIfAbsent(value, __ -> new ObjectOpenHashSet<>()).addAll(values);
                }
            }
        }
        namedClues = Object2ObjectMaps.unmodifiable(labelledClues);
    }

    @NotNull
    public static final Clue random(@NotNull final ClueLevel level) {
        final EnumMap<TreasureTrailType, Set<Clue>> typeMap = allClues.get(level);
        final TreasureTrailType randomEntry = Utils.getRandomCollectionElement(typeMap.keySet());
        final Set<Clue> entries = typeMap.get(randomEntry);
        return Utils.getRandomCollectionElement(entries);
    }

    public Class<? extends Enum<? extends Clue>> getInternalEnum() {
        return internalEnum;
    }

    public Enum<? extends Clue>[] getConstants() {
        return constants;
    }

    public Object2IntOpenHashMap<ClueLevel> getWeights() {
        return weights;
    }

    public Object2ObjectOpenHashMap<ClueLevel, Set<Clue>> getClues() {
        return clues;
    }
}
