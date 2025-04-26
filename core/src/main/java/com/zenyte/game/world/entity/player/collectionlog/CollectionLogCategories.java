package com.zenyte.game.world.entity.player.collectionlog;

import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Handles the collection log category player value retrievers.
 *
 * @author Stan van der Bend
 */
public final class CollectionLogCategories {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionLogCategories.class);

    private static final Map<String, Function<Player, Integer>[]> CATEGORY_FUNCTION_MAP = new HashMap<>();

    static {
        for (CollectionLogCategoryType categoryType : CollectionLogCategoryType.values)
            CollectionLogCategories.register(categoryType.name(), categoryType.function);
    }

    /**
     * Registers the collection log category.
     *
     * @param name          the name to register the functions at, which will be {@link #formateCategoryName(String)} formatted}.
     * @param functions     the optional functions to apply to the player, to retrieve values from for the interface, max 3.
     */
    @SafeVarargs
    public static void register(String name, Function<Player, Integer>... functions) {

        name = formateCategoryName(name);

        if (functions.length > 3)
            LOGGER.warn("More than 3 functions registered for {}, only the first three are displayed", name);

        if (CATEGORY_FUNCTION_MAP.containsKey(name)) {
            LOGGER.error("Did not register {} because they key is already taken", name);
            return;
        }

        CATEGORY_FUNCTION_MAP.put(name, functions);
        LOGGER.trace("Registered collection log category {} with {} functions", name, functions.length);
    }

    static Function<Player, Integer>[] getFunctions(final String name) {
        final String filtered = formateCategoryName(name);
        return CATEGORY_FUNCTION_MAP.get(filtered);
    }

    @NotNull
    private static String formateCategoryName(String name) {
        return name.toUpperCase()
                .replaceAll("['()]", "")
                .replace(' ', '_');
    }
}