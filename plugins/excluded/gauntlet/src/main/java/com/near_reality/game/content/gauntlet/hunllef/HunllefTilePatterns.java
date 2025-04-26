package com.near_reality.game.content.gauntlet.hunllef;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

/**
 * @author Andys1814.
 * @since 2/7/2022.
 */
public final class HunllefTilePatterns {

    public static final List<List<Pair<Integer, Integer>>> EASY_PATTERNS = buildEasyPatterns();

    public static final List<List<Pair<Integer, Integer>>> MEDIUM_PATTERNS = buildMediumPatterns();

    public static final List<List<Pair<Integer, Integer>>> HARD_PATTERNS = buildHardPatterns();

    private static List<List<Pair<Integer, Integer>>> buildEasyPatterns() {
        List<List<Pair<Integer, Integer>>> locations = new ObjectArrayList<>();

        // Corner squares
        locations.add(square(6, 0, 0));
        locations.add(square(6, 6, 0));
        locations.add(square(6, 0, 6));
        locations.add(square(6, 6, 6));

        // Middle squares
        locations.add(square(6, 2, 2));
        locations.add(square(6, 4, 2));
        locations.add(square(6, 2, 4));
        locations.add(square(6, 4, 4));

        // North - south rectangles
        locations.add(rectangle(4, 12, 0, 0));
        locations.add(rectangle(4, 12, 8, 0));

        // East - west rectangles
        locations.add(rectangle(12, 4, 0, 0));
        locations.add(rectangle(12, 4, 0, 8));

        return locations;
    }

    /** Two additional medium patterns are similar to the rectangle ones in easy pattern, except they always spawn together. */
    private static List<List<Pair<Integer, Integer>>> buildMediumPatterns() {
        List<List<Pair<Integer, Integer>>> locations = new ObjectArrayList<>();

        // North - south rectangle pair
        List<Pair<Integer, Integer>> northSouthRectangles = new ObjectArrayList<>();
        northSouthRectangles.addAll(rectangle(4, 12, 0, 0));
        northSouthRectangles.addAll(rectangle(4, 12, 8, 0));
        locations.add(northSouthRectangles);

        // East - west rectangle pair
        List<Pair<Integer, Integer>> eastWestRectangles = new ObjectArrayList<>();
        eastWestRectangles.addAll(rectangle(12, 4, 0, 0));
        eastWestRectangles.addAll(rectangle(12, 4, 0, 8));
        locations.add(eastWestRectangles);

        return locations;
    }

    private static List<List<Pair<Integer, Integer>>> buildHardPatterns() {
        List<List<Pair<Integer, Integer>>> locations = new ObjectArrayList<>();

        // Five squares
        List<Pair<Integer, Integer>> fiveSquares = new ObjectArrayList<>();
        fiveSquares.addAll(square(3, 0, 0));
        fiveSquares.addAll(square(3, 9, 0));
        fiveSquares.addAll(square(3, 0, 9));
        fiveSquares.addAll(square(3, 9, 9));
        fiveSquares.addAll(square(4, 4, 4));
        locations.add(fiveSquares);

        // Outer ring
        List<Pair<Integer, Integer>> outerRing = new ObjectArrayList<>();

        // North - south rectangles
        outerRing.addAll(rectangle(2, 12, 0, 0));
        outerRing.addAll(rectangle(2, 12, 10, 0));

        // East - west rectangles - smaller than ones above to avoid collisions in corners.
        outerRing.addAll(rectangle(8, 2, 2, 0));
        outerRing.addAll(rectangle(8, 2, 2, 10));

        locations.add(outerRing);

        // Four squares based in center
        List<Pair<Integer, Integer>> fourSquaresCenter = new ObjectArrayList<>();
        fourSquaresCenter.addAll(square(4, 1, 1));
        fourSquaresCenter.addAll(square(4, 7, 1));
        fourSquaresCenter.addAll(square(4, 1, 7));
        fourSquaresCenter.addAll(square(4, 7, 7));
        locations.add(fourSquaresCenter);


        // Five squares based on corners
        List<Pair<Integer, Integer>> fourSquaresCorner = new ObjectArrayList<>();
        fourSquaresCorner.addAll(square(4, 0, 0));
        fourSquaresCorner.addAll(square(4, 8, 0));
        fourSquaresCorner.addAll(square(4, 0, 8));
        fourSquaresCorner.addAll(square(4, 8, 8));
        locations.add(fourSquaresCorner);

        return locations;
    }

    private static List<Pair<Integer, Integer>> square(int size, int offsetX, int offsetY) {
        List<Pair<Integer, Integer>> locations = new ObjectArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                locations.add(Pair.of(offsetX + i, offsetY + j));
            }
        }

        return locations;
    }

    private static List<Pair<Integer, Integer>> rectangle(int sizeX, int sizeY, int offsetX, int offsetY) {
        List<Pair<Integer, Integer>> locations = new ObjectArrayList<>();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                locations.add(Pair.of(i + offsetX, j + offsetY));
            }
        }

        return locations;
    }
}
