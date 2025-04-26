package com.zenyte.game.content.multicannon;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.region.RSPolygon;

import java.util.EnumMap;
import java.util.Map;

/**
 * An enumeration containing all the animations for each direction
 * as well as the angle of the next rotation.
 *
 * @author Kris | 13. okt 2017 : 13:05.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum MulticannonDirection {
    NORTH_WEST(514, 514, 9246, 9237, new int[][] {{-18, 18}, {-18, 7}, {-7, 7}, {-7, 5}, {-6, 5}, {-6, 4}, {-5, 4}, {-5, 3}, {-3, 3}, {-3, 1}, {-3, 1}, {0, 1}, {0, 4}, {-2, 4}, {-2, 6}, {-3, 6}, {-3, 7}, {-4, 7}, {-4, 8}, {-6, 8}, {-6, 18}}),
    NORTH(515, 515, 9239, 9230, new int[][] {{-5, 19}, {-5, 8}, {-2, 8}, {-2, 7}, {-4, 7}, {-4, 6}, {-3, 6}, {-3, 5}, {-2, 5}, {-2, 4}, {-1, 4}, {-1, 2}, {2, 2}, {2, 4}, {3, 4}, {3, 5}, {4, 5}, {4, 6}, {5, 6}, {5, 7}, {3, 7}, {3, 8}, {6, 8}, {6, 19}}),
    NORTH_EAST(516, 516, 9240, 9231, new int[][] {{18, 19}, {7, 19}, {7, 8}, {5, 8}, {5, 7}, {4, 7}, {4, 6}, {3, 6}, {3, 4}, {1, 4}, {1, 4}, {1, 1}, {4, 1}, {4, 3}, {6, 3}, {6, 4}, {7, 4}, {7, 5}, {8, 5}, {8, 7}, {18, 7}}),
    EAST(517, 517, 9241, 9232, new int[][] {{19, 6}, {8, 6}, {8, 3}, {7, 3}, {7, 5}, {6, 5}, {6, 4}, {5, 4}, {5, 3}, {4, 3}, {4, 2}, {2, 2}, {2, -1}, {4, -1}, {4, -2}, {5, -2}, {5, -3}, {6, -3}, {6, -4}, {7, -4}, {7, -2}, {8, -2}, {8, -5}, {19, -5}}),
    SOUTH_EAST(518, 518, 9242, 9233, new int[][] {{19, -17}, {19, -6}, {8, -6}, {8, -4}, {7, -4}, {7, -3}, {6, -3}, {6, -2}, {4, -2}, {4, 0}, {4, 0}, {1, 0}, {1, -3}, {3, -3}, {3, -5}, {4, -5}, {4, -6}, {5, -6}, {5, -7}, {7, -7}, {7, -17}}),
    SOUTH(519, 519, 9243, 9234, new int[][] {{6, -18}, {6, -7}, {3, -7}, {3, -6}, {5, -6}, {5, -5}, {4, -5}, {4, -4}, {3, -4}, {3, -3}, {2, -3}, {2, -1}, {-1, -1}, {-1, -3}, {-2, -3}, {-2, -4}, {-3, -4}, {-3, -5}, {-4, -5}, {-4, -6}, {-2, -6}, {-2, -7}, {-5, -7}, {-5, -18}}),
    SOUTH_WEST(520, 520, 9244, 9235, new int[][] {{-17, -18}, {-6, -18}, {-6, -7}, {-4, -7}, {-4, -6}, {-3, -6}, {-3, -5}, {-2, -5}, {-2, -3}, {0, -3}, {0, -3}, {0, 0}, {-3, 0}, {-3, -2}, {-5, -2}, {-5, -3}, {-6, -3}, {-6, -4}, {-7, -4}, {-7, -6}, {-17, -6}}),
    WEST(521, 521, 9245, 9236, new int[][] {{-18, -5}, {-7, -5}, {-7, -2}, {-6, -2}, {-6, -4}, {-5, -4}, {-5, -3}, {-4, -3}, {-4, -2}, {-3, -2}, {-3, -1}, {-1, -1}, {-1, 2}, {-3, 2}, {-3, 3}, {-4, 3}, {-4, 4}, {-5, 4}, {-5, 5}, {-6, 5}, {-6, 3}, {-7, 3}, {-7, 6}, {-18, 6}});

    public static final MulticannonDirection[] values = values();
    private final Animation animation, fireAnimation, ornAnimation, ornFireAnimation;
    private final int[][] polygonPoints;

    MulticannonDirection(final int animation, final int fireAnimation, final int ornAnimation, final int ornFireAnimation, final int[][] polygonPoints) {
        this.animation = new Animation(animation);
        this.fireAnimation = new Animation(fireAnimation);
        this.ornAnimation = new Animation(ornAnimation);
        this.ornFireAnimation = new Animation(ornFireAnimation);
        this.polygonPoints = polygonPoints;
    }

    static Map<MulticannonDirection, RSPolygon> create(final Location center) {
        final EnumMap<MulticannonDirection, RSPolygon> map = new EnumMap<>(MulticannonDirection.class);
        final int x = center.getX();
        final int y = center.getY();
        final int z = center.getPlane();
        for (int i = values.length - 1; i >= 0; i--) {
            final MulticannonDirection direction = values[i];
            final int[][] translatedPoints = translate(x, y, direction.getPolygonPoints());
            map.put(direction, new RSPolygon(translatedPoints, z));
        }
        return map;
    }

    private static int[][] translate(final int xOffset, final int yOffset, final int[][] coordinates) {
        final int[][] translated = new int[coordinates.length][2];
        for (int i = translated.length - 1; i >= 0; i--) {
            translated[i][0] = coordinates[i][0] + xOffset;
            translated[i][1] = coordinates[i][1] + yOffset;
        }
        return translated;
    }

    public Animation getAnimation(DwarfMultiCannonType type) {
        if (type == DwarfMultiCannonType.ORNAMENT) {
            return ornAnimation;
        }

        return animation;
    }

    public Animation getFireAnimation(DwarfMultiCannonType type) {
        if (type == DwarfMultiCannonType.ORNAMENT) {
            return ornFireAnimation;
        }

        return fireAnimation;
    }

    public int[][] getPolygonPoints() {
        return polygonPoints;
    }
}
