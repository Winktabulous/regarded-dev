package com.zenyte.game.content.skills.farming;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 03/02/2019 02:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Seedling {
    OAK(5312, 5358, 5364, 5370), WILLOW(5313, 5359, 5365, 5371), MAPLE(5314, 5360, 5366, 5372), YEW(5315, 5361, 5367, 5373), MAGIC(5316, 5362, 5368, 5374), SPIRIT(5317, 5363, 5369, 5375), APPLE(5283, 5480, 5488, 5496), BANANA(5284, 5481, 5489, 5497), ORANGE(5285, 5482, 5490, 5498), CURRY(5286, 5483, 5491, 5499), PINEAPPLE(5287, 5484, 5492, 5500), PAPAYA(5288, 5485, 5493, 5501), PALM(5289, 5486, 5494, 5502), CALQUAT(5290, 5487, 5495, 5503), TEAK(21486, 21469, 21473, 21477), MAHOGANY(21488, 21471, 21475, 21480), CELASTRUS(22869, 22848, 22852, 22856), REDWOOD(22871, 22850, 22854, 22859), DRAGONFRUIT(22877, 22862, 22864, 22866);
    private final int seed;
    private final int seedling;
    private final int wateredSeedling;
    private final int sapling;
    public static final Seedling[] values = values();
    private static final Int2ObjectOpenHashMap<Seedling> ingredientMap = new Int2ObjectOpenHashMap<>(values.length * 2);
    private static final Int2ObjectOpenHashMap<Seedling> map = new Int2ObjectOpenHashMap<>(values.length * 3);
    private static final Int2ObjectMap<Seedling> wateredSeedlingMap = new Int2ObjectOpenHashMap<>(values.length);
    private static final Int2ObjectMap<Seedling> seedlingMap = new Int2ObjectOpenHashMap<>(values.length);
    private static final Int2ObjectMap<Seedling> saplingMap = new Int2ObjectOpenHashMap<>(values.length);

    static {
        for (final Seedling seed : values) {
            ingredientMap.put(seed.seed, seed);
            ingredientMap.put(seed.seedling, seed);
            seedlingMap.put(seed.seedling, seed);
            map.put(seed.wateredSeedling, seed);
            wateredSeedlingMap.put(seed.wateredSeedling, seed);
            saplingMap.put(seed.sapling, seed);
        }
        map.putAll(ingredientMap);
    }

    public static final Seedling getSeedling(final int id) {
        return map.get(id);
    }

    public static final Seedling getSapling(final int id) {
        return saplingMap.get(id);
    }

    public static final Seedling getWateredSeedling(final int id) {
        return wateredSeedlingMap.get(id);
    }

    public static final Seedling getWaterableSeedling(final int id) {
        return seedlingMap.get(id);
    }

    public static final boolean containsSeed(final int seed) {
        return ingredientMap.containsKey(seed);
    }

    public static final boolean containsSapling(final int sapling) {
        return saplingMap.containsKey(sapling);
    }

    Seedling(int seed, int seedling, int wateredSeedling, int sapling) {
        this.seed = seed;
        this.seedling = seedling;
        this.wateredSeedling = wateredSeedling;
        this.sapling = sapling;
    }

    public static Int2ObjectOpenHashMap<Seedling> getIngredientMap() {
        return ingredientMap;
    }

    public int getSeed() {
        return seed;
    }

    public int getSeedling() {
        return seedling;
    }

    public int getWateredSeedling() {
        return wateredSeedling;
    }

    public int getSapling() {
        return sapling;
    }
}
